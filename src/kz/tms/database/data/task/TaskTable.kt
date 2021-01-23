package kz.tms.database.data.task

import kz.tms.database.data.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object TaskTable : Table("task") {
    val id = long("id").autoIncrement()
    val title = varchar("title", 200)
    val description = text("description").nullable()
    val dueDate = datetime("due_date")
    val userId = long("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id, name = "task_pkey")
}