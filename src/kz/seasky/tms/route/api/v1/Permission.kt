package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.utils.Permission
import kz.seasky.tms.utils.success

fun Route.permission() {
    get("/permissions") {
        call.success(data = Permission.all)
    }
}