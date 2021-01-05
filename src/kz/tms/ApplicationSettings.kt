package kz.tms

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import java.util.*
import kotlin.collections.HashMap

class ApplicationSettings {

    private val config = ConfigFactory.load("application.conf")

    val databaseConfig by lazy {
        val databaseConfig = config.getConfig("database")
        HikariConfig(databaseConfig.toProperties())
    }

    val jwtConfig by lazy {
        val jwtConfig = config.getConfig("jwt")
        jwtConfig.toHashMap()
    }

    private fun Config.toProperties(): Properties {
        return Properties().apply {
            for (e in entrySet()) {
                setProperty(e.key, getString(e.key))
            }
        }
    }

    private fun Config.toHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            for (e in entrySet()) {
                this[e.key] = getString(e.key)
            }
        }
    }
}