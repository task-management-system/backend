package kz.seasky.tms.database.tables.file

import kotlinx.uuid.exposed.KotlinxUUIDTable
import org.jetbrains.exposed.sql.and

object FileTable : KotlinxUUIDTable("file") {
    val name = varchar("name", 64)
    val size = integer("size")
    val path = varchar("path", 255)

    override val primaryKey by lazy { PrimaryKey(id, name = "pk_file_id") }

    init {
        uniqueIndex(customIndexName = "uq_file_name_size_path", name, size, path)
        check("ch_file_size") {
            (size greater 0) and (size lessEq 26214400)
        }
    }
}