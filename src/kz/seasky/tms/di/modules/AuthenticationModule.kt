package kz.seasky.tms.di.modules

import kz.seasky.tms.authentication.AuthenticationRepository
import kz.seasky.tms.authentication.AuthenticationService
import kz.seasky.tms.authentication.AuthenticationServiceImpl
import kz.seasky.tms.utils.ApplicationSettings
import kz.seasky.tms.utils.JWTConfig
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val authenticationModule = module(createdAtStart = true) {
    single { JWTConfig(jwtProperties = get<ApplicationSettings>().jwtProperties) }

    singleBy<AuthenticationService, AuthenticationServiceImpl>()
    single<AuthenticationRepository> { AuthenticationRepository(transactionService = get()) }

//    single { AuthenticationService(get(), get()) }
}