package kz.seasky.tms.di.modules

import kz.seasky.tms.authentication.AuthenticationService
import kz.seasky.tms.utils.ApplicationSettings
import kz.seasky.tms.utils.JWTConfig
import org.koin.dsl.module

val authenticationModule = module {
    single { JWTConfig(jwtProperties = get<ApplicationSettings>().jwtProperties) }

    single { AuthenticationService(get(), get(), get()) }
}