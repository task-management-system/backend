package kz.tms.database.data.user

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserRepository {
    fun getAll(): List<User> {
        return UsersTable
            .selectAll()
            .map {
                toUser(it)
            }
    }

    fun getByIdOrNull(id: Long): User? {
        return UsersTable
            .select { UsersTable.id eq id }
            .map { toUser(it) }
            .singleOrNull()
    }

    fun getByUsernameOrNull(username: String): User? {
        return UsersTable
            .select { UsersTable.username eq username }
            .map { toUser(it) }
            .singleOrNull()
    }
}