package kz.seasky.tms.model.task

import kz.seasky.tms.model.status.Status

data class TaskReceiveDetail(
    val id: String,
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val createdAt: String,
    val parent: Task
) {
    data class Task(
        val id: String,
        val status: Status
    )
}