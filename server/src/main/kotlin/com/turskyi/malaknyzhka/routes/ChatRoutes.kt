package com.turskyi.malaknyzhka.routes

import com.turskyi.malaknyzhka.ai.AiService
import com.turskyi.malaknyzhka.ai.models.ChatRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ChatRoutes")

/**
 * Defines the chat routes for the AI backend.
 *
 * **Endpoint:** `POST /chat`
 *
 * **Request Body:**
 * ```json
 * {
 *   "message": "Привіт, Тарасе! Яку пораду ти даси молоді?",
 *   "pageNumber": 10,
 *   "pageText": "Учітесь, читайте, і чужому научайтесь, й свого не цурайтесь."
 * }
 * ```
 *
 * **Response Body:**
 * ```json
 * {
 *   "answer": "Добрий день, молодій людино! Як ти бачиш на сторінці 10 моєї \"Малої Книжки\"...",
 *   "providerUsed": "groq"
 * }
 * ```
 */
fun Route.chatRoutes(aiService: AiService) {
    route("/chat") {
        post {
            try {
                val request = call.receive<ChatRequest>()
                logger.info("Received chat request: message='${request.message}', pageNumber=${request.pageNumber}")
                if (request.message.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Message cannot be empty"
                    )
                    return@post
                }
                val response = aiService.chat(
                    message = request.message,
                    pageNumber = request.pageNumber,
                    pageText = request.pageText
                )
                call.respond(response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to (e.message ?: "Unknown error"))
                )
            }
        }
    }
}
