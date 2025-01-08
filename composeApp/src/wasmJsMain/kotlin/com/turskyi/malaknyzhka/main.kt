package com.turskyi.malaknyzhka

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.rememberNavController
import com.turskyi.malaknyzhka.ui.App
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    val body: HTMLElement = document.body ?: return
    ComposeViewport(body) {
        val navController: NavHostController = rememberNavController()
        App(
            settings = remember {
                createSettings()
            },
            navController = navController,
        )
        LaunchedEffect(Unit) {
            window.bindToNavigation(navController)
        }
    }
}