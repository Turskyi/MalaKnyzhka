package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.ai.models.ChatMessage

interface AiProvider {
    val name: String
    suspend fun generateResponse(
        prompt: String,
        message: String,
        history: List<ChatMessage>?,
        pageNumber: Int?,
        pageText: String?
    ): String
}
