package kz.tms.utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import java.util.*

class ApplicationSettings {

    private val config = ConfigFactory.load("application.conf")

    val databaseConfig by lazy {
        val databaseConfig = config.getConfig("database")
        HikariConfig(databaseConfig.toProperties())
    }

    val jwtProperties by lazy {
        val jwtConfig = config.getConfig("jwt")
        jwtConfig.toProperties()
    }

    private fun Config.toProperties(): Properties {
        return Properties().apply {
            for (e in entrySet()) {
                setProperty(e.key, getString(e.key))
            }
        }
    }

}