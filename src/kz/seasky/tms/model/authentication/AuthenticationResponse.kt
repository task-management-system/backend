package kz.seasky.tms.model.authentication

import kz.seasky.tms.model.user.User

data class AuthenticationResponse(
    val token: String,
    val user: User
)