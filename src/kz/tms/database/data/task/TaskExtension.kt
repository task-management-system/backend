package kz.tms.database.data.task

import kz.tms.model.task.Task
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toTask() = Task(
    id = this[TaskTable.id],
    title = this[TaskTable.title],
    description = this[TaskTable.description],
    dueDate = this[TaskTable.dueDate].millis,
    creatorId = this[TaskTable.creatorId]
)