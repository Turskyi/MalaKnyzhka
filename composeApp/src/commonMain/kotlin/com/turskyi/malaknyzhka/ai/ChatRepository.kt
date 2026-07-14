package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.ai.models.ChatHistoryMessage
import com.turskyi.malaknyzhka.ai.models.ChatMessage
import com.turskyi.malaknyzhka.ai.models.ChatRequest
import com.turskyi.malaknyzhka.ai.models.MessageRole
import com.turskyi.malaknyzhka.getCurrentTimeMillis
import com.turskyi.malaknyzhka.usecases.toUserPageNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepository(private val api: ChatApi) {
    private val _messages: MutableStateFlow<List<ChatMessage>> =
        MutableStateFlow(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun setInitialMessage(text: String) {
        if (_messages.value.isEmpty()) {
            _messages.value = listOf(
                ChatMessage(
                    role = MessageRole.SHEVCHENKO,
                    text = text,
                    timestamp = getCurrentTimeMillis(),
                )
            )
        }
    }

    suspend fun sendMessage(
        text: String,
        pageNumber: Int?,
        pageText: String?,
    ) {
        val history: List<ChatHistoryMessage> = _messages.value.map { msg ->
            ChatHistoryMessage(
                role = if (msg.role == MessageRole.USER) "user" else "assistant",
                content = msg.text,
            )
        }

        val userMessage = ChatMessage(
            role = MessageRole.USER,
            text = text,
            timestamp = getCurrentTimeMillis(),
        )
        _messages.value += userMessage

        try {
            val request = ChatRequest(
                message = text,
                history = history,
                pageNumber = pageNumber?.toUserPageNumber(),
                pageText = pageText,
            )
            val response = api.sendMessage(request)
            val aiMessage = ChatMessage(
                role = MessageRole.SHEVCHENKO,
                text = response.answer,
                timestamp = getCurrentTimeMillis(),
            )
            _messages.value += aiMessage
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
            val errorMessage = ChatMessage(
                role = MessageRole.SHEVCHENKO,
                text = "Вибачте, сталася помилка. Спробуйте ще раз пізніше.",
                timestamp = getCurrentTimeMillis(),
            )
            _messages.value += errorMessage
        }
    }
}
