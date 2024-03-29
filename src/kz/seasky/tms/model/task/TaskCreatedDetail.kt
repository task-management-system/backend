package kz.seasky.tms.model.task

import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.status.Status
import kz.seasky.tms.model.user.User

data class TaskCreatedDetail(
    val id: String,
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val createdAt: String,
    val status: Status,
    val files: List<File>,
    val taskInstances: List<TaskInstance>
) {
    data class TaskInstance(
        val id: String,
        val executor: User,
        val status: Status,
        val files: List<File>
    )
}
