package kz.seasky.tms.model.role

import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.ReceiveValidator

open class RoleValidator(
    @Transient open val power: Long,
    @Transient open val meaning: String
) : ReceiveValidator {
    override fun <T> validate(): T {
        //@formatter:off
        val message = when {
            power < 0         -> "Значение поля power не может быть отрицательным"
            meaning.isBlank() -> "Значение поля meaning не может быть пустым"
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}