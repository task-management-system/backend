package kz.tms.database.data.status

import org.jetbrains.exposed.sql.Table

object StatusTable : Table("status") {
    val id = short("id").autoIncrement()
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id, name = "status_pkey")
}