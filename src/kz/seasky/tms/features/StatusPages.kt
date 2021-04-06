package kz.seasky.tms.features

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.seasky.tms.enums.BuildVariant
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.exceptions.WarningException
import kz.seasky.tms.extensions.error
import kz.seasky.tms.extensions.warning
import kz.seasky.tms.utils.BuildConfig
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Application.installStatusPages() {
    install(StatusPages) {
        byStatusCode()

        byException()

        exception<Throwable> { e ->
            if (BuildConfig.buildVariant == BuildVariant.Develop) {
                e.printStackTrace()
            }

            call.error<Nothing>(
                statusCode = HttpStatusCode.InternalServerError,
                message = "Что-то пошло не так",
                stackTrace = e.stackTraceToString()
            )
        }
    }
}

/** Errors by status code */
fun StatusPages.Configuration.byStatusCode() {
    status(HttpStatusCode.Unauthorized) { code ->
        call.error<Nothing>(
            statusCode = code,
            message = "Авторизационные данные недействительны"
        )
    }

    status(HttpStatusCode.NotFound) { code ->
        call.error<Nothing>(
            statusCode = code,
            message = "Страница не найдена"
        )
    }
}

/** Errors by exception */
fun StatusPages.Configuration.byException() {
    exception<ErrorException> { e ->
        call.error<Nothing>(
            statusCode = e.statusCode ?: HttpStatusCode.BadRequest,
            message = e.message,
            stackTrace = if (e.withStackTrace) e.stackTraceToString() else null
        )
    }

    exception<WarningException> { e ->
        call.warning<Nothing>(
            statusCode = e.statusCode ?: HttpStatusCode.OK,
            message = e.message
        )
    }

    exception<ExposedSQLException> { e ->
        call.error<Nothing>(
            statusCode = HttpStatusCode.BadRequest,
            message = e.message
        )
    }

    exception<EntityNotFoundException> { e ->
        call.error<Nothing>(
            statusCode = HttpStatusCode.BadRequest,
            message = e.message
        )
    }
}