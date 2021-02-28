package kz.tms.di.modules

import kz.tms.authentication.AuthenticationService
import kz.tms.database.DatabaseConnector
import kz.tms.database.TransactionService
import kz.tms.database.TransactionServiceImpl
import kz.tms.database.data.detail.DetailRepository
import kz.tms.database.data.detail.DetailService
import kz.tms.database.data.role.RoleRepository
import kz.tms.database.data.role.RoleService
import kz.tms.database.data.status.StatusRepository
import kz.tms.database.data.status.StatusService
import kz.tms.database.data.task.TaskRepository
import kz.tms.database.data.task.TaskService
import kz.tms.database.data.user.UserRepository
import kz.tms.database.data.user.UserService
import org.koin.dsl.module

val databaseModule = module(createdAtStart = true) {
    single { DatabaseConnector(dataSource = get()) }

    single<TransactionService> { TransactionServiceImpl(databaseConnector = get()) }

    single { DetailRepository() }

    single { DetailService(get(), get()) }

    single { RoleRepository() }

    single { RoleService(get(), get()) }

    single { StatusRepository() }

    single { StatusService(get(), get()) }

    single { TaskRepository() }

    single { TaskService(get(), get()) }

    single { UserRepository() }

    single { UserService(get(), get()) }

    single { AuthenticationService(get(), get(), get()) }
}