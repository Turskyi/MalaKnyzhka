package com.turskyi.malaknyzhka.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WasmTextToSpeech : TextToSpeech {
    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun speak(text: String) {
        stop()
        jsSpeak(
            text,
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
    "(text, onStart, onEnd) => { " +
            "window.speechSynthesis.cancel(); " +
            "const utterance = new SpeechSynthesisUtterance(text); " +
            "utterance.lang = 'uk-UA'; " +
            "utterance.onstart = onStart; " +
            "utterance.onend = onEnd; " +
            "utterance.onerror = onEnd; " +
            "window.speechSynthesis.speak(utterance); " +
            "}"
)
private external fun jsSpeak(
    text: String,
    onStart: () -> Unit,
    onEnd: () -> Unit
)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => { window.speechSynthesis.cancel(); }")
private external fun jsStop()

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => { return window.speechSynthesis.getVoices().some(v => v.lang.startsWith('uk')); }")
private external fun jsIsLanguageAvailable(): Boolean
