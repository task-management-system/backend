package kz.seasky.tms.database.data.task

import kz.seasky.tms.database.data.user.UserTable
import kz.seasky.tms.extensions.selectAll
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.TaskCreate
import kz.seasky.tms.model.task.TaskEntity
import kz.seasky.tms.model.task.TaskWithCreator
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

    fun getAll(userId: Long, paging: Paging): List<TaskWithCreator> {
        return TaskTable
            .leftJoin(UserTable)
            .selectAll(TaskTable.id, paging)
            .andWhere { TaskTable.creatorId eq userId }
            .map { resultRow -> resultRow.toTaskWithCreator() }
    }

    fun insert(task: TaskCreate): TaskEntity? {
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