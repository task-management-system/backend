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
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        password = resultRow[UserTable.password],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
        roleId = resultRow[UserTable.roleId]
    )
}

fun toUserResponse(resultRow: ResultRow): UserResponse {
    return UserResponse(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
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
        it[UserTable.username] = user.username
        it[UserTable.password] = user.password
        it[UserTable.name] = user.name
        it[UserTable.email] = user.email
        it[UserTable.roleId] = user.roleId
    }
}