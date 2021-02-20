package com.mdreamfever.fstar.service

import com.mdreamfever.fstar.component.JwtUtils
import com.mdreamfever.fstar.model.FStarAdmin
import com.mdreamfever.fstar.repository.FStarAdminRepository
import com.mdreamfever.fstar.repository.FStarUserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {


    @Autowired
    private lateinit var fStarAdminRepository: FStarAdminRepository

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var adminDetailsService: AdminDetailsService

    private val logger = LoggerFactory.getLogger(javaClass)

    fun login(username: String, password: String): String? {
        return try {
            val userDetails = adminDetailsService.loadUserByUsername(username)
            if (!passwordEncoder.matches(password, userDetails.password)) {
                throw BadCredentialsException("密码不正确")
            }
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            jwtUtils.generateToken(userDetails)
        } catch (e: AuthenticationException) {
            logger.warn("登录异常:${e.message}")
            null
        }
    }

    fun register(username: String, password: String): String? {
        return try {
            val user = fStarAdminRepository.findByUsername(username)
            if (user != null) {
                null
            } else {
                val newUser = FStarAdmin(null, username, passwordEncoder.encode(password), "ROLE_USER")
                fStarAdminRepository.save(newUser)
                val userDetails = adminDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                SecurityContextHolder.getContext().authentication = authentication
                jwtUtils.generateToken(userDetails)
            }
        } catch (e: Exception) {
            logger.error(e.message)
            null
        }
    }
}