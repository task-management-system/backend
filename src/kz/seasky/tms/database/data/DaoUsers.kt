package kz.seasky.tms.database.data

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kotlinx.uuid.exposed.KotlinxUUIDTable
import org.jetbrains.exposed.dao.id.EntityID

object DaoUsers : KotlinxUUIDTable(name = "dao_users") {
    val username = varchar("username", 50).uniqueIndex()
    val name = varchar("name", 50).nullable()
    val isActive = bool("is_active")
    val role = reference("dao_roles_id", DaoRoles)

    override val primaryKey = PrimaryKey(id)
}

object DaoRoles : KotlinxUUIDTable(name = "dao_roles") {
    val name = varchar("name", 100)
    val power = long("power")

    override val primaryKey = PrimaryKey(id)
}

class DaoRolesEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    companion object : KotlinxUUIDEntityClass<DaoRolesEntity>(DaoRoles)

    //@formatter:off
    var name  by DaoRoles.name
    var power by DaoRoles.power
    //@formatter:on
}

data class DaoRole(
    val id: String,
    val name: String,
    val power: Long
)

class DaoUsersEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    companion object : KotlinxUUIDEntityClass<DaoUsersEntity>(DaoUsers)

    //@formatter:off
    var username by DaoUsers.username
    var name     by DaoUsers.name
    var isActive by DaoUsers.isActive
    var role     by DaoRolesEntity referencedOn DaoUsers.role
    //@formatter:on

    fun toDaoUsersEntity(newUser: DaoUserAddAndUpdate): DaoUsersEntity {
        username = newUser.username
        name = newUser.name
        isActive = newUser.isActive
        role = DaoRolesEntity.findById(UUID(newUser.roleId))!!

        return this
    }

    fun toDaoUser(): DaoUser {
        return DaoUser(
            id = id.value.toString(),
            username = username,
            name = name,
            isActive = isActive,
            role = DaoRole(
                id = role.id.toString(),
                name = role.name,
                power = role.power,
            )
        )
    }
}

data class DaoUser(
    val id: String,
    val username: String,
    val name: String?,
    val isActive: Boolean,
    val role: DaoRole
)

data class DaoUserAddAndUpdate(
    val username: String,
    val name: String?,
    val isActive: Boolean,
    val roleId: String
)