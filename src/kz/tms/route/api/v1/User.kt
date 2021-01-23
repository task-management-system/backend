package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kz.tms.database.data.roles.RoleService
import kz.tms.database.data.user.UserService
import kz.tms.database.data.user.merge
import kz.tms.features.withPermission
import kz.tms.model.Paging
import kz.tms.model.user.UserPayload
import kz.tms.utils.*
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService: UserService by inject()
    val roleService: RoleService by inject()

    route("/user") {
        withPermission(Permission.ViewUser.power) {
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

        withPermission(Permission.InsertUser.power) {
            put {
                val userPayload = call.receive<UserPayload>()
                val roleId = roleService.getIdByPowerOrNull(userPayload.rolePower) ?: return@put call.error<Nothing>(
                    message = "Не удалось найти роль"
                )

                val user = userPayload merge roleId
                userService.insert(user).insertRespond(this)
            }
        }

        withPermission(Permission.DeleteUser.power) {
            delete {
                val id = call.parameters["id"]?.toLong() ?: return@delete call.warning<Nothing>(
                    message = "Укажите идентификатор пользователя",
                )

                userService.deleteById(id).deleteRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )
                val userPayload = call.receive<UserPayload>()
                val roleId = roleService.getIdByPowerOrNull(userPayload.rolePower) ?: return@patch call.error<Nothing>(
                    message = "Не удалось найти роль"
                )

                val user = userPayload merge roleId

                userService.updateById(id, user).updateRespond(this)
            }
        }

        withPermission(Permission.UpdateUser.power) {
            patch("/lock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )

                userService.lock(id).respond(
                    context = this,
                    successMessage = "Пользователь заблокирован",
                    errorMessage = "Не удалось заблокировать пользователя"
                )
            }

            patch("/unlock") {
                val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                    message = "Укажите идентификатор пользователя"
                )

                userService.unlock(id).respond(
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
                val paging = call.receiveOrNull<Paging>() ?: return@get call.error<Nothing>(
                    message = "Ожидался пейлоад, а получилось как всегда"
                )

                val result = paging.validate()
                if (!result.isNullOrEmpty()) return@get call.error<Nothing>(
                    message = result
                )

                call.success(
                    data = mapOf(
                        "totalCount" to userService.count(),
                        "page" to paging.page,
                        "size" to paging.size,
                        "users" to userService.getAll(paging)
                    )
                )
            }

            put {
                val userPayloads = call.receive<Array<UserPayload>>()
                val roleIds = arrayListOf<Long>()
                userPayloads.forEachIndexed { index, item ->
                    val roleId = roleService.getIdByPowerOrNull(item.rolePower) ?: return@put call.error(
                        message = "Не удалось найти роль по индексу $index",
                        data = mapOf("index" to index)
                    )
                    roleIds.add(roleId)
                }

                val users = userPayloads merge roleIds

                userService.batchInsert(users).insertRespond(this)
            }
        }
    }
}