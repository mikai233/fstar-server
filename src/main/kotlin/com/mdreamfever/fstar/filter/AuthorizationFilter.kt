package com.mdreamfever.fstar.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.utils.AesCryptUtil
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter : OncePerRequestFilter() {
    private val apiToken = "D4Jvu**4W@#sI7jmTTn@dV21"
    private val log = LoggerFactory.getLogger(javaClass)
    private fun authenticate(request: HttpServletRequest): Boolean {
        val token = request.getHeader("Authorization")
        return try {
            val var1 = AesCryptUtil.decryptS(token).split(' ')
            !(var1[0] != apiToken || System.currentTimeMillis() - var1[1].toLong() > 30000)
        } catch (e: Exception) {
            log.info(e.message)
            false
        }
    }

    override fun doFilterInternal(p0: HttpServletRequest, p1: HttpServletResponse, p2: FilterChain) {
        if (authenticate(p0)) {
            p2.doFilter(p0, p1)
        } else {
            p1.contentType = "application/json"
            p1.characterEncoding = "UTF-8"
            val writer = p1.writer
            writer.write(ObjectMapper().writeValueAsString(FResult.validateFailed()))
            writer.flush()
        }
    }
}