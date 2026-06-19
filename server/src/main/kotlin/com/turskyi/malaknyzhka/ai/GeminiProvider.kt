package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class GeminiProvider(
    private val apiKey: String,
    private val client: HttpClient
) : AiProvider {
    override val name: String = "gemini"

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

    @Serializable
    private data class GeminiResponse(
        val candidates: List<Candidate>? = null
    )

    @Serializable
    private data class Candidate(
        val content: Content
    )

    override suspend fun generateResponse(
        prompt: String,
        message: String,
        pageNumber: Int?,
        pageText: String?
    ): String {
        val url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
        val response: GeminiResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    systemInstruction = Content(parts = listOf(Part(text = prompt))),
                    contents = listOf(
                        Content(
                            role = "user",
                            parts = listOf(Part(text = message))
                        )
                    )
                )
            )
        }.body()

        return response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Empty response from Gemini")
    }
}
