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
import kz.seasky.tms.utils.FileHelper

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

@Deprecated("WIP, can be replaced in future")
suspend fun ApplicationCall.file(files: HashMap<Char, MutableList<Any>>) {
    when {
        files[FileHelper.KEY_SUCCESS].isNullOrEmpty() -> {
            error(
                message = "Не удалось загрузить файл(ы)",
                data = files
            )
        }
        files[FileHelper.KEY_ERROR].isNullOrEmpty() -> {
            success(
                message = "Файл(ы) успешно загружен(ы)",
                data = files
            )
        }
        else -> {
            warning(
                message = "${files[FileHelper.KEY_SUCCESS]?.size ?: 0} файл(а) успешно загружен(ы). ${files[FileHelper.KEY_ERROR]?.size ?: 0} файл(ов) не удалось загрузить",
                data = files
            )
        }
    }
}


suspend inline fun <reified T> ApplicationCall.receiveOrException(): T {
    return receiveOrNull() ?: throw ErrorException(Message.FILL_PAYLOAD)
}

suspend inline fun <reified T : ReceiveValidator> ApplicationCall.receiveAndValidate(): T {
    return receiveOrException<T>().validate()
}

inline fun <reified P : Principal> ApplicationCall.getPrincipal(): P {
    return authentication.principal() as? P ?: throw ErrorException(
        message = Message.PRINCIPAL_NOT_FOUND,
        statusCode = HttpStatusCode.Unauthorized
    )
}

@Suppress("FoldInitializerAndIfToElvis")
@OptIn(UUIDExperimentalAPI::class)
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