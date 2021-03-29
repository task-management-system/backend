package kz.seasky.tms.database.tables.status

import kz.seasky.tms.database.utils.ShortEntity
import kz.seasky.tms.database.utils.ShortEntityClass
import kz.seasky.tms.model.status.Status
import org.jetbrains.exposed.dao.id.EntityID

class StatusEntity(id: EntityID<Short>) : ShortEntity(id) {
    //@formatter:off
    var name by StatusTable.name
    //@formatter:on

    companion object : ShortEntityClass<StatusEntity>(StatusTable)

    fun toStatus(): Status {
        return Status(
            id = id.value,
            name = name
        )
    }
}