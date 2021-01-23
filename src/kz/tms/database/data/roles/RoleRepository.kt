package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.*

class RoleRepository {
    fun getAllOrEmpty(): List<Role> {
        return RoleTable
            .selectAll()
            .map { toRole(it) }
    }

    fun getIdByPowerOrNull(power: Int): Long? {
        return RoleTable
            .select { RoleTable.power eq power }
            .map { it[RoleTable.id] }
            .singleOrNull()
    }

    fun getRoleById(id: Long): Role {
        return RoleTable
            .select { RoleTable.id eq id }
            .map { toRole(it) }
            .single()
    }

    fun insert(role: Role): List<ResultRow>? {
        return RoleTable
            .insert { insertStatement ->
                insertStatement.toRole(role)
            }.resultedValues
    }
}