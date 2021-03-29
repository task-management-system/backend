package kz.seasky.tms.repository.status

import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.model.status.Status

class StatusRepository {
    fun getAll(): List<Status> {
        return StatusEntity
            .all()
            .map(StatusEntity::toStatus)
    }
}