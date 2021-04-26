package kz.seasky.tms.route.api.v1

import io.ktor.auth.*
import io.ktor.routing.*
import kz.seasky.tms.utils.JWT_NAME_STANDARD

fun Route.v1() {
    route("/api/v1") {
        docs()

        authentication()

        statistic()

        authenticate(JWT_NAME_STANDARD) {
            permission()

            role()

            status()

            task()

            user()
        }
    }
}
