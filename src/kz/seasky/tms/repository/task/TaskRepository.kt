package kz.seasky.tms.repository.task

import kotlinx.uuid.UUID
import kz.seasky.tms.database.tables.file.FileEntity
import kz.seasky.tms.database.tables.file.TaskFileTable
import kz.seasky.tms.database.tables.file.TaskInstanceFileTable
import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.database.tables.task.TaskEntity
import kz.seasky.tms.database.tables.task.TaskInstanceEntity
import kz.seasky.tms.database.tables.task.TaskInstanceTable
import kz.seasky.tms.database.tables.task.TaskTable
import kz.seasky.tms.enums.Status
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.all
import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.file.FileInsert
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

class TaskRepository {
    fun countAllByStatus(): Map<Short, Long> {
        return TaskTable
            .slice(TaskTable.status, TaskTable.status.count())
            .selectAll()
            .groupBy(TaskTable.status)
            .associate { row ->
                row[TaskTable.status].value to row[TaskTable.status.count()]
            }
    }

    fun countAllByStatus(dueDate: DateTime): Map<Short, Long> {
        return TaskTable
            .slice(TaskTable.status, TaskTable.status.count())
            .select { TaskTable.dueDate greaterEq dueDate }
            .groupBy(TaskTable.status)
            .associate { row ->
                row[TaskTable.status].value to row[TaskTable.status.count()]
            }
    }

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

    fun prepareTask(creatorId: UUID, task: TaskPrepare): Task {
        return TaskEntity.insert(
            creatorId = creatorId,
            task = task,
            statusId = Status.Prepared.value
        ).toTask()
    }

    fun insertTask(creatorId: UUID, task: TaskInsert): Task {
        return TaskEntity.insert(
            creatorId = creatorId,
            task = task,
            statusId = Status.New.value
        ).toTask()
    }

    fun insertDetails(executorId: UUID, taskId: UUID): TaskInstance {
        return TaskInstanceEntity
            .insert(executorId, taskId)
    }

    fun getReceived(userId: UUID, taskInstanceId: UUID): TaskInstance? {
        return TaskInstanceTable
            .innerJoin(TaskTable)
            .select { (TaskInstanceTable.executor eq userId) and (TaskInstanceTable.id eq taskInstanceId) }
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
                    executor = taskInstance.executor.toUser(),
                    status = taskInstance.status.toStatus(),
                    files = taskInstance.file.map(FileEntity::toFile)
                )
            }
    }

    /**
     * @return true if update succeeded else if not
     */
    fun updateInstanceStatusOrSkip(taskInstanceId: UUID): Boolean {
        val taskInstance = TaskInstanceEntity[taskInstanceId]
        return if (taskInstance.status.id.value == Status.New.value) {
            TaskInstanceTable.update(
                where = { TaskInstanceTable.id eq taskInstanceId },
                body = { statement ->
                    statement[status] = Status.InWork.value
                }
            )

            taskInstance.refresh()
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

    /**
     * @return [Pair] where
     * [Pair.first] is nullable created [File],
     * [Pair.second] is nullable exception message
     */
    fun insertFileToCreated(userId: UUID, taskId: UUID, file: FileInsert): Pair<File?, String?> {
        try {
            val task = TaskEntity[taskId]

            if (task.creator.id.value != userId) return null to "Так не пойдет, ты хто такой?"

            val statusId = task.status.id.value
            if (statusId == Status.InWork.value || statusId == Status.Canceled.value || statusId == Status.Closed.value) return null to "Не удалось добавить запись, задачка уже в процессе"

            val fileEntity = FileEntity.insert(file)

            TaskFileTable.insert { statement ->
                statement[TaskFileTable.task] = taskId
                statement[TaskFileTable.file] = fileEntity.id
            }

            return fileEntity.toFile() to null
        } catch (e: ExposedSQLException) {
            return null to "Не удалось добавить запись, возможно дубликаты по коням!"
        }
    }

    /**
     * @return [Pair] where
     * [Pair.first] is nullable created [File],
     * [Pair.second] is nullable exception message
     */
    @Suppress("FoldInitializerAndIfToElvis")
    fun insertFileToReceived(userId: UUID, taskInstanceId: UUID, file: FileInsert): Pair<File?, String?> {
        try {
            val taskInstance = TaskInstanceEntity[taskInstanceId]

            if (taskInstance.executor.id.value != userId) return null to "Так не пойдет, ты хто такой?"

            if (taskInstance.status.id.value == Status.InWork.value) {
                val fileEntity = FileEntity.insert(file)

                TaskInstanceFileTable.insert { statement ->
                    statement[TaskInstanceFileTable.taskInstance] = taskInstanceId
                    statement[TaskInstanceFileTable.file] = fileEntity.id
                }

                return fileEntity.toFile() to null
            }

            return null to "Не удалось добавить запись, задачка уже закрыта :/"
        } catch (e: ExposedSQLException) {
            return null to "Не удалось добавить запись, возможно дубликаты по коням!"
        }
    }

    fun getFile(userId: UUID, fileId: UUID): File {
        if (!validateFile(userId, fileId)) throw ErrorException("Не удалось провалидировать файл")
        return FileEntity[fileId].toFile()
    }

    fun deleteFile(userId: UUID, fileId: UUID): File {
        if (!validateFile(userId, fileId)) throw ErrorException("Не удалось провалидировать файл")
        val file = FileEntity[fileId]
        file.delete()
        return file.toFile()
    }

    /**
     * @return true if file count > 0, false otherwise
     */
    private fun validateFile(userId: UUID, fileId: UUID): Boolean {
        val count = TaskTable
            .leftJoin(TaskFileTable)
            .innerJoin(TaskInstanceTable)
            .leftJoin(TaskInstanceFileTable)
            .select {
                //@formatter:off
                ((TaskTable.creator eq userId) or (TaskInstanceTable.executor eq userId)) and
                ((TaskFileTable.file eq fileId) or (TaskInstanceFileTable.file eq fileId))
                //@formatter:on
            }
            .count()

        return count > 0
    }

    private fun getStatus(tasks: List<TaskInstance>): Status {
        //@formatter:off
        val counts = hashMapOf(
            Status.New      to tasks.filter { it.status.id == Status.New.value }.size,
            Status.InWork   to tasks.filter { it.status.id == Status.InWork.value }.size,
            Status.Canceled to tasks.filter { it.status.id == Status.Canceled.value }.size,
            Status.Closed   to tasks.filter { it.status.id == Status.Closed.value }.size
        )
        //@formatter:on
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