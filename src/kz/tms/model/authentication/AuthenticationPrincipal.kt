package kz.tms.model.authentication

import io.ktor.auth.*

class AuthenticationPrincipal(
    val username: String
) : Principal
