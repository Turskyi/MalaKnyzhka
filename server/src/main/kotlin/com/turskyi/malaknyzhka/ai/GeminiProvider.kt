package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class GeminiProvider(
    private val apiKey: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "gemini"
    override val modelName: String = "gemini-2.5-flash"

    @Serializable
    private data class GeminiRequest(
        val contents: List<Content>,
        val systemInstruction: Content? = null
    )

    @Serializable
    private data class Content(
        val role: String? = null,
        val parts: List<Part>
    )

    @Serializable
    private data class Part(
        val text: String
    )

    override suspend fun generateResponse(
        prompt: String,
        message: String,
        history: List<com.turskyi.malaknyzhka.ai.models.ChatMessage>?,
        pageNumber: Int?,
        pageText: String?
    ): String {
        val url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val contents = mutableListOf<Content>()
        history?.forEach { msg ->
            val role = if (msg.role == "user") "user" else "model"
            contents.add(
                Content(
                    role = role,
                    parts = listOf(Part(text = msg.content))
                )
            )
        }
        contents.add(
            Content(
                role = "user",
                parts = listOf(Part(text = message))
            )
        )

        val responseBody = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    systemInstruction = Content(parts = listOf(Part(text = prompt))),
                    contents = contents
                )
            )
        }.bodyAsText()

        return AiResponseParser.extractGeminiText(responseBody, name)
    }
}
