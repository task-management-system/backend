package kz.tms.database.data.task

import kz.tms.model.paging.Paging
import kz.tms.model.task.Task
import kz.tms.utils.selectAll
import org.jetbrains.exposed.sql.selectAll

class TaskRepository {
    fun count(): Long {
        return TaskTable
            .selectAll()
            .count()
    }

    fun getAll(paging: Paging): List<Task> {
        return TaskTable
            .selectAll(TaskTable.id, paging)
            .map { resultRow -> resultRow.toTask() }
    }
}