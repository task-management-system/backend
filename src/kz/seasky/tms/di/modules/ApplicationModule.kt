package kz.seasky.tms.di.modules

import kz.seasky.tms.utils.ApplicationSettings
import kz.seasky.tms.utils.ClientConfig
import org.koin.dsl.module

val applicationModule = module(createdAtStart = true) {
    single { ApplicationSettings() }

    single { ClientConfig(get<ApplicationSettings>().notificationProperties).client }
}
