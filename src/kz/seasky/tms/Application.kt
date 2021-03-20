package kz.seasky.tms

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.netty.*
import kz.seasky.tms.di.modules.applicationModule
import kz.seasky.tms.di.modules.databaseModule
import kz.seasky.tms.features.PermissionFeature
import kz.seasky.tms.features.installAuthentication
import kz.seasky.tms.features.installRouting
import kz.seasky.tms.features.installStatusPages
import org.koin.ktor.ext.Koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    setDI()
    setRestAPI()
}

private fun Application.setDI() {
    install(Koin) {
        modules(
            applicationModule,
            databaseModule
        )
    }
}

private fun Application.setRestAPI() {
    //TODO Детальнее изучить обе фичи
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        gson { serializeNulls() }
    }

    //TODO Научиться писать собственные фичи, а не вот это вот все
    installAuthentication()

    install(PermissionFeature) {
        register { principal -> principal.power }
    }

    installStatusPages()

    installRouting()
}