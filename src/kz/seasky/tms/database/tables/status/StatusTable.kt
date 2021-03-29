package kz.seasky.tms.database.tables.status

import kz.seasky.tms.database.utils.ShortIdTable

object StatusTable : ShortIdTable("status") {
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(id, name = "pk_status_id")
}