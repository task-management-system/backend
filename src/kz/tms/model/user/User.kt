package kz.tms.model.user

import kz.tms.exceptions.UserChangePasswordException
import kz.tms.model.role.Role
import kz.tms.utils.MAX_PASSWORD_LENGTH
import kz.tms.utils.MIN_PASSWORD_LENGTH

interface IUser {
    val id: Long?
    val username: String
    val name: String?
    val email: String?
}

data class User(
    override val id: Long?,
    override val username: String,
    override val name: String?,
    override val email: String?
) : IUser

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

//TODO rename
data class UserChangePassword(
    val currentPassword: String,
    val newPassword: String
) {
    fun postValidate() {
        val message = when {
            newPassword.length < MIN_PASSWORD_LENGTH -> "Минимальное длина пароля $MIN_PASSWORD_LENGTH"
            newPassword.length > MAX_PASSWORD_LENGTH -> "Максимальная длина пароля $MIN_PASSWORD_LENGTH"
            else -> null
        }

        if (message != null) throw UserChangePasswordException(message)
    }
}