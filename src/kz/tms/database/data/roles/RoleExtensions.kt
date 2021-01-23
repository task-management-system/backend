package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun toRole(resultRow: ResultRow): Role {
    return Role(
        power = resultRow[RolesTable.power],
        text = resultRow[RolesTable.text]
    )
}

fun InsertStatement<Number>.toRole(role: Role) {
    let {
        it[RolesTable.power] = role.power
        it[RolesTable.text] = role.text
    }
}