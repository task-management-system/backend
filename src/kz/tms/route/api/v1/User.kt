package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kz.tms.database.data.user.UserService
import kz.tms.features.withPermission
import kz.tms.model.paging.PagingResponse
import kz.tms.model.user.User
import kz.tms.utils.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val service: UserService by inject()

    route("/user") {
        withPermission(Permission.ViewUser.power) {
            get {
                val id = call.parameters["id"]?.toLong() ?: return@get call.warning<Nothing>(
                    message = "Укажите идентификатор пользователя",
                )

                val user = service.getByIdOrNull(id) ?: return@get call.error<Nothing>(
                    message = "Не удалось найти пользователя по указанному идентификатору"
                )

                call.success(data = user)
            }
        }

        withPermission(Permission.InsertUser.power) {
            put {
                val user = call.receive<User>()
                service.insert(user).insertRespond(this)
            }
        }

        withPermission(Permission.DeleteUser.power) {
            delete {
                val id = call.parameters["id"]?.toLong() ?: return@delete call.warning<Nothing>(
                    message = "Укажите идентификатор пользователя",
                )

                service.deleteById(id).deleteRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )
                val user = call.receiveOrNull<User>() ?: return@patch call.error<Nothing>(
                    message = "Заполни пейлоад а АААА"
                )

                service.updateById(id, user).updateRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch("/lock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )

                service.lock(id).respond(
                    context = this,
                    successMessage = "Пользователь заблокирован",
                    errorMessage = "Не удалось заблокировать пользователя"
                )
            }

            patch("/unlock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )

                service.unlock(id).respond(
                    context = this,
                    successMessage = "Пользователь разблокирован",
                    errorMessage = "Не удалось разблокировать пользователя"
                )
            }
        }
    }

    withPermission(Permission.ViewUser.power) {
        route("/users") {
            get {
                val paging = call.parameters.asPaging()

                call.success(
                    data = PagingResponse(
                        total = service.count(),
                        list = service.getAll(paging)
                    )
                )
            }

            put {
                val users = call.receiveOrNull<Array<User>>()?.toList() ?: call.error<Nothing>(
                    message = "Дай пейлоад братан"
                )
                service.batchInsert(users as List<User>).insertRespond(this)
            }
        }
    }
}