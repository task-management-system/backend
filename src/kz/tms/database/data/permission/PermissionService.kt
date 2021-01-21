package kz.tms.database.data.permission

import kz.tms.database.TransactionService
import kz.tms.model.permission.Permission

class PermissionService(
    private val transactionService: TransactionService,
    private val repository: PermissionRepository
) {
    suspend fun getAll(): List<Permission> {
        return transactionService.transaction {
            repository.getAll()
        }
    }

    suspend fun getAllAsPair(): List<Pair<String, Long>> {
        return transactionService.transaction {
            repository.getAll().map { it.name to it.power }
        }
    }
}