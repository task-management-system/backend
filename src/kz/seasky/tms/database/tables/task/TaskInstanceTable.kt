package kz.seasky.tms.database.tables.task

import kotlinx.uuid.exposed.KotlinxUUIDTable
import kz.seasky.tms.database.tables.status.StatusTable
import kz.seasky.tms.database.tables.user.UserTable

object TaskInstanceTable : KotlinxUUIDTable("task_instance") {
    val task = reference("task_id", TaskTable.id, fkName = "fk_task_instance_task_id_id")
    val executor = reference("executor_id", UserTable.id, fkName = "fk_task_instance_executor_id_id")
    val status = reference("status_id", StatusTable.id, fkName = "fk_task_instance_status_id_id")

    override val primaryKey = PrimaryKey(id, name = "pk_task_instance_id")
}