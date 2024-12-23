package com.turskyi.malaknyzhka

import App
import DATA_STORE_FILE_NAME
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import createDataStore

fun main() {
    val prefs = createDataStore {
        DATA_STORE_FILE_NAME
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Мала Книжка (Тарас Шевченко)",
        ) {
            App(
                prefs = prefs
            )
        }
    }
}