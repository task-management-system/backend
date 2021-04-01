package kz.seasky.tms.model.task

import kz.seasky.tms.model.user.User

data class TaskPreview(
    val id: String,
    val title: String,
    val description: String?,
    val dueDate: String,
    val creator: User
)