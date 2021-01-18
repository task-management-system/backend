package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.claims.ClaimService
import kz.tms.utils.respond
import org.koin.ktor.ext.inject

fun Route.claims() {
    val service: ClaimService by inject()

    get("/claims") {
        call.respond(data = service.getAll())
    }
}