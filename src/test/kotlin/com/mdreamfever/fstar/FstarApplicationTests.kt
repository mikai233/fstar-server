package com.mdreamfever.fstar

import com.mdreamfever.fstar.config.CustomConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate

@SpringBootTest
class FStarApplicationTests {

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var customConfig: CustomConfig

    @Test
    fun contextLoads() {
    }

    @Test
    fun customProperties() {
        val result = stringRedisTemplate.execute {
            return@execute it.bitCount("2021-01-14".toByteArray())
        }
        print(result)
    }
}
