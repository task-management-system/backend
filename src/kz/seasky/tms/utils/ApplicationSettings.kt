package kz.seasky.tms.utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.util.*

@OptIn(KoinApiExtension::class)
class ApplicationSettings : KoinComponent {

    private val fileName = getKoin().getProperty("conf", "")
    private val config = ConfigFactory.load(fileName)

    val hikariConfig by lazy {
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