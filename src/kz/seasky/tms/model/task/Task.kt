package kz.seasky.tms.model.task

import kz.seasky.tms.model.file.File
import kz.seasky.tms.model.user.User

/**
 *  @param dueDate output the date time in ISO8601 format (yyyy-MM-dd'T'HH:mm:ss.SSSZZ).
 *  @param createdAt output the date time in ISO8601 format (yyyy-MM-dd'T'HH:mm:ss.SSSZZ).
 */
data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val markdown: String?,
    val dueDate: String,
    val createdAt: String,
    val creator: User,
    val file: List<File>
)