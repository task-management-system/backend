package kz.seasky.tms.database.data.user

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.database.data.role.RoleEntity
import kz.seasky.tms.extensions.asUUID
import kz.seasky.tms.extensions.crypt
import kz.seasky.tms.model.user.User
import kz.seasky.tms.model.user.UserInsert
import kz.seasky.tms.model.user.UserUpdate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.update

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
        /**
         * Using non new lambda because password is a custom function
         */
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

        fun update(user: UserUpdate): User? {
            UserTable.update(
                where = { UserTable.id eq UUID(user.id) },
                body = { statement ->
                    statement[username] = user.username
                    statement[name] = user.name
                    statement[email] = user.email
                    statement[role] = user.roleId
                }
            )

            return findById(user.id.asUUID())?.toUser()
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