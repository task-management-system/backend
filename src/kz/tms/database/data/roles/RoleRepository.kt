package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class RoleRepository {
    fun getAllOrEmpty(): List<Role> {
        return RolesTable
            .selectAll()
            .map { toRole(it) }
    }

    fun getIdByPowerOrNull(power: Int): Long? {
        return RolesTable
            .select { RolesTable.power eq power }
            .map { it[RolesTable.id].value }
            .singleOrNull()
    }

    fun getRoleById(id: Long): Role {
        return RolesTable
            .select { RolesTable.id eq id }
            .map { toRole(it) }
            .single()
    }

    fun insert(role: Role): List<ResultRow>? {
        return RolesTable
            .insert { insertStatement ->
                insertStatement.toRole(role)
            }.resultedValues
    }
}