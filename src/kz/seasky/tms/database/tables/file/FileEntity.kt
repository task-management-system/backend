package kz.seasky.tms.database.tables.file

import kotlinx.uuid.UUID
import kotlinx.uuid.exposed.KotlinxUUIDEntity
import kotlinx.uuid.exposed.KotlinxUUIDEntityClass
import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.file.FileInsert
import org.jetbrains.exposed.dao.id.EntityID

class FileEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    //@formatter:off
    var name by FileTable.name
    var size by FileTable.size
    var path by FileTable.path
    //@formatter:on

    companion object : KotlinxUUIDEntityClass<FileEntity>(FileTable) {
        fun insert(file: FileInsert): FileEntity {
            return FileEntity.new {
                name = file.name
                size = file.size
                path = file.path
            }
        }
    }

    fun toFile(): File {
        return File(
            id = id.value.toString(),
            name = name,
            size = size,
            path = path,
        )
    }
}