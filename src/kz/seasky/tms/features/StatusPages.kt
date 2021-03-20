package kz.seasky.tms.features

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.seasky.tms.exceptions.PagingException
import kz.seasky.tms.exceptions.PermissionException
import kz.seasky.tms.exceptions.UserChangePasswordException
import kz.seasky.tms.utils.error

fun Application.installStatusPages() {
    install(StatusPages) {
        /** Errors by status code */
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

        /** Errors by exception */
        exception<PermissionException> { e ->
            call.error<Nothing>(
                statusCode = HttpStatusCode.Forbidden,
                message = e.message
            )
        }

        exception<PagingException> { e ->
            call.error<Nothing>(
                statusCode = HttpStatusCode.BadRequest,
                message = e.message
            )
        }

        exception<UserChangePasswordException> { e ->
            call.error<Nothing>(
                statusCode = HttpStatusCode.BadRequest,
                message = e.message
            )
        }

        exception<Throwable> { e ->
            //TODO configure with build variant
            e.printStackTrace()
            call.error<Nothing>(
                statusCode = HttpStatusCode.InternalServerError,
                message = "Что-то пошло не так",
                stackTrace = e.stackTraceToString()
            )
        }
    }
}