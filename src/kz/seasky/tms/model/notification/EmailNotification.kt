package kz.seasky.tms.model.notification

sealed class EmailNotification : Notification {
    data class NewTask(
        override val receiverEmail: String,
        override val taskTitle: String,
        val creatorName: String,
        val taskDueDate: String
    ) : EmailNotification()

    data class CloseTask(
        override val receiverEmail: String,
        override val taskTitle: String,
        val executorNames: List<String>,
        val taskCreatedDate: String,
        val taskDueDate: String
    ) : EmailNotification()

    data class DeleteTask(
        override val receiverEmail: String,
        override val taskTitle: String,
        val creatorName: String
    ) : EmailNotification()
}