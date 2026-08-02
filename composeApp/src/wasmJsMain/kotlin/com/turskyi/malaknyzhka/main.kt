@file:OptIn(ExperimentalWasmJsInterop::class)

package com.turskyi.malaknyzhka

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavHostController
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.ObservableSettings
import com.turskyi.malaknyzhka.infrastructure.WasmTextToSpeech
import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.SettingsKeys
import com.turskyi.malaknyzhka.models.SettingsUserSettingsRepository
import com.turskyi.malaknyzhka.share.WasmShareManager
import com.turskyi.malaknyzhka.ui.App
import com.turskyi.malaknyzhka.usecases.HostnameSettingsResolver
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.PopStateEvent
import org.w3c.dom.events.Event

@JsFun("() => hideLoadingOverlay()")
external fun hideLoadingOverlay()

@JsFun("(title, description, url) => updateMetadata(title, description, url)")
external fun updateMetadata(title: String, description: String, url: String)

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalBrowserHistoryApi::class,
    ExperimentalWasmJsInterop::class
)
fun main() {
    val body: HTMLElement = document.body ?: return
    ComposeViewport(body) {
        val settings = remember { createSettings() }
        val platform = remember { getPlatform() }
        val userSettingsRepository = remember(settings) {
            SettingsUserSettingsRepository(settings)
        }

        val overrides = remember(platform) { HostnameSettingsResolver.resolve(platform.hostname) }

        var currentExperience by remember {
            mutableStateOf(overrides.experience ?: userSettingsRepository.getExperience(Experience.TARAS))
        }

        DisposableEffect(settings) {
            val observableSettings = settings as? ObservableSettings
            val listener = observableSettings?.addStringListener(
                SettingsKeys.EXPERIENCE,
                Experience.TARAS.name
            ) { newValue ->
                currentExperience = try {
                    Experience.valueOf(newValue)
                } catch (_: Exception) {
                    Experience.TARAS
                }
            }
            onDispose {
                listener?.deactivate()
            }
        }

        // Reconcile favicon on startup
        LaunchedEffect(Unit) {
            platform.syncLauncherIcon(currentExperience, immediate = true)
        }

        val navController: NavHostController = rememberNavController()
        App(
            settings = settings,
            textToSpeech = remember { WasmTextToSpeech() },
            shareManager = remember { WasmShareManager() },
            navController = navController,
            platform = platform
        )

        LaunchedEffect(Unit) {
            hideLoadingOverlay()
        }

        LaunchedEffect(navController, currentExperience) {
            val updateMetadataForRoute: (String?) -> Unit = { route ->
                val baseUrl = "https://shevchenkoai.com"
                when (route) {
                    com.turskyi.malaknyzhka.router.NavigationDestination.Landing.name -> {
                        val title = if (currentExperience == Experience.BOOK) {
                            "Мала Книжка ✦ Тарас Шевченко"
                        } else {
                            "Тарас Шевченко ✦"
                        }
                        updateMetadata(
                            title,
                            "Досліджуйте творчість та життя Тараса Шевченка за допомогою ШІ.",
                            baseUrl
                        )
                    }
                    com.turskyi.malaknyzhka.router.NavigationDestination.Book.name -> {
                        updateMetadata(
                            "Мала Книжка ✦ Тарас Шевченко",
                            "Читати поезію Тараса Шевченка онлайн.",
                            "$baseUrl/Book"
                        )
                    }
                    com.turskyi.malaknyzhka.router.NavigationDestination.Chat.name -> {
                        updateMetadata(
                            "Чат з Тарасом Шевченком",
                            "Поспілкуйтеся з Кобзарем за допомогою штучного інтелекту.",
                            "$baseUrl/Chat"
                        )
                    }
                    else -> {
                        if (route != null) {
                            updateMetadata(
                                "$route ✦ Тарас Шевченко",
                                "Сторінка $route у додатку Тарас Шевченко.",
                                "$baseUrl/$route"
                            )
                        }
                    }
                }
            }

            // Update immediately when experience changes on current route
            updateMetadataForRoute(navController.currentBackStackEntry?.destination?.route)

            navController.currentBackStackEntryFlow.collect { entry ->
                updateMetadataForRoute(entry.destination.route)
            }
        }

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
