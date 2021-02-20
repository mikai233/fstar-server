package com.mdreamfever.fstar.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdreamfever.fstar.model.FResult
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(p0: HttpServletRequest, p1: HttpServletResponse, p2: AuthenticationException) {
        p1.apply {
            characterEncoding = "UTF-8"
            contentType = "application/json"
            writer.println(ObjectMapper().writeValueAsString(FResult.unauthorized()))
            writer.flush()
        }
    }
}