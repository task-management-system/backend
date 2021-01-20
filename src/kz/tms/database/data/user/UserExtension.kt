package kz.tms.database.data.user

import kz.tms.database.data.roles.toRole
import kz.tms.model.role.Role
import kz.tms.model.user.User
import kz.tms.model.user.UserPayload
import kz.tms.model.user.UserResponse
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun toUser(resultRow: ResultRow): User {
    return User(
        id = resultRow[UsersTable.id],
        username = resultRow[UsersTable.username],
        password = resultRow[UsersTable.password],
        name = resultRow[UsersTable.name],
        email = resultRow[UsersTable.email],
        roleId = resultRow[UsersTable.roleId]
    )
}

fun toUserResponse(resultRow: ResultRow): UserResponse {
    return UserResponse(
        id = resultRow[UsersTable.id],
        username = resultRow[UsersTable.username],
        name = resultRow[UsersTable.name],
        email = resultRow[UsersTable.email],
        role = toRole(resultRow)
    )
}

fun toUserResponse(user: User, role: Role): UserResponse {
    return UserResponse(
        id = user.id,
        username = user.username,
        name = user.name,
        email = user.email,
        role = role
    )
}

infix fun User.merge(role: Role): UserResponse {
    return UserResponse(
        id = id,
        username = username,
        name = name,
        email = email,
        role = role
    )
}

fun toUser(userPayload: UserPayload, roleId: Long): User {
    return User(
        username = userPayload.username,
        password = userPayload.password,
        name = userPayload.name,
        email = userPayload.email,
        roleId = roleId
    )
}

infix fun UserPayload.merge(roleId: Long): User {
    return User(
        username = username,
        password = password,
        name = name,
        email = email,
        roleId = roleId
    )
}

fun InsertStatement<Number>.toUser(user: User) {
    let {
        it[UsersTable.username] = user.username
        it[UsersTable.password] = user.password
        it[UsersTable.name] = user.name
        it[UsersTable.email] = user.email
        it[UsersTable.roleId] = user.roleId
    }
}