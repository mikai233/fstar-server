package com.mdreamfever.fstar.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Message(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
    val content: String,
    var publishTime: LocalDateTime?,
    val maxVisibleBuildNumber: Int,
    val minVisibleBuildNumber: Int,
)
