package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.FStarUser
import org.springframework.data.jpa.repository.JpaRepository

interface FStarUserRepository : JpaRepository<FStarUser, String> {
    fun findAllByAndroidVersion(androidVersion: String): List<FStarUser>
    fun findAllByBrand(brand: String): List<FStarUser>
    fun findFirstByAndroidId(androidId: String): FStarUser?
}