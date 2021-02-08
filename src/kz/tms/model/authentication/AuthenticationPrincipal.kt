package kz.tms.model.authentication

import io.ktor.auth.*

class AuthenticationPrincipal(
    val userId: Long?,
    val username: String,
    val power: Int
) : Principal
