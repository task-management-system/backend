package kz.tms.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import kz.tms.model.Response
import kz.tms.model.paging.Paging
import org.jetbrains.exposed.sql.*

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

suspend fun Int.respond(
    context: PipelineContext<Unit, ApplicationCall>,
    successMessage: String,
    errorMessage: String,
    successStatusCode: HttpStatusCode = HttpStatusCode.OK,
    errorStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) {
    context.apply {
        when (this@respond != 0) {
            true -> call.success<Nothing>(
                statusCode = successStatusCode,
                message = successMessage
            )
            false -> call.error<Nothing>(
                statusCode = errorStatusCode,
                message = errorMessage
            )
        }
    }
}

//TODO rename
suspend fun Int.insertRespond(
    context: PipelineContext<Unit, ApplicationCall>,
    successStatusCode: HttpStatusCode = HttpStatusCode.Created,
    errorStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) {
    respond(
        context = context,
        successMessage = "Данные успешно добавлены",
        errorMessage = "Не удалось добавить данные",
        successStatusCode = successStatusCode,
        errorStatusCode = errorStatusCode
    )
}

//TODO rename
suspend fun Int.updateRespond(
    context: PipelineContext<Unit, ApplicationCall>,
    successStatusCode: HttpStatusCode = HttpStatusCode.OK,
    errorStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) {
    respond(
        context = context,
        successMessage = "Данные успешно обновлены",
        errorMessage = "Не удалось обновить данные",
        successStatusCode = successStatusCode,
        errorStatusCode = errorStatusCode
    )
}

//TODO rename
suspend fun Int.deleteRespond(
    context: PipelineContext<Unit, ApplicationCall>,
    successStatusCode: HttpStatusCode = HttpStatusCode.OK,
    errorStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) {
    respond(
        context = context,
        successMessage = "Данные успешно удалены",
        errorMessage = "Не удалось удалить данные",
        successStatusCode = successStatusCode,
        errorStatusCode = errorStatusCode
    )
}

fun FieldSet.selectAll(id: Column<Long>, paging: Paging): Query {
    return Query(this, null)
        .orderBy(id to paging.sortOrder)
        .limit(paging.limit, paging.offset)
}