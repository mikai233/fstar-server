package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.FStarAdmin
import org.springframework.data.jpa.repository.JpaRepository

interface FStarAdminRepository : JpaRepository<FStarAdmin, Int> {
    fun findByUsername(username: String): FStarAdmin?
}