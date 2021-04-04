package kz.seasky.tms.repository.task

import io.ktor.http.content.*
import kotlinx.uuid.UUID
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.exceptions.WarningException
import kz.seasky.tms.extensions.asUUID
import kz.seasky.tms.extensions.copyToSuspend
import kz.seasky.tms.extensions.fileSize
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.paging.PagingResponse
import kz.seasky.tms.model.task.*
import kz.seasky.tms.utils.FILE_ROOT_DIR
import java.io.File

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository,
) {
    @Suppress("NAME_SHADOWING")
    suspend fun getReceivedPreview(
        userId: String,
        statusId: Short,
        paging: Paging
    ): PagingResponse<List<TaskInstancePreview>> {
        return transactionService.transaction {
            val userId = userId.asUUID()
            val totalCount = repository.countReceived(userId, statusId)
            val tasks = repository.getAllReceived(userId, statusId, paging)

            return@transaction PagingResponse(
                total = totalCount,
                list = tasks
            )
        }
    }


    @Suppress("NAME_SHADOWING")
    suspend fun getCreatedPreview(userId: String, statusId: Short, paging: Paging): PagingResponse<List<TaskPreview>> {
        return transactionService.transaction {
            val userId = userId.asUUID()
            val totalCount = repository.countCreated(userId, statusId)
            val tasks = repository.getAllCreated(userId, statusId, paging)

            return@transaction PagingResponse(
                total = totalCount,
                list = tasks
            )
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun createTaskAndTaskInstance(creatorId: String, newTask: TaskInsert): Task {
        return transactionService.transaction {
            val creatorId = creatorId.asUUID()
            val task = repository.insertTask(creatorId, newTask)

            newTask.executorIds.forEach { executorId ->
                repository.insertDetails(executorId.asUUID(), task.id.asUUID())
            }

            return@transaction task
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun getReceived(userId: String, taskId: UUID): TaskReceiveDetail {
        return transactionService.transaction {
            val userId = userId.asUUID()

            val task = repository.getReceived(userId, taskId) ?: throw ErrorException("Не удалось найти задачу")

            if (repository.updateInstanceStatusOrSkip(taskId)) {
                repository.updateTaskStatus(task.task.id.asUUID())
            }

            return@transaction TaskReceiveDetail(
                id = task.id,
                title = task.task.title,
                description = task.task.description,
                markdown = task.task.markdown,
                dueDate = task.task.dueDate,
                createdAt = task.task.createdAt,
                parent = TaskReceiveDetail.Task(task.task.id, task.status)
            )
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun getCreated(userId: String, taskId: UUID): TaskCreatedDetail {
        return transactionService.transaction {
            val userId = userId.asUUID()

            val task = repository.getCreated(userId, taskId) ?: throw ErrorException("Не удалось найти задачу")
            val taskInstances = repository.getCreatedInstances(taskId)

            return@transaction TaskCreatedDetail(
                id = task.id,
                title = task.title,
                description = task.description,
                markdown = task.markdown,
                dueDate = task.dueDate,
                createdAt = task.createdAt,
                taskInstances = taskInstances
            )
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun cancel(userId: String, taskId: UUID): TaskReceiveDetail {
        return transactionService.transaction {
            val userId = userId.asUUID()

            val task = repository.cancel(userId, taskId)
                ?: throw WarningException("Вы пытаетесь отменить не свое задание? А может задача уже отменена?!  А может вы хаццкер???")

            repository.updateTaskStatus(task.task.id.asUUID())

            return@transaction TaskReceiveDetail(
                id = task.id,
                title = task.task.title,
                description = task.task.description,
                markdown = task.task.markdown,
                dueDate = task.task.dueDate,
                createdAt = task.task.createdAt,
                parent = TaskReceiveDetail.Task(task.task.id, task.status)
            )
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun close(userId: String, taskId: UUID): TaskReceiveDetail {
        return transactionService.transaction {
            val userId = userId.asUUID()

            val task = repository.close(userId, taskId)
                ?: throw WarningException("Вы пытаетесь закончить не свое задание? А может задача уже закончена?! А может вы хаццкер???")

            repository.updateTaskStatus(task.task.id.asUUID())

            return@transaction TaskReceiveDetail(
                id = task.id,
                title = task.task.title,
                description = task.task.description,
                markdown = task.task.markdown,
                dueDate = task.task.dueDate,
                createdAt = task.task.createdAt,
                parent = TaskReceiveDetail.Task(task.task.id, task.status)
            )
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun delete(userId: String, taskId: UUID) {
        return transactionService.transaction {
            val userId = userId.asUUID()

            repository.delete(userId, taskId) ?: throw WarningException("А задание уже удалить нельзя, вот беда")
        }
    }

    @Suppress("NAME_SHADOWING")
    suspend fun addFile(
        userId: UUID,
        taskId: UUID,
        parts: List<PartData.FileItem>
    ): HashMap<String, MutableList<String>> {
        return transactionService.transaction {
            val keySuccess = "fileSuccess"
            val keyError = "fileError"
            val files = hashMapOf<String, MutableList<String>>(
                keySuccess to mutableListOf(),
                keyError to mutableListOf()
            )

            for (part in parts) {
                val fileName = part.originalFileName ?: continue
                val dirName = "$FILE_ROOT_DIR/$taskId"
                val dir = File(dirName)
                if (!dir.exists()) dir.mkdir()
                val ext = File(fileName).extension
                val file = File(dirName, "ts${System.currentTimeMillis()}hc${fileName.hashCode()}.$ext")
                part.streamProvider().use { input ->
                    if (input.fileSize()) {
                        file.outputStream().buffered().use { output ->
                            input.copyToSuspend(output)
                        }
                        files[keySuccess]?.add(fileName)
                    } else {
                        files[keyError]?.add(fileName)
                    }
                }

                part.dispose()
            }

            return@transaction files
        }
    }
}