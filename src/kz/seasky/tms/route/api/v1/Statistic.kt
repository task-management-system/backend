package kz.seasky.tms.route.api.v1

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kz.seasky.tms.extensions.success
import kz.seasky.tms.model.statistic.Statistic
import kz.seasky.tms.repository.statistic.StatisticService
import org.koin.ktor.ext.inject

fun Route.statistic() {
    val service: StatisticService by inject()

    get("/smile") { call.respondText("Smile") }

    get("/sweet") { call.respondText("Sweet") }

    get("/sister") { call.respondText("Sister") }

    get("/statistic") {
        val statistic = call.sessions.get<Statistic>()?.let { statistic ->
            return@let statistic
        } ?: service.getAndUpdateSession(call.sessions)
        call.success(data = statistic)
    }

    get("/surprise") { call.respondText("Surprise") }

    get("/service") { call.respondText("Service") }

    get("/we-are-spirit") { call.respondText("We Are Spirit") }
}