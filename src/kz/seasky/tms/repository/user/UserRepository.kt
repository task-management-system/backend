package kz.seasky.tms.repository.user

import kotlinx.uuid.UUID
import kz.seasky.tms.database.tables.user.UserEntity
import kz.seasky.tms.database.tables.user.UserTable
import kz.seasky.tms.extensions.crypt
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.user.User
import kz.seasky.tms.model.user.UserInsert
import kz.seasky.tms.model.user.UserUpdate
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class UserRepository {
    fun count(): Long {
        return UserEntity
            .all()
            .count()
    }

    fun insert(user: UserInsert): User? {
        return UserEntity.insert(user)
    }

    fun batchInsert(users: List<UserInsert>): List<User?> {
        return UserEntity.batchInsert(users)
    }

    fun updateById(user: UserUpdate): User? {
        return UserEntity.update(user)
    }

    fun lock(id: UUID): User? {
        val user = UserEntity[id]

        if (!user.isActive) return null

        user.isActive = false
        return user.toUser()
    }

    fun unlock(id: UUID): User? {
        val user = UserEntity[id]

        if (user.isActive) return null

        user.isActive = true
        return user.toUser()
    }

    fun deleteById(id: UUID) {
        UserEntity[id].delete()
    }

    fun getAll(): List<User> {
        return UserEntity
            .all()
            .orderBy(UserTable.createdAt to SortOrder.ASC)
            .map(UserEntity::toUser)
    }

    fun getAll(paging: Paging): List<User> {
        return UserEntity
            .all()
            .orderBy(UserTable.createdAt to paging.sortOrder)
            .limit(paging.limit, paging.offset)
            .map(UserEntity::toUser)
    }

    fun getByIdOrNull(id: UUID): User? {
        return UserEntity
            .findById(id)
            ?.toUser()
    }

    fun getByUsernameOrByEmailOrNull(usernameOrEmail: String): User? {
        return UserEntity
            .find { (UserTable.username eq usernameOrEmail) or (UserTable.email eq usernameOrEmail) }
            .firstOrNull()
            ?.toUser()
    }

    fun validatePassword(id: UUID, password: String): Boolean? {
        val match = UserTable.password.crypt(password)
        return UserTable
            .slice(match)
            .select { UserTable.id eq id }
            .map { rs -> rs[match] }
            .firstOrNull()
    }

    fun changePassword(id: UUID, newPassword: String): User? {
        UserTable.update(
            where = { UserTable.id eq id },
            body = { statement ->
                statement[password] = crypt(newPassword)
            }
        )

        return UserEntity.findById(id)?.toUser()
    }
}