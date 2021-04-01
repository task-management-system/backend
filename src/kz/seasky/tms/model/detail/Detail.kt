package kz.seasky.tms.model.detail

import kz.seasky.tms.model.task.Task

data class Detail(
    val id: String,
    val task: Task
)