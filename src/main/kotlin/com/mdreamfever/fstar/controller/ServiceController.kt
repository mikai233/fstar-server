package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.config.CustomConfig
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.model.FStarUser
import com.mdreamfever.fstar.model.FlaskQuery
import com.mdreamfever.fstar.model.ParseConfig
import com.mdreamfever.fstar.repository.FStarUserRepository
import com.mdreamfever.fstar.repository.ParseConfigRepository
import com.qiniu.util.Auth
import com.qiniu.util.UrlSafeBase64
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.security.Principal
import java.time.LocalDateTime

/**
 * 使用到RestTemplate部分的接口可以不用管，暂时没有用到，为小程序设计的
 * 七牛云的Token部分也可以不用管，这个是在客户端上传配置文件的时候要用到的
 * 如果需要可以在七牛云申请，然后在配置文件中配置access-key和secret-key
 */
@RestController
@RequestMapping("/v2/service")
class ServiceController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var customConfig: CustomConfig

    @Autowired
    private lateinit var fStarUserRepository: FStarUserRepository

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var parseConfigRepository: ParseConfigRepository

    @PostMapping("/vitality")
    fun vitalityStatistics(@RequestBody fStarUser: FStarUser): FResult {
        if (!fStarUserRepository.exists(Example.of(fStarUser))) {
            fStarUserRepository.save(fStarUser)
        }
        var id: Int? = null
        if (fStarUser.platform == "android" && fStarUser.androidId != null) {
            val androidId = fStarUser.androidId
            val user = fStarUserRepository.findFirstByAndroidId(androidId)
            id = user?.id
        }
        if (id != null) {
//            val dayFormatter = SimpleDateFormat("yyyy-MM-dd")
//            stringRedisTemplate.opsForValue()
//                .setBit(dayFormatter.format(Date(System.currentTimeMillis())), id.toLong(), true)
            stringRedisTemplate.opsForZSet()
                .apply {
                    add("active_user", id.toString(), (System.currentTimeMillis() + 24 * 60 * 60 * 1000).toDouble())
                    add(
                        "active_user_week",
                        id.toString(),
                        (System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000).toDouble()
                    )
                    add(
                        "active_user_month",
                        id.toString(),
                        (System.currentTimeMillis() + 30 * 7 * 24 * 60 * 60 * 1000L).toDouble()
                    )
                }
        }
        return FResult.success()
    }

    @GetMapping("/empty_room")
    fun getEmptyRoom() {

    }

    @GetMapping("/config")
    fun getCourseParseConfig(@RequestParam page: Int, @RequestParam size: Int): FResult {
        val pageRequest = PageRequest.of(page, size)
        return FResult.success(data = parseConfigRepository.findAll(pageRequest))
    }

    @GetMapping("/config/school/{schoolName}")
    fun getCourseParseConfigBySchoolName(@PathVariable schoolName: String): FResult {
        return FResult.success(data = parseConfigRepository.findAllBySchoolNameStartingWith(schoolName))
    }

    @GetMapping("/config/author/{authorName}")
    fun getCourseParseConfigByAuthorName(@PathVariable authorName: String): FResult {
        return FResult.success(data = parseConfigRepository.findAllByAuthor(authorName))
    }

    @GetMapping("/config/user")
    fun getCourseParseConfigByUsername(principal: Principal?): FResult {
        if (principal == null) {
            return FResult.unauthorized()
        }
        return FResult.success(data = parseConfigRepository.findAllByUser(principal.name))
    }

    @PostMapping("/config")
    fun addConfig(@RequestBody parseConfig: ParseConfig, principal: Principal?): FResult {
        if (principal == null) {
            return FResult.unauthorized()
        }
        parseConfig.run {
            publishTime = LocalDateTime.now()
            download = 0
        }
        parseConfigRepository.save(parseConfig)
        return FResult.success()
    }

    @DeleteMapping("/config")
    fun deleteConfig(@RequestParam id: Int, principal: Principal?): FResult {
        if (principal == null) {
            return FResult.unauthorized()
        }
        val config = parseConfigRepository.findById(id)
        return if (config.isPresent && config.get().user == principal.name) {
            parseConfigRepository.deleteById(id)
            FResult.success()
        } else {
            FResult.failed()
        }
    }

    @PostMapping("/just/course/{mode}")
    fun queryJustSystemCourse(
        @PathVariable mode: String,
        @RequestBody flaskQuery: FlaskQuery
    ): FResult {
        val restTemplate = RestTemplate()
        return when (mode) {
            "just" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/just/course?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn/course?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&vpn_username=${flaskQuery.vpnUsername}&vpn_password=${flaskQuery.vpnPassword}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn2" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn2/course?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            else -> {
                FResult.failed(message = "参数不正确")
            }
        }
    }

    @PostMapping("/just/score/{mode}")
    fun queryJustSystemScore(@PathVariable mode: String, @RequestBody flaskQuery: FlaskQuery): FResult {
        val restTemplate = RestTemplate()
        return when (mode) {
            "just" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/just/score?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}&xsfs=${flaskQuery.xsfs}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn/score?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&vpn_username=${flaskQuery.vpnUsername}&vpn_password=${flaskQuery.vpnPassword}" +
                            "&kksj=${flaskQuery.kksj}&xsfs=${flaskQuery.xsfs}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn2" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn2/score?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}&xsfs=${flaskQuery.xsfs}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            else -> {
                FResult.failed(message = "参数不正确")
            }
        }
    }

    @PostMapping("/just/score2/{mode}")
    fun queryJustSystemScore2(@PathVariable mode: String, @RequestBody flaskQuery: FlaskQuery): FResult {
        val restTemplate = RestTemplate()
        return when (mode) {
            "just" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/just/score2?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn/score2?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&vpn_username=${flaskQuery.vpnUsername}&vpn_password=${flaskQuery.vpnPassword}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            "vpn2" -> {
                val url =
                    "http://${customConfig.flaskHost}:${customConfig.flaskPort}/vpn2/score2?username=${flaskQuery.username}&password=${flaskQuery.password}" +
                            "&kksj=${flaskQuery.kksj}"
                val result = restTemplate.getForEntity<FResult>(url)
                if (result.hasBody()) {
                    FResult.success(data = result.body)
                } else {
                    FResult.failed()
                }
            }
            else -> {
                FResult.failed(message = "参数不正确")
            }
        }
    }

    @GetMapping("/just/school_bus")
    fun getJustSchoolBus(): FResult {
        val url = stringRedisTemplate.opsForValue()["school_bus"]
        return FResult.success(data = url)
    }

    @GetMapping("/just/school_calendar")
    fun getJustSchoolCalendar(): FResult {
        val url = stringRedisTemplate.opsForValue()["school_calendar"]
        return FResult.success(data = url)
    }

    @GetMapping("/upload_token")
    fun getUploadToken(@RequestParam key: String, principal: Principal?): FResult {
        if (principal == null) {
            return FResult.unauthorized()
        }
        val auth = Auth.create(customConfig.accessKey, customConfig.secretKey)
        val token = auth.uploadToken(customConfig.bucket, key, 60, null)
        return FResult.success(data = token)
    }

    @GetMapping("/authorization_token")
    fun getAuthorizationToken(
        @RequestParam method: String,
        @RequestParam url: String,
        @RequestParam key: String, principal: Principal?
    ): FResult {
        logger.info("method: {}, url: {}", method, url)
        if (principal == null) {
            return FResult.unauthorized()
        }
        val httpMethod = when {
            method.equals("GET", true) -> "GET"
            method.equals("POST", true) -> "POST"
            method.equals("PUT", true) -> "PUT"
            method.equals("DELETE", true) -> "DELETE"
            method.equals("PATCH", true) -> "PATCH"
            method.equals("CONNECT", true) -> "CONNECT"
            method.equals("TRACE", true) -> "TRACE"
            method.equals("HEAD", true) -> "HEAD"
            method.equals("OPTIONS", true) -> "OPTIONS"
            else -> "GET"
        }
        val auth = Auth.create(customConfig.accessKey, customConfig.secretKey)
        val headers = auth.qiniuAuthorization(httpMethod, url, null, null)
        val token = headers.get("Authorization")
        logger.info(token)
        return FResult.success(
            data = mapOf(
                "token" to token,
                "encodedEntryURI" to UrlSafeBase64.encodeToString("${customConfig.bucket}:$key")
            )
        )
    }

    @GetMapping("/code_host")
    fun getCodeHost(): FResult {
        return FResult.success(data = "http://resource.mdreamfever.com")
    }
}