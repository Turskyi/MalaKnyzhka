package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.ai.models.ChatMessage
import com.turskyi.malaknyzhka.ai.models.ChatResponse
import org.slf4j.LoggerFactory

class AiService(
    private val providers: List<AiProvider>
) {
    private val logger = LoggerFactory.getLogger(AiService::class.java)

    suspend fun chat(
        message: String,
        history: List<ChatMessage>?,
        pageNumber: Int?,
        pageText: String?
    ): ChatResponse {
        val prompt = buildPrompt(pageNumber, pageText)
        val errors = mutableListOf<String>()

        for (provider in providers) {
            try {
                logger.info("Trying AI provider: ${provider.name}")
                val answer = provider.generateResponse(
                    prompt,
                    message,
                    history,
                    pageNumber,
                    pageText
                )
                return ChatResponse(answer, provider.name)
            } catch (e: Exception) {
                val errorMessage = "${provider.name}: ${e.message}"
                logger.warn("AI provider failed: $errorMessage")
                errors.add(errorMessage)
            }
        }

        logger.error("All AI providers failed: ${errors.joinToString("; ")}")
        throw Exception("All providers failed: ${errors.joinToString("; ")}")
    }

    private fun buildPrompt(pageNumber: Int?, pageText: String?): String {
        val basePrompt = """
            You are Taras Shevchenko, the famous Ukrainian poet, writer, and artist.
            Respond in the first person.
            Speak as Taras Shevchenko would, using respectful and educational tone.
            Discuss your poetry, life, language, history, and cultural context.
            Never claim to be the real historical person if explicitly asked about your nature as an AI, but stay in character for the conversation.
            If asked modern questions, answer as an interpretation of what you might say based on your writings and worldview.
            Your answers should be mostly in Ukrainian, as that is your soul's language, but you can respond in the language of the user if they speak to you in another language, while maintaining your Ukrainian spirit.
        """.trimIndent()

        val contextPrompt =
            if (pageNumber != null && !pageText.isNullOrBlank()) {
                "\n\nThe user is currently reading page $pageNumber of your 'Mala Knyzhka'. The manuscript text on this page is:\n$pageText"
            } else {
                ""
            }

        return basePrompt + contextPrompt
    }
}
