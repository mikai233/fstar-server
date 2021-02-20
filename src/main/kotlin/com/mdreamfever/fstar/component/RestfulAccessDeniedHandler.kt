package com.mdreamfever.fstar.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdreamfever.fstar.model.FResult
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestfulAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(p0: HttpServletRequest, p1: HttpServletResponse, p2: AccessDeniedException) {
        p1.apply {
            characterEncoding = "UTF-8"
            contentType = "application/json"
            writer.print(ObjectMapper().writeValueAsString(FResult.forbidden()))
            writer.flush()
        }
    }
}