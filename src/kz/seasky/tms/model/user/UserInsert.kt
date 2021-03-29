package kz.seasky.tms.model.user

import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.ReceiveValidator
import kz.seasky.tms.utils.MAX_PASSWORD_LENGTH
import kz.seasky.tms.utils.MAX_USERNAME_LENGTH
import kz.seasky.tms.utils.MIN_PASSWORD_LENGTH
import kz.seasky.tms.utils.MIN_USERNAME_LENGTH

class UserInsert(
    val username: String,
    val password: String,
    val name: String? = null,
    val email: String? = null,
    val isActive: Boolean = false,
    val roleId: Short
) : ReceiveValidator {
    override fun <T> validate(): T {
        val usernameRegex by lazy { """\p{Alnum}+""".toRegex() } //[a-z][A-Z][0-9]
        val passwordRegex by lazy { """\p{Graph}+""".toRegex() } //[a-z][A-Z][0-9]!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~

        //@formatter:off
        val message = when {
            username.length < MIN_USERNAME_LENGTH   -> "Минимальное длина имени пользователя $MIN_USERNAME_LENGTH"
            username.length > MAX_USERNAME_LENGTH   -> "Максимальная длина имени пользователя $MAX_USERNAME_LENGTH"
            !username.matches(usernameRegex)        -> "Неверный паттерн имени пользователя, доступные символы [a-z][A-Z][0-9]"
            password.length < MIN_PASSWORD_LENGTH   -> "Минимальное длина пароля $MIN_PASSWORD_LENGTH"
            password.length > MAX_PASSWORD_LENGTH   -> "Максимальная длина пароля $MAX_PASSWORD_LENGTH"
            !password.matches(passwordRegex)        -> """Неверный паттерн пароля, доступные символы [a-z][A-Z][0-9]!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"""
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}