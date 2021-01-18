package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.database.data.user.UserResponse
import kz.tms.database.data.user.UserService
import kz.tms.enums.MessageType
import kz.tms.model.Message
import kz.tms.model.authentication.AuthenticationCredential
import kz.tms.model.authentication.AuthenticationResponse
import kz.tms.utils.JWTConfig
import kz.tms.utils.respond
import org.koin.ktor.ext.inject

fun Route.authentication() {
    val jwtConfig: JWTConfig by inject()
    val userService: UserService by inject()

    post("/authentication") {
        val credentials = call.receive<AuthenticationCredential>()
        val user = userService.getByUsernameOrNull(credentials.username)

        if (user != null) {
            if (user.password == credentials.password) {
                val token = jwtConfig.makeToken(user.username)
                val userResponse = UserResponse(user.username, user.name, user.email, user.role)
                call.respond(
                    message = Message(MessageType.Success, "Аутентификация прошла успешно"),
                    data = AuthenticationResponse(token, userResponse)
                )
            } else {
                call.respond(
                    message = Message(MessageType.Error, "Неверный пароль"),
                    data = null
                )
            }
        } else {
            call.respond(
                message = Message(
                    MessageType.Error,
                    "Пользователь с такими данными не найден, проверьте корректность введенных данных"
                ),
                data = null
            )
        }
    }
}