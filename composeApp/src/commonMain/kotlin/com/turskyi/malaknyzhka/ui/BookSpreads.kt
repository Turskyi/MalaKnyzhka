package com.turskyi.malaknyzhka.ui

import TextPages
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources._002
import malaknyzhka.composeapp.generated.resources._003
import malaknyzhka.composeapp.generated.resources._004
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun BookSpreads(
    dividerPosition: Float,
    currentPage: Int,
    onDividerPositionChange: (Float) -> Unit,
    screenWidth: Dp
) {
    val bookSpreads: List<DrawableResource> = listOf(
        Res.drawable._002,
        Res.drawable._003,
        Res.drawable._004,
    )

    val isWideScreen = screenWidth > 600.dp

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
                        .background(Color(0xFFf0e7d8)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(bookSpreads[currentPage]),
                        contentDescription = "Book Spread",
                        modifier = Modifier.fillMaxSize()
                    )
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
                        TextPages(currentPage)
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
                    onPositionChange = onDividerPositionChange
                )
            }
        }
    }
}











