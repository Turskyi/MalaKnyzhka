package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.ai.AiService
import com.turskyi.malaknyzhka.ai.GeminiProvider
import com.turskyi.malaknyzhka.ai.GroqProvider
import com.turskyi.malaknyzhka.ai.MistralProvider
import com.turskyi.malaknyzhka.config.AppConfig
import com.turskyi.malaknyzhka.routes.chatRoutes
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(
        Netty,
        port = port,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    val config = AppConfig()

    val httpClient = HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    val providers = listOf(
        GroqProvider(config.groqApiKey, httpClient),
        MistralProvider(config.mistralApiKey, httpClient),
        GeminiProvider(config.geminiApiKey, httpClient)
    )

    val aiService = AiService(providers)

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    routing {
        get("/") {
            call.respondText("Mala Knyzhka AI Server is running!")
        }
        chatRoutes(aiService)
    }
}
