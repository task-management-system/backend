package kz.seasky.tms.database.data.role

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.role.Role

class RoleService(
    private val transactionService: TransactionService,
    private val repository: RoleRepository
) {
    suspend fun getAllOrEmpty(): List<Role> {
        return transactionService.transaction {
            repository.getAllOrEmpty()
        }
    }

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

    suspend fun insert(role: Role): Int {
        return transactionService.transaction {
            repository.insert(role)?.size ?: 0
        }
    }

    suspend fun batchInsert(roles: List<Role>): Int {
        return transactionService.transaction {
            repository.batchInsert(roles)
        }.size
    }

    suspend fun update(id: Long, role: Role): Int {
        return transactionService.transaction {
            repository.update(id, role)
        }
    }
}