package kz.tms.database.data.roles

import kz.tms.database.TransactionService
import kz.tms.model.role.Role

class RoleService(
    private val transactionService: TransactionService,
    private val repository: RoleRepository
) {
    suspend fun getIdByPowerOrNull(power: Int): Long? {
        return transactionService.transaction {
            repository.getIdByPowerOrNull(power)
        }
    }

    suspend fun getRoleById(id: Long): Role {
        return transactionService.transaction {
            repository.getRoleById(id)
        }
    }
}