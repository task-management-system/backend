package kz.tms.model.user

import kz.tms.model.role.Role

interface IUser {
    val id: Long?
    val username: String
    val name: String?
    val email: String?
}

data class UserEntity(
    override val id: Long? = null,
    override val username: String,
    override val name: String?,
    override val email: String?,
    val password: String,
    val isActive: Boolean?,
    val roleId: Long
) : IUser

data class UserWithRoleId(
    override val id: Long?,
    override val username: String,
    override val name: String?,
    override val email: String?,
    val roleId: Long
) : IUser

data class UserWithRole(
    override val id: Long?,
    override val username: String,
    override val name: String?,
    override val email: String?,
    val isActive: Boolean?,
    val role: Role
) : IUser