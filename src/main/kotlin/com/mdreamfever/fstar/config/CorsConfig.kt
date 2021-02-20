package com.mdreamfever.fstar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun cors(): CorsFilter {
        val config = CorsConfiguration()
        config.apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
        }
        val configurationPropertySource = UrlBasedCorsConfigurationSource()
        configurationPropertySource.registerCorsConfiguration("/**", config)
        return CorsFilter(configurationPropertySource)
    }
}