package kz.tms

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import kz.tms.database.data.authentication.AuthenticationPayload
import kz.tms.database.data.authentication.jwtRealm
import kz.tms.database.data.authentication.makeJwtVerifier
import kz.tms.database.data.user.UserService
import kz.tms.di.modules.applicationModule
import kz.tms.di.modules.databaseModule
import kz.tms.route.api.v1.v1
import kz.tms.utils.statusHandler
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>) {
    EngineMain.main(args)
}


@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    setKoin()

    setRestAPI()
}

private fun Application.setKoin() {
    install(Koin) {
        modules(
            applicationModule,
            databaseModule
        )
    }
}

private fun Application.setRestAPI() {
    //TODO Read more about this feature
    install(DefaultHeaders)
    //TODO Read more about this feature
    install(CallLogging)

    install(Locations)

    install(ContentNegotiation) {
        gson { serializeNulls() }
    }

    install(Authentication) {
        val service: UserService by inject()

        jwt("token") {
            realm = jwtRealm
            verifier(makeJwtVerifier())

            validate { credential ->

                val username = credential.payload.getClaim("username").asString()
                val password = credential.payload.getClaim("password").asString()

                val u2 = service.getByUsernameOrNull(username)

                if (u2 != null) {
                    if (u2.username == username && u2.password == password) {
                        JWTPrincipal(credential.payload)
                        AuthenticationPayload(username, password)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        }
    }

    install(StatusPages) {
        statusHandler()
    }

    install(Routing) {
        trace { application.log.trace(it.buildText()) }
        v1()
    }
}

