package kz.tms.model.authentication

import io.ktor.auth.*

class AuthenticationCredential(
    val usernameOrEmail: String,
    val password: String
) : Credential