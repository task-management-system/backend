package kz.seasky.tms.repository.task

import kotlinx.uuid.UUID
import kz.seasky.tms.database.tables.detail.DetailEntity
import kz.seasky.tms.database.tables.detail.DetailTable
import kz.seasky.tms.database.tables.task.TaskEntity
import kz.seasky.tms.database.tables.task.TaskTable
import kz.seasky.tms.extensions.all
import kz.seasky.tms.model.detail.Detail
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.Task
import kz.seasky.tms.model.task.TaskInsert
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

class TaskRepository {
    fun countReceived(userId: UUID, statusId: Short): Long {
        return DetailEntity
            .find { (DetailTable.executor eq userId) and (DetailTable.status eq statusId) }
            .count()
    }

    fun countCreated(userId: UUID, statusId: Short): Long {
        return TaskEntity
            .find { (TaskTable.creator eq userId) and (TaskTable.status eq statusId) }
            .count()
    }

    fun getAllReceived(userId: UUID, statusId: Short, paging: Paging): List<Detail> {
        return DetailTable
            .innerJoin(TaskTable)
            .select { (DetailTable.executor eq userId) and (DetailTable.status eq statusId) }
            .all(TaskTable.id, paging)
            .map { row ->
                DetailEntity.wrapRow(row).toDetail()
            }
    }

    fun getAllCreated(userId: UUID, statusId: Short, paging: Paging): List<Task> {
        return TaskEntity
            .find { (TaskTable.creator eq userId) and (TaskTable.status eq statusId) }
            .all(TaskTable.createdAt, paging)
            .map(TaskEntity::toTask)
    }

    fun insertTask(creatorId: UUID, task: TaskInsert): Task {
        return TaskEntity
            .insert(creatorId, task)
    }

    fun insertDetails(executorId: UUID, taskId: UUID): Detail {
        return DetailEntity
            .insert(executorId, taskId)
    }
}