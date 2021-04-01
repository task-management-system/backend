package kz.seasky.tms.model.task

import kz.seasky.tms.model.status.Status

data class TaskInstance(
    val id: String,
    val task: Task,
    val status: Status
)