package kz.tms.database.data.detail

import kz.tms.database.data.task.TaskTable
import kz.tms.database.data.task.toTask
import kz.tms.model.paging.Paging
import kz.tms.model.task.Task
import kz.tms.utils.selectAll
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll

class DetailRepository {
    fun count(userId: Long, statusId: Short): Long {
        return DetailTable
            .selectAll()
            .andWhere {
                (DetailTable.executorId eq userId) and (DetailTable.statusId eq statusId)
            }
            .count()
    }

    fun getAll(userId: Long, statusId: Short, paging: Paging): List<Task> {
        return DetailTable
            .leftJoin(TaskTable)
            .slice(TaskTable.columns)
            .selectAll(TaskTable.id, paging)
            .andWhere {
                (DetailTable.executorId eq userId) and (DetailTable.statusId eq statusId)
            }
            .map { it.toTask() }
    }
}