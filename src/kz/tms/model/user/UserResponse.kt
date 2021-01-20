package kz.tms.model.user

import kz.tms.model.role.Role

data class UserResponse(
    val id: Long?,
    val username: String,
    val name: String?,
    val email: String?,
    val role: Role
)