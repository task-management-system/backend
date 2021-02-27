package kz.tms.database.data.detail

import kz.tms.model.task.DetailCreate
import org.jetbrains.exposed.sql.statements.BatchInsertStatement

fun BatchInsertStatement.toDetail(detail: DetailCreate) {
    set(DetailTable.taskId, detail.taskId)
    set(DetailTable.executorId, detail.executorId)
    set(DetailTable.statusId, detail.statusId)
}