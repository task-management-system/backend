package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.database.data.user.UserService
import kz.tms.database.data.user.merge
import kz.tms.features.withPermission
import kz.tms.model.user.UserPayload
import kz.tms.utils.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService: UserService by inject()
    val roleService: RoleService by inject()

    route("/user") {

        withPermission(Permission.ViewUser) {
            get {
                val id = call.parameters["id"]?.toLong() ?: return@get call.warning<Nothing>(
                    message = "Укажите идентификатор пользователя",
                )

                val user = userService.getByIdOrNull(id) ?: return@get call.error<Nothing>(
                    message = "Не удалось найти пользователя по указанному идентификатору"
                )

                call.success(data = user)
            }
        }

        withPermission(Permission.InsertUser) {
            put {
                val userPayload = call.receive<UserPayload>()
                val roleId = roleService.getIdByPowerOrNull(userPayload.rolePower) ?: return@put call.error<Nothing>(
                    message = "Не удалось найти роль"
                )

                val user = userPayload merge roleId
                val insertResult = userService.insert(user)

                call.success(
                    statusCode = HttpStatusCode.Created,
                    message = "Пользователь успешно добавлен",
                    data = insertResult.resultedValues
                )
            }
        }

        withPermission(Permission.DeleteUser) {
            delete {
                val id = call.parameters["id"]?.toLong() ?: return@delete call.warning<Nothing>(
                    message = "Укажите идентификатор пользователя",
                )

                when (userService.deleteById(id) != 0) {
                    true -> call.success<Nothing>(
                        message = "Пользователь успешно удален"
                    )
                    false -> call.error<Nothing>(
                        message = "Не удалось удалить пользователя, возможно его и не существует вовсе ¯\\_(ツ)_/¯"
                    )
                }
            }
        }
    }

    withPermission(Permission.ViewUser) {
        get("/users") {
            call.success(data = userService.getAll())
        }
    }
}