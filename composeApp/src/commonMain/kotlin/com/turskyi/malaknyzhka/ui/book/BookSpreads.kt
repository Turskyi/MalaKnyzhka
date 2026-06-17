package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.ui.LocalThemeMode
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun BookSpreads(
    bookSpreads: List<DrawableResource>,
    dividerPosition: Float,
    currentPage: Int,
    onDividerPositionChange: (Float) -> Unit,
    screenWidth: Dp,
    appLang: AppLang
) {
    val isWideScreen: Boolean = screenWidth > 600.dp
    val themeMode = LocalThemeMode.current
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val density: Density = LocalDensity.current
        val screenHeightPx: Float = with(density) { maxHeight.toPx() }
        val screenWidthPx: Float = with(density) { maxWidth.toPx() }

        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier.fillMaxSize()
            ) {
                // Book spreads with background.
                Box(
                    Modifier
                        .fillMaxWidth(
                            if (isWideScreen) dividerPosition else 1f,
                        )
                        .fillMaxHeight(
                            if (isWideScreen) 1f else dividerPosition,
                        )
                        .background(
                            if (isDark) Color(0xFF121212)
                            else MaterialTheme.colors.background
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ZoomableImage(bookSpreads[currentPage])
                }

                // Position TextPages.
                Box(
                    Modifier.align(
                        if (isWideScreen)
                            Alignment.CenterEnd
                        else
                            Alignment.BottomStart,
                    )
                        .fillMaxWidth(
                            if (isWideScreen) 1f - dividerPosition else 1f,
                        )
                        .fillMaxHeight(
                            if (isWideScreen) 1f else 1f - dividerPosition,
                        )
                ) {
                    // if too narrow.
                    if (dividerPosition < 0.88f) {
                        TextPages(currentPage, appLang)
                    }
                }

                // Draggable Divider with Button
                DraggableDividerWithButton(
                    modifier = Modifier.align(
                        if (isWideScreen)
                            Alignment.CenterStart
                        else
                            Alignment.TopStart,
                    ),
                    dividerPosition = dividerPosition,
                    screenHeightOrWidthPx = if (isWideScreen)
                        screenWidthPx
                    else
                        screenHeightPx,
                    dragOrientation = if (isWideScreen)
                        Orientation.Horizontal
                    else
                        Orientation.Vertical,
                    onPositionChange = onDividerPositionChange,
                    currentPage = currentPage,
                    totalPages = bookSpreads.size,
                )
            }
        }
    }
}