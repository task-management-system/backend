package kz.seasky.tms.repository.task

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.extensions.asUUID
import kz.seasky.tms.model.detail.Detail
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.paging.PagingResponse
import kz.seasky.tms.model.task.Task
import kz.seasky.tms.model.task.TaskInsert

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository,
) {
    @Suppress("NAME_SHADOWING")
    suspend fun getReceived(userId: String, statusId: Short, paging: Paging): PagingResponse<List<Detail>> {
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
    suspend fun getCreated(userId: String, statusId: Short, paging: Paging): PagingResponse<List<Task>> {
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
    suspend fun createTaskAndDetails(creatorId: String, newTask: TaskInsert): Task {
        return transactionService.transaction {
            val creatorId = creatorId.asUUID()
            val task = repository.insertTask(creatorId, newTask)

            newTask.executorIds.forEach { executorId ->
                repository.insertDetails(executorId.asUUID(), task.id.asUUID())
            }

            task
        }
    }

//
//    suspend fun getAll(userId: Long, paging: Paging): List<TaskWithCreator> {
//        return transactionService.transaction {
//            repository.getAll(userId, paging)
//        }
//    }
//
//    suspend fun insert(task: TaskCreate): TaskEntity? {
//        return transactionService.transaction {
//            repository.insert(task)
//        }
//    }

//    suspend fun delete(id: Long): Int {
//        return transactionService.transaction {
//            repository.delete(id)
//        }
//    }
}