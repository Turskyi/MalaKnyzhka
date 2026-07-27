package com.turskyi.malaknyzhka

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.compose.rememberNavController
import com.turskyi.malaknyzhka.infrastructure.WasmTextToSpeech
import com.turskyi.malaknyzhka.share.WasmShareManager
import com.turskyi.malaknyzhka.ui.App
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.PopStateEvent
import org.w3c.dom.events.Event

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalBrowserHistoryApi::class,
    ExperimentalWasmJsInterop::class
)
fun main() {
    val body: HTMLElement = document.body ?: return
    ComposeViewport(body) {
        val navController: NavHostController = rememberNavController()
        App(
            settings = remember {
                createSettings()
            },
            textToSpeech = remember { WasmTextToSpeech() },
            shareManager = remember { WasmShareManager() },
            navController = navController,
        )
        LaunchedEffect(navController) {
            // Fix for the "BookBook" bug:
            // bindToBrowserNavigation captures the current pathname as the base URL.
            // If we are at "/Book", it captures that and appends the route name,
            // resulting in "/BookBook". We reset to "/" to ensure the origin is the base.
            if (window.location.pathname != "/") {
                window.history.replaceState(null, "", "/")
            }

            navController.bindToBrowserNavigation(
                getBackStackEntryRoute = { entry ->
                    val route = entry.destination.route ?: ""
                    // Map Landing to root path for a cleaner URL
                    if (route == com.turskyi.malaknyzhka.router.NavigationDestination.Landing.name) "" else route
                }
            )
        }

        DisposableEffect(navController) {
            val callback: (Event) -> Unit = { event ->
                val popStateEvent = event as? PopStateEvent
                if (popStateEvent?.state == null) {
                    val path = window.location.pathname.removePrefix("/")
                    if (path.isNotEmpty() && path != navController.currentDestination?.route) {
                        navController.navigate(path)
                    }
                }
            }
            window.addEventListener("popstate", callback)
            onDispose {
                window.removeEventListener("popstate", callback)
            }
        }
    }
}
