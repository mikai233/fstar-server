package com.mdreamfever.fstar.service

import com.mdreamfever.fstar.model.AdminUserDetails
import com.mdreamfever.fstar.repository.FStarAdminRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdminDetailsService : UserDetailsService {

    @Autowired
    private lateinit var fStarAdminRepository: FStarAdminRepository

    override fun loadUserByUsername(p0: String): UserDetails {
        val user = fStarAdminRepository.findByUsername(p0)
        if (user != null) {
            return AdminUserDetails(user)
        } else {
            throw UsernameNotFoundException("用户名不存在")
        }
    }
}