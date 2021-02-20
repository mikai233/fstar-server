package com.mdreamfever.fstar.component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTask {
    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Scheduled(fixedRate = 7 * 60 * 1000)
    fun removeExpiredActiveUser() {
        val now = System.currentTimeMillis()
        stringRedisTemplate.opsForZSet().apply {
            removeRangeByScore("active_user", 0.0, now.toDouble())
            removeRangeByScore("active_user_week", 0.0, now.toDouble())
            removeRangeByScore("active_user_month", 0.0, now.toDouble())
        }
    }
}