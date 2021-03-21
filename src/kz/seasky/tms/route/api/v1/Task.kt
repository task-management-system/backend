package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.seasky.tms.database.data.detail.DetailService
import kz.seasky.tms.database.data.task.TaskService
import kz.seasky.tms.extensions.asPaging
import kz.seasky.tms.extensions.error
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.paging.PagingResponse
import kz.seasky.tms.model.task.DetailCreate
import kz.seasky.tms.model.task.TaskCreate
import org.koin.ktor.ext.inject

fun Route.task() {
    val detailService: DetailService by inject()
    val taskService: TaskService by inject()

    route("/task") {
        //TODO
        put {
            val userId = call.principal<AuthenticationPrincipal>()?.userId ?: return@put call.error<Nothing>(
                statusCode = HttpStatusCode.Unauthorized,
                message = Message.PRINCIPAL_NOT_FOUND
            )
            val taskCreate = call.receive<TaskCreate>()
            taskCreate.creatorId = userId

            val task = taskService.insert(taskCreate) ?: return@put call.error<Nothing>(
                message = "Не удалось создать задачу"
            )

            val details = arrayListOf<DetailCreate>()
            taskCreate.executorIds.forEach { executorId ->
                details.add(DetailCreate(task.taskId, executorId, 1))
            }

            val batchInsertResult = detailService.batchInsert(details)

            when {
                batchInsertResult == 0 -> {
                    taskService.delete(task.taskId)
                    call.error<Nothing>(message = "Не удалось добавить детали")
                }
                batchInsertResult >= 1 -> call.success<Nothing>(message = "Задача создана")
            }
        }
    }

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

        get("/created") {
            val userId = call.principal<AuthenticationPrincipal>()?.userId ?: return@get call.error<Nothing>(
                statusCode = HttpStatusCode.Unauthorized,
                message = Message.PRINCIPAL_NOT_FOUND
            )
            val paging = call.parameters.asPaging()

            val totalCount = taskService.count(userId)
            val tasks = taskService.getAll(userId, paging)

            call.success(data = PagingResponse(total = totalCount, list = tasks))
        }
    }
}