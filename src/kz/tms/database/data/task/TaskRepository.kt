package kz.tms.database.data.task

import kz.tms.model.paging.Paging
import kz.tms.model.task.Task
import kz.tms.utils.selectAll
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll

class TaskRepository {
    fun count(userId: Long): Long {
        return TaskTable
            .selectAll()
            .andWhere { TaskTable.creatorId eq userId }
            .count()
    }

    fun getAll(userId: Long, paging: Paging): List<Task> {
        return TaskTable
            .selectAll(TaskTable.id, paging)
            .andWhere { TaskTable.creatorId eq userId }
            .map { resultRow -> resultRow.toTask() }
    }
}