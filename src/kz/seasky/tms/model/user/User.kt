package kz.seasky.tms.model.user

import kz.seasky.tms.model.role.Role

data class User(
    val id: String,
    val username: String,
    val name: String?,
    val email: String?,
    val isActive: Boolean,
    val role: Role
)