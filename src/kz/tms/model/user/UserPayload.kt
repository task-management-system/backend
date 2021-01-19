package kz.tms.model.user

data class UserPayload(
    val username: String,
    val password: String,
    val name: String?,
    val email: String?,
    val rolePower: Long
)