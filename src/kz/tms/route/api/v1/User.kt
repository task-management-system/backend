package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kz.tms.database.data.user.UserService
import kz.tms.model.Response
import org.koin.ktor.ext.inject

fun Route.user() {
    val service: UserService by inject()

    get("/users") {
        call.respond(Response(null, service.getAll()))
    }
}