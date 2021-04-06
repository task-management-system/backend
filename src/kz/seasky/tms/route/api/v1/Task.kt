package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.extensions.*
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.task.TaskInsert
import kz.seasky.tms.repository.task.TaskService
import kz.seasky.tms.utils.FILE_DEFAULT_SIZE
import kz.seasky.tms.utils.FileHelper
import kz.seasky.tms.utils.asMib
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

        route("/file") {
            post("/add") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()
                val parts = call.receiveMultipart().readAllParts()

                val files = service.addFile(userId.asUUID(), taskId, parts.filterIsInstance<PartData.FileItem>())

                if (files[FileHelper.KEY_ERROR].isNullOrEmpty()) {
                    call.success(
                        message = "Файл(ы) успешно загружен(ы)",
                        data = files[FileHelper.KEY_SUCCESS]
                    )
                } else {
                    call.warning(
                        message = "Максимальный размер файла ${FILE_DEFAULT_SIZE.asMib()}МБ, по этой причине не удалось загрузить ${files[FileHelper.KEY_ERROR]?.size} файл(ов).",
                        data = files
                    )
                }
            }
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
                    data = service.cancelTaskInstance(userId, taskId)
                )
            }

            patch("/close") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()

                call.success(
                    message = "Задача успешно закрыта",
                    data = service.closeTask(userId, taskId)
                )
            }

            patch("/delete") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>()

                service.deleteTask(userId, taskId)

                call.success(
                    message = "Задача успешно удалена",
                    data = mapOf("id" to taskId.toString())
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