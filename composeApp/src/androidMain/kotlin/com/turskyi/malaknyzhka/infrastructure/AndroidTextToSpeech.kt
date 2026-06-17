package com.turskyi.malaknyzhka.infrastructure

import android.content.Context
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import android.speech.tts.TextToSpeech as AndroidTTS

class AndroidTextToSpeech(context: Context) : TextToSpeech,
    AndroidTTS.OnInitListener {
    private var tts: AndroidTTS? = AndroidTTS(context, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    override val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    override fun onInit(status: Int) {
        if (status == AndroidTTS.SUCCESS) {
            val result = tts?.setLanguage(Locale("uk", "UA"))
            if (result != AndroidTTS.LANG_MISSING_DATA && result != AndroidTTS.LANG_NOT_SUPPORTED) {
                isInitialized = true
                tts?.setOnUtteranceProgressListener(object :
                    UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }

                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
            }
        }
    }

    override fun speak(text: String) {
        if (isInitialized) {
            // Using QUEUE_FLUSH to stop any previous playback session.
            tts?.speak(text, AndroidTTS.QUEUE_FLUSH, null, "MalaKnyzhkaTTS")
            _isSpeaking.value = true
        }
    }

    override fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    override fun isLanguageAvailable(): Boolean {
        // More robust check could be added here if needed, 
        // but isInitialized already checks for Ukrainian support in onInit.
        return isInitialized
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
