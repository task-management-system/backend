package kz.tms.model.task

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val dueDate: Long,
    val creatorId: Long
)