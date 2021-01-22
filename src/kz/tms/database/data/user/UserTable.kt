package kz.tms.database.data.user

import kz.tms.database.data.roles.RolesTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserTable : Table("user") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 50)
    val password = varchar("password", 50)
    val name = varchar("name", 50).nullable()
    val email = varchar("email", 50).nullable()
    val isActive = bool("is_active")
    val roleId = long("role_id").references(RolesTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id, name = "user_pkey")
}