package kz.seasky.tms.database.data.detail

import kz.seasky.tms.database.data.task.TaskTable
import kz.seasky.tms.database.data.task.toTaskWithCreatorAndDetailId
import kz.seasky.tms.database.data.user.UserTable
import kz.seasky.tms.model.paging.Paging
import kz.seasky.tms.model.task.DetailCreate
import kz.seasky.tms.model.task.TaskWithCreatorAndDetailId
import kz.seasky.tms.utils.selectAll
import org.jetbrains.exposed.sql.*

class DetailRepository {
    fun count(userId: Long, statusId: Short): Long {
        return DetailTable
            .selectAll()
            .andWhere {
                (DetailTable.executorId eq userId) and (DetailTable.statusId eq statusId)
            }
            .count()
    }

    fun getAll(userId: Long, statusId: Short, paging: Paging): List<TaskWithCreatorAndDetailId> {
        return DetailTable
            .leftJoin(TaskTable)
            .join(UserTable, JoinType.LEFT, UserTable.id, TaskTable.creatorId)
            .selectAll(DetailTable.id, paging)
            .andWhere {
                (DetailTable.executorId eq userId) and (DetailTable.statusId eq statusId)
            }
            .map { resultRow -> resultRow.toTaskWithCreatorAndDetailId() }
    }

    fun batchInsert(details: List<DetailCreate>): List<ResultRow> {
        return DetailTable
            .batchInsert(details) { detail ->
                toDetail(detail)
            }
    }
}