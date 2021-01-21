package kz.tms.database.data.permission

import kz.tms.model.permission.Permission
import org.jetbrains.exposed.sql.selectAll

class PermissionRepository {
    fun getAll(): List<Permission> {
        return PermissionTable
            .selectAll()
            .map {
                toPermission(it)
            }.sortedBy {
                it.power
            }
    }
}