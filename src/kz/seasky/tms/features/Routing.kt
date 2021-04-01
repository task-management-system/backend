package kz.seasky.tms.features

import io.ktor.application.*
import io.ktor.routing.*
import kz.seasky.tms.route.api.v1.v1

fun Application.installRouting() {
    install(Routing) {
        trace { application.log.trace(it.buildText()) }
        v1()
    }
}