package kz.seasky.tms.di.modules

import kz.seasky.tms.database.data.detail.DetailRepository
import kz.seasky.tms.database.data.detail.DetailService
import kz.seasky.tms.database.data.role.RoleRepository
import kz.seasky.tms.database.data.role.RoleService
import kz.seasky.tms.database.data.status.StatusRepository
import kz.seasky.tms.database.data.status.StatusService
import kz.seasky.tms.database.data.task.TaskRepository
import kz.seasky.tms.database.data.task.TaskService
import kz.seasky.tms.database.data.user.UserRepository
import kz.seasky.tms.database.data.user.UserService
import org.koin.dsl.module

val repositoryModule = module(createdAtStart = true) {
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
}