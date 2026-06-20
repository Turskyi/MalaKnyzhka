package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.AppConstants
import com.turskyi.malaknyzhka.ai.models.ChatRequest
import com.turskyi.malaknyzhka.ai.models.ChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ChatApi {
    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    suspend fun sendMessage(request: ChatRequest): ChatResponse {
        return client.post("${AppConstants.AI_CHAT_BASE_URL}/chat") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
