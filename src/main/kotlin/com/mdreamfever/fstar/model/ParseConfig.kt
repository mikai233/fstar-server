package com.mdreamfever.fstar.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ParseConfig(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int,
    val schoolName: String,
    val schoolUrl: String,
    val user: String,
    val author: String,
    val preUrl: String?,
    val codeUrl: String,
    var publishTime: LocalDateTime?,
    var download: Int?,
    val remark: String
)
