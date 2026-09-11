package com.turskyi.malaknyzhka.ai

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.LoggerFactory

internal object AiResponseParser {
    private val logger = LoggerFactory.getLogger(
        AiResponseParser::class.java,
    )
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun extractOpenAiStyleText(rawBody: String, providerName: String): String {
        val root = parseJson(rawBody, providerName)
        val rootObject = root as? JsonObject
            ?: throw Exception("Invalid $providerName response: not a JSON object")

        if (rootObject.containsKey("error")) {
            val error = rootObject["error"]?.jsonObject
            val message = error?.get("message")?.jsonPrimitive?.contentOrNull
                ?: rootObject.toString()
            throw Exception("${providerName} API error: $message")
        }

        val choices = rootObject["choices"]?.jsonArray
            ?: throw Exception("Empty response from $providerName")

        val firstChoice = choices.firstOrNull()?.jsonObject
            ?: throw Exception("Empty response from $providerName")

        val message = firstChoice["message"]?.jsonObject
        return extractText(message?.get("content"), providerName)
    }

    fun extractGeminiText(rawBody: String, providerName: String): String {
        val root = parseJson(rawBody, providerName)
        val rootObject = root as? JsonObject
            ?: throw Exception("Invalid $providerName response: not a JSON object")

        if (rootObject.containsKey("error")) {
            val error = rootObject["error"]?.jsonObject
            val message = error?.get("message")?.jsonPrimitive?.contentOrNull
                ?: rootObject.toString()
            throw Exception("${providerName} API error: $message")
        }

        val candidates = rootObject["candidates"]?.jsonArray
            ?: throw Exception("Empty response from $providerName")

        val firstCandidate = candidates.firstOrNull()?.jsonObject
            ?: throw Exception("Empty response from $providerName")

        val content = firstCandidate["content"]?.jsonObject
        val parts = content?.get("parts")?.jsonArray
            ?: throw Exception("Empty response from $providerName")

        val text = parts.joinToString(separator = "") { part ->
            part.jsonObject["text"]?.jsonPrimitive?.contentOrNull ?: ""
        }

        if (text.isBlank()) {
            throw Exception("Empty response from $providerName")
        }
        return text
    }

    private fun parseJson(rawBody: String, providerName: String): JsonElement {
        return try {
            json.parseToJsonElement(rawBody)
        } catch (e: Exception) {
            logger.warn(
                "Invalid $providerName response: ${rawBody.take(500)}",
                e
            )
            throw Exception("Invalid $providerName response: ${rawBody.take(500)}")
        }
    }

    private fun extractText(
        element: JsonElement?,
        providerName: String
    ): String {
        val value = when (element) {
            null -> null
            is JsonPrimitive -> element.contentOrNull
            is JsonArray -> element.joinToString(separator = "") { part ->
                val text = when (part) {
                    is JsonObject -> part["text"]?.jsonPrimitive?.contentOrNull
                        ?: part["content"]?.jsonPrimitive?.contentOrNull
                        ?: part.toString()

                    is JsonPrimitive -> part.contentOrNull ?: ""
                    else -> part.toString()
                }
                text
            }

            is JsonObject -> element["text"]?.jsonPrimitive?.contentOrNull
                ?: element["content"]?.jsonPrimitive?.contentOrNull
                ?: element.toString()

            JsonNull -> null
        }

        val text = value?.trim().orEmpty()
        if (text.isBlank()) {
            throw Exception("Empty response from $providerName")
        }
        return text
    }
}
