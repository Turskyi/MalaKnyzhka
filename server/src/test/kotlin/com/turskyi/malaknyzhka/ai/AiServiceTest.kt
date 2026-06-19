package com.turskyi.malaknyzhka.ai

import com.turskyi.malaknyzhka.ai.models.ChatMessage
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AiServiceTest {

    class MockProvider(
        override val name: String,
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
}
