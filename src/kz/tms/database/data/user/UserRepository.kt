package kz.tms.database.data.user

import kz.tms.database.data.roles.RolesTable
import kz.tms.model.user.User
import kz.tms.model.user.UserResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepository {
    fun insert(user: User): InsertStatement<Number> {
        return UsersTable.insert { insertStatement ->
            insertStatement.toUser(user)
        }
    }

    fun deleteById(id: Long): Int {
        return UsersTable.deleteWhere {
            UsersTable.id eq id
        }
    }

    fun getAll(): List<UserResponse> {
        return UsersTable
            .leftJoin(RolesTable)
            .selectAll()
            .map {
                toUserResponse(it)
            }
    }

    fun getByIdOrNull(id: Long): UserResponse? {
        return UsersTable
            .leftJoin(RolesTable)
            .select { UsersTable.id eq id }
            .map { toUserResponse(it) }
            .singleOrNull()
    }

    fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): User? {
        return UsersTable
            .select { (UsersTable.username eq usernameOrEmail) or (UsersTable.email eq usernameOrEmail) }
            .map { toUser(it) }
            .singleOrNull()
    }
}