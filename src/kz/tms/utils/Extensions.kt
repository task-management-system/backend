package kz.tms.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kz.tms.model.Response

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

suspend fun Int.patchCall(
    context: PipelineContext<Unit, ApplicationCall>,
    successMessage: String? = null,
    errorMessage: String? = null
) {
    context.apply {
        when (this@patchCall != 0) {
            true -> call.success<Nothing>(
                message = successMessage ?: "Данные успешно обновлены"
            )
            false -> call.error<Nothing>(
                message = errorMessage ?: "Не удалось обновить данные"
            )
        }
    }
}