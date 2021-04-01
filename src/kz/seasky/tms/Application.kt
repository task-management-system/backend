package kz.seasky.tms

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.netty.*
import kz.seasky.tms.di.modules.applicationModule
import kz.seasky.tms.di.modules.authenticationModule
import kz.seasky.tms.di.modules.databaseModule
import kz.seasky.tms.di.modules.repositoryModule
import kz.seasky.tms.enums.BuildVariant
import kz.seasky.tms.features.PermissionFeature
import kz.seasky.tms.features.installAuthentication
import kz.seasky.tms.features.installRouting
import kz.seasky.tms.features.installStatusPages
import kz.seasky.tms.utils.BuildConfig
import org.koin.ktor.ext.Koin

fun main(args: Array<String>) {
//    EngineMain.main(args + arrayOf("-P:args=${args.toList()}"))
    EngineMain.main(args)
}

@Suppress("unused")
@JvmOverloads
fun Application.module(testing: Boolean = false) {
//    setBuildConfig()
    setDI()
    setRestAPI()
}

private fun Application.setBuildConfig() {
    val configuration = environment.config.property("args").getString()
    val buildVariant = when {
        configuration.contains("-config=./resources/application-dev.conf") -> BuildVariant.Develop
        configuration.contains("-config=./resources/application.conf") -> BuildVariant.Product
        else -> throw IllegalArgumentException("Не удалось определить build variant")
    }
    BuildConfig.buildVariant = buildVariant
}

private fun Application.setDI() {
    install(Koin) {
        modules(
            applicationModule,
            databaseModule,
            authenticationModule,
            repositoryModule
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

    installAuthentication()

    install(PermissionFeature) {
        register { principal -> principal.power }
    }

    installStatusPages()

    installRouting()
}