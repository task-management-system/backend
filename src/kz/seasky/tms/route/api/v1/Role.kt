package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.seasky.tms.database.data.role.RoleService
import kz.seasky.tms.extensions.error
import kz.seasky.tms.model.Message
import kz.seasky.tms.model.role.Role
import kz.seasky.tms.utils.*
import org.koin.ktor.ext.inject

fun Route.role() {
    val service: RoleService by inject()

    route("/role") {
//        put {
//            val role = call.receiveOrNull<Role>() ?: return@put call.error<Nothing>(
//                message = Message.FILL_PAYLOAD
//            )
//
//            service.insert(role)
//
//            call.successfullyAdded()
//        }

        patch {
            val id = call.parameters["id"]?.toLong() ?: return@patch call.error<Nothing>(
                message = Message.INDICATE_ID + "роли"
            )
            val role = call.receiveOrNull<Role>() ?: return@patch call.error<Nothing>(
                message = Message.FILL_PAYLOAD
            )

//            service.update(id, role)

        }
    }

//    route("/roles") {
//        get {
//            call.success(data = service.getAllOrEmpty())
//        }
//
//        put {
//            val roles = call.receiveOrNull<List<Role>>() ?: return@put call.error<Nothing>(
//                message = Message.FILL_PAYLOAD
//            )
//
//            service.batchInsert(roles)
//
//            call.successfullyAdded()
//        }
//    }
}