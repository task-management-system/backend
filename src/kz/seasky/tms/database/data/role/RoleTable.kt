package kz.seasky.tms.database.data.role

import kz.seasky.tms.database.utils.ShortEntity
import kz.seasky.tms.database.utils.ShortEntityClass
import kz.seasky.tms.database.utils.ShortIdTable
import kz.seasky.tms.model.role.Role
import org.jetbrains.exposed.dao.id.EntityID

object RoleTable : ShortIdTable("role") {
    val power = RoleTable.long("power")
    val meaning = RoleTable.varchar("meaning", 128)

    override val primaryKey by lazy { PrimaryKey(id, name = "pk_role_id") }
}

class RoleEntity(id: EntityID<Short>) : ShortEntity(id) {
    //@formatter:off
    var power   by RoleTable.power
    var meaning by RoleTable.meaning
    //@formatter:on

    companion object : ShortEntityClass<RoleEntity>(RoleTable)

    fun toRole(): Role {
        return Role(
            id = id.value,
            power = power,
            meaning = meaning
        )
    }
}