package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.extensions.*
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.task.TaskInsert
import kz.seasky.tms.repository.task.TaskService
import org.koin.ktor.ext.inject

fun Route.task() {
    val service: TaskService by inject()

    route("/task") {
        put {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val task = call.receiveAndValidate<TaskInsert>()

            call.success(
                message = "Задача успешно создана",
                data = service.createTaskAndTaskInstance(userId, task)
            )
        }

        get("/received") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val taskId = call.getId<UUID>()

            call.success(data = service.getReceived(userId, taskId))
        }

        get("/created") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val taskId = call.getId<UUID>()

            call.success(data = service.getCreated(userId, taskId))
        }

        route("action") {
            patch("/cancel") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()

                call.success(
                    message = "Задача успешно отменена",
                    data = service.cancel(userId, taskId)
                )
            }

            patch("/close") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()

                call.success(
                    message = "Задача успешно закрыта",
                    data = service.close(userId, taskId)
                )
            }

            patch("/delete") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()

                service.delete(userId, taskId)

                call.success(
                    message = "Задача успешно удалена",
                    data = taskId.map()
                )
            }
        }
    }

    route("/tasks") {
        get("/received") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val statusId = call.getId<Short>("statusId")
            val paging = call.parameters.asPaging()

            call.success(data = service.getReceivedPreview(userId, statusId, paging))
        }

        get("/created") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val statusId = call.getId<Short>("statusId")
            val paging = call.parameters.asPaging()

            call.success(data = service.getCreatedPreview(userId, statusId, paging))
        }
    }
}