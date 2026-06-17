package com.turskyi.malaknyzhka.infrastructure

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFoundation.AVSpeechBoundary
import platform.AVFoundation.AVSpeechSynthesisVoice
import platform.AVFoundation.AVSpeechSynthesizer
import platform.AVFoundation.AVSpeechSynthesizerDelegateProtocol
import platform.AVFoundation.AVSpeechUtterance
import platform.darwin.NSObject

class IosTextToSpeech : TextToSpeech {
    private val synthesizer = AVSpeechSynthesizer()
    private val delegate = TTSDelegate { _isSpeaking.value = it }

    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        synthesizer.delegate = delegate
    }

    override fun speak(text: String) {
        stop()
        val utterance = AVSpeechUtterance(string = text)
        // Ukrainian voice
        utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage("uk-UA")
        if (utterance.voice != null) {
            synthesizer.speakUtterance(utterance)
            _isSpeaking.value = true
        }
    }

    override fun stop() {
        if (synthesizer.isSpeaking()) {
            synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
            _isSpeaking.value = false
        }
    }

    override fun isLanguageAvailable(): Boolean {
        return AVSpeechSynthesisVoice.voiceWithLanguage("uk-UA") != null
    }

    private class TTSDelegate(val onSpeakingChange: (Boolean) -> Unit) :
        NSObject(), AVSpeechSynthesizerDelegateProtocol {
        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didStartSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(true)
        }

        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didFinishSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(false)
        }

        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didCancelSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(false)
        }
    }
}
