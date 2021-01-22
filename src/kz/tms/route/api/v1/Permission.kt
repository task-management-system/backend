package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.utils.Permission
import kz.tms.utils.success

fun Route.permission() {
    get("/permissions") {
        call.success(data = Permission.all)
    }
}