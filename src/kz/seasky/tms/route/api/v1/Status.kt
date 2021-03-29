package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.extensions.success
import kz.seasky.tms.repository.status.StatusService
import org.koin.ktor.ext.inject

fun Route.status() {
    val service: StatusService by inject()

    route("/statuses") {
        get {
            call.success(data = service.getAll())
        }
    }
}