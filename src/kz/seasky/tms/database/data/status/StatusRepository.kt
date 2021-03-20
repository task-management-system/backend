package kz.seasky.tms.database.data.status

import kz.seasky.tms.model.status.StatusEntity
import org.jetbrains.exposed.sql.selectAll

class StatusRepository {
    fun getAll(): List<StatusEntity> {
        return StatusTable
            .selectAll()
            .orderBy(StatusTable.id)
            .map { resultRow -> resultRow.toStatusEntity() }
    }
}