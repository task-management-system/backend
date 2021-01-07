package kz.tms.features

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.tms.enums.MessageType
import kz.tms.model.Message
import kz.tms.utils.respond

fun Application.installStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            call.respond(
                message = Message(MessageType.Error, "Дружок пирожок авторазьку то не прошел"),
                data = null
            )
        }
    }
}