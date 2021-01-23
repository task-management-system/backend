package kz.tms.database.data.role

import org.jetbrains.exposed.sql.Table

object RoleTable : Table("role") {
    val id = long("id").autoIncrement()
    val power = integer("power")
    val text = varchar("text", 100)

    override val primaryKey = PrimaryKey(id, name = "roles_pkey")
}