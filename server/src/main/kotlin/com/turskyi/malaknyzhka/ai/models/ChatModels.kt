package com.turskyi.malaknyzhka.ai.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val message: String,
    val pageNumber: Int? = null,
    val pageText: String? = null
)

@Serializable
data class ChatResponse(
    val answer: String,
    val providerUsed: String
)
