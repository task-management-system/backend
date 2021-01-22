package kz.tms.features

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.tms.exceptions.PermissionException
import kz.tms.utils.error

fun Application.installStatusPages() {
    install(StatusPages) {
        /**
         * Errors by status code
         */
        status(HttpStatusCode.Unauthorized) { code ->
            call.error<Nothing>(
                statusCode = code,
                message = "Дружок пирожок авторазьку то не прошел, купи мне питсы скину лог пасс от админки"
            )
        }

        /**
         * Errors by exception
         */
        exception<PermissionException> { e ->
            call.error<Nothing>(
                statusCode = HttpStatusCode.Forbidden,
                message = e.message
            )
        }

        exception<Throwable> { e ->
            call.error<Nothing>(
                statusCode = HttpStatusCode.InternalServerError,
                message = "Что-то пошло не так",
                stackTrace = e.stackTraceToString()
            )
        }
    }
}