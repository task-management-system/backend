package kz.tms.database.data.user

import org.jetbrains.exposed.sql.ResultRow

fun toUser(resultRow: ResultRow): User {
    return User(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        password = resultRow[UserTable.password],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email]
    )
}