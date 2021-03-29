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
        val emailRegex by lazy { """^(?=.{1,64}@)[\p{Alnum}_-]+(\\.[\p{Alnum}_-]+)*@[^-][\p{Alnum}-]+(\\.[\p{Alnum}-]+)*(\\.[\p{Alpha}]{2,})${'$'}""".toRegex() } //https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/

        type = when {
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
                    !email.matches(emailRegex)              -> """Неверный паттерн почты, доступные символы ^(?=.{1,64}@)[\\p{Alnum}_-]+(\\\\.[\\p{Alnum}_-]+)*@[^-][\\p{Alnum}-]+(\\\\.[\\p{Alnum}-]+)*(\\\\.[\\p{Alpha}]{2,})${'$'}"""
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