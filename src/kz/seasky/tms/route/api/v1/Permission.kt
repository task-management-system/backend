package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.extensions.success
import kz.seasky.tms.utils.Permission

fun Route.permission() {
    get("/permissions") {
        call.success(data = Permission.all)
    }
}