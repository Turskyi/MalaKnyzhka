package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.Platform
import com.turskyi.malaknyzhka.ai.ChatApi
import com.turskyi.malaknyzhka.ai.ChatRepository
import com.turskyi.malaknyzhka.ai.ChatScreen
import com.turskyi.malaknyzhka.ai.ChatViewModel
import com.turskyi.malaknyzhka.getPlatform
import com.turskyi.malaknyzhka.infrastructure.TextToSpeech
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.AppLocale
import com.turskyi.malaknyzhka.models.BookmarkRepository
import com.turskyi.malaknyzhka.models.LocalWindowInfo
import com.turskyi.malaknyzhka.models.PlatformType
import com.turskyi.malaknyzhka.models.SettingsBookRepository
import com.turskyi.malaknyzhka.models.SettingsBookmarkRepository
import com.turskyi.malaknyzhka.models.SettingsUserSettingsRepository
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.models.UserSettingsRepository
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.models.rememberAppLocale
import com.turskyi.malaknyzhka.router.NavigationDestination
import com.turskyi.malaknyzhka.share.ShareManager
import com.turskyi.malaknyzhka.ui.about.AboutPage
import com.turskyi.malaknyzhka.ui.book.BookmarksPage
import com.turskyi.malaknyzhka.ui.book.Page
import com.turskyi.malaknyzhka.ui.landing.LandingPage
import com.turskyi.malaknyzhka.ui.privacy.PrivacyPolicyPage
import com.turskyi.malaknyzhka.ui.support.SupportPage
import com.turskyi.malaknyzhka.usecases.toInternalPageIndex
import kotlinx.coroutines.flow.collectLatest

@Composable
fun App(
    settings: Settings,
    textToSpeech: TextToSpeech,
    shareManager: ShareManager,
    navController: NavHostController = rememberNavController(),
    platform: Platform = remember { getPlatform() }
) {
    val appLocale: AppLocale = rememberAppLocale()
    val userSettingsRepository: UserSettingsRepository = remember(settings) {
        SettingsUserSettingsRepository(settings)
    }
    val viewModel: AppViewModel = viewModel {
        AppViewModel(appLocale, userSettingsRepository)
    }

    val chatViewModel: ChatViewModel = viewModel {
        ChatViewModel(ChatRepository(ChatApi()))
    }

    val bookmarkRepository: BookmarkRepository = remember(settings) {
        SettingsBookmarkRepository(settings)
    }

    // This is the app-wide UI language state.
    val appGlobalLanguage: AppLang by viewModel.appGlobalLanguage.collectAsState()

    val changeAppGlobalLanguage: (AppLang) -> Unit = { newLang: AppLang ->
        viewModel.changeAppGlobalLanguage(newLang)
    }

    val themeMode: ThemeMode by viewModel.themeMode.collectAsState()

    val changeThemeMode: (ThemeMode) -> Unit = { newMode: ThemeMode ->
        viewModel.changeThemeMode(newMode)
    }

    val startDestination: String = remember(platform) {
        val initial = platform.initialRoute?.removePrefix("/")
        val matchedDestination = NavigationDestination.entries.firstOrNull {
            it.name.equals(
                initial?.split("/")?.firstOrNull(),
                ignoreCase = true
            )
        }

        if (platform.type == PlatformType.WEB && matchedDestination != null) {
            matchedDestination.name
        } else if (initial?.startsWith("book/") == true) {
            // Special handling for book deep links
            NavigationDestination.Book.name
        } else if (platform.type == PlatformType.WEB) {
            NavigationDestination.Landing.name
        } else {
            NavigationDestination.Book.name
        }
    }

    // Effect to handle deep links if they contain a page number
    LaunchedEffect(platform.initialRoute) {
        val initial = platform.initialRoute?.removePrefix("/")
        if (initial?.startsWith("book/") == true) {
            val userPageNumber = initial.substringAfter("book/").toIntOrNull()
            if (userPageNumber != null) {
                val bookRepository = SettingsBookRepository(settings)
                val internalIndex = userPageNumber.toInternalPageIndex()
                bookRepository.saveCurrentPage(internalIndex)
            }
        }
    }

    val onBack: () -> Unit = remember(navController, platform) {
        {
            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            } else {
                val home = if (platform.type == PlatformType.WEB) {
                    NavigationDestination.Landing.name
                } else {
                    NavigationDestination.Book.name
                }
                // Avoid navigating to the same destination we are already on
                if (navController.currentBackStackEntry?.destination?.route != home) {
                    navController.navigate(home) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(shareManager) {
        shareManager.toastMessages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    CompositionLocalProvider(
        LocalAppLanguage provides appGlobalLanguage,
        LocalChangeAppLanguage provides changeAppGlobalLanguage,
        LocalThemeMode provides themeMode,
        LocalChangeThemeMode provides changeThemeMode,
        LocalShareManager provides shareManager,
    ) {
        // The 'key' forces the entire tree to be recreated when the language
        // changes.
        // This is essential on iOS for stringResource() to pick up the new
        // locale.
        key(appGlobalLanguage) {
            AppTheme(themeMode = themeMode) {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) {
                    BoxWithConstraints(Modifier.fillMaxSize()) {
                        val windowInfo = WindowInfo(screenWidth = maxWidth)
                        CompositionLocalProvider(
                            LocalWindowInfo provides windowInfo,
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = startDestination
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
                                        bookRepository = SettingsBookRepository(
                                            settings
                                        ),
                                        bookmarkRepository = bookmarkRepository,
                                        textToSpeech = textToSpeech,
                                        chatViewModel = chatViewModel,
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
                                        },
                                        onNavigateToBookmarks = {
                                            navController.navigate(
                                                NavigationDestination.Bookmarks.name,
                                            ) {
                                                launchSingleTop = true
                                            }
                                        },
                                        onNavigateToChat = { pageNumber, pageText ->
                                            chatViewModel.currentPageNumber =
                                                pageNumber
                                            chatViewModel.currentPageText =
                                                pageText
                                            chatViewModel.setExpanded(true)
                                            navController.navigate(
                                                NavigationDestination.Chat.name
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                                composable(route = NavigationDestination.Chat.name) {
                                    ChatScreen(
                                        viewModel = chatViewModel,
                                        onBack = onBack
                                    )
                                }
                                composable(route = NavigationDestination.Bookmarks.name) {
                                    BookmarksPage(
                                        bookmarkRepository = bookmarkRepository,
                                        onBookmarkClick = { pageNumber: Int ->
                                            SettingsBookRepository(settings)
                                                .saveCurrentPage(pageNumber)
                                            navController.navigate(
                                                NavigationDestination.Book.name
                                            ) {
                                                popUpTo(
                                                    NavigationDestination.Book.name
                                                ) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onBack = onBack
                                    )
                                }
                                composable(route = NavigationDestination.PrivacyPolicy.name) {
                                    PrivacyPolicyPage(onBack = onBack)
                                }
                                composable(route = NavigationDestination.Support.name) {
                                    SupportPage(onBack = onBack)
                                }
                                composable(route = NavigationDestination.About.name) {
                                    AboutPage(onBack = onBack)
                                }
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

val LocalThemeMode: ProvidableCompositionLocal<ThemeMode> =
    compositionLocalOf { ThemeMode.SYSTEM }

val LocalChangeThemeMode: ProvidableCompositionLocal<(ThemeMode) -> Unit> =
    compositionLocalOf { error("ChangeThemeMode not provided") }

val LocalShareManager: ProvidableCompositionLocal<ShareManager> =
    compositionLocalOf { error("ShareManager not provided") }
