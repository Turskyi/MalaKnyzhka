package com.turskyi.malaknyzhka.ui

import androidx.compose.runtime.Composable
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.models.PageSettings
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(settings: Settings) {
    AppTheme {
        Page(PageSettings(settings))
    }
}