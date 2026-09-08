package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.ai.models.ChatMessage
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AiServiceTest {

    class MockProvider(
        override val name: String,
        override val modelName: String = "mock-model",
        private val shouldFail: Boolean = false,
        private val response: String = "Success"
    ) : AiProvider {
        override suspend fun generateResponse(
            prompt: String,
            message: String,
            history: List<ChatMessage>?,
            pageNumber: Int?,
            pageText: String?
        ): String {
            if (shouldFail) throw Exception("Failed")
            return response
        }
    }

    @Test
    fun testFallbackFirstSucceeds() = runBlocking {
        val providers = listOf(
            MockProvider("groq", response = "Groq response"),
            MockProvider("mistral", response = "Mistral response")
        )
        val service = AiService(providers)

        val result = service.chat("hello", null, null, null)

        assertEquals("groq", result.providerUsed)
        assertEquals("Groq response", result.answer)
    }

    @Test
    fun testFallbackFirstFails() = runBlocking {
        val providers = listOf(
            MockProvider("groq", shouldFail = true),
            MockProvider("mistral", response = "Mistral response")
        )
        val service = AiService(providers)

        val result = service.chat("hello", null, null, null)

        assertEquals("mistral", result.providerUsed)
        assertEquals("Mistral response", result.answer)
    }

    @Test
    fun testFallbackAllFail() {
        runBlocking {
            val providers = listOf(
                MockProvider("groq", shouldFail = true),
                MockProvider("mistral", shouldFail = true),
                MockProvider("gemini", shouldFail = true)
            )
            val service = AiService(providers)

            assertFailsWith<Exception> {
                service.chat("hello", null, null, null)
            }
        }
    }

    @Test
    fun testRemoveReasoning() = runBlocking {
        val providers = listOf(
            MockProvider("groq", response = "<think>reasoning</think>Actual answer"),
            MockProvider("mistral", response = "<thought>reasoning</thought>Mistral answer")
        )
        val service = AiService(providers)

        val result = service.chat("hello", null, null, null)
        assertEquals("Actual answer", result.answer)
    }

    @Test
    fun testRemoveTruncatedReasoning() = runBlocking {
        val providers = listOf(
            MockProvider("groq", response = "Hello! <think>reasoning... (truncated)"),
        )
        val service = AiService(providers)

        val result = service.chat("hello", null, null, null)
        assertEquals("Hello!", result.answer)
    }

    @Test
    fun testBlankAnswerAfterCleaningFails() = runBlocking {
        val providers = listOf(
            MockProvider("groq", response = "<think>only reasoning</think>"),
            MockProvider("mistral", response = "Mistral answer")
        )
        val service = AiService(providers)

        val result = service.chat("hello", null, null, null)
        assertEquals("mistral", result.providerUsed)
        assertEquals("Mistral answer", result.answer)
    }
}
