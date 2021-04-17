package kz.seasky.tms.model.file

data class File(
    val id: String,
    val name: String,
    val size: Int,
    val path: String
)