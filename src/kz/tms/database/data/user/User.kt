package kz.tms.database.data.user

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val name: String?,
    val email: String?
)