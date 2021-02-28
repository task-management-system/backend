package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.status.StatusService
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.status() {
    val service: StatusService by inject()

    route("/statuses") {
        get {
            call.success(data = service.getAll())
        }
    }
}