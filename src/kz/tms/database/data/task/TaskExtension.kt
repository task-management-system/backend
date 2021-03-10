package kz.tms.database.data.task

import kz.tms.database.data.detail.DetailTable
import kz.tms.database.data.user.toUser
import kz.tms.model.task.TaskCreate
import kz.tms.model.task.TaskEntity
import kz.tms.model.task.TaskWithCreator
import kz.tms.model.task.TaskWithCreatorAndDetailId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.joda.time.DateTime

fun ResultRow.toTask() = TaskEntity(
    taskId = this[TaskTable.id],
    title = this[TaskTable.title],
    description = this[TaskTable.description],
    dueDate = this[TaskTable.dueDate].millis,
    creatorId = this[TaskTable.creatorId]
)

fun ResultRow.toTaskWithCreator() = TaskWithCreator(
    taskId = get(TaskTable.id),
    title = get(TaskTable.title),
    description = get(TaskTable.description),
    dueDate = get(TaskTable.dueDate).millis,
    creator = toUser()
)

fun ResultRow.toTaskWithCreatorAndDetailId() = TaskWithCreatorAndDetailId(
    taskId = get(TaskTable.id),
    detailId = get(DetailTable.id),
    title = get(TaskTable.title),
    description = get(TaskTable.description),
    dueDate = get(TaskTable.dueDate).millis,
    creator = toUser()
)

fun InsertStatement<Number>.toTask(task: TaskCreate) {
    set(TaskTable.title, task.title)
    set(TaskTable.description, task.description)
    set(TaskTable.dueDate, DateTime(task.dueDate))
    set(TaskTable.creatorId, task.creatorId!!)
}