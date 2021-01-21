package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.permission.PermissionService
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.permission() {
    val service: PermissionService by inject()

    get("/permissions") {
        call.success(data = service.getAll())
    }
}