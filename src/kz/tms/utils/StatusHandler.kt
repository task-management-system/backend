package kz.tms.utils

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.tms.enums.MessageType
import kz.tms.model.response.Message

fun StatusPages.Configuration.statusHandler() {
    status(HttpStatusCode.Unauthorized) {
        call.respond(
            message = Message(MessageType.Error, "Дружок пирожок авторазьку то не прошел"),
            data = null
        )
    }
}
