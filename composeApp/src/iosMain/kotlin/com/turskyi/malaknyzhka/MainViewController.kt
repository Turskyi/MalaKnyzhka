package com.turskyi.malaknyzhka

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.turskyi.malaknyzhka.ui.App

fun MainViewController() = ComposeUIViewController {
    App(
        settings = remember {
            createDataStore()
        }
    )
}
