package com.turskyi.malaknyzhka

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.turskyi.malaknyzhka.infrastructure.IosTextToSpeech
import com.turskyi.malaknyzhka.ui.App

// This function is the entry point for the iOS application.
// It is called from the Swift code, so do not remove it even if it is marked
// as unused.
@Suppress("FunctionName")
fun MainViewController() = ComposeUIViewController {
    App(
        settings = remember {
            createDataStore()
        },
        textToSpeech = remember { IosTextToSpeech() }
    )
}
