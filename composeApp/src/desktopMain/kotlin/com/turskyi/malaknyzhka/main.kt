package com.turskyi.malaknyzhka

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.turskyi.malaknyzhka.infrastructure.DesktopTextToSpeech
import com.turskyi.malaknyzhka.share.DesktopShareManager
import com.turskyi.malaknyzhka.ui.App

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Мала Книжка ✦ Тарас Шевченко",
        ) {
            App(
                settings = remember { createSettings() },
                textToSpeech = remember { DesktopTextToSpeech() },
                shareManager = remember { DesktopShareManager() }
            )
        }
    }
}
