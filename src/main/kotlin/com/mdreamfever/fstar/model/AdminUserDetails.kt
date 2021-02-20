package com.mdreamfever.fstar.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AdminUserDetails(private val fStarAdmin: FStarAdmin) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return fStarAdmin.roles?.split(",")?.map { SimpleGrantedAuthority(it) }?.toMutableList() ?: arrayListOf(
            SimpleGrantedAuthority("ROLE_USER")
        )
    }

    override fun getPassword(): String {
        return fStarAdmin.password
    }

    override fun getUsername(): String {
        return fStarAdmin.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}