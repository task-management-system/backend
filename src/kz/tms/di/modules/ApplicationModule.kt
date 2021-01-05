package kz.tms.di.modules

import com.zaxxer.hikari.HikariDataSource
import kz.tms.ApplicationSettings
import kz.tms.utils.JWTConfig
import org.koin.dsl.module
import javax.sql.DataSource

val applicationModule = module(createdAtStart = true) {
    single { ApplicationSettings() }

    single<DataSource> { HikariDataSource(get<ApplicationSettings>().databaseConfig) }

    single { JWTConfig(get<ApplicationSettings>().jwtConfig) }
}
