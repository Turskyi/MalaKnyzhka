package com.turskyi.malaknyzhka

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.turskyi.malaknyzhka.ui.App

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Мала Книжка (Тарас Шевченко)",
        ) {
            App(
                settings = createSettings()
            )
        }
    }
}