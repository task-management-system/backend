package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.enums.Status
import kz.seasky.tms.extensions.*
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.notification.EmailNotification
import kz.seasky.tms.model.task.TaskInsert
import kz.seasky.tms.model.task.TaskPrepare
import kz.seasky.tms.repository.task.TaskService
import kz.seasky.tms.repository.user.UserService
import org.koin.ktor.ext.inject

fun Route.task() {
    val service: TaskService by inject()
    val userService: UserService by inject()
    val client: HttpClient by inject()

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
            val user = call.getPrincipal<AuthenticationPrincipal>()
            val task = call.receiveAndValidate<TaskInsert>()

            call.success(
                message = "Задача успешно создана",
                data = service.createTaskAndTaskInstances(user.id, task)
            )

            val executorEmails = task.executorIds.mapNotNull { userService.getById(it).email }
            val notification =
                executorEmails.map { EmailNotification.NewTask(it, task.title, user.username, task.dueDate) }
            client.post {
                url { encodedPath = "/api/v1/notification/new-task" }
                body = notification
            }
        }

        //TODO Experimental path name
        val fileIdPath = "fileIdPath"
        route("/file/{$fileIdPath}") {
            get {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val fileId = call.getId<UUID>(fileIdPath)

                call.respondFile(service.getFile(userId.asUUID(), fileId))
            }

            delete {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val fileId = call.getId<UUID>(fileIdPath)

                service.removeFile(userId.asUUID(), fileId)

                call.success(
                    message = "Файл успешно удален",
                    data = mapOf("id" to fileId.toString())
                )
            }
        }

        //TODO Experimental path name
        val taskInstanceIdPath = "taskInstanceIdPath"
        route("/received/{$taskInstanceIdPath}") {
            get {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskInstanceId = call.getId<UUID>(taskInstanceIdPath)

                call.success(data = service.getReceived(userId, taskInstanceId))
            }

            route("/file") {
                put {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskInstanceId = call.getId<UUID>(taskInstanceIdPath)
                    val parts = call.receiveMultipart().readAllParts()

                    val files = service.addFileToReceived(
                        userId = userId.asUUID(),
                        taskInstanceId = taskInstanceId,
                        parts = parts.filterIsInstance<PartData.FileItem>()
                    )

                    call.file(files)
                }
            }
        }

        //TODO Experimental path name
        val taskIdPath = "taskIdPath"
        route("/created/{$taskIdPath}") {
            get {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id
                val taskId = call.getId<UUID>(taskIdPath)

                call.success(data = service.getCreated(userId, taskId))
            }

            route("/file") {
                put {
                    val userId = call.getPrincipal<AuthenticationPrincipal>().id
                    val taskId = call.getId<UUID>(taskIdPath)
                    val parts = call.receiveMultipart().readAllParts()

                    val files = service.addFileToCreated(
                        userId = userId.asUUID(),
                        taskId = taskId,
                        parts = parts.filterIsInstance<PartData.FileItem>()
                    )

                    call.file(files)
                }
            }
        }

        route("/action") {
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

                val task = service.closeTask(userId, taskId)

                call.success(
                    message = "Задача успешно закрыта",
                    data = task
                )

                if (task.parent.status.id != Status.Closed.value) return@patch

                val receiver = task.creator.email ?: return@patch
                val executorUsernames = service.getTaskExecutors(task.parent.id.asUUID()).map { it.username }
                val notification =
                    EmailNotification.CloseTask(receiver, task.title, executorUsernames, task.createdAt, task.dueDate)
                client.post {
                    url { encodedPath = "/api/v1/notification/close-task" }
                    body = notification
                }
            }

            patch("/delete") {
                val user = call.getPrincipal<AuthenticationPrincipal>()
                val taskId = call.getId<UUID>()

                val executorEmails = service.getTaskExecutors(taskId).mapNotNull { it.email }
                val task = service.deleteTask(user.id.asUUID(), taskId)

                call.success(
                    message = "Задача успешно удалена",
                    data = mapOf("id" to taskId.toString())
                )

                val notification = executorEmails.map { EmailNotification.DeleteTask(it, task.title, user.username) }
                client.post {
                    url { encodedPath = "/api/v1/notification/delete-task" }
                    body = notification
                }
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