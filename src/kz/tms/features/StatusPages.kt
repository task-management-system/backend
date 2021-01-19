package kz.tms.features

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kz.tms.utils.error

fun Application.installStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) {
            call.error<Nothing>(
                statusCode = HttpStatusCode.Unauthorized,
                message = "Дружок пирожок авторазьку то не прошел, купи мне питсы скину лог пасс от админки"
            )
        }
    }
}