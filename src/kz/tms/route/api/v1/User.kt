package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kz.tms.database.data.user.UserService
import kz.tms.model.response.Response
import kz.tms.utils.respond
import org.koin.ktor.ext.inject

fun Route.user() {
    val service: UserService by inject()

    get("/user2") {
        val username = try {
            call.parameters["username"]!!
        } catch (e: Exception) {
            ""
        }
        call.respond(data = service.getByUsernameOrNull(username))
    }

    get("/users") {
        call.respond(Response(null, service.getAll()))
    }
}