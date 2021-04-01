package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.extensions.receiveAndValidate
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.authentication.AuthenticationCredential
import kz.seasky.tms.repository.authentication.AuthenticationService
import org.koin.ktor.ext.inject

fun Route.authentication() {
    val service: AuthenticationService by inject()

    post("/authentication") {
        val credentials = call.receiveAndValidate<AuthenticationCredential>()

        call.success(data = service.authenticate(credentials))
    }
}