package kz.seasky.tms.database.utils

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

open class ShortIdTable(name: String = "", columnName: String = "id") : IdTable<Short>(name) {
    override val id: Column<EntityID<Short>> = short(columnName).autoIncrement().entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}