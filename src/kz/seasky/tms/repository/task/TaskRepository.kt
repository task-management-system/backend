package kz.seasky.tms.repository.task

import kotlinx.uuid.UUID
import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.database.tables.task.TaskEntity
import kz.seasky.tms.database.tables.task.TaskInstanceEntity
import kz.seasky.tms.database.tables.task.TaskInstanceTable
import kz.seasky.tms.database.tables.task.TaskTable
import kz.seasky.tms.enums.Status
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

    fun getReceived(userId: UUID, taskId: UUID): TaskInstance? {
        return TaskInstanceTable
            .innerJoin(TaskTable)
            .select { (TaskInstanceTable.executor eq userId) and (TaskInstanceTable.id eq taskId) }
            .map { row ->
                TaskInstanceEntity.wrapRow(row).toTaskInstance()
            }
            .singleOrNull()
    }

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

    /**
     * @return true if update succeeded else if not
     */
    fun updateInstanceStatusOrSkip(taskId: UUID): Boolean {
        return if (TaskInstanceEntity[taskId].status.id.value == 1.toShort()) {
            TaskInstanceTable.update(
                where = { TaskInstanceTable.id eq taskId },
                body = { statement ->
                    statement[status] = 2.toShort()
                }
            )

            true
        } else {
            false
        }
    }

    fun updateTaskStatus(taskId: UUID) {
        val tasks = TaskInstanceEntity
            .find {
                TaskInstanceTable.task eq taskId
            }
            .map(TaskInstanceEntity::toTaskInstance)

        val status = getStatus(tasks)

        TaskTable.update(
            where = { TaskTable.id eq taskId },
            body = { statement ->
                statement[TaskTable.status] = status.value
            }
        )
    }

    fun cancel(userId: UUID, taskId: UUID): TaskInstance? {
        val task = TaskInstanceEntity[taskId]

        if (task.executor.id.value != userId) return null

        if (task.status.id.value == Status.Canceled.value || task.status.id.value == Status.Closed.value) return null

        task.status = StatusEntity[Status.Canceled.value]

        return task.toTaskInstance()
    }

    fun close(userId: UUID, taskId: UUID): TaskInstance? {
        val task = TaskInstanceEntity[taskId]

        if (task.executor.id.value != userId) return null

        if (task.status.id.value == Status.Canceled.value || task.status.id.value == Status.Closed.value) return null

        task.status = StatusEntity[Status.Closed.value]

        return task.toTaskInstance()
    }

    fun delete(userId: UUID, taskId: UUID): Unit? {
        val task = TaskEntity[taskId]

        if (task.creator.id.value != userId) return null

        if (task.status.id.value != Status.New.value) return null

        return task.delete()
    }

    private fun getStatus(tasks: List<TaskInstance>): Status {
        val counts = hashMapOf(
            Status.New to tasks.filter { it.status.id == Status.New.value }.size,
            Status.InWork to tasks.filter { it.status.id == Status.InWork.value }.size,
            Status.Canceled to tasks.filter { it.status.id == Status.Canceled.value }.size,
            Status.Closed to tasks.filter { it.status.id == Status.Closed.value }.size
        )
        val count = tasks.count()

        if (counts[Status.Canceled] == count) {
            return Status.Canceled
        }

        if (counts[Status.Closed] == count - counts[Status.Canceled]!!) {
            return Status.Closed
        }

        if (counts[Status.New] == count - counts[Status.Canceled]!!) {
            return Status.New
        }

        return Status.InWork
    }
}