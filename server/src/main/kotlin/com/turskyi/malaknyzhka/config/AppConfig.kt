package com.turskyi.malaknyzhka.config

import io.github.cdimascio.dotenv.Dotenv
import org.slf4j.LoggerFactory

class AppConfig {
    private val logger = LoggerFactory.getLogger(AppConfig::class.java)
    private val dotenv: Dotenv? = try {
        Dotenv.configure().ignoreIfMissing().load()
    } catch (e: Exception) {
        logger.warn("Could not load .env file, falling back to system environment variables. Error: ${e.message}")
        null
    }

    val groqApiKey: String = getEnv("GROQ_API_KEY")
    val mistralApiKey: String = getEnv("MISTRAL_API_KEY")
    val geminiApiKey: String = getEnv("GEMINI_API_KEY")

    private fun getEnv(key: String): String {
        val value = dotenv?.get(key) ?: System.getenv(key)
        if (value.isNullOrBlank()) {
            logger.warn("Environment variable $key is missing! AI features using this provider will fail.")
            return ""
        }
        return value
    }
}
