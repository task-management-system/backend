package kz.seasky.tms.model.user

import kz.seasky.tms.model.role.Role

data class User(
    override val id: String,
    override val username: String,
    val name: String?,
    val email: String?,
    val isActive: Boolean,
    val role: Role
) : ShortUser

data class UserShort(
    override val id: String,
    override val username: String
) : ShortUser