package kz.tms.database.data.user

import kz.tms.database.TransactionService
import kz.tms.model.Paging
import kz.tms.model.user.User
import kz.tms.model.user.UserResponse

class UserService(
    private val transactionService: TransactionService,
    private val repository: UserRepository
) {
    suspend fun count(): Long {
        return transactionService.transaction {
            repository.count()
        }
    }

    suspend fun insert(user: User): Int {
        return transactionService.transaction {
            repository.insert(user)
        }?.size ?: 0
    }

    suspend fun batchInsert(users: List<User>): Int {
        return transactionService.transaction {
            repository.batchInsert(users)
        }.size
    }

    suspend fun updateById(id: Long, user: User): Int {
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

    suspend fun deleteById(id: Long): Int {
        return transactionService.transaction {
            repository.deleteById(id)
        }
    }

    suspend fun getAll(paging: Paging): List<UserResponse> {
        return transactionService.transaction {
            repository.getAll(paging)
        }
    }

    suspend fun getByIdOrNull(id: Long): UserResponse? {
        return transactionService.transaction {
            repository.getByIdOrNull(id)
        }
    }

    suspend fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): User? {
        return transactionService.transaction {
            repository.getByUsernameOrByEmailOrNull(usernameOrEmail)
        }
    }
}