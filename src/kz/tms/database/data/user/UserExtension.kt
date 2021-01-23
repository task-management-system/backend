package kz.tms.database.data.user

import kz.tms.database.data.role.toRole
import kz.tms.model.role.Role
import kz.tms.model.user.User
import kz.tms.model.user.UserPayload
import kz.tms.model.user.UserResponse
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

fun toUser(resultRow: ResultRow): User {
    return User(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        password = resultRow[UserTable.password],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
        isActive = resultRow[UserTable.isActive],
        roleId = resultRow[UserTable.roleId]
    )
}

fun toUserResponse(resultRow: ResultRow): UserResponse {
    return UserResponse(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
        isActive = resultRow[UserTable.isActive],
        role = toRole(resultRow)
    )
}

infix fun User.merge(role: Role): UserResponse {
    return UserResponse(
        id = id,
        username = username,
        name = name,
        email = email,
        isActive = isActive,
        role = role
    )
}

infix fun UserPayload.merge(roleId: Long): User {
    return User(
        username = username,
        password = password,
        name = name,
        email = email,
        isActive = isActive ?: false,
        roleId = roleId
    )
}

infix fun Array<UserPayload>.merge(roleIds: List<Long>): List<User> {
    val resultList = arrayListOf<User>()
    for (i in roleIds.indices) {
        resultList.add(this[i] merge roleIds[i])
    }
    return resultList
}

fun InsertStatement<Number>.toUser(user: User) {
    let {
        it[UserTable.username] = user.username
        it[UserTable.password] = user.password
        it[UserTable.name] = user.name
        it[UserTable.email] = user.email
        it[UserTable.isActive] = user.isActive
        it[UserTable.roleId] = user.roleId
    }
}

fun BatchInsertStatement.toUser(user: User) {
    let {
        it[UserTable.username] = user.username
        it[UserTable.password] = user.password
        it[UserTable.name] = user.name
        it[UserTable.email] = user.email
        it[UserTable.isActive] = user.isActive
        it[UserTable.roleId] = user.roleId
    }
}

fun UpdateStatement.toUser(user: User) {
    let {
        it[UserTable.username] = user.username
        it[UserTable.password] = user.password
        it[UserTable.name] = user.name
        it[UserTable.email] = user.email
        it[UserTable.isActive] = user.isActive
        it[UserTable.roleId] = user.roleId
    }
}