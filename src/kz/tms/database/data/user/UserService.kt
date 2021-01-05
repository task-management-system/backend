package kz.tms.database.data.user

import kz.tms.database.TransactionService

class UserService(
    private val transactionService: TransactionService,
    private val repository: UserRepository
) {
    suspend fun getAll(): List<User> {
        return transactionService.transaction {
            repository.getAll()
        }
    }

    suspend fun getByIdOrNull(id: Long): User? {
        return transactionService.transaction {
            repository.getByIdOrNull(id)
        }
    }

    suspend fun getByUsernameOrNull(username: String): User? {
        return transactionService.transaction {
            repository.getByUsernameOrNull(username)
        }
    }
}