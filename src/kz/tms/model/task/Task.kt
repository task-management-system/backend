package kz.tms.model.task

interface ITask {
    val id: Long?
    val title: String
    val description: String?
    val dueDate: Long
    var creatorId: Long?
}

data class TaskEntity(
    override val id: Long,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    override var creatorId: Long?
) : ITask

data class TaskCreate(
    override val id: Long? = null,
    override val title: String,
    override val description: String?,
    override val dueDate: Long,
    override var creatorId: Long?,
    val executorIds: List<Long>,
) : ITask

data class DetailCreate(
    val taskId: Long,
    val executorId: Long,
    val statusId: Short
)