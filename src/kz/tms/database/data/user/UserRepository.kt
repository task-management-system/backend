package kz.tms.database.data.user

import kz.tms.database.data.roles.RolesTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UserRepository {
    fun getAll(): List<User> {
        return UsersTable
            .leftJoin(RolesTable)
            .selectAll()
            .map {
                toUser(it)
            }
    }

    fun getByIdOrNull(id: Long): User? {
        return UsersTable
            .leftJoin(RolesTable)
            .select { UsersTable.id eq id }
            .map { toUser(it) }
            .singleOrNull()
    }

    fun getByUsernameOrNull(username: String): User? {
        return UsersTable
            .leftJoin(RolesTable)
            .select { UsersTable.username eq username }
            .map { toUser(it) }
            .singleOrNull()
    }
}