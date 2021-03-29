package kz.seasky.tms.database.tables.role

import kz.seasky.tms.database.utils.ShortIdTable

object RoleTable : ShortIdTable("role") {
    val power = RoleTable.long("power")
    val meaning = RoleTable.varchar("meaning", 128)

    override val primaryKey by lazy { PrimaryKey(id, name = "pk_role_id") }
}