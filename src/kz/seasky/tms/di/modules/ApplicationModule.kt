package kz.seasky.tms.di.modules

import kz.seasky.tms.utils.ApplicationSettings
import org.koin.dsl.module

val applicationModule = module(createdAtStart = true) {
    single { ApplicationSettings() }
}
