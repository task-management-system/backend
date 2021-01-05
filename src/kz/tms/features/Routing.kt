package kz.tms.features

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.route.api.v1.v1

fun Application.installRouting() {
    install(Routing) {
        trace { application.log.trace(it.buildText()) }
        v1()
    }
}