package kz.tms.route.api.v1

import io.ktor.auth.*
import io.ktor.routing.*

fun Route.v1() {
    route("/api/v1") {
        authentication()

        authenticate("token") {
            user()
        }
    }
}