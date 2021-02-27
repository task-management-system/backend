package kz.tms.database.data.detail

import kz.tms.database.TransactionService
import kz.tms.model.paging.Paging
import kz.tms.model.task.Task

class DetailService(
    private val transactionService: TransactionService,
    private val repository: DetailRepository
) {
    suspend fun count(userId: Long, statusId: Short): Long {
        return transactionService.transaction {
            repository.count(userId, statusId)
        }
    }

    suspend fun getAll(userId: Long, statusId: Short, paging: Paging): List<Task> {
        return transactionService.transaction {
            repository.getAll(userId, statusId, paging)
        }
    }
}