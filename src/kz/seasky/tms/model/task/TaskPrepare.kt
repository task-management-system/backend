package kz.seasky.tms.model.task

import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.isValidTime
import kz.seasky.tms.model.ReceiveValidator

open class TaskPrepare(
    @Transient open val title: String,
    @Transient open val description: String?,
    @Transient open val markdown: String?,
    @Transient open val dueDate: String
) : ReceiveValidator {
    companion object {
        const val MIN_TITLE_LENGTH = 4
    }

    override fun <T> validate(): T {
        //@formatter:off
        val message = when {
            title.isBlank()                 -> "Значение поля title не может быть пустым"
            title.length < MIN_TITLE_LENGTH -> "Значение поля title не может быть меньше $MIN_TITLE_LENGTH"
            !dueDate.isValidTime()          -> "Невалидное значение поля dueDate"
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}