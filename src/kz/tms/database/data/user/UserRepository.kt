package kz.tms.database.data.user

import kz.tms.database.data.roles.RolesTable
import kz.tms.model.user.User
import kz.tms.model.user.UserResponse
import org.jetbrains.exposed.sql.*

class UserRepository {
    fun insert(user: User): List<ResultRow>? {
        return UserTable.insert { insertStatement ->
            insertStatement.toUser(user)
        }.resultedValues
    }

    fun batchInsert(users: List<User>): List<ResultRow> {
        return UserTable.batchInsert(users) { user ->
            toUser(user)
        }
    }

    fun updateById(id: Long, user: User): Int {
        return UserTable.update(
            where = { UserTable.id eq id },
            body = { statement -> statement.toUser(user) }
        )
    }

    fun lock(id: Long): Int {
        return UserTable.update(
            where = { UserTable.id eq id },
            body = { statement -> statement[isActive] = false }
        )
    }

    fun unlock(id: Long): Int {
        return UserTable.update(
            where = { UserTable.id eq id },
            body = { statement -> statement[isActive] = true }
        )
    }

    fun deleteById(id: Long): Int {
        return UserTable.deleteWhere {
            UserTable.id eq id
        }
    }

    fun getAll(): List<UserResponse> {
        return UserTable
            .leftJoin(RolesTable)
            .selectAll()
            .map {
                toUserResponse(it)
            }
    }

    fun getByIdOrNull(id: Long): UserResponse? {
        return UserTable
            .leftJoin(RolesTable)
            .select { UserTable.id eq id }
            .map { toUserResponse(it) }
            .singleOrNull()
    }

    fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): User? {
        return UserTable
            .select { (UserTable.username eq usernameOrEmail) or (UserTable.email eq usernameOrEmail) }
            .map { toUser(it) }
            .singleOrNull()
    }
}