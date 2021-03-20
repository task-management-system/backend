package kz.seasky.tms.di.modules

import com.zaxxer.hikari.HikariDataSource
import kz.seasky.tms.utils.ApplicationSettings
import kz.seasky.tms.utils.JWTConfig
import org.koin.dsl.module
import javax.sql.DataSource

val applicationModule = module(createdAtStart = true) {
    single { ApplicationSettings() }

    single<DataSource> { HikariDataSource(get<ApplicationSettings>().databaseConfig) }

    single { JWTConfig(jwtProperties = get<ApplicationSettings>().jwtProperties) }
}
