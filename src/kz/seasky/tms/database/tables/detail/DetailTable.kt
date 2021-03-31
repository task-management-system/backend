package kz.seasky.tms.database.tables.detail

import kotlinx.uuid.exposed.KotlinxUUIDTable
import kz.seasky.tms.database.tables.status.StatusTable
import kz.seasky.tms.database.tables.task.TaskTable
import kz.seasky.tms.database.tables.user.UserTable

object DetailTable : KotlinxUUIDTable("detail") {
    val task = reference("task_id", TaskTable.id, fkName = "fk_detail_task_id_id")
    val executor = reference("executor_id", UserTable.id, fkName = "fk_detail_executor_id_id")
    val status = reference("status_id", StatusTable.id, fkName = "fk_detail_status_id_id")

    override val primaryKey = PrimaryKey(id, name = "pk_detail_id")
}