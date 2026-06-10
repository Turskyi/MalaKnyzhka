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
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.BookRepository
import com.turskyi.malaknyzhka.models.LocalWindowInfo
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.ui.LocalAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeAppLanguage
import com.turskyi.malaknyzhka.ui.drawer.DrawerPanel
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.menu
import malaknyzhka.composeapp.generated.resources.search_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Page(
    bookRepository: BookRepository,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    val viewModel: BookViewModel = viewModel { BookViewModel(bookRepository) }

    // Get the global app language and the function to change it.
    val appGlobalLanguage: AppLang = LocalAppLanguage.current

    val changeAppGlobalLanguage: (AppLang) -> Unit =
        LocalChangeAppLanguage.current

    val isDrawerOpen: Boolean by viewModel.isDrawerOpen.collectAsState()
    val isSearchOpen: Boolean by viewModel.isSearchOpen.collectAsState()
    val dividerPosition: Float by viewModel.dividerPosition.collectAsState()
    val currentPage: Int by viewModel.currentPage.collectAsState()

    // Screen width detection.
    val windowInfo: WindowInfo = LocalWindowInfo.current

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().background(
                Color.White,
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
                        color = Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu),
                    contentDescription = stringResource(Res.string.menu),
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🔍 Search button in top-right corner.
            IconButton(
                onClick = { viewModel.setSearchOpen(true) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(4.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(Res.string.search_description),
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
                currentLanguage = appGlobalLanguage,
                onLanguageChange = {
                    viewModel.onLanguageChange(it.code)
                    changeAppGlobalLanguage(it)
                },
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
