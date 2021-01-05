package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import kz.tms.database.data.authentication.AuthenticationPayload
import kz.tms.database.data.authentication.makeToken
import kz.tms.database.data.user.UserService
import kz.tms.enums.MessageType
import kz.tms.model.response.Message
import kz.tms.utils.respond
import org.koin.ktor.ext.inject

@OptIn(InternalAPI::class)
fun Route.authentication() {
    val userService: UserService by inject()

    post("/authentication") {
        val payload = call.receive<AuthenticationPayload>()

        val user = userService.getByUsernameOrNull(payload.username)

        val asd = mapOf(
            "test" to "VltM4nfheqcJSyH887H+4NEOm2tDuKCl83p5axYXlF0=".decodeBase64Bytes()
        )
        val userTable = UserHashedTableAuth(getDigestFunction("SHA-256", { "ktor" }), asd)
        val a = userTable.authenticate(UserPasswordCredential("admin", "admin"))

        if (user != null) {
            if (user.password == payload.password) {
                val token = makeToken(user.username, user.password)
                call.respond(
                    message = Message(MessageType.Success, "Аутентификация прошла успешно"),
                    data = mapOf("token" to token)
                )
            } else {
                call.respond(message = Message(MessageType.Error, "Неверный пароль"), data = null)
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