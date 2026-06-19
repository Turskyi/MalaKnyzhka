package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class GroqProvider(
    private val apiKey: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "groq"

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

    @Serializable
    private data class GroqResponse(
        val choices: List<Choice>
    )

    @Serializable
    private data class Choice(
        val message: GroqMessage
    )

    override suspend fun generateResponse(
        prompt: String,
        message: String,
        pageNumber: Int?,
        pageText: String?
    ): String {
        val response: GroqResponse =
            client.post("https://api.groq.com/openai/v1/chat/completions") {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    GroqRequest(
                        model = "llama-3.3-70b-versatile",
                        messages = listOf(
                            GroqMessage(role = "system", content = prompt),
                            GroqMessage(role = "user", content = message)
                        )
                    )
                )
            }.body()

        return response.choices.firstOrNull()?.message?.content
            ?: throw Exception("Empty response from Groq")
    }
}
