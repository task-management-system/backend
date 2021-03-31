package kz.seasky.tms.extensions

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import kotlinx.uuid.UUID
import kotlinx.uuid.UUIDExperimentalAPI
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.ReceiveValidator
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

suspend inline fun <reified T> ApplicationCall.receiveOrException(): T {
    return receiveOrNull() ?: throw ErrorException(Message.FILL_PAYLOAD)
}

suspend inline fun <reified T : ReceiveValidator> ApplicationCall.receiveAndValidate(): T {
    return validate(receiveOrNull())
}

fun <T : ReceiveValidator> validate(whatToValidate: T?): T {
    return whatToValidate?.validate() ?: throw ErrorException(Message.FILL_PAYLOAD)
}

inline fun <reified P : Principal> ApplicationCall.getPrincipal(): P {
    return authentication.principal() as? P ?: throw ErrorException(
        message = Message.PRINCIPAL_NOT_FOUND,
        statusCode = HttpStatusCode.Unauthorized
    )
}

@Suppress("FoldInitializerAndIfToElvis")
@OptIn(ExperimentalStdlibApi::class, UUIDExperimentalAPI::class)
inline fun <reified T> ApplicationCall.getId(idName: String = "id"): T {
    val id = parameters[idName] ?: throw ErrorException(Message.INDICATE_ID + "'$idName'")

    val result = when (T::class) {
        String::class -> id
        UUID::class -> if (UUID.isValidUUIDString(id)) UUID(id) else null
        Short::class -> id.toShortOrNull()
        Int::class -> id.toIntOrNull()
        Long::class -> id.toLongOrNull()
        else -> null
    }

    if (result == null) throw ErrorException("Can't convert passed $id to type ${T::class}")

    return result as T
}

fun String.asUUID() = UUID(this)

fun String.asLong() = toLongOrNull() ?: throw ErrorException("Can't convert string '$this' to long")