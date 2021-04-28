package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.routing.*
import kotlinx.uuid.UUID
import kz.seasky.tms.exceptions.ErrorException
import kz.seasky.tms.extensions.*
import kz.seasky.tms.features.withPermission
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

        withPermission(Permission.ViewUser.power or Permission.Administrator.power) {
            get {
                val id = call.getId<String>()

                call.success(data = service.getById(id))
            }
        }

        withPermission(Permission.InsertUser.power or Permission.Administrator.power) {
            put {
                val user = call.receiveAndValidate<UserInsert>()

                call.success(
                    statusCode = HttpStatusCode.Created,
                    message = "Пользователь успешно добавлен",
                    data = service.insert(user)
                )
            }
        }

        withPermission(Permission.DeleteUser.power or Permission.Administrator.power) {
            delete {
                val id = call.getId<String>()

                service.deleteById(id)

                call.success(
                    message = "Пользователь успешно удален",
                    data = mapOf("id" to id)
                )
            }
        }

        patch {
            val principal = call.getPrincipal<AuthenticationPrincipal>()
            val user = call.receiveAndValidate<UserUpdate>()

            val permission = Permission.Administrator.power
            val isCurrentUser = principal.id == user.id
            val isPermissionEnough = principal.power and permission != 0L

            if (!isCurrentUser) {
                if (!isPermissionEnough) throw ErrorException("У вас недостаточно прав")
            }

            val result = if (isCurrentUser && !isPermissionEnough) service.updateAsUser(user) else service.update(user)

            call.success(
                message = "Пользователь успешно обновлен",
                data = result
            )
        }

        patch("/change-password") {
            val principal = call.getPrincipal<AuthenticationPrincipal>()
            val user = call.receiveAndValidate<UserChangePassword>()
            if (principal.id != user.id) throw ErrorException("Не пытайтесь поменять пароль другому пользователю")

            service.validatePassword(user.id.asUUID(), user.oldPassword)

            call.success(
                message = "Пароль успешно изменен",
                data = service.changePassword(user.id.asUUID(), user.newPassword)
            )
        }

        withPermission(Permission.UpdateUser.power or Permission.Administrator.power) {
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
        }
    }

    route("/users") {
        withPermission(Permission.ViewUser.power or Permission.Administrator.power) {
            get {
                call.success(data = service.getAll())
            }

            get("/available") {
                val userId = call.getPrincipal<AuthenticationPrincipal>().id.asUUID()

                call.success(data = service.getAllAvailable(userId))
            }
        }

        withPermission(Permission.UpdateUser.power or Permission.Administrator.power) {
            //FIXME
            put {
                val users = call.receiveOrException<Array<UserInsert>>().toList()

                call.success(
                    message = "Пользователи успешно добавлены",
                    data = service.batchInsert(users)
                )
            }
        }
    }
}