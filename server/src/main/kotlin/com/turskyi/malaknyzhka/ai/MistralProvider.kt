package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class MistralProvider(
    private val apiKey: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "mistral"
    override val modelName: String = "mistral-large-latest"

    @Serializable
    private data class MistralRequest(
        val model: String,
        val messages: List<MistralMessage>
    )

    @Serializable
    private data class MistralMessage(
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
        val messages = mutableListOf<MistralMessage>()
        messages.add(MistralMessage(role = "system", content = prompt))

        history?.forEach { msg ->
            messages.add(MistralMessage(role = msg.role, content = msg.content))
        }

        messages.add(MistralMessage(role = "user", content = message))

        val response = client.post("https://api.mistral.ai/v1/chat/completions") {
            header("Authorization", "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(
                MistralRequest(
                    model = "mistral-large-latest",
                    messages = messages
                )
            )
        }

        val bodyText = response.bodyAsText()
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Mistral API error: ${response.status} - $bodyText")
        }

        return AiResponseParser.extractOpenAiStyleText(bodyText, name)
    }
}
