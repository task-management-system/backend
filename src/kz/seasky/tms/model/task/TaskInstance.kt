package kz.seasky.tms.model.task

import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.status.Status

data class TaskInstance(
    val id: String,
    val task: Task,
    val files: List<File>,
    val status: Status
)