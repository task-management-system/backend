package kz.seasky.tms.database.tables.task

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.database.tables.user.UserEntity
import kz.seasky.tms.model.task.TaskInstance
import kz.seasky.tms.model.task.TaskInstancePreview
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId

class TaskInstanceEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    //@formatter:off
    var task     by TaskEntity referencedOn TaskInstanceTable.task
    var executor by UserEntity referencedOn TaskInstanceTable.executor
    var status   by StatusEntity referencedOn TaskInstanceTable.status
    //@formatter:on

    companion object : KotlinxUUIDEntityClass<TaskInstanceEntity>(TaskInstanceTable) {
        fun insert(executorId: UUID, taskId: UUID): TaskInstance {
            val id = TaskInstanceTable.insertAndGetId { statement ->
                statement[task] = taskId
                statement[executor] = executorId
            }

            return TaskInstanceEntity[id].toTaskInstance()
        }
    }

    fun toTaskInstance(): TaskInstance {
        return TaskInstance(
            id = id.value.toString(),
            task = task.toTask(),
            status = status.toStatus()
        )
    }

    fun toTaskInstancePreview(): TaskInstancePreview {
        return TaskInstancePreview(
            id = id.value.toString(),
            task = task.toTaskPreview()
        )
    }
}