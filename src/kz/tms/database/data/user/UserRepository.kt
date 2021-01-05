package kz.tms.database.data.user

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserRepository {
    fun getAll(): List<User> {
        return UserTable
            .selectAll()
            .map {
                toUser(it)
            }
    }

    fun getByIdOrNull(id: Long): User? {
        return UserTable
            .select { UserTable.id eq id }
            .map { toUser(it) }
            .singleOrNull()
    }

    fun getByUsernameOrNull(username: String): User? {
        return UserTable
            .select { UserTable.username eq username }
            .map { toUser(it) }
            .singleOrNull()
    }
}