package kz.tms.database.data.user

import kz.tms.database.data.role.RoleTable
import kz.tms.model.paging.Paging
import kz.tms.model.user.IUser
import kz.tms.model.user.UserEntity
import kz.tms.model.user.UserWithRole
import kz.tms.utils.selectAll
import org.jetbrains.exposed.sql.*

class UserRepository {
    fun count(): Long {
        return UserTable
            .selectAll()
            .count()
    }

    fun insert(userEntity: UserEntity): List<ResultRow>? {
        return UserTable
            .insert { insertStatement ->
                insertStatement.toUser(userEntity)
            }.resultedValues
    }

    fun batchInsert(userEntities: List<UserEntity>): List<ResultRow> {
        return UserTable
            .batchInsert(userEntities) { user ->
                toUser(user)
            }
    }

    fun updateById(id: Long, user: IUser): Int {
        return UserTable
            .update(
                where = { UserTable.id eq id },
                body = { statement -> statement.toUser(user) }
            )
    }

    fun lock(id: Long): Int {
        return UserTable
            .update(
                where = { UserTable.id eq id },
                body = { statement -> statement[isActive] = false }
            )
    }

    fun unlock(id: Long): Int {
        return UserTable
            .update(
                where = { UserTable.id eq id },
                body = { statement -> statement[isActive] = true }
            )
    }

    fun deleteById(id: Long): Int {
        return UserTable
            .deleteWhere {
                UserTable.id eq id
            }
    }

    fun getAll(paging: Paging): List<UserWithRole> {
        return UserTable
            .leftJoin(RoleTable)
            .selectAll(UserTable.id, paging)
            .map {
                toUserResponse(it)
            }
    }

    fun getByIdOrNull(id: Long): UserWithRole? {
        return UserTable
            .leftJoin(RoleTable)
            .select { UserTable.id eq id }
            .map { toUserResponse(it) }
            .singleOrNull()
    }

    fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): UserEntity? {
        return UserTable
            .select { (UserTable.username eq usernameOrEmail) or (UserTable.email eq usernameOrEmail) }
            .map { toUser(it) }
            .singleOrNull()
    }
}