package kz.seasky.tms.database.tables.user

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.database.tables.role.RoleEntity
import kz.seasky.tms.extensions.crypt
import kz.seasky.tms.model.user.User
import kz.seasky.tms.model.user.UserInsert
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId

class UserEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    //@formatter:off
    var username    by UserTable.username
    var password    by UserTable.password
    var name        by UserTable.name
    var email       by UserTable.email
    var isActive    by UserTable.isActive
    var role        by RoleEntity referencedOn UserTable.role
    //@formatter:on

    companion object : KotlinxUUIDEntityClass<UserEntity>(UserTable) {
        fun insert(user: UserInsert): User? {
            val id = UserTable.insertAndGetId { statement ->
                statement[username] = user.username
                statement[password] = crypt(user.password)
                statement[name] = user.name
                statement[email] = user.email
                statement[isActive] = user.isActive
                statement[role] = user.roleId
            }

            return findById(id)?.toUser()
        }

        fun batchInsert(users: List<UserInsert>): List<User?> {
            val resultUsers = mutableListOf<User?>()

            users.forEach { user ->
                resultUsers.add(insert(user))
            }

            return resultUsers
        }
    }

    fun toUser(): User {
        return User(
            id = id.value.toString(),
            username = username,
            name = name,
            email = email,
            isActive = isActive,
            role = role.toRole()
        )
    }
}