package com.mdreamfever.fstar.controller

import com.mdreamfever.fstar.model.FResult
import com.mdreamfever.fstar.model.FStarUser
import com.mdreamfever.fstar.repository.FStarUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/user")
class UserController {
    @Autowired
    private lateinit var fStarUserRepository: FStarUserRepository

    @PostMapping
    fun addUser(@RequestBody fStarUser: FStarUser): FResult {
        return FResult.success(data = fStarUserRepository.save(fStarUser))
    }
}