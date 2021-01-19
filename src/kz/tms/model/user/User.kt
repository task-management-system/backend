package kz.tms.model.user

data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val name: String?,
    val email: String?,
    val roleId: Long
)