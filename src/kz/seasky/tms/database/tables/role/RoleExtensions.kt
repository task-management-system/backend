package kz.seasky.tms.database.tables.role

//fun toRole(resultRow: ResultRow): Role {
//    return Role(
//        id = resultRow[RoleTable.id],
//        power = resultRow[RoleTable.power],
//        text = resultRow[RoleTable.text]
//    )
//}
//
//fun InsertStatement<Number>.toRole(role: Role) {
//    let {
//        it[RoleTable.power] = role.power
//        it[RoleTable.text] = role.text
//    }
//}
//
//fun BatchInsertStatement.toRole(role: Role) {
//    let {
//        it[RoleTable.power] = role.power
//        it[RoleTable.text] = role.text
//    }
//}
//
//fun UpdateStatement.toRole(role: Role) {
//    let {
//        it[RoleTable.power] = role.power
//        it[RoleTable.text] = role.text
//    }
//}