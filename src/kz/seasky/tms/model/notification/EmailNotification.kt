package kz.seasky.tms.model.notification

class EmailNotification(
    val receiverEmail: String,
    val taskTitle: String,
    val creatorName: String,
    val taskDueDate: String
)