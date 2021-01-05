package kz.tms.di.modules

import kz.tms.database.DatabaseConnector
import kz.tms.database.TransactionService
import kz.tms.database.TransactionServiceImpl
import kz.tms.database.data.user.UserRepository
import kz.tms.database.data.user.UserService
import org.koin.dsl.module

val databaseModule = module(createdAtStart = true) {
    single {
        DatabaseConnector(dataSource = get())
    }

    single<TransactionService> {
        TransactionServiceImpl(databaseConnector = get())
    }

    single { UserRepository() }

    single { UserService(get(), get()) }
}