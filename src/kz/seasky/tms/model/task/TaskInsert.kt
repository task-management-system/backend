package kz.seasky.tms.model.task

import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.isValidTime
import kz.seasky.tms.extensions.isValidUUID
import kz.seasky.tms.model.ReceiveValidator

class TaskInsert(
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val executorIds: Set<String>
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
            executorIds.isEmpty()           -> "Значение поля executorIds не может быть пустым"
            executorIds.any { executorId ->
                !executorId.isValidUUID()
            }                               -> "Невалидный UUID в поле executorsIds"
            else -> null
        }
        //@formatter:on

        if (message != null) throw ErrorException(message)

        @Suppress("UNCHECKED_CAST")
        return this as T
    }
}