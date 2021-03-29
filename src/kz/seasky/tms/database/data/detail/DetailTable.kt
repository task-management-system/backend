package kz.seasky.tms.database.data.detail

import kotlinx.uuid.exposed.kotlinxUUID
import kz.seasky.tms.database.data.status.StatusTable
import kz.seasky.tms.database.data.task.TaskTable
import kz.seasky.tms.database.data.user.UserTable
import org.jetbrains.exposed.sql.Table

object DetailTable : Table("detail") {
    val id = long("id").autoIncrement()
    val taskId = long("task_id").references(TaskTable.id)
    val executorId = kotlinxUUID("executor_id").references(UserTable.id)
    val statusId = short("status_id").references(StatusTable.id)

    override val primaryKey = PrimaryKey(id, name = "detail_pkey")
}