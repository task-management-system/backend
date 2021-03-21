package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.database.data.status.StatusService
import kz.seasky.tms.extensions.success
import org.koin.ktor.ext.inject

fun Route.status() {
    val service: StatusService by inject()

    route("/statuses") {
        get {
            call.success(data = service.getAll())
        }
    }
}