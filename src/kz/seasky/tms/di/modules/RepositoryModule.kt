package kz.seasky.tms.di.modules

import kz.seasky.tms.repository.role.RoleRepository
import kz.seasky.tms.repository.role.RoleService
import kz.seasky.tms.repository.status.StatusRepository
import kz.seasky.tms.repository.status.StatusService
import kz.seasky.tms.repository.task.TaskRepository
import kz.seasky.tms.repository.task.TaskService
import kz.seasky.tms.repository.user.UserRepository
import kz.seasky.tms.repository.user.UserService
import org.koin.dsl.module

val repositoryModule = module {
    single { RoleRepository() }

    single { RoleService(get(), get()) }

    single { StatusRepository() }

    single { StatusService(get(), get()) }

    single { TaskRepository() }

    single { TaskService(get(), get()) }

    single { UserRepository() }

    single { UserService(get(), get()) }
}