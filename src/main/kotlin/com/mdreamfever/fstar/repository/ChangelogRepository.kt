package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.Changelog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

interface ChangelogRepository : JpaRepository<Changelog, Int> {
    fun findAllByBuildNumberLessThanEqual(buildNumber: Int): List<Changelog>
}