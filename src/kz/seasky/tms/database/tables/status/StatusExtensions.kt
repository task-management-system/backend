package kz.seasky.tms.database.tables.status

import kz.seasky.tms.model.status.StatusEntity
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toStatusEntity(): StatusEntity {
    return StatusEntity(
        get(StatusTable.id),
        get(StatusTable.name)
    )
}