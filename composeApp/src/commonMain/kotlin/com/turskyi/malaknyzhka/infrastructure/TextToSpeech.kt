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
     * Starts speaking the given [text] in Ukrainian.
     * If already speaking, it should stop the previous session first.
     */
    fun speak(text: String)

    /**
     * Stops the current speech playback.
     */
    fun stop()

    /**
     * Checks if Ukrainian language is supported by the platform's TTS engine.
     */
    fun isLanguageAvailable(): Boolean
}
