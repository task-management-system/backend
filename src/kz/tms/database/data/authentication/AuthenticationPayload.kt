package kz.tms.database.data.authentication

import io.ktor.auth.*

class AuthenticationPayload(
    val username: String,
    val password: String
) : Principal