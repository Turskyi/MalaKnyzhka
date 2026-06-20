package com.turskyi.malaknyzhka.infrastructure

import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.AVFAudio.AVSpeechBoundary
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechSynthesizerDelegateProtocol
import platform.AVFAudio.AVSpeechUtterance
import platform.darwin.NSObject

class IosTextToSpeech : TextToSpeech {
    private val synthesizer = AVSpeechSynthesizer()
    private val delegate = TTSDelegate { _isSpeaking.value = it }

    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        synthesizer.delegate = delegate
    }

    override fun speak(text: String, languageCode: String) {
        stop()
        val utterance = AVSpeechUtterance(string = text)
        val languageTag = when (languageCode) {
            "uk" -> "uk-UA"
            "en" -> "en-US"
            else -> languageCode
        }
        utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage(languageTag)
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
        @ObjCSignatureOverride
        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didStartSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(true)
        }

        @ObjCSignatureOverride
        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didFinishSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(false)
        }

        @ObjCSignatureOverride
        override fun speechSynthesizer(
            synthesizer: AVSpeechSynthesizer,
            didCancelSpeechUtterance: AVSpeechUtterance
        ) {
            onSpeakingChange(false)
        }
    }
}
