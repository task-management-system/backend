package kz.tms.database.data.roles

import org.jetbrains.exposed.sql.Table

object RolesTable : Table("roles") {
    val id = long("id").autoIncrement()
    val power = long("power")
    val text = varchar("text", 100)

    override val primaryKey = PrimaryKey(id, name = "roles_pkey")
}