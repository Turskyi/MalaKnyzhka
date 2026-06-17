package com.turskyi.malaknyzhka.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class DesktopTextToSpeech : TextToSpeech {
    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()
    private var process: Process? = null

    override fun speak(text: String) {
        stop()
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        if (os.contains("mac")) {
            _isSpeaking.value = true
            val thread = Thread {
                try {
                    // Lesya is the name of the Ukrainian voice on macOS.
                    // We also try to use the language code.
                    process = ProcessBuilder("say", "-v", "Lesya", text).start()
                    process?.waitFor()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isSpeaking.value = false
                }
            }
            thread.isDaemon = true
            thread.start()
        }
    }

    override fun stop() {
        process?.destroy()
        _isSpeaking.value = false
    }

    override fun isLanguageAvailable(): Boolean {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        return if (os.contains("mac")) {
            try {
                val p = ProcessBuilder("say", "-v", "?").start()
                val output = p.inputStream.bufferedReader().readText()
                output.contains("uk_UA") || output.contains("Ukrainian")
            } catch (e: Exception) {
                println("Error checking language availability: ${e.message}")
                false
            }
        } else {
            false
        }
    }
}
