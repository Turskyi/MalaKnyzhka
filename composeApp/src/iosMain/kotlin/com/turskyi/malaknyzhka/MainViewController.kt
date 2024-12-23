package com.turskyi.malaknyzhka

import App
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        prefs = remember {
            createDataStore()
        }
    )
}
