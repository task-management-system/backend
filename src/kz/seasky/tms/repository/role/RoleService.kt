package kz.seasky.tms.repository.role

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.role.Role
import kz.seasky.tms.model.role.RoleInsert
import kz.seasky.tms.model.role.RoleUpdate

class RoleService(
    private val transactionService: TransactionService,
    private val repository: RoleRepository
) {
    suspend fun getAll(): List<Role> {
        return transactionService.transaction {
            repository.getAll()
        }
    }

    suspend fun insert(role: RoleInsert): Role {
        return transactionService.transaction {
            repository.insert(role)
        }
    }

    suspend fun update(role: RoleUpdate): Role {
        return transactionService.transaction {
            repository.update(role)
        }
    }

    suspend fun delete(id: Short) {
        transactionService.transaction {
            repository.delete(id)
        }
    }

    suspend fun batchInsert(roles: List<RoleInsert>): List<Role> {
        return transactionService.transaction {
            repository.batchInsert(roles)
        }
    }
}