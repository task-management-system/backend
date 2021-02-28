package kz.tms.database.data.status

import kz.tms.model.status.StatusEntity
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toStatusEntity(): StatusEntity {
    return StatusEntity(
        get(StatusTable.id),
        get(StatusTable.name)
    )
}