package kz.seasky.tms.features

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.repository.user.UserService
import kz.seasky.tms.utils.JWTConfig
import kz.seasky.tms.utils.JWT_CLAIM_ID
import kz.seasky.tms.utils.JWT_NAME_STANDARD
import org.koin.ktor.ext.inject

fun Application.installAuthentication() {
    val jwtConfig: JWTConfig by inject()
    val userService: UserService by inject()

    install(Authentication) {
        jwt(JWT_NAME_STANDARD) {
            realm = jwtConfig.realm
            verifier(jwtConfig.verifier)
            //FIXME Cause I don't get it
            validate { credential ->
                val id = credential.payload.getClaim(JWT_CLAIM_ID).asString()
                val user = userService.getById(id)
                if (id == user.id && user.isActive) {
                    AuthenticationPrincipal(user.id, user.username, user.role.power)
                } else {
                    null
                }
            }
        }
    }
}