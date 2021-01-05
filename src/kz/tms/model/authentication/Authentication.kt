package kz.tms.model.authentication

import io.ktor.auth.*

class AuthenticationCredential(
    val username: String,
    val password: String
) : Credential

class AuthenticationPrincipal(
    val username: String
) : Principal