package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.config.FStarEncrypt
import com.mdreamfever.fstar.model.*
import com.mdreamfever.fstar.repository.ChangelogRepository
import com.mdreamfever.fstar.repository.FStarUserRepository
import com.mdreamfever.fstar.repository.ScoreRepository
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

/**
 * 这个是客户端和服务端版本迁移后为了兼容以前的客户端需要的Controller
 */
@RestController
@RequestMapping("/")
class RetainController {

    @Autowired
    private lateinit var fStarUserRepository: FStarUserRepository

    @Autowired
    private lateinit var scoreRepository: ScoreRepository

    @Autowired
    private lateinit var changelogRepository: ChangelogRepository

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @PostMapping("/api/version")
    @FStarEncrypt
    fun getLatestVersion(@RequestBody clientVersion: ClientVersion): ResponseEntity<Version> {
        val user = fStarUserRepository.findFirstByAndroidId(clientVersion.androidId)
        val updateUser = FStarUser(
            id = user?.id,
            appVersion = clientVersion.version,
            buildNumber = clientVersion.buildNumber.toInt(),
            androidId = clientVersion.androidId,
            brand = clientVersion.brand,
            device = clientVersion.device,
            platform = clientVersion.platform,
            product = clientVersion.product,
            model = clientVersion.model,
            androidVersion = clientVersion.androidVersion
        )
        fStarUserRepository.save(updateUser)
        stringRedisTemplate.opsForZSet()
            .apply {
                add(
                    "active_user",
                    clientVersion.androidId,
                    (System.currentTimeMillis() + 24 * 60 * 60 * 1000).toDouble()
                )
                add(
                    "active_user_week",
                    clientVersion.androidId,
                    (System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000).toDouble()
                )
                add(
                    "active_user_month",
                    clientVersion.androidId,
                    (System.currentTimeMillis() + 30 * 7 * 24 * 60 * 60 * 1000L).toDouble()
                )
            }
        val description = stringRedisTemplate.opsForValue().get("description") ?: ""
        val downloadUrl = stringRedisTemplate.opsForValue().get("downloadUrl") ?: ""
        val releaseVersion = stringRedisTemplate.opsForValue().get("releaseVersion") ?: ""
        val buildNumber = stringRedisTemplate.opsForValue().get("buildNumber") ?: ""
        val version = Version(description, downloadUrl, buildNumber, releaseVersion)
        return ResponseEntity.ok(version)
    }

    @FStarEncrypt
    @GetMapping("/api/changelog/{buildNumber}")
    fun getChangelogLessThan(@ApiParam("构建号") @PathVariable buildNumber: Long): ResponseEntity<List<Changelog>> {
        return ResponseEntity.ok(changelogRepository.findAll())
    }

    @FStarEncrypt
    @GetMapping("/api/service")
    fun getKey(key: String): ResponseEntity<Map<String, String>> {
        val value = stringRedisTemplate.opsForValue().get(key) ?: ""
        return ResponseEntity.ok(mapOf(key to value))
    }

    @FStarEncrypt
    @GetMapping("/api/score/class")
    fun getScoreByClass(
        @RequestParam classNumber: String, @RequestParam semester: String
    ): ResponseEntity<List<Score>> {
        return ResponseEntity.ok(scoreRepository.findAllByStudentNumberStartingWith(classNumber))
    }

    @Transactional
    @FStarEncrypt
    @PostMapping("/api/score")
    fun addScores(@RequestBody scores: List<Score2>): ResponseEntity<Boolean> {
        if (scores.isNotEmpty()) {
            scoreRepository.deleteAllByStudentNumber(scores.first().studentNumber)
        }
        val mappedScores = scores.map {
            Score(
                null,
                it.studentNumber,
                it.no,
                it.semester,
                it.scoreNo,
                it.name,
                it.score,
                it.credit,
                it.period,
                it.evaluationMode,
                it.courseProperty,
                it.courseNature,
                it.alternativeCourseNumber,
                it.alternativeCourseName,
                it.scoreFlag
            )
        }
        scoreRepository.saveAll(mappedScores)
        return ResponseEntity.ok(true)
    }
}