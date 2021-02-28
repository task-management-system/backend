package kz.tms.database.data.task

import kz.tms.model.paging.Paging
import kz.tms.model.task.ITask
import kz.tms.model.task.TaskEntity
import kz.tms.utils.selectAll
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class TaskRepository {
    fun count(userId: Long): Long {
        return TaskTable
            .selectAll()
            .andWhere { TaskTable.creatorId eq userId }
            .count()
    }

    fun getAll(userId: Long, paging: Paging): List<TaskEntity> {
        return TaskTable
            .selectAll(TaskTable.id, paging)
            .andWhere { TaskTable.creatorId eq userId }
            .map { resultRow -> resultRow.toTask() }
    }

    fun insert(task: ITask): TaskEntity? {
        return TaskTable
            .insert { insertStatement ->
                insertStatement.toTask(task)
            }.resultedValues?.map { resultRow ->
                resultRow.toTask()
            }?.singleOrNull()
    }

    fun delete(id: Long): Int {
        return TaskTable.deleteWhere { TaskTable.id eq id }
    }
}