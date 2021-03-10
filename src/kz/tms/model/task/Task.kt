package kz.tms.model.task

import kz.tms.model.user.User

interface ITask {
    val taskId: Long?
    val title: String
    val description: String?
    val dueDate: Long
}

data class TaskEntity(
    override val taskId: Long,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    val creatorId: Long?
) : ITask

data class TaskWithCreator(
    override val taskId: Long?,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    val creator: User
) : ITask

data class TaskWithCreatorAndDetailId(
    override val taskId: Long?,
    val detailId: Long?,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    val creator: User
) : ITask

data class TaskCreate(
    override val taskId: Long? = null,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    var creatorId: Long?,
    val executorIds: List<Long>,
) : ITask

data class DetailCreate(
    val taskId: Long,
    val executorId: Long,
    val statusId: Short
)