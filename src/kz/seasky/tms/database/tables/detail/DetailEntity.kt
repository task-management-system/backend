package kz.seasky.tms.database.tables.detail

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.database.tables.task.TaskEntity
import kz.seasky.tms.database.tables.user.UserEntity
import kz.seasky.tms.model.detail.Detail
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId

class DetailEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    //@formatter:off
    var task     by TaskEntity referencedOn DetailTable.task
    var executor by UserEntity referencedOn DetailTable.executor
    var status   by StatusEntity referencedOn DetailTable.status
    //@formatter:on

    companion object : KotlinxUUIDEntityClass<DetailEntity>(DetailTable) {
        fun insert(executorId: UUID, taskId: UUID): Detail {
            val id = DetailTable.insertAndGetId { statement ->
                statement[task] = taskId
                statement[executor] = executorId
            }

            return DetailEntity[id].toDetail()
        }
    }

    fun toDetail(): Detail {
        return Detail(
            id = id.value.toString(),
            task = task.toTask()
        )
    }
}