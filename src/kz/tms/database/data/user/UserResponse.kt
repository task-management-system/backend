package kz.tms.database.data.user

import kz.tms.database.data.roles.Role

data class UserResponse(
    val username: String,
    val name: String?,
    val email: String?,
    val role: Role
)