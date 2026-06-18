package com.turskyi.malaknyzhka.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WasmTextToSpeech : TextToSpeech {
    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun speak(text: String, languageCode: String) {
        stop()
        val lang = if (languageCode == "uk") "uk-UA" else "en-US"
        jsSpeak(
            text,
            lang,
            { _isSpeaking.value = true },
            { _isSpeaking.value = false })
    }

    override fun stop() {
        jsStop()
        _isSpeaking.value = false
    }

    override fun isLanguageAvailable(): Boolean = jsIsLanguageAvailable()
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    "(text, lang, onStart, onEnd) => { " +
            "window.speechSynthesis.cancel(); " +
            "const utterance = new SpeechSynthesisUtterance(text); " +
            "utterance.lang = lang; " +
            "utterance.onstart = onStart; " +
            "utterance.onend = onEnd; " +
            "utterance.onerror = onEnd; " +
            "window.speechSynthesis.speak(utterance); " +
            "}"
)
private external fun jsSpeak(
    text: String,
    lang: String,
    onStart: () -> Unit,
    onEnd: () -> Unit
)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => { window.speechSynthesis.cancel(); }")
private external fun jsStop()

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => { return window.speechSynthesis.getVoices().some(v => v.lang.startsWith('uk')); }")
private external fun jsIsLanguageAvailable(): Boolean
