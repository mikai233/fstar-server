package com.mdreamfever.fstar.filter

import com.mdreamfever.fstar.component.JwtUtils
import com.mdreamfever.fstar.config.CustomConfig
import com.mdreamfever.fstar.service.AdminDetailsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var customConfig: CustomConfig

    @Autowired
    private lateinit var jwtUtils: JwtUtils


    @Autowired
    private lateinit var adminDetailsService: AdminDetailsService

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(p0: HttpServletRequest, p1: HttpServletResponse, p2: FilterChain) {
        val authHeader = p0.getHeader(customConfig.tokenHeader)
        if (authHeader != null && authHeader.startsWith(customConfig.tokenPrefix)) {
            val authToken = authHeader.substring(customConfig.tokenPrefix.length + 1)
            val username = jwtUtils.getUserNameFromToken(authToken)
            log.info("checking username:$username")
            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = adminDetailsService.loadUserByUsername(username)
                if (jwtUtils.validateToken(authToken, userDetails)) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(p0)
                    log.info("authenticated user:$username")
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        p2.doFilter(p0, p1)
    }
}