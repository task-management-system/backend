package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.database.data.user.UserService
import kz.tms.database.data.user.merge
import kz.tms.model.user.UserPayload
import kz.tms.utils.error
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.user() {
    val userService: UserService by inject()
    val roleService: RoleService by inject()

    put("/insert_user") {
        val userPayload = call.receive<UserPayload>()
        val roleId = roleService.getIdByPowerOrNull(userPayload.rolePower)

        if (roleId == null) {
            call.error<Nothing>(
                message = "Не удалось найти роль"
            )
            return@put
        }

        val user = userPayload merge roleId
        val insertResult = userService.insert(user)
        call.success(
            message = "Данные успешно добавлены",
            data = insertResult.resultedValues
        )
    }

    get("/users") {
        call.success(
            data = userService.getAll()
        )
    }
}