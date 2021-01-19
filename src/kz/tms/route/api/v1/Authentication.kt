package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.tms.authentication.AuthenticationService
import kz.tms.model.authentication.AuthenticationCredential
import kz.tms.utils.respond
import org.koin.ktor.ext.inject

fun Route.authentication() {
    val service: AuthenticationService by inject()

    post("/authentication") {
        val credentials = call.receive<AuthenticationCredential>()
        call.respond(response = service.authenticate(credentials))
    }
}