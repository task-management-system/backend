package kz.seasky.tms.model.authentication

import io.ktor.auth.*

class AuthenticationPrincipal(
    val id: String,
    val username: String,
    val power: Long
) : Principal
