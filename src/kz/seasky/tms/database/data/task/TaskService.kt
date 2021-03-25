package kz.seasky.tms.database.data.task

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.TaskCreate
import kz.seasky.tms.model.task.TaskEntity
import kz.seasky.tms.model.task.TaskWithCreator

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository
) {
    suspend fun count(userId: Long): Long {
        return transactionService.transaction {
            repository.count(userId)
        }
    }

    suspend fun getAll(userId: Long, paging: Paging): List<TaskWithCreator> {
        return transactionService.transaction {
            repository.getAll(userId, paging)
        }
    }

    suspend fun insert(task: TaskCreate): TaskEntity? {
        return transactionService.transaction {
            repository.insert(task)
        }
    }

    suspend fun delete(id: Long): Int {
        return transactionService.transaction {
            repository.delete(id)
        }
    }
}