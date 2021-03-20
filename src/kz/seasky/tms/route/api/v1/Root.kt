package kz.seasky.tms.route.api.v1

import io.ktor.auth.*
import io.ktor.routing.*

fun Route.v1() {
    route("/api/v1") {
        docs()

        authentication()

        authenticate("token") {
            permission()

            role()

            status()

            task()

            user()
        }
    }
}