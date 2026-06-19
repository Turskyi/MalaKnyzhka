package com.turskyi.malaknyzhka.ai.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatHistoryMessage(
    val role: String,
    val content: String,
)

@Serializable
data class ChatRequest(
    val message: String,
    val history: List<ChatHistoryMessage>? = null,
    val pageNumber: Int? = null,
    val pageText: String? = null,
)

@Serializable
data class ChatResponse(
    val answer: String,
    val providerUsed: String,
)

enum class MessageRole {
    USER,
    SHEVCHENKO,
}

data class ChatMessage(
    val role: MessageRole,
    val text: String,
    val timestamp: Long = 0L,
)
