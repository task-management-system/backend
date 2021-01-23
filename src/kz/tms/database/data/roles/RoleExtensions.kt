package kz.tms.database.data.roles

import kz.tms.model.role.Role
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun toRole(resultRow: ResultRow): Role {
    return Role(
        power = resultRow[RoleTable.power],
        text = resultRow[RoleTable.text]
    )
}

fun InsertStatement<Number>.toRole(role: Role) {
    let {
        it[RoleTable.power] = role.power
        it[RoleTable.text] = role.text
    }
}