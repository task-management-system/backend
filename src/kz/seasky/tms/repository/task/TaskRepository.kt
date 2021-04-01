package kz.seasky.tms.repository.task

import kotlinx.uuid.UUID
import kz.seasky.tms.database.tables.task.TaskEntity
import kz.seasky.tms.database.tables.task.TaskInstanceEntity
import kz.seasky.tms.database.tables.task.TaskInstanceTable
import kz.seasky.tms.database.tables.task.TaskTable
import kz.seasky.tms.extensions.all
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class TaskRepository {
    fun countReceived(userId: UUID, statusId: Short): Long {
        return TaskInstanceEntity
            .find { (TaskInstanceTable.executor eq userId) and (TaskInstanceTable.status eq statusId) }
            .count()
    }

    fun countCreated(userId: UUID, statusId: Short): Long {
        return TaskEntity
            .find { (TaskTable.creator eq userId) and (TaskTable.status eq statusId) }
            .count()
    }

    fun getAllReceived(userId: UUID, statusId: Short, paging: Paging): List<TaskInstancePreview> {
        return TaskInstanceTable
            .innerJoin(TaskTable)
            .select { (TaskInstanceTable.executor eq userId) and (TaskInstanceTable.status eq statusId) }
            .all(TaskTable.id, paging)
            .map { row ->
                TaskInstanceEntity.wrapRow(row).toTaskInstancePreview()
            }
    }

    fun getAllCreated(userId: UUID, statusId: Short, paging: Paging): List<TaskPreview> {
        return TaskEntity
            .find { (TaskTable.creator eq userId) and (TaskTable.status eq statusId) }
            .all(TaskTable.createdAt, paging)
            .map(TaskEntity::toTaskPreview)
    }

    fun insertTask(creatorId: UUID, task: TaskInsert): Task {
        return TaskEntity
            .insert(creatorId, task)
    }

    fun insertDetails(executorId: UUID, taskId: UUID): TaskInstance {
        return TaskInstanceEntity
            .insert(executorId, taskId)
    }

    //FIXME
    fun getReceived(userId: UUID, taskId: UUID): TaskInstance? {
        return TaskInstanceTable
            .innerJoin(TaskTable)
            .select { (TaskInstanceTable.executor eq userId) and (TaskInstanceTable.id eq taskId) }
            .map { row ->
                TaskInstanceEntity.wrapRow(row).toTaskInstance()
            }
            .singleOrNull()
    }

    //FIXME
    fun getCreated(userId: UUID, taskId: UUID): Task? {
        return TaskEntity
            .find { (TaskTable.creator eq userId) and (TaskTable.id eq taskId) }
            .firstOrNull()
            ?.toTask()
    }

    fun getCreatedInstances(taskId: UUID): List<TaskCreatedDetail.TaskInstance> {
        return TaskInstanceEntity
            .find { (TaskInstanceTable.task eq taskId) }
            .map { taskInstance ->
                TaskCreatedDetail.TaskInstance(
                    id = taskInstance.id.toString(),
                    status = taskInstance.status.toStatus()
                )
            }
    }

    fun updateStatusOrSkip(taskId: UUID) {
        if (TaskInstanceEntity[taskId].status.id.value == 1.toShort()) {
            TaskInstanceTable.update(
                where = { TaskInstanceTable.id eq taskId },
                body = { r ->
                    r[status] = 2.toShort()
                }
            )
        }
    }
}