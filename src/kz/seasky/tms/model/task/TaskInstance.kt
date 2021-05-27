package kz.seasky.tms.model.task

import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.status.Status

data class TaskInstance(
    val id: String,
    val task: Task,
    val files: List<File>,
    val status: Status
) {
    fun toTaskReceiveDetail(): TaskReceiveDetail {
        return TaskReceiveDetail(
            id = id,
            title = task.title,
            description = task.description,
            markdown = task.markdown,
            dueDate = task.dueDate,
            createdAt = task.createdAt,
            creator = task.creator,
            status = status,
            files = files,
            parent = TaskReceiveDetail.Task(
                id = task.id,
                files = task.files,
                status = status
            )
        )
    }
}