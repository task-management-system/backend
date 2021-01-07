package kz.tms.database.data.user

import kz.tms.database.data.roles.RolesTable
import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 50)
    val password = varchar("password", 50)
    val name = varchar("name", 50).nullable()
    val email = varchar("email", 50).nullable()
    val role = reference("role_id", RolesTable.id)

    override val primaryKey = PrimaryKey(id, name = "user_pkey")
}