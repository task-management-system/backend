package kz.tms.database.data.task

import kz.tms.model.task.ITask
import kz.tms.model.task.TaskEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.joda.time.DateTime

fun ResultRow.toTask() = TaskEntity(
    id = this[TaskTable.id],
    title = this[TaskTable.title],
    description = this[TaskTable.description],
    dueDate = this[TaskTable.dueDate].millis,
    creatorId = this[TaskTable.creatorId]
)

fun InsertStatement<Number>.toTask(task: ITask) {
    set(TaskTable.title, task.title)
    set(TaskTable.description, task.description)
    set(TaskTable.dueDate, DateTime(task.dueDate))
    set(TaskTable.creatorId, task.creatorId!!)
}