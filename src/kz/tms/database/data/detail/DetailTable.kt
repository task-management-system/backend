package kz.tms.database.data.detail

import kz.tms.database.data.status.StatusTable
import kz.tms.database.data.task.TaskTable
import kz.tms.database.data.user.UserTable
import org.jetbrains.exposed.sql.Table

object DetailTable : Table("detail") {
    val id = long("id").autoIncrement()
    val taskId = long("task_id").references(TaskTable.id)
    val executorId = long("executor_id").references(UserTable.id)
    val statusId = short("status_id").references(StatusTable.id)

    override val primaryKey = PrimaryKey(id, name = "detail_pkey")
}