package kz.seasky.tms

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kz.seasky.tms.di.modules.applicationModule
import kz.seasky.tms.di.modules.authenticationModule
import kz.seasky.tms.di.modules.databaseModule
import kz.seasky.tms.di.modules.repositoryModule
import kz.seasky.tms.enums.BuildVariant
import kz.seasky.tms.features.PermissionFeature
import kz.seasky.tms.features.installAuthentication
import kz.seasky.tms.features.installRouting
import kz.seasky.tms.features.installStatusPages
import kz.seasky.tms.model.statistics.Statistics
import kz.seasky.tms.utils.BuildConfig
import kz.seasky.tms.utils.COOKIE_STATISTICS_NAME
import kz.seasky.tms.utils.SESSION_ROOT_DIR
import org.koin.ktor.ext.Koin
import java.io.File

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
    //TODO Детальнее изучить 3 фичи
    install(DefaultHeaders)
    install(CallLogging)
    install(CORS)

    install(Sessions) {
        cookie<Statistics>(
            name = COOKIE_STATISTICS_NAME,
            storage = directorySessionStorage(File(SESSION_ROOT_DIR), true),
            block = {
                cookie.maxAgeInSeconds = 5 * 60 * 1000 // 5 min
            }
        )
    }

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