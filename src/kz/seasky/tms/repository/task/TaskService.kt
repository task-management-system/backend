package kz.seasky.tms.repository.task

import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.uuid.UUID
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.exceptions.WarningException
import kz.seasky.tms.extensions.asUUID
import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.file.FileInsert
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.paging.PagingResponse
import kz.seasky.tms.model.task.*
import kz.seasky.tms.utils.FILE_DEFAULT_SIZE
import kz.seasky.tms.utils.FileHelper

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository,
    private val fileHelper: FileHelper
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
    suspend fun cancelTaskInstance(userId: String, taskId: UUID): TaskReceiveDetail {
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
    suspend fun closeTask(userId: String, taskId: UUID): TaskReceiveDetail {
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
    suspend fun deleteTask(userId: String, taskId: UUID) {
        return transactionService.transaction {
            val userId = userId.asUUID()

            repository.delete(userId, taskId) ?: throw WarningException("А задание уже удалить нельзя, вот беда")
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun addFile(
        userId: UUID,
        taskId: UUID,
        parts: List<PartData.FileItem>
    ): HashMap<Char, MutableList<File>> {
        return transactionService.transaction {
            val files = hashMapOf<Char, MutableList<File>>(
                FileHelper.KEY_SUCCESS to mutableListOf(),
                FileHelper.KEY_ERROR to mutableListOf()
            )

            for (part in parts) {
                val originalFilename = part.originalFileName ?: continue
                part.streamProvider().use { input ->
                    val bytes = input.readBytes() //TODO too long if big file sent
                    val file = fileHelper.prepareFile(taskId, originalFilename, bytes)
                    val fileInsert = FileInsert(originalFilename, bytes.size, file.canonicalPath)
                    if (bytes.size in 1..FILE_DEFAULT_SIZE) {
                        val fileDescriptor = repository.insertFile(userId, taskId, fileInsert)
                        if (fileDescriptor != null) {
                            file.outputStream().use { output ->
                                withContext(Dispatchers.IO) {
                                    output.write(bytes)
                                }
                            }
                            files[FileHelper.KEY_SUCCESS]?.add(fileDescriptor)
                        } else {
                            Unit
                            val errorFileDescriptor = File(
                                id = "",
                                name = fileInsert.name,
                                size = fileInsert.size,
                                path = ""
                            )
                            files[FileHelper.KEY_ERROR]?.add(errorFileDescriptor)
                        } //My fucking god
                    } else {
                        //fixme
                        val errorFileDescriptor = File(
                            id = "",
                            name = fileInsert.name,
                            size = fileInsert.size,
                            path = ""
                        )
                        files[FileHelper.KEY_ERROR]?.add(errorFileDescriptor)
                    }
                }

                part.dispose
            }

            return@transaction files
        }
    }
}

val Any?.ignoreResult get() = Unit