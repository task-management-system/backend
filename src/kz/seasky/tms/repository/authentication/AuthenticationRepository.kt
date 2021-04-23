package kz.seasky.tms.repository.authentication

import kotlinx.uuid.UUID
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.database.tables.user.UserEntity
import kz.seasky.tms.database.tables.user.UserTable
import kz.seasky.tms.enums.AuthenticationType
import kz.seasky.tms.extensions.crypt
import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.model.user.User
import org.jetbrains.exposed.sql.select

class AuthenticationRepository(private val transactionService: TransactionService) {
    suspend fun getUser(credentials: AuthenticationCredential): User? = transactionService.transaction {
        UserEntity.find {
            when (credentials.type) {
                AuthenticationType.Username -> (UserTable.username eq credentials.username!!)
                AuthenticationType.Email -> (UserTable.email eq credentials.email!!)
            }
        }.firstOrNull()?.toUser()
    }

    /**
     * @return true if [password] equals with password in database, false if not
     */
    suspend fun validatePassword(id: String, password: String): Boolean = transactionService.transaction {
        val match = UserTable.password.crypt(password)
        UserTable
            .slice(match)
            .select { UserTable.id eq UUID(id) }
            .map { row -> row[match] }
            .firstOrNull() ?: false
    }
}
