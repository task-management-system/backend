package kz.seasky.tms.database.data.role

import kz.seasky.tms.model.role.Role
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

fun toRole(resultRow: ResultRow): Role {
    return Role(
        id = resultRow[RoleTable.id],
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

fun BatchInsertStatement.toRole(role: Role) {
    let {
        it[RoleTable.power] = role.power
        it[RoleTable.text] = role.text
    }
}

fun UpdateStatement.toRole(role: Role) {
    let {
        it[RoleTable.power] = role.power
        it[RoleTable.text] = role.text
    }
}