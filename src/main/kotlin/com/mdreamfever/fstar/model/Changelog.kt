package com.mdreamfever.fstar.model

import javax.persistence.*

@Entity
@Table(name = "changelog_v2")
data class Changelog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
    val buildNumber: Int?,
    val version: String?,
    val description: String?,
    val downloadUrl: String?
)
