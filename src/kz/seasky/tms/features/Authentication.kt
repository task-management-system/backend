package kz.seasky.tms.features

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import kz.seasky.tms.database.data.role.RoleService
import kz.seasky.tms.database.data.user.UserService
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.utils.JWTConfig
import org.koin.ktor.ext.inject

fun Application.installAuthentication() {
    val jwtConfig: JWTConfig by inject()
    val userService: UserService by inject()
    val roleService: RoleService by inject()

    install(Authentication) {
        jwt("token") {
            realm = jwtConfig.realm
            verifier(jwtConfig.verifier)
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()

                val user = userService.getByUsernameOrByEmailOrNull(username)
                val role = user?.roleId?.let {
                    roleService.getRoleById(it)
                }

                if (user != null && user.isActive!!) {
                    AuthenticationPrincipal(user.id, username, role?.power ?: 0)
                } else {
                    null
                }
            }
        }
    }
}