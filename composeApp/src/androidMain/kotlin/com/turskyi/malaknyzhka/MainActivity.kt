package com.turskyi.malaknyzhka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.turskyi.malaknyzhka.infrastructure.AndroidTextToSpeech
import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.SettingsUserSettingsRepository
import com.turskyi.malaknyzhka.share.AndroidShareManager
import com.turskyi.malaknyzhka.ui.App

class MainActivity : ComponentActivity() {
    private lateinit var tts: AndroidTextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContext.context = applicationContext
        tts = AndroidTextToSpeech(applicationContext)

        // Ensure the launcher icon matches the current setting on startup
        // We do this BEFORE setContent to avoid recomposition side-effects
        val settings = createSettings(applicationContext)
        val userSettingsRepository = SettingsUserSettingsRepository(settings)
        val androidPlatform = AndroidPlatform(intent?.data?.let { uri ->
            if (uri.scheme == "malaknyzhka" && uri.host == "page") {
                val userPage = uri.lastPathSegment?.toIntOrNull() ?: 1
                "book/$userPage"
            } else null
        })
        val currentExperience = userSettingsRepository.getExperience(Experience.BOOK)
        androidPlatform.syncLauncherIcon(currentExperience, immediate = true)

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
                settings = settings,
                textToSpeech = tts,
                shareManager = remember { AndroidShareManager(applicationContext) },
                platform = remember(initialRoute) { AndroidPlatform(initialRoute) }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        // Apply any pending launcher icon changes when the app goes to background
        AndroidPlatform.pendingExperience?.let { experience ->
            AndroidPlatform().syncLauncherIcon(experience, immediate = true)
        }
    }

    override fun onDestroy() {
        tts.release()
        super.onDestroy()
    }
}
