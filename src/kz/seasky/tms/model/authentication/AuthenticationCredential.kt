package kz.seasky.tms.model.authentication

import io.ktor.auth.*
import kz.seasky.tms.enums.AuthenticationType
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.ReceiveValidator
import kz.seasky.tms.utils.MAX_PASSWORD_LENGTH
import kz.seasky.tms.utils.MAX_USERNAME_LENGTH
import kz.seasky.tms.utils.MIN_PASSWORD_LENGTH
import kz.seasky.tms.utils.MIN_USERNAME_LENGTH

data class AuthenticationCredential(
    val username: String? = null,
    val email: String? = null,
    val password: String
) : Credential, ReceiveValidator {

    lateinit var type: AuthenticationType

    @Suppress("UNCHECKED_CAST")
    override fun <T> validate(): T {
        val usernameRegex by lazy { """\p{Alnum}+""".toRegex() } //[a-z][A-Z][0-9]
        val passwordRegex by lazy { """\p{Graph}+""".toRegex() } //[a-z][A-Z][0-9]!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        val emailRegex by lazy { """(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""".toRegex() }

        type = when {
            username == null && email == null -> throw ErrorException("Укажите имя пользователя или почту")
            username == null -> AuthenticationType.Email
            email == null -> AuthenticationType.Username
            else -> throw ErrorException("Укажите имя пользователя или почту")
        }

        var message: String? = null

        //@formatter:off
        when (type) {
            AuthenticationType.Username -> {
                requireNotNull(username) //Cast to not null because it cannot be null
                message = when {
                    username.length < MIN_USERNAME_LENGTH   -> "Минимальное длина имени пользователя $MIN_USERNAME_LENGTH"
                    username.length > MAX_USERNAME_LENGTH   -> "Максимальная длина имени пользователя $MAX_USERNAME_LENGTH"
                    !username.matches(usernameRegex)        -> "Неверный паттерн имени пользователя, доступные символы [a-z][A-Z][0-9]"
                    password.length < MIN_PASSWORD_LENGTH   -> "Минимальное длина пароля $MIN_PASSWORD_LENGTH"
                    password.length > MAX_PASSWORD_LENGTH   -> "Максимальная длина пароля $MAX_PASSWORD_LENGTH"
                    !password.matches(passwordRegex)        -> """Неверный паттерн пароля, доступные символы [a-z][A-Z][0-9]!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"""
                    else -> null
                }
            }
            AuthenticationType.Email -> {
                requireNotNull(email) //Cast to not null because it cannot be null
                message = when {
                    !email.matches(emailRegex)              -> """Неверный паттерн почты, доступные символы (?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])"""
                    password.length < MIN_PASSWORD_LENGTH   -> "Минимальное длина пароля $MIN_PASSWORD_LENGTH"
                    password.length > MAX_PASSWORD_LENGTH   -> "Максимальная длина пароля $MAX_PASSWORD_LENGTH"
                    !password.matches(passwordRegex)        -> """Неверный паттерн пароля, доступные символы [a-z][A-Z][0-9]!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"""
                    else -> null
                }
            }
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        return this as T
    }
}