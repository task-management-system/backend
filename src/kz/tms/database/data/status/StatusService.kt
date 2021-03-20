package kz.tms.database.data.status

import kz.tms.database.TransactionService
import kz.tms.model.status.StatusEntity

class StatusService(
    private val transactionService: TransactionService,
    private val repository: StatusRepository
) {
    suspend fun getAll(): List<StatusEntity> {
        return transactionService.transaction {
            repository.getAll()
        }
    }
}