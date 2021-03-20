package kz.seasky.tms.database.data.status

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.status.StatusEntity

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