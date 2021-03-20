package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kz.seasky.tms.database.data.user.UserService
import kz.seasky.tms.features.withPermission
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.paging.PagingResponse
import kz.seasky.tms.model.user.UserChangePassword
import kz.seasky.tms.model.user.UserEntity
import kz.seasky.tms.model.user.UserWithRoleId
import kz.seasky.tms.utils.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val service: UserService by inject()

    route("/user") {
        get("current") {
            val principal = call.principal<AuthenticationPrincipal>() ?: return@get call.error<Nothing>(
                statusCode = HttpStatusCode.Unauthorized,
                message = Message.PRINCIPAL_NOT_FOUND
            )

            call.success(data = service.getByIdOrNull(principal.userId!!))
        }

        withPermission(Permission.ViewUser.power) {
            get {
                val id = call.parameters["id"]?.toLong() ?: return@get call.warning<Nothing>(
                    message = Message.INDICATE_USER_ID,
                )

                val user = service.getByIdOrNull(id) ?: return@get call.error<Nothing>(
                    message = "Не удалось найти пользователя по указанному идентификатору"
                )

                call.success(data = user)
            }
        }

        withPermission(Permission.InsertUser.power) {
            put {
                val user = call.receive<UserEntity>()
                service.insert(user).insertRespond(this)
            }
        }

        withPermission(Permission.DeleteUser.power) {
            delete {
                val id = call.parameters["id"]?.toLong() ?: return@delete call.warning<Nothing>(
                    message = Message.INDICATE_USER_ID,
                )

                service.deleteById(id).deleteRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = Message.INDICATE_USER_ID
                )
                val user = call.receiveOrNull<UserWithRoleId>() ?: return@patch call.error<Nothing>(
                    message = Message.FILL_PAYLOAD
                )

                service.updateById(id, user).updateRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch("/lock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = Message.INDICATE_USER_ID
                )

                service.lock(id).respond(
                    context = this,
                    successMessage = "Пользователь заблокирован",
                    errorMessage = "Не удалось заблокировать пользователя"
                )
            }

            patch("/unlock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = Message.INDICATE_USER_ID
                )

                service.unlock(id).respond(
                    context = this,
                    successMessage = "Пользователь разблокирован",
                    errorMessage = "Не удалось разблокировать пользователя"
                )
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch("/change-password") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = Message.INDICATE_USER_ID
                )
                val userChangePassword = call.receiveOrNull<UserChangePassword>() ?: return@patch call.error<Nothing>(
                    message = Message.FILL_PAYLOAD
                )

                userChangePassword.postValidate()

                service.validatePassword(id, userChangePassword.currentPassword) ?: return@patch call.error<Nothing>(
                    message = "Текущий пароль не совпадает с паролем в базе"
                )

                when (service.changePassword(id, userChangePassword.newPassword)) {
                    0 -> call.error<Nothing>(message = "Не удалось изменить пароль")
                    1 -> call.success<Nothing>(message = "Пароль успешно изменен")
                }
            }
        }
    }

    withPermission(Permission.ViewUser.power) {
        route("/users") {
            get {
                call.success(
                    data = PagingResponse(
                        total = service.count(),
                        list = service.getAll()
                    )
                )
            }

            put {
                val users = call.receiveOrNull<Array<UserEntity>>()?.toList() ?: call.error<Nothing>(
                    message = Message.FILL_PAYLOAD
                )
                service.batchInsert(users as List<UserEntity>).insertRespond(this)
            }
        }
    }
}