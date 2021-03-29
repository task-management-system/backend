package kz.seasky.tms.di.modules

import com.zaxxer.hikari.HikariDataSource
import kz.seasky.tms.database.DatabaseConnector
import kz.seasky.tms.database.TransactionService
import kz.seasky.tms.database.TransactionServiceImpl
import kz.seasky.tms.utils.ApplicationSettings
import org.koin.dsl.module
import org.koin.experimental.builder.singleBy
import javax.sql.DataSource

val databaseModule = module(createdAtStart = true) {
    single<DataSource> { HikariDataSource(get<ApplicationSettings>().hikariConfig) }

    single<DatabaseConnector> { DatabaseConnector(dataSource = get()) }

    singleBy<TransactionService, TransactionServiceImpl>()

//    single<TransactionService> { TransactionServiceImpl(databaseConnector = get()) }
}