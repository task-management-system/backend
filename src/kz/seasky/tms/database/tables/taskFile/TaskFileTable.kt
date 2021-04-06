package kz.seasky.tms.database.tables.taskFile

import kz.seasky.tms.database.tables.file.FileTable
import kz.seasky.tms.database.tables.task.TaskTable
import org.jetbrains.exposed.sql.Table

object TaskFileTable : Table("task_file") {
    val taskId = reference("task_id", TaskTable, fkName = "fk_task_file_task_id_id")
    val fileId = reference("file_id", FileTable, fkName = "fk_task_file_file_id_id")

    override val primaryKey = PrimaryKey(taskId, fileId, name = "pk_task_file_task_id_file_id")
}