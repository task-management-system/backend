package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.ResultRow

fun toRole(resultRow: ResultRow): Role {
    return Role(
        power = resultRow[RolesTable.power],
        text = resultRow[RolesTable.text]
    )
}