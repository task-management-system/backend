package kz.seasky.tms.database.tables.task

import kotlinx.uuid.exposed.KotlinxUUIDTable
import kz.seasky.tms.database.tables.status.StatusTable
import kz.seasky.tms.database.tables.user.UserTable
import org.jetbrains.exposed.sql.jodatime.datetime

object TaskTable : KotlinxUUIDTable("task") {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val markdown = text("markdown").nullable()
    val dueDate = datetime("due_date")
    val createdAt = datetime("created_at")
    val creator = reference("creator_id", UserTable.id, fkName = "fk_task_creator_id_id")
    val status = reference("status_id", StatusTable.id, fkName = "fk_task_status_id_id")

    override val primaryKey = PrimaryKey(id, name = "pk_task_id")
}