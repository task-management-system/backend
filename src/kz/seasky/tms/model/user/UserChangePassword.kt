package kz.seasky.tms.model.user

import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.isValidUUID
import kz.seasky.tms.model.ReceiveValidator
import kz.seasky.tms.model.WithId
import kz.seasky.tms.utils.MAX_PASSWORD_LENGTH
import kz.seasky.tms.utils.MIN_PASSWORD_LENGTH

class UserChangePassword(
    override val id: String,
    val oldPassword: String,
    val newPassword: String
) : ReceiveValidator, WithId {
    override fun <T> validate(): T {
        val passwordRegex by lazy { """\p{Graph}+""".toRegex() } //[a-z][A-Z][0-9]!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~

        //@formatter:off
        val message = when {
            newPassword.length < MIN_PASSWORD_LENGTH  -> "Минимальное длина нового пароля $MIN_PASSWORD_LENGTH"
            newPassword.length > MAX_PASSWORD_LENGTH  -> "Максимальная длина нового пароля $MAX_PASSWORD_LENGTH"
            !newPassword.matches(passwordRegex)       -> """Неверный паттерн нового пароля, доступные символы [a-z][A-Z][0-9]!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"""
            !id.isValidUUID()                         -> "Невалидный UUID"
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}