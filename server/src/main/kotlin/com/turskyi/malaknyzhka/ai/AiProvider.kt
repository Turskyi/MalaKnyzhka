package com.turskyi.malaknyzhka.ai

interface AiProvider {
    val name: String
    suspend fun generateResponse(
        prompt: String,
        message: String,
        pageNumber: Int?,
        pageText: String?
    ): String
}
