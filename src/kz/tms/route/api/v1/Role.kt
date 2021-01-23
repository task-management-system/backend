package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.roles.RoleService
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.role() {
    val service: RoleService by inject()

    route("/roles") {
        get {
            call.success(data = service.getAllOrEmpty())
        }
    }
}