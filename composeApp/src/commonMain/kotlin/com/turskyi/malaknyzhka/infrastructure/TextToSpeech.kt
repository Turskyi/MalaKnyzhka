package com.turskyi.malaknyzhka.infrastructure

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for Text-to-Speech functionality.
 */
interface TextToSpeech {
    /**
     * Observable state of whether the engine is currently speaking.
     */
    val isSpeaking: StateFlow<Boolean>

    /**
     * Starts speaking the given [text] in the specified [languageCode].
     * If already speaking, it should stop the previous session first.
     */
    fun speak(text: String, languageCode: String)

    /**
     * Stops the current speech playback.
     */
    fun stop()

    /**
     * Checks if Ukrainian language is supported by the platform's TTS engine.
     */
    fun isLanguageAvailable(): Boolean
}
