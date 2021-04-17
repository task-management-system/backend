package kz.seasky.tms.model.task

import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.status.Status

data class TaskCreatedDetail(
    val id: String,
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val createdAt: String,
    val files: List<File>,
    val taskInstances: List<TaskInstance>
) {
    data class TaskInstance(
        val id: String,
        val status: Status
    )
}
