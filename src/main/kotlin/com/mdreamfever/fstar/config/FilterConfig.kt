package com.mdreamfever.fstar.config

import com.mdreamfever.fstar.filter.AuthorizationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun authenticationBean(): FilterRegistrationBean<AuthorizationFilter> {
        val filterReg = FilterRegistrationBean(AuthorizationFilter())
        filterReg.addUrlPatterns("/api/*")
        return filterReg
    }
}