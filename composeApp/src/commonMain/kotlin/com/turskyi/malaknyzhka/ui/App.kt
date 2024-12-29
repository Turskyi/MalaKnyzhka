package com.turskyi.malaknyzhka.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.router.NavigationDestination
import com.turskyi.malaknyzhka.util.isOnWeb
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(settings: Settings) {
    var currentDestination: NavigationDestination by remember {
        mutableStateOf(
            if (isOnWeb())
                NavigationDestination.Landing
            else
                NavigationDestination.Book
        )
    }

    AppTheme {
        when (currentDestination) {
            is NavigationDestination.Landing -> {
                LandingPage(onNavigateToBook = {
                    currentDestination = NavigationDestination.Book
                })
            }

            is NavigationDestination.Book -> {
                Page(PageSettings(settings))
            }
        }
    }
}