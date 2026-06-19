package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turskyi.malaknyzhka.infrastructure.BookContentRegistry
import com.turskyi.malaknyzhka.infrastructure.TextToSpeech
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.BookRepository
import com.turskyi.malaknyzhka.models.BookmarkRepository
import com.turskyi.malaknyzhka.models.LocalWindowInfo
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.ui.LocalAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeThemeMode
import com.turskyi.malaknyzhka.ui.LocalThemeMode
import com.turskyi.malaknyzhka.ui.drawer.DrawerPanel
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.menu
import malaknyzhka.composeapp.generated.resources.search_description
import malaknyzhka.composeapp.generated.resources.tts_play
import malaknyzhka.composeapp.generated.resources.tts_stop
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Page(
    bookRepository: BookRepository,
    bookmarkRepository: BookmarkRepository,
    textToSpeech: TextToSpeech,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    onNavigateToChat: (pageNumber: Int, pageText: String) -> Unit,
) {
    val viewModel: BookViewModel = viewModel {
        BookViewModel(bookRepository, bookmarkRepository, textToSpeech)
    }

    // Stop speech when navigating away from the reading screen.
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopSpeech()
        }
    }

    // Get the global app language and the function to change it.
    val appGlobalLanguage: AppLang = LocalAppLanguage.current

    val changeAppGlobalLanguage: (AppLang) -> Unit =
        LocalChangeAppLanguage.current

    val currentThemeMode: ThemeMode = LocalThemeMode.current
    val onThemeChange: (ThemeMode) -> Unit = LocalChangeThemeMode.current

    val isDrawerOpen: Boolean by viewModel.isDrawerOpen.collectAsState()
    val isSearchOpen: Boolean by viewModel.isSearchOpen.collectAsState()
    val dividerPosition: Float by viewModel.dividerPosition.collectAsState()
    val currentPage: Int by viewModel.currentPage.collectAsState()
    val isBookmarked: Boolean by viewModel.isBookmarked.collectAsState()
    val isSpeaking: Boolean by viewModel.isSpeaking.collectAsState()

    val currentPoemText: String = stringResource(
        BookContentRegistry.allPoemPages[currentPage],
    )

    // Screen width detection.
    val windowInfo: WindowInfo = LocalWindowInfo.current

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().background(
                MaterialTheme.colors.background,
            )
        ) {
            BookSpreads(
                bookSpreads = viewModel.bookSpreads,
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onDividerPositionChange = viewModel::onDividerPositionChange,
                screenWidth = windowInfo.screenWidth,
                appLang = appGlobalLanguage
            )

            // 📖 Bottom page switcher.
            Box(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            ) {
                PageSwitcherButtons(
                    currentPage = currentPage,
                    onPageChange = viewModel::onNewPage,
                )
            }

            // 🍔 Burger button in top-left corner.
            IconButton(
                onClick = { viewModel.setDrawerOpen(true) },
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(4.dp)
                    .background(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu),
                    contentDescription = stringResource(Res.string.menu),
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🔖 Bookmark button in top-right corner.
            IconButton(
                onClick = { viewModel.toggleBookmark() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 4.dp, end = 76.dp)
                    .background(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    imageVector = if (isBookmarked) {
                        Icons.Filled.Bookmark
                    } else {
                        Icons.Outlined.BookmarkBorder
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🔊 Text-to-Speech button in top-right corner.
            if (viewModel.isTtsAvailable()) {
                IconButton(
                    onClick = {
                        viewModel.toggleSpeech(
                            text = currentPoemText,
                            languageCode = appGlobalLanguage.code,
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(WindowInsets.statusBars.asPaddingValues())
                        .padding(top = 4.dp, end = 40.dp)
                        .background(
                            color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                            shape = CircleShape
                        ).size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isSpeaking) {
                            Icons.Default.Stop
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = if (isSpeaking) {
                            stringResource(Res.string.tts_stop)
                        } else {
                            stringResource(Res.string.tts_play)
                        },
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            // 🔍 Search button in top-right corner.
            IconButton(
                onClick = { viewModel.setSearchOpen(true) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(4.dp)
                    .background(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.search_description),
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🤖 AI Chat button in top-right corner.
            IconButton(
                onClick = {
                    onNavigateToChat(currentPage, currentPoemText)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(top = 40.dp, end = 4.dp)
                    .background(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = "AI Chat",
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🪟 Semi-transparent overlay.
            if (isDrawerOpen) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { viewModel.setDrawerOpen(false) }
                )
            }

            DrawerPanel(
                visible = isDrawerOpen,
                onClose = { viewModel.setDrawerOpen(false) },
                onNavigateToAbout = onNavigateToAbout,
                onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                onNavigateToSupport = onNavigateToSupport,
                onNavigateToBookmarks = onNavigateToBookmarks,
                currentLanguage = appGlobalLanguage,
                onLanguageChange = {
                    viewModel.onLanguageChange(it.code)
                    changeAppGlobalLanguage(it)
                },
                currentThemeMode = currentThemeMode,
                onThemeChange = onThemeChange,
            )

            if (isSearchOpen) {
                SearchPanel(
                    onClose = { viewModel.setSearchOpen(false) },
                    onResultClick = { pageIndex ->
                        viewModel.setSearchOpen(false)
                        viewModel.onNewPage(pageIndex)
                    }
                )
            }
        }
    }
}
