package kz.seasky.tms.database.tables.task

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.database.tables.file.FileEntity
import kz.seasky.tms.database.tables.file.TaskFileTable
import kz.seasky.tms.database.tables.status.StatusEntity
import kz.seasky.tms.database.tables.user.UserEntity
import kz.seasky.tms.model.task.Task
import kz.seasky.tms.model.task.TaskPrepare
import kz.seasky.tms.model.task.TaskPreview
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId
import org.joda.time.DateTime

class TaskEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    //@formatter:off
    var title       by TaskTable.title
    var description by TaskTable.description
    var markdown    by TaskTable.markdown
    var dueDate     by TaskTable.dueDate
    var createdAt   by TaskTable.createdAt
    var creator     by UserEntity referencedOn TaskTable.creator
    var status      by StatusEntity referencedOn TaskTable.status
    var file        by FileEntity via TaskFileTable
    //@formatter:on

    companion object : KotlinxUUIDEntityClass<TaskEntity>(TaskTable) {
        fun insert(creatorId: UUID, task: TaskPrepare, statusId: Short): TaskEntity {
            val id = TaskTable.insertAndGetId { statement ->
                statement[title] = task.title
                statement[description] = task.description
                statement[dueDate] = DateTime(task.dueDate)
                statement[markdown] = task.markdown
                statement[creator] = creatorId
                statement[status] = statusId
            }

            return TaskEntity[id]
        }
    }

    fun toTask(): Task {
        return Task(
            id = id.value.toString(),
            title = title,
            description = description,
            markdown = markdown,
            dueDate = dueDate.toString(),
            createdAt = createdAt.toString(),
            creator = creator.toUser(),
            status = status.toStatus(),
            files = file.map(FileEntity::toFile)
        )
    }

    fun toTaskPreview(): TaskPreview {
        return TaskPreview(
            id = id.value.toString(),
            title = title,
            description = description,
            dueDate = dueDate.toString(),
            creator = creator.toUser()
        )
    }
}