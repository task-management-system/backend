package kz.tms.database.data.roles

import org.jetbrains.exposed.dao.id.IdTable

object RoleTable : IdTable<Long>("role") {
    override val id = long("id").autoIncrement().entityId()
    val power = integer("power")
    val text = varchar("text", 100)

    override val primaryKey = PrimaryKey(id, name = "roles_pkey")
}