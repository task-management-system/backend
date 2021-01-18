package kz.tms.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kz.tms.model.Message
import kz.tms.model.Response

suspend fun <T> ApplicationCall.respond(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    message: Message? = null,
    data: T
) {
    respond(statusCode, Response(message, data))
}