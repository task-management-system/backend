package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kz.seasky.tms.authentication.AuthenticationService
import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.utils.respond
import org.koin.ktor.ext.inject

fun Route.authentication() {
    val service: AuthenticationService by inject()

    post("/authentication") {
        val credentials = call.receive<AuthenticationCredential>()
        call.respond(response = service.authenticate(credentials))
    }
}