package kz.seasky.tms.database.data.user

import kz.seasky.tms.database.data.role.RoleTable
import kz.seasky.tms.extensions.selectAll
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.user.IUser
import kz.seasky.tms.model.user.UserEntity
import kz.seasky.tms.model.user.UserWithRole
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
                insertStatement.toUserEntity(userEntity)
            }.resultedValues
    }

    fun batchInsert(userEntities: List<UserEntity>): List<ResultRow> {
        return UserTable
            .batchInsert(userEntities) { user ->
                toUserEntity(user)
            }
    }

    fun updateById(id: Long, user: IUser): Int {
        return UserTable
            .update(
                where = { UserTable.id eq id },
                body = { statement -> statement.toUserEntity(user) }
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

    fun validatePassword(id: Long, currentPassword: String): UserEntity? {
        return UserTable
            .select { (UserTable.id eq id) and (UserTable.password eq currentPassword) }
            .map { toUserEntity(it) }
            .singleOrNull()
    }

    fun changePassword(id: Long, newPassword: String): Int {
        return UserTable
            .update(
                where = { UserTable.id eq id },
                body = { statement -> statement[password] = newPassword }
            )
    }

    fun deleteById(id: Long): Int {
        return UserTable
            .deleteWhere {
                UserTable.id eq id
            }
    }

    fun getAll(): List<UserWithRole> {
        return UserTable
            .leftJoin(RoleTable)
            .selectAll()
            .orderBy(UserTable.id)
            .map { toUserResponse(it) }
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
            .map { toUserEntity(it) }
            .singleOrNull()
    }
}