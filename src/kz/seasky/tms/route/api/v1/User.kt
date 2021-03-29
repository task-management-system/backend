package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.*
import kz.seasky.tms.features.withPermission
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.authentication.AuthenticationPrincipal
import kz.seasky.tms.model.user.UserChangePassword
import kz.seasky.tms.model.user.UserInsert
import kz.seasky.tms.model.user.UserUpdate
import kz.seasky.tms.repository.user.UserService
import kz.seasky.tms.utils.Permission
import org.koin.ktor.ext.inject

fun Route.user() {
    val service: UserService by inject()

    route("/user") {
        get("current") {
            val principal = call.getPrincipal<AuthenticationPrincipal>()

            call.success(data = service.getById(principal.id))
        }

        withPermission(Permission.ViewUser.power) {
            get {
                val id = call.getId<String>()

                call.success(data = service.getById(id))
            }
        }

        withPermission(Permission.InsertUser.power) {
            put {
                val user = call.receiveAndValidate<UserInsert>()

                call.success(
                    statusCode = HttpStatusCode.Created,
                    message = "Пользователь успешно добавлен",
                    data = service.insert(user)
                )
            }
        }

        withPermission(Permission.DeleteUser.power) {
            delete {
                val id = call.getId<String>()

                service.deleteById(id)

                call.success(
                    message = "Пользователь успешно удален",
                    data = mapOf("id" to id)
                )
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch {
                val user = call.receiveAndValidate<UserUpdate>()

                call.success(
                    message = "Пользователь успешно обновлен",
                    data = service.update(user)
                )
            }

            patch("/lock") {
                val id = call.getId<UUID>()

                call.success(
                    message = "Пользователь заблокирован",
                    data = service.lock(id)
                )
            }

            patch("/unlock") {
                val id = call.getId<UUID>()

                call.success(
                    message = "Пользователь разблокирован",
                    data = service.unlock(id)
                )
            }

            patch("/change-password") {
                val user = call.receiveAndValidate<UserChangePassword>()

                if (!service.validatePassword(user.id.asUUID(), user.oldPassword)) throw ErrorException(
                    message = "Неверный старый пароль, проверьте корректность введенных данных"
                )

                call.success(
                    message = "Пароль успешно изменен",
                    data = service.changePassword(user.id.asUUID(), user.newPassword)
                )
            }
        }
    }

    route("/users") {
        withPermission(Permission.ViewUser.power) {
            get {
                call.success(data = service.getAll())
            }
        }

        withPermission(Permission.UpdateUser.power) {
            //FIXME
            put {
                val users = call.receiveOrNull<List<UserInsert>>() ?: throw ErrorException(
                    message = Message.FILL_PAYLOAD
                )

                call.success(
                    message = "Пользователи успешно добавлены",
                    data = service.batchInsert(users)
                )
            }
        }
    }
}