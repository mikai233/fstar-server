package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.model.Changelog
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.model.Message
import com.mdreamfever.fstar.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/v2/admin")
class AdminController {

    @Autowired
    private lateinit var parseConfigRepository: ParseConfigRepository

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var fStarUserRepository: FStarUserRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @Autowired
    private lateinit var changelogRepository: ChangelogRepository

    @PostMapping("/changelog")
    fun addChangelog(@RequestBody changelog: Changelog): FResult {
        val result = changelogRepository.save(changelog)
        return FResult.success(data = result)
    }

    @DeleteMapping("/changelog")
    fun deleteChangelog(): FResult {
        changelogRepository.deleteAll()
        return FResult.success()
    }

    @DeleteMapping("/changelog/{id}")
    fun deleteChangelogById(@PathVariable id: Int): FResult {
        changelogRepository.deleteById(id)
        return FResult.success()
    }

    @PutMapping("/changelog")
    fun updateChangelog(@RequestBody changelog: Changelog): FResult {
        changelogRepository.save(changelog)
        return FResult.success()
    }

    @GetMapping("/score")
    fun getScore(@RequestParam page: Int, @RequestParam size: Int): FResult {
        val pageRequest = PageRequest.of(page, size)
        return FResult.success(data = scoreRepository.findAll(pageRequest))
    }

    @DeleteMapping("/score")
    fun deleteScore(): FResult {
        scoreRepository.deleteAll()
        return FResult.success()
    }

    @DeleteMapping("/score/course_id/{courseId}")
    fun deleteScoreByCourseId(@PathVariable courseId: String): FResult {
        scoreRepository.deleteByScoreNo(courseId)
        return FResult.success()
    }

    @DeleteMapping("/score/class/{classNumber}")
    fun deleteScoreByClassNumber(@PathVariable classNumber: String): FResult {
        scoreRepository.deleteAllByStudentNumberStartingWith(classNumber)
        return FResult.success()
    }

    @GetMapping("/user/count")
    fun countUser(): FResult {
        return FResult.success(data = fStarUserRepository.count())
    }

    @GetMapping("/user/all")
    fun getUserNoPage(): FResult {
        return FResult.success(data = fStarUserRepository.findAll())
    }

    @GetMapping("/user")
    fun getUser(@RequestParam page: Int, @RequestParam size: Int): FResult {
        val pageRequest = PageRequest.of(page, size)
        return FResult.success(data = fStarUserRepository.findAll(pageRequest))
    }

    @GetMapping("/user/{id}")
    fun getUserByStudentNumber(@PathVariable id: String): FResult {
        return FResult.success(data = fStarUserRepository.findById(id))
    }

    @GetMapping("/user/device/{version}")
    fun getUserByDeviceVersion(@PathVariable version: String): FResult {
        return FResult.success(data = fStarUserRepository.findAllByAndroidVersion(version))
    }

    @GetMapping("user/device/{brand}")
    fun getUserByBrand(@PathVariable brand: String): FResult {
        return FResult.success(data = fStarUserRepository.findAllByBrand(brand))
    }

    @DeleteMapping("/user")
    fun deleteUser(): FResult {
        fStarUserRepository.deleteAll()
        return FResult.success()
    }

    @DeleteMapping("/user/{id}")
    fun deleteUserById(@PathVariable id: String): FResult {
        fStarUserRepository.deleteById(id)
        return FResult.success()
    }

    @DeleteMapping("/message/{id}")
    fun deleteMessageById(@PathVariable id: Int): FResult {
        messageRepository.deleteById(id)
        return FResult.success()
    }

    @PostMapping("/message")
    fun addMessage(@RequestBody message: Message): FResult {
        message.publishTime = LocalDateTime.now()
        val result = messageRepository.save(message)
        return FResult.success(data = result)
    }

    @DeleteMapping("/message")
    fun deleteAllMessage(): FResult {
        messageRepository.deleteAll()
        return FResult.success()
    }

    @PutMapping("/message")
    fun updateMessage(@RequestBody message: Message): FResult {
        val result = messageRepository.save(message)
        return FResult.success(data = result)
    }

    @GetMapping("/just/school_bus")
    fun addJustSchoolBus(@RequestParam url: String): FResult {
        stringRedisTemplate.opsForValue()["school_bus"] = url
        return FResult.success(data = url)
    }

    @GetMapping("/just/school_calendar")
    fun addJustSchoolCalendar(@RequestParam url: String): FResult {
        stringRedisTemplate.opsForValue()["school_calendar"] = url
        return FResult.success(data = url)
    }

    @DeleteMapping("/config/school/{schoolName}")
    fun deleteConfigBySchoolName(@PathVariable schoolName: String): FResult {
        parseConfigRepository.deleteAllBySchoolName(schoolName)
        return FResult.success()
    }

    @DeleteMapping("/config/author/{authorName}")
    fun deleteConfigByAuthorName(@PathVariable authorName: String): FResult {
        parseConfigRepository.deleteAllByAuthor(authorName)
        return FResult.success()
    }

    @GetMapping("/vitality")
    fun getVitality(): FResult {
//        val todayFormatter = SimpleDateFormat("yyyy-MM-dd")
//        val key = stringRedisTemplate.opsForValue()[todayFormatter.format(Date(System.currentTimeMillis()))] ?: ""
//        val result = stringRedisTemplate.execute {
//            val value = it.get("2021-01-14".toByteArray())
//            print(value)
//            return@execute it.bitCount(value!!)
//        }
//        print(result)
        //这里用Redis的Bitmap来统计活跃度才是实时的，最准确的方法
        val day = stringRedisTemplate.opsForZSet().size("active_user")
        val week = stringRedisTemplate.opsForZSet().size("active_user_week")
        val month = stringRedisTemplate.opsForZSet().size("active_user_month")

        return FResult.success(data = mapOf("day" to day, "week" to week, "month" to month))
    }
}