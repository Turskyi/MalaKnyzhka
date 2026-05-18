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

    // This is the app-wide UI language state.
    var appGlobalLanguage: AppLang by remember {
        mutableStateOf(appLocaleManager.getLocale().toApLang())
    }

    val changeAppGlobalLanguage: (AppLang) -> Unit = { newLang: AppLang ->
        // 1. Persist the change on the platform (stores in NSUserDefaults on
        // iOS).
        appLocaleManager.setLocale(newLang)
        // 2. Update the state to trigger Compose recomposition.
        appGlobalLanguage = newLang
    }

    LaunchedEffect(Unit) {
        if (isOnDesktop()) {
            appLocaleManager.setLocale(appGlobalLanguage)
        } else if (isOnAndroid() && !appLocaleManager.hasUserEverSetLanguage()) {
            if (appGlobalLanguage == AppLang.DEFAULT) {
                appLocaleManager.setLocale(AppLang.DEFAULT)
            } else {
                changeAppGlobalLanguage(AppLang.DEFAULT)
            }
        }
    }

    CompositionLocalProvider(
        LocalAppLanguage provides appGlobalLanguage,
        LocalChangeAppLanguage provides changeAppGlobalLanguage,
    ) {
        // The 'key' forces the entire tree to be recreated when the language
        // changes.
        // This is essential on iOS for stringResource() to pick up the new
        // locale.
        key(appGlobalLanguage) {
            AppTheme {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val windowInfo = WindowInfo(screenWidth = maxWidth)
                    CompositionLocalProvider(
                        LocalWindowInfo provides windowInfo,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = if (isOnWeb())
                                NavigationDestination.Landing.name
                            else
                                NavigationDestination.Book.name
                        ) {
                            composable(
                                route = NavigationDestination.Landing.name,
                            ) {
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
                            composable(
                                route = NavigationDestination.Book.name,
                            ) {
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
                            composable(
                                route = NavigationDestination.Support.name,
                            ) {
                                SupportPage(
                                    onBack = {
                                        if (navController.previousBackStackEntry != null) {
                                            navController.popBackStack()
                                        }
                                    },
                                )
                            }
                            composable(
                                route = NavigationDestination.About.name,
                            ) {
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
