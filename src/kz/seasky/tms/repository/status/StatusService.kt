package kz.seasky.tms.repository.status

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.status.Status

class StatusService(
    private val transactionService: TransactionService,
    private val repository: StatusRepository
) {
    suspend fun getAll(): List<Status> {
        return transactionService.transaction {
            repository.getAll()
        }
    }
}