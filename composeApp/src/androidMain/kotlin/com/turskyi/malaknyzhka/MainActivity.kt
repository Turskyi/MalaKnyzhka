package com.turskyi.malaknyzhka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.turskyi.malaknyzhka.infrastructure.AndroidTextToSpeech
import com.turskyi.malaknyzhka.share.AndroidShareManager
import com.turskyi.malaknyzhka.ui.App

class MainActivity : ComponentActivity() {
    private lateinit var tts: AndroidTextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = AndroidTextToSpeech(applicationContext)

        setContent {
            App(
                settings = remember {
                    createSettings(applicationContext)
                },
                textToSpeech = tts,
                shareManager = remember { AndroidShareManager(applicationContext) }
            )
        }
    }

    override fun onDestroy() {
        tts.release()
        super.onDestroy()
    }
}
