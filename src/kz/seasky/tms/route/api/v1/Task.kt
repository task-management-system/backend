package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.extensions.*
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.task.TaskInsert
import kz.seasky.tms.model.task.TaskPrepare
import kz.seasky.tms.repository.task.TaskService
import org.koin.ktor.ext.inject

fun Route.task() {
    val service: TaskService by inject()

    route("/task") {
        put("/prepare") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val task = call.receiveAndValidate<TaskPrepare>()

            call.success(
                message = "Задача успешно подготовлена",
                data = service.prepareTask(userId.asUUID(), task)
            )
        }

        put("/create") {
            val userId = call.getPrincipal<AuthenticationPrincipal>().id
            val task = call.receiveAndValidate<TaskInsert>()

            call.success(
                message = "Задача успешно создана",
                data = service.createTaskAndTaskInstance(userId, task)
            )
        }

        route("/received/{taskInstanceId}") {
            get {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>("taskInstanceId")

                call.success(data = service.getReceived(userId, taskId))
            }

            route("/file") {
                put {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>("taskInstanceId")
                    val parts = call.receiveMultipart().readAllParts()

                    val files = service.addFileToReceived(
                        userId = userId.asUUID(),
                        taskId = taskId,
                        parts = parts.filterIsInstance<PartData.FileItem>()
                    )

                    call.file(files)
                }


                delete {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>("taskInstanceId")
                    val fileId = call.getId<UUID>()

                    service.removeFileFromReceived(userId.asUUID(), taskId, fileId)

                    call.success(
                        message = "Файл успешно удален",
                        data = mapOf("id" to fileId.toString())
                    )
                }
            }
        }

        //TODO Experimental
        val createdTaskId = "taskId"
        route("/created/{$createdTaskId}") {
            get {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>(createdTaskId)

                call.success(data = service.getCreated(userId, taskId))
            }

            route("/file") {
                get {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>(createdTaskId)
                    val fileId = call.getId<UUID>()

                    val file = service.getFileFromCreated(
                        userId = userId.asUUID(),
                        taskId = taskId,
                        fileId = fileId
                    )

                    call.respondFile(file)
                }

                put {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>(createdTaskId)
                    val parts = call.receiveMultipart().readAllParts()

                    val files = service.addFileToCreated(
                        userId = userId.asUUID(),
                        taskId = taskId,
                        parts = parts.filterIsInstance<PartData.FileItem>()
                    )

                    call.file(files)
                }

                delete {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>(createdTaskId)
                    val fileId = call.getId<UUID>()

                    service.removeFileFromCreated(userId.asUUID(), taskId, fileId)

                    call.success(
                        message = "Файл успешно удален",
                        data = mapOf("id" to fileId.toString())
                    )
                }
            }
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