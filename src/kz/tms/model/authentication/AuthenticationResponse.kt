package kz.tms.model.authentication

import kz.tms.database.data.user.User

data class AuthenticationResponse(
    val token: String,
    val user: User
)