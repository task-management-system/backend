package kz.seasky.tms.repository.status

import kz.seasky.tms.database.data.status.StatusTable
import kz.seasky.tms.database.data.status.toStatusEntity
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