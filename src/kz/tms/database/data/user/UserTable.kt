package kz.tms.database.data.user

import org.jetbrains.exposed.sql.Table

object UserTable : Table("user") {
    val id = long("id").autoIncrement()
    val username = varchar("username", 50)
    val password = varchar("password", 50)
    val name = varchar("name", 50).nullable()
    val email = varchar("email", 50).nullable()

    override val primaryKey = PrimaryKey(id, name = "user_pkey")
}

