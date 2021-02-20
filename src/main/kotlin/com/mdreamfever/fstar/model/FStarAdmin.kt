package com.mdreamfever.fstar.model

import javax.persistence.*

@Entity
@Table(name = "fstar_admin")
data class FStarAdmin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
    val username: String,
    val password: String,
    val roles: String?
)
