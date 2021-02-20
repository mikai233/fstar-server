package com.mdreamfever.fstar.repository

import com.mdreamfever.fstar.model.ParseConfig
import org.springframework.data.jpa.repository.JpaRepository

interface ParseConfigRepository : JpaRepository<ParseConfig, Int> {
    fun findAllBySchoolNameStartingWith(schoolName: String): List<ParseConfig>
    fun findAllByAuthor(author: String): List<ParseConfig>
    fun findAllByUser(user: String): List<ParseConfig>
    fun deleteAllBySchoolName(schoolName: String)
    fun deleteAllByAuthor(author: String)
}