package kz.seasky.tms.extensions

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.Response

suspend fun <T> ApplicationCall.respond(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    response: Response<T>,
) {
    respond(status = statusCode, message = response)
}

suspend fun <T> ApplicationCall.success(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    message: String? = null,
    data: T? = null
) {
    respond(statusCode = statusCode, response = Response.Success(message, data))
}

suspend fun ApplicationCall.successfullyAdded() {
    respond(
        statusCode = HttpStatusCode.Created,
        response = Response.Success(Message.DATA_SUCCESSFULLY_ADDED, null)
    )
}

suspend fun ApplicationCall.successfullyUpdated() {
    respond(
        statusCode = HttpStatusCode.OK,
        response = Response.Success(Message.DATA_SUCCESSFULLY_UPDATED, null)
    )
}

suspend fun ApplicationCall.successfullyDeleted() {
    respond(
        statusCode = HttpStatusCode.OK,
        response = Response.Success(Message.DATA_SUCCESSFULLY_DELETED, null)
    )
}

suspend fun <T> ApplicationCall.error(
    statusCode: HttpStatusCode = HttpStatusCode.BadRequest,
    message: String? = null,
    data: T? = null,
    stackTrace: String? = null
) {
    respond(statusCode = statusCode, response = Response.Error(message, data, stackTrace))
}

suspend fun <T> ApplicationCall.warning(
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    message: String? = null,
    data: T? = null
) {
    respond(statusCode = statusCode, response = Response.Warning(message, data))
}