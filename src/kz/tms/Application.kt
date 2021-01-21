package kz.tms

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.tms.database.data.permission.PermissionService
import kz.tms.di.modules.applicationModule
import kz.tms.di.modules.databaseModule
import kz.tms.features.PermissionFeature
import kz.tms.features.installAuthentication
import kz.tms.features.installRouting
import kz.tms.features.installStatusPages
import kz.tms.utils.Permission
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
    //TODO Переименовать
    validatePermissions()

    //TODO Детальнее изучить обе фичи
    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        gson { serializeNulls() }
    }

    //TODO Научиться писать собственные фичи, а не вот это вот все
    installAuthentication()

    installStatusPages()

    installRouting()
}

private fun Application.validatePermissions() {
    val permissionService: PermissionService by inject()
    val permission: Permission.Companion by inject()

    val scope = GlobalScope.launch {
        val remotePermissions = permissionService.getAllAsPair()
        val localPermissions = permission.pair

        val remoteSize = remotePermissions.size
        val localSize = localPermissions.size

        if (remoteSize != localSize) throw RuntimeException(
            """
                Неверная длина разрешений 
                Удаленно - $remoteSize
                Локально - $localSize
            """.trimIndent()
        )

        val remoteDiff = remotePermissions - localPermissions
        val localDiff = localPermissions - remotePermissions

        if (remoteDiff.isNotEmpty() || localDiff.isNotEmpty()) throw RuntimeException(
            """
                Различия в разрешениях
                Удаленный список - $remotePermissions
                Локальный список - $localPermissions
                Разница удаленного списка - $remoteDiff
                Разница локально списка   - $localDiff
            """.trimIndent()
        )
    }

    scope.start()
}



