package kz.tms.route.api.v1

import io.ktor.http.content.*
import io.ktor.routing.*
import java.io.File

fun Route.assets() {
    static("/assets") {
        staticRootFolder = File("assets")
//        example
//        file("/claims", "claims.json")
    }
}