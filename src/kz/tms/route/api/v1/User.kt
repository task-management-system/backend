package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.database.data.user.UserService
import kz.tms.database.data.user.merge
import kz.tms.model.user.UserPayload
import kz.tms.utils.error
import kz.tms.utils.success
import kz.tms.utils.warning
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService: UserService by inject()
    val roleService: RoleService by inject()

    put("/insert_user") {
        val userPayload = call.receive<UserPayload>()
        val roleId = roleService.getIdByPowerOrNull(userPayload.rolePower) ?: return@put call.error<Nothing>(
            message = "Не удалось найти роль"
        )

        val user = userPayload merge roleId
        val insertResult = userService.insert(user)

        call.success(
            statusCode = HttpStatusCode.Accepted,
            message = "Данные успешно добавлены",
            data = insertResult.resultedValues
        )
    }

    get("/user") {
        val id = call.parameters["id"]?.toLong() ?: return@get call.warning<Nothing>(
            message = "Укажите идентификатор пользователя",
        )

        val user = userService.getByIdOrNull(id) ?: return@get call.error<Nothing>(
            message = "Не удалось найти пользователя по указанному идентификатору"
        )

        call.success(data = user)
    }

    get("/users") {
        call.success(
            data = userService.getAll()
        )
    }
}