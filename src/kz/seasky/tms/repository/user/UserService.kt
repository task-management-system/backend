package kz.seasky.tms.repository.user

import io.ktor.http.*
import kotlinx.uuid.UUID
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.exceptions.WarningException
import kz.seasky.tms.model.user.User
import kz.seasky.tms.model.user.UserInsert
import kz.seasky.tms.model.user.UserUpdate
import kz.seasky.tms.utils.FileHelper
import java.io.InputStream

class UserService(
    private val transactionService: TransactionService,
    private val repository: UserRepository,
    private val fileHelper: FileHelper
) {
    suspend fun count(): Long {
        return transactionService.transaction {
            repository.count()
        }
    }

    suspend fun insert(user: UserInsert): User {
        return transactionService.transaction {
            repository.insert(user) ?: throw ErrorException("Не удалось добавить пользователя")
        }
    }

    suspend fun batchInsert(users: List<UserInsert>): List<User?> {
        return transactionService.transaction {
            repository.batchInsert(users)
        }
    }

    suspend fun update(user: UserUpdate): User {
        return transactionService.transaction {
            repository.updateById(user) ?: throw ErrorException("Не удалось обновить пользователя")
        }
    }

    suspend fun updateAsUser(user: UserUpdate): User {
        return transactionService.transaction {
            repository.updateByIdAsUser(user) ?: throw ErrorException("Не удалось обновить пользователя")
        }
    }

    suspend fun lock(id: UUID): User {
        return transactionService.transaction {
            repository.lock(id) ?: throw WarningException("Пользователь и так заблокирован")
        }
    }

    suspend fun unlock(id: UUID): User {
        return transactionService.transaction {
            repository.unlock(id) ?: throw WarningException("Пользователь и так разблокирован")
        }
    }

    suspend fun validatePassword(id: UUID, password: String) {
        return transactionService.transaction {
            val exception = ErrorException("Неверный старый пароль, проверьте корректность введенных данных")
            val result = repository.validatePassword(id, password) ?: throw exception
            if (!result) throw exception
        }
    }

    suspend fun changePassword(id: UUID, newPassword: String): User {
        return transactionService.transaction {
            repository.changePassword(id, newPassword) ?: throw ErrorException("Не удалось сменить пароль")
        }
    }

    suspend fun uploadAvatar(id: UUID, contentType: ContentType, stream: InputStream): User? {
        return transactionService.transaction {
            val fileSize: Long = 10 * 1024 * 1024
            val image = stream.use { fileHelper.readBytesAndValidate(it, fileSize) }
            if (image.isEmpty()) throw ErrorException("Не удалось загрузить аватар")
            return@transaction repository.uploadAvatar(id, contentType, image)
        }
    }

    suspend fun deleteById(id: String) {
        return transactionService.transaction {
            repository.deleteById(UUID(id))
        }
    }

    suspend fun getAll(): List<User> {
        return transactionService.transaction {
            repository.getAll()
        }
    }

    suspend fun getById(id: String): User {
        return transactionService.transaction {
            repository.getByIdOrNull(UUID(id)) ?: throw ErrorException("Не удалось найти пользователя")
        }
    }

    suspend fun getAllAvailable(userId: UUID): List<User> {
        return transactionService.transaction {
            return@transaction repository.getAllAvailable(userId)
        }
    }
}