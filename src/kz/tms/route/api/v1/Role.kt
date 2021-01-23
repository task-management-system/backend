package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.model.role.Role
import kz.tms.utils.error
import kz.tms.utils.insertRespond
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.role() {
    val service: RoleService by inject()

    route("/roles") {
        get {
            call.success(data = service.getAllOrEmpty())
        }

        put {
            val role = call.receiveOrNull<Role>() ?: return@put call.error<Nothing>(
                message = "Да дай ты мне пейлоад падла"
            )

            service.insert(role).insertRespond(this)
        }
    }
}