package com.turskyi.malaknyzhka

import App
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App(
        settings = remember {
            createDataStore()
        }
    )
}
