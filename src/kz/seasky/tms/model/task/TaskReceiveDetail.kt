package kz.seasky.tms.model.task

import com.google.gson.annotations.Expose
import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.status.Status
import kz.seasky.tms.model.user.User

data class TaskReceiveDetail(
    val id: String,
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val createdAt: String,
    @Expose(serialize = false)
    val creator: User,
    val status: Status,
    val files: List<File>,
    val parent: Task
) {
    data class Task(
        val id: String,
        val files: List<File>,
        val status: Status
    )
}