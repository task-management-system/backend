package kz.tms.database.data.user

import kz.tms.database.data.roles.toRole
import org.jetbrains.exposed.sql.ResultRow

fun toUser(resultRow: ResultRow): User {
    return User(
        id = resultRow[UsersTable.id],
        username = resultRow[UsersTable.username],
        password = resultRow[UsersTable.password],
        name = resultRow[UsersTable.name],
        email = resultRow[UsersTable.email],
        role = toRole(resultRow)
    )
}