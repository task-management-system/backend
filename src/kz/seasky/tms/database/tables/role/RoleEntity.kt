package kz.seasky.tms.database.tables.role

import kz.seasky.tms.database.utils.ShortEntity
import kz.seasky.tms.database.utils.ShortEntityClass
import kz.seasky.tms.model.role.Role
import kz.seasky.tms.model.role.RoleInsert
import kz.seasky.tms.model.role.RoleUpdate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.update

class RoleEntity(id: EntityID<Short>) : ShortEntity(id) {
    //@formatter:off
    var power   by RoleTable.power
    var meaning by RoleTable.meaning
    //@formatter:on

    companion object : ShortEntityClass<RoleEntity>(RoleTable) {
        fun insert(role: RoleInsert): Role {
            return RoleEntity.new {
                power = role.power
                meaning = role.meaning
            }.toRole()
        }

        fun update(role: RoleUpdate): Role {
            RoleTable.update(
                where = { RoleTable.id eq role.id },
                body = { statement ->
                    statement[power] = role.power
                    statement[meaning] = role.meaning
                }
            )

            return RoleEntity[role.id].toRole()
        }

        fun batchInsert(roles: List<RoleInsert>): List<Role> {
            val result = mutableListOf<Role>()

            roles.forEach { role ->
                result.add(insert(role))
            }

            return result
        }
    }

    fun toRole(): Role {
        return Role(
            id = id.value,
            power = power,
            meaning = meaning
        )
    }
}