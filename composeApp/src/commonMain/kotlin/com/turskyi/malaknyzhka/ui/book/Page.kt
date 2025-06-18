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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.ui.LocalAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeAppLanguage
import com.turskyi.malaknyzhka.ui.drawer.DrawerPanel
import com.turskyi.malaknyzhka.util.rememberWindowSize
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.menu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Page(
    pageSettings: PageSettings,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    // Get the global app language and the function to change i.t.
    val appGlobalLanguage: AppLang = LocalAppLanguage.current

    val changeAppGlobalLanguage: (AppLang) -> Unit =
        LocalChangeAppLanguage.current

    var isDrawerOpen: Boolean by remember { mutableStateOf(false) }

    val initialPositionInTheMiddle = 0.5f

    var currentLanguage: String by remember {
        mutableStateOf(pageSettings.getCurrentLanguage())
    }

    var dividerPosition: Float by remember {
        mutableStateOf(initialPositionInTheMiddle)
    }

    // Constants for divider limits.
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    // State for current page initialized with value from settings.
    var currentPage: Int by remember {
        mutableStateOf(pageSettings.getCurrentPage())
    }

    // Function to handle page changes.
    fun onNewPage(newPage: Int) {
        pageSettings.saveCurrentPage(newPage)
        currentPage = newPage
    }

    // Screen width detection.
    val windowInfo: WindowInfo = rememberWindowSize()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize().background(
                Color.White,
            )
        ) {
            BookSpreads(
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onDividerPositionChange = { newPosition: Float ->
                    dividerPosition = newPosition.coerceIn(
                        maxTopFraction,
                        minBottomFraction,
                    )
                },
                screenWidth = windowInfo.screenWidth
            )

            // 📖 Bottom page switcher.
            Box(
                Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            ) {
                PageSwitcherButtons(
                    currentPage = currentPage,
                    onPageChange = ::onNewPage,
                )
            }

            // 🍔 Burger button in top-left corner.
            IconButton(
                onClick = { isDrawerOpen = true },
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

            // 🪟 Semi-transparent overlay.
            if (isDrawerOpen) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { isDrawerOpen = false }
                )
            }

            DrawerPanel(
                visible = isDrawerOpen,
                onClose = { isDrawerOpen = false },
                onNavigateToAbout = onNavigateToAbout,
                onNavigateToPrivacyPolicy = onNavigateToPrivacyPolicy,
                onNavigateToSupport = onNavigateToSupport,
                currentLanguage = appGlobalLanguage,
                onLanguageChange = {
                    pageSettings.saveCurrentLanguage(it.code)
                    currentLanguage = it.code
                    changeAppGlobalLanguage(it)
                },
            )
        }
    }
}
