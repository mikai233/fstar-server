package com.mdreamfever.fstar.model

import javax.persistence.*

@Entity
@Table(name = "fstar_user")
data class FStarUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
    val appVersion: String?,
    val buildNumber: Int?,
    val androidId: String?,
    val androidVersion: String?,
    val brand: String?,
    val device: String?,
    val model: String?,
    val product: String?,
    val platform: String
)
