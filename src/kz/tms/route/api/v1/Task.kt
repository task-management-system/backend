package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.routing.*
import kz.tms.database.data.detail.DetailService
import kz.tms.database.data.task.TaskService
import kz.tms.model.Message
import kz.tms.model.authentication.AuthenticationPrincipal
import kz.tms.model.paging.PagingResponse
import kz.tms.utils.asPaging
import kz.tms.utils.error
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.task() {
    val detailService: DetailService by inject()
    val taskService: TaskService by inject()

    route("/tasks") {
        get("/received") {
            val userId = call.principal<AuthenticationPrincipal>()?.userId ?: return@get call.error<Nothing>(
                statusCode = HttpStatusCode.Unauthorized,
                message = Message.PRINCIPAL_NOT_FOUND
            )
            val statusId = call.parameters["statusId"]?.toShort() ?: return@get call.error<Nothing>(
                message = "Вы не указали идентификатор статуса"
            )
            val paging = call.parameters.asPaging()

            val totalCount = detailService.count(userId, statusId)
            val tasks = detailService.getAll(userId, statusId, paging)

            call.success(data = PagingResponse(total = totalCount, list = tasks))
        }

    route("/task") {

    }
}