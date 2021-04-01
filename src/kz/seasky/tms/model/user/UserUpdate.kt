package kz.seasky.tms.model.user

import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.ReceiveValidator
import kz.seasky.tms.utils.MAX_USERNAME_LENGTH
import kz.seasky.tms.utils.MIN_USERNAME_LENGTH

class UserUpdate(
    val id: String,
    val username: String,
    val name: String? = null,
    val email: String? = null,
    val roleId: Short
) : ReceiveValidator {
    @OptIn(UUIDExperimentalAPI::class)
    override fun <T> validate(): T {
        val usernameRegex by lazy { """\p{Alnum}+""".toRegex() } //[a-z][A-Z][0-9]

        //@formatter:off
        val message = when {
            username.length < MIN_USERNAME_LENGTH   -> "Минимальное длина имени пользователя $MIN_USERNAME_LENGTH"
            username.length > MAX_USERNAME_LENGTH   -> "Максимальная длина имени пользователя $MAX_USERNAME_LENGTH"
            !username.matches(usernameRegex)        -> "Неверный паттерн имени пользователя, доступные символы [a-z][A-Z][0-9]"
            !UUID.isValidUUIDString(id)             -> "Невалидный UUID"
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}