package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.config.CustomConfig
import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.model.FStarAdmin
import com.mdreamfever.fstar.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/auth")
class AuthController {

    @Autowired
    private lateinit var customConfig: CustomConfig

    @Autowired
    private lateinit var authService: AuthService


    @PostMapping
    fun login(@RequestBody admin: FStarAdmin): FResult {
        val token = authService.login(admin.username, admin.password)
        return FResult.success(
            data = mapOf(
                "tokenHeader" to customConfig.tokenHeader,
                "tokenPrefix" to customConfig.tokenPrefix,
                "token" to token
            )
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody admin: FStarAdmin): FResult {
        val token = authService.register(admin.username, admin.password)
        return if (token == null) {
            FResult.failed(message = "用户名已存在")
        } else {
            FResult.success(
                data = mapOf(
                    "tokenHeader" to customConfig.tokenHeader,
                    "tokenPrefix" to customConfig.tokenPrefix,
                    "token" to token
                )
            )
        }
    }
}