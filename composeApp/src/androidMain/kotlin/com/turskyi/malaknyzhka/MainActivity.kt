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
        AppContext.context = applicationContext
        tts = AndroidTextToSpeech(applicationContext)

        setContent {
            val initialRoute = intent?.data?.let { uri ->
                if (uri.scheme == "malaknyzhka" && uri.host == "page") {
                    // Convert "malaknyzhka://page/123" to "book/122" (internal 0-based)
                    val userPage = uri.lastPathSegment?.toIntOrNull() ?: 1
                    // We'll handle the routing in App.kt
                    "book/$userPage"
                } else null
            }

            App(
                settings = remember {
                    createSettings(applicationContext)
                },
                textToSpeech = tts,
                shareManager = remember { AndroidShareManager(applicationContext) },
                platform = AndroidPlatform(initialRoute)
            )
        }
    }

    override fun onDestroy() {
        tts.release()
        super.onDestroy()
    }
}
