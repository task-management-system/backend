package kz.tms.route.api.v1

import io.ktor.application.*
import io.ktor.routing.*
import kz.tms.database.data.claims.ClaimService
import kz.tms.utils.success
import org.koin.ktor.ext.inject

fun Route.claims() {
    val service: ClaimService by inject()

    get("/claims") {
        call.success(data = service.getAll())
    }
}