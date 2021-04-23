package kz.seasky.tms.di.modules

import kz.seasky.tms.repository.authentication.AuthenticationRepository
import kz.seasky.tms.repository.authentication.AuthenticationService
import kz.seasky.tms.repository.authentication.AuthenticationServiceImpl
import kz.seasky.tms.utils.ApplicationSettings
import kz.seasky.tms.utils.JWTConfig
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy

val authenticationModule = module(createdAtStart = true) {
    single { JWTConfig(properties = get<ApplicationSettings>().jwtProperties) }

    singleBy<AuthenticationService, AuthenticationServiceImpl>()
    single { AuthenticationRepository(transactionService = get()) }
}