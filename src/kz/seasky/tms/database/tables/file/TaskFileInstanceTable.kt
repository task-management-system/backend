package kz.seasky.tms.database.tables.file

import kz.seasky.tms.database.tables.task.TaskInstanceTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TaskFileInstanceTable : Table("task_instance_file") {
    val taskInstance =
        reference("task_instance_id", TaskInstanceTable, fkName = "fk_task_instance_file_task_instance_id_id")
    val file =
        reference("file_id", FileTable, fkName = "fk_task_instance_file_file_id_id", onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(taskInstance, file, name = "pk_task_instance_file_task_instance_id_file_id")
}