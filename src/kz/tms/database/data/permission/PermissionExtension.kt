package kz.tms.database.data.permission

import kz.tms.model.permission.Permission
import org.jetbrains.exposed.sql.ResultRow

fun toPermission(resultRow: ResultRow): Permission {
    return Permission(
        name = resultRow[PermissionTable.name],
        power = resultRow[PermissionTable.power],
        text = resultRow[PermissionTable.text]
    )
}