package kz.seasky.tms.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

class ClientConfig(properties: Properties) {
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            host = properties.getProperty("host")
            port = properties.getProperty("port").toInt()
        }

        engine {
            maxConnectionsCount = 100
            endpoint {
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 15000
                connectTimeout = 15000
                connectAttempts = 5
            }
        }
    }
}

