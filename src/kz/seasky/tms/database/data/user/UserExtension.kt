package kz.seasky.tms.database.data.user

import kz.seasky.tms.database.data.role.toRole
import kz.seasky.tms.model.role.Role
import kz.seasky.tms.model.user.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

fun toUserEntity(resultRow: ResultRow): UserEntity {
    return UserEntity(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        password = resultRow[UserTable.password],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
        isActive = resultRow[UserTable.isActive],
        roleId = resultRow[UserTable.roleId]
    )
}

fun ResultRow.toUser(): User {
    return User(
        id = get(UserTable.id),
        username = get(UserTable.username),
        name = get(UserTable.name),
        email = get(UserTable.email)
    )
}

fun toUserResponse(resultRow: ResultRow): UserWithRole {
    return UserWithRole(
        id = resultRow[UserTable.id],
        username = resultRow[UserTable.username],
        name = resultRow[UserTable.name],
        email = resultRow[UserTable.email],
        isActive = resultRow[UserTable.isActive],
        role = toRole(resultRow)
    )
}

infix fun UserEntity.merge(role: Role): UserWithRole {
    return UserWithRole(
        id = id,
        username = username,
        name = name,
        email = email,
        isActive = isActive ?: false,
        role = role
    )
}

fun InsertStatement<Number>.toUserEntity(userEntity: UserEntity) {
    let {
        it[UserTable.username] = userEntity.username
        it[UserTable.password] = userEntity.password
        it[UserTable.name] = userEntity.name
        it[UserTable.email] = userEntity.email
        it[UserTable.isActive] = userEntity.isActive ?: false
        it[UserTable.roleId] = userEntity.roleId
    }
}

fun BatchInsertStatement.toUserEntity(userEntity: UserEntity) {
    let {
        it[UserTable.username] = userEntity.username
        it[UserTable.password] = userEntity.password
        it[UserTable.name] = userEntity.name
        it[UserTable.email] = userEntity.email
        it[UserTable.isActive] = userEntity.isActive ?: false
        it[UserTable.roleId] = userEntity.roleId
    }
}

fun UpdateStatement.toUserEntity(user: IUser) {
    when (user) {
        is UserWithRoleId -> {
            set(UserTable.username, user.username)
            set(UserTable.name, user.name)
            set(UserTable.email, user.email)
            set(UserTable.roleId, user.roleId)
        }
    }
}