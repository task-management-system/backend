package kz.tms.database.data.task

import kz.tms.database.TransactionService
import kz.tms.model.paging.Paging
import kz.tms.model.task.Task

class TaskService(
    private val transactionService: TransactionService,
    private val repository: TaskRepository
) {
    suspend fun count(userId: Long): Long {
        return transactionService.transaction {
            repository.count(userId)
        }
    }

    suspend fun getAll(userId: Long, paging: Paging): List<Task> {
        return transactionService.transaction {
            repository.getAll(userId, paging)
        }
    }
}