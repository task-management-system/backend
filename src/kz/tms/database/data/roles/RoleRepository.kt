package kz.tms.database.data.roles

import org.jetbrains.exposed.sql.select

class RoleRepository {
    fun getByIdOrNull(id: Long?): Role? {
        return id?.let {
            RolesTable.select { RolesTable.id eq id }.map { resultRow ->
                toRole(resultRow)
            }.singleOrNull()
        }
    }
}