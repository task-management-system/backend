package kz.tms.database.data.roles

import org.jetbrains.exposed.sql.ResultRow

fun toRole(resultRow: ResultRow): Role {
    return Role(
        power = resultRow[RolesTable.power],
        text = resultRow[RolesTable.text]
    )
}