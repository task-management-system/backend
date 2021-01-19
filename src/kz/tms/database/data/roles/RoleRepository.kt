package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.select

class RoleRepository {
    fun getIdByPowerOrNull(power: Long): Long? {
        return RolesTable
            .select { RolesTable.power eq power }
            .map { it[RolesTable.power] }
            .singleOrNull()
    }

    fun getRoleById(id: Long): Role {
        return RolesTable
            .select { RolesTable.id eq id }
            .map { toRole(it) }
            .single()
    }
}