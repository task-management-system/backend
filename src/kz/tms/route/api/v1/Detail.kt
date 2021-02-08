package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.database.data.detail.DetailService
import kz.tms.model.authentication.AuthenticationPrincipal
import kz.tms.model.paging.Paging
import kz.tms.utils.error
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.detail() {
    val detailService: DetailService by inject()

    get("/details") {
        val statusId = call.parameters["statusId"]?.toShort() ?: return@get call.error<Nothing>(
            message = "Дай статус айди"
        )
        val paging = call.receiveOrNull<Paging>() ?: return@get call.error<Nothing>(
            message = "Дай пейлоад"
        )

        val result = paging.validate()
        if (!result.isNullOrEmpty()) return@get call.error<Nothing>(
            message = result
        )

        val userId = call.principal<AuthenticationPrincipal>()?.userId ?: return@get call.error<Nothing>(
            statusCode = HttpStatusCode.Unauthorized,
            message = "Не удалось получить аутентификационные данные"
        )

        call.success(data = detailService.getAll(userId, statusId, paging))
    }
}