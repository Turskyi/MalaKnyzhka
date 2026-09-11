package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class GroqProvider(
    private val apiKey: String,
    private val model: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "groq"
    override val modelName: String = model

    @Serializable
    private data class GroqRequest(
        val model: String,
        val messages: List<GroqMessage>
    )

    @Serializable
    private data class GroqMessage(
        val role: String,
        val content: String
    )

    override suspend fun generateResponse(
        prompt: String,
        message: String,
        history: List<com.turskyi.malaknyzhka.ai.models.ChatMessage>?,
        pageNumber: Int?,
        pageText: String?
    ): String {
        val messages = mutableListOf<GroqMessage>()
        messages.add(GroqMessage(role = "system", content = prompt))

        history?.forEach { msg ->
            messages.add(GroqMessage(role = msg.role, content = msg.content))
        }

        messages.add(GroqMessage(role = "user", content = message))

        val responseBody = client.post("https://api.groq.com/openai/v1/chat/completions") {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(
                GroqRequest(
                    model = model,
                    messages = messages
                )
            )
        }.bodyAsText()

        return AiResponseParser.extractOpenAiStyleText(responseBody, name)
    }
}
