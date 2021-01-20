package kz.tms.database.data.user

import kz.tms.database.TransactionService
import kz.tms.model.user.User
import kz.tms.model.user.UserResponse
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserService(
    private val transactionService: TransactionService,
    private val repository: UserRepository
) {
    suspend fun insert(user: User): InsertStatement<Number> {
        return transactionService.transaction {
            repository.insert(user)
        }
    }

    suspend fun getAll(): List<UserResponse> {
        return transactionService.transaction {
            repository.getAll()
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