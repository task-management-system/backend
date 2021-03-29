package kz.seasky.tms.database.data.task

import org.jetbrains.exposed.sql.deleteWhere

class TaskRepository {
//    fun count(userId: Long): Long {
//        return TaskTable
//            .selectAll()
//            .andWhere { TaskTable.creatorId eq userId }
//            .count()
//    }
//
//    fun getAll(userId: Long, paging: Paging): List<TaskWithCreator> {
//        return TaskTable
//            .leftJoin(UserTable)
//            .selectAll(TaskTable.id, paging)
//            .andWhere { TaskTable.creatorId eq userId }
//            .map { resultRow -> resultRow.toTaskWithCreator() }
//    }
//
//    fun insert(task: TaskCreate): TaskEntity? {
//        return TaskTable
//            .insert { insertStatement ->
//                insertStatement.toTask(task)
//            }.resultedValues?.map { resultRow ->
//                resultRow.toTask()
//            }?.singleOrNull()
//    }

    fun delete(id: Long): Int {
        return TaskTable.deleteWhere { TaskTable.id eq id }
    }
}