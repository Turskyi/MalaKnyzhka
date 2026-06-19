package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class MistralProvider(
    private val apiKey: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "mistral"

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

    @Serializable
    private data class MistralResponse(
        val choices: List<Choice>
    )

    @Serializable
    private data class Choice(
        val message: MistralMessage
    )

    override suspend fun generateResponse(
        prompt: String,
        message: String,
        pageNumber: Int?,
        pageText: String?
    ): String {
        val response: MistralResponse =
            client.post("https://api.mistral.ai/v1/chat/completions") {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    MistralRequest(
                        model = "mistral-large-latest",
                        messages = listOf(
                            MistralMessage(role = "system", content = prompt),
                            MistralMessage(role = "user", content = message)
                        )
                    )
                )
            }.body()

        return response.choices.firstOrNull()?.message?.content
            ?: throw Exception("Empty response from Mistral")
    }
}
