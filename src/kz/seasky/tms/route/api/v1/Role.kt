package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.extensions.getId
import kz.seasky.tms.extensions.receiveAndValidate
import kz.seasky.tms.extensions.receiveOrException
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.role.RoleInsert
import kz.seasky.tms.model.role.RoleUpdate
import kz.seasky.tms.repository.role.RoleService
import org.koin.ktor.ext.inject

fun Route.role() {
    val service: RoleService by inject()

    route("/role") {
        put {
            val role = call.receiveAndValidate<RoleInsert>()

            call.success(
                message = "Роль успешно добавлена",
                data = service.insert(role)
            )
        }

        patch {
            val role = call.receiveAndValidate<RoleUpdate>()

            call.success(
                message = "Роль успешно обновлена",
                data = service.update(role)
            )
        }

        delete {
            val id = call.getId<Short>()

            service.delete(id)

            call.success(
                message = "Роль успешно удалена",
                data = mapOf("id" to id)
            )
        }
    }

    route("/roles") {
        get {
            call.success(data = service.getAll())
        }

        //FIXME
        put {
            val roles = call.receiveOrException<Array<RoleInsert>>().toList()

            call.success(
                message = "Роли успешно добавлены",
                data = service.batchInsert(roles)
            )
        }
    }
}