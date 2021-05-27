package kz.seasky.tms.model.notification

interface Notification {
    val receiverEmail: String
    val taskTitle: String
}