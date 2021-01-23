package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.model.role.Role
import kz.tms.utils.error
import kz.tms.utils.insertRespond
import kz.tms.utils.success
import kz.tms.utils.updateRespond
import org.koin.ktor.ext.inject

fun Route.role() {
    val service: RoleService by inject()

    route("/role") {
        put {
            val role = call.receiveOrNull<Role>() ?: return@put call.error<Nothing>(
                message = "Да дай ты мне пейлоад падла"
            )

            service.insert(role).insertRespond(this)
        }

        patch {
            val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                message = "Да дай ты мне айдишник падла"
            )
            val role = call.receiveOrNull<Role>() ?: return@patch call.error<Nothing>(
                message = "Да дай ты мне пейлоад падла"
            )

            service.update(id, role).updateRespond(this)
        }
    }

    route("/roles") {
        get {
            call.success(data = service.getAllOrEmpty())
        }

        put {
            val roles = call.receiveOrNull<List<Role>>() ?: return@put call.error<Nothing>(
                message = "Да ты мне дай чертовые роли"
            )

            service.batchInsert(roles).insertRespond(this)
        }
    }
}