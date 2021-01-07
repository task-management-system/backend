package kz.tms.database.data.claims

import org.jetbrains.exposed.sql.Table

object ClaimsTable : Table("claims") {
    val name = varchar("name", 100)
    val power = long("power")
    val text = varchar("text", 100)
}