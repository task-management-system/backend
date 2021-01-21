package kz.tms.database.data.permission

import org.jetbrains.exposed.sql.Table

object PermissionTable : Table("permission") {
    val name = varchar("name", 100)
    val power = long("power")
    val text = varchar("text", 100)
}