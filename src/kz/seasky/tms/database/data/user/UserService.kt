package kz.seasky.tms.database.data.user

import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.user.IUser
import kz.seasky.tms.model.user.UserEntity
import kz.seasky.tms.model.user.UserWithRole

class UserService(
    private val transactionService: TransactionService,
    private val repository: UserRepository
) {
    suspend fun count(): Long {
        return transactionService.transaction {
            repository.count()
        }
    }

    suspend fun insert(userEntity: UserEntity): Int {
        return transactionService.transaction {
            repository.insert(userEntity)
        }?.size ?: 0
    }

    suspend fun batchInsert(userEntities: List<UserEntity>): Int {
        return transactionService.transaction {
            repository.batchInsert(userEntities)
        }.size
    }

    suspend fun updateById(id: Long, user: IUser): Int {
        return transactionService.transaction {
            repository.updateById(id, user)
        }
    }

    suspend fun lock(id: Long): Int {
        return transactionService.transaction {
            repository.lock(id)
        }
    }

    suspend fun unlock(id: Long): Int {
        return transactionService.transaction {
            repository.unlock(id)
        }
    }

    //TODO rewrite this shit
    suspend fun validatePassword(id: Long, currentPassword: String): UserEntity? {
        return transactionService.transaction {
            repository.validatePassword(id, currentPassword)
        }
    }

    suspend fun changePassword(id: Long, newPassword: String): Int {
        return transactionService.transaction {
            repository.changePassword(id, newPassword)
        }
    }

    suspend fun deleteById(id: Long): Int {
        return transactionService.transaction {
            repository.deleteById(id)
        }
    }

    suspend fun getAll(): List<UserWithRole> {
        return transactionService.transaction {
            repository.getAll()
        }
    }

    suspend fun getAll(paging: Paging): List<UserWithRole> {
        return transactionService.transaction {
            repository.getAll(paging)
        }
    }

    suspend fun getByIdOrNull(id: Long): UserWithRole? {
        return transactionService.transaction {
            repository.getByIdOrNull(id)
        }
    }

    suspend fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): UserEntity? {
        return transactionService.transaction {
            repository.getByUsernameOrByEmailOrNull(usernameOrEmail)
        }
    }
}