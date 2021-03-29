package kz.seasky.tms.database.tables.task

import kotlinx.uuid.exposed.kotlinxUUID
import kz.seasky.tms.database.tables.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object TaskTable : Table("task") {
    val id = long("id").autoIncrement()
    val title = varchar("title", 200)
    val description = text("description").nullable()
    val dueDate = datetime("due_date")
    val creatorId = kotlinxUUID("creator_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id, name = "task_pkey")
}