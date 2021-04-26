package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.statistics.Statistics
import kz.seasky.tms.repository.statistics.StatisticsService
import org.koin.ktor.ext.inject

fun Route.statistics() {
    val service: StatisticsService by inject()

    get("/smile") { call.respondText("Smile") }

    get("/sweet") { call.respondText("Sweet") }

    get("/sister") { call.respondText("Sister") }

    get("/statistics") {
        val statistics = call.sessions.get<Statistics>() ?: service.getAndUpdateSession(call.sessions)
        call.success(data = statistics)
    }

    get("/surprise") { call.respondText("Surprise") }

    get("/service") { call.respondText("Service") }

    get("/we-are-spirit") { call.respondText("We Are Spirit") }
}