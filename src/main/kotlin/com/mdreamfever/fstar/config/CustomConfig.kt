package com.mdreamfever.fstar.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import kotlin.properties.Delegates

@Component
@ConfigurationProperties(prefix = "fstar")
class CustomConfig {
    lateinit var tokenPrefix: String
    lateinit var tokenHeader: String
    lateinit var tokenSecret: String
    var tokenExpiration by Delegates.notNull<Long>()
    lateinit var accessKey: String
    lateinit var secretKey: String
    lateinit var bucket: String
    lateinit var flaskHost: String
    lateinit var flaskPort: String
}