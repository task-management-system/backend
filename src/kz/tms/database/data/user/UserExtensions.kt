package kz.tms.database.data.user

import kz.tms.database.data.roles.Role
import kz.tms.database.data.roles.RolesTable
import kz.tms.database.data.roles.toRole
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

fun toUser(resultRow: ResultRow): User {
    return User(
        id = resultRow[UsersTable.id],
        username = resultRow[UsersTable.username],
        password = resultRow[UsersTable.password],
        name = resultRow[UsersTable.name],
        email = resultRow[UsersTable.email],
        role = getRoleOrNull(resultRow[UsersTable.role])
    )
}

fun getRoleOrNull(id: Long?): Role? {
    return id?.let {
        RolesTable.select { RolesTable.id eq id }.map { resultRow ->
            toRole(resultRow)
        }.singleOrNull()
    }
}