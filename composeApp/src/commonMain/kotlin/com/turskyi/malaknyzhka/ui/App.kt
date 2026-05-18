package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.AppLocaleManager
import com.turskyi.malaknyzhka.models.LocalWindowInfo
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.models.rememberAppLocaleManager
import com.turskyi.malaknyzhka.router.NavigationDestination
import com.turskyi.malaknyzhka.ui.about.AboutPage
import com.turskyi.malaknyzhka.ui.book.Page
import com.turskyi.malaknyzhka.ui.landing.LandingPage
import com.turskyi.malaknyzhka.ui.privacy.PrivacyPolicyPage
import com.turskyi.malaknyzhka.ui.support.SupportPage
import com.turskyi.malaknyzhka.util.isOnAndroid
import com.turskyi.malaknyzhka.util.isOnDesktop
import com.turskyi.malaknyzhka.util.isOnWeb
import com.turskyi.malaknyzhka.util.toApLang

@Composable
fun App(
    settings: Settings,
    navController: NavHostController = rememberNavController()
) {

    val appLocaleManager: AppLocaleManager = rememberAppLocaleManager()
    // This is the app-wide UI language.
    var appGlobalLanguage: AppLang by remember {
        mutableStateOf(appLocaleManager.getLocale().toApLang())
    }

    val changeAppGlobalLanguage: (AppLang) -> Unit = { newLang: AppLang ->
        // Tells platform to change locale.
        appLocaleManager.setLocale(newLang)
        // Updates Compose state for UI recomposition.
        appGlobalLanguage = newLang
    }

    // This LaunchedEffect ensures that on the very first launch,
    // if getLocale() returned the intended default,
    // we also explicitly call setLocale() to ensure the platform/system
    // reflects this default and the "user_set" flag is persisted.
    // This is because getLocale() might just READ the default without APPLYING
    // it.
    LaunchedEffect(Unit) {
        if (isOnDesktop()) {
            // JVM doesn't persist Locale.setDefault(),
            // so we re-apply it at every startup.
            appLocaleManager.setLocale(appGlobalLanguage)
        } else if (isOnAndroid() && !appLocaleManager.hasUserEverSetLanguage()) {
            // This is the very first session where no language has been
            // explicitly set.
            // Even if appGlobalLanguage was initialized to DEFAULT by
            // getLocale(),
            // call setLocale to ensure it's applied to the system
            // (e.g., AppCompatDelegate) and the "user set" flag is stored.
            if (appGlobalLanguage == AppLang.DEFAULT) {
                appLocaleManager.setLocale(AppLang.DEFAULT)
            } else {
                // This case implies getLocale() didn't return your intended
                // default initially,
                // which means the logic in getLocale() needs review.
                // However, we can still force the default here.
                changeAppGlobalLanguage(AppLang.DEFAULT)
            }
        }
    }

    CompositionLocalProvider(
        LocalAppLanguage provides appGlobalLanguage,
        LocalChangeAppLanguage provides changeAppGlobalLanguage,
    ) {
        key(appGlobalLanguage) {
            AppTheme {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val windowInfo = WindowInfo(screenWidth = maxWidth)
                    CompositionLocalProvider(LocalWindowInfo provides windowInfo) {
                        NavHost(
                            navController = navController,
                            startDestination = if (isOnWeb())
                                NavigationDestination.Landing.name
                            else
                                NavigationDestination.Book.name
                        ) {
                            composable(route = NavigationDestination.Landing.name) {
                                LandingPage(
                                    onNavigateToBook = {
                                        navController.navigate(
                                            NavigationDestination.Book.name
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateToPrivacyPolicy = {
                                        navController.navigate(
                                            NavigationDestination.PrivacyPolicy.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateToSupport = {
                                        navController.navigate(
                                            NavigationDestination.Support.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateToAbout = {
                                        navController.navigate(
                                            NavigationDestination.About.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(route = NavigationDestination.Book.name) {
                                Page(
                                    PageSettings(settings),
                                    onNavigateToPrivacyPolicy = {
                                        navController.navigate(
                                            NavigationDestination.PrivacyPolicy.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateToSupport = {
                                        navController.navigate(
                                            NavigationDestination.Support.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                    onNavigateToAbout = {
                                        navController.navigate(
                                            NavigationDestination.About.name,
                                        ) {
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(
                                route = NavigationDestination.PrivacyPolicy.name,
                            ) {
                                PrivacyPolicyPage(
                                    onBack = {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    },
                                )
                            }
                            composable(route = NavigationDestination.Support.name) {
                                SupportPage(
                                    onBack = {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    },
                                )
                            }
                            composable(route = NavigationDestination.About.name) {
                                AboutPage(
                                    onBack = {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalAppLanguage: ProvidableCompositionLocal<AppLang> =
    compositionLocalOf { AppLang.DEFAULT }


val LocalChangeAppLanguage: ProvidableCompositionLocal<(AppLang) -> Unit> =
    compositionLocalOf { error("ChangeAppLanguage not provided") }
