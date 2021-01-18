package kz.tms.database.data.user

import kz.tms.database.data.roles.Role

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val name: String?,
    val email: String?,
    val role: Role
)