package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.infrastructure.BookSpreadsRegistry
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun PageSwitcherButtons(
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    val totalPages: Int = BookSpreadsRegistry.allBookSpreads.size
    val firstPage = 0

    Box(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        // Previous Page Button.
        PageSwitcherButton(
            onClick = {
                val newPage: Int = (currentPage - 1).coerceAtLeast(firstPage)
                onPageChange(newPage)
            },
            enabled = currentPage != firstPage,
            modifier = Modifier.align(Alignment.BottomStart).padding(4.dp),
            label = "<"
        )

        // Slider for page selection.
        Slider(
            value = currentPage.toFloat(),
            onValueChange = { value: Float ->
                val newPage: Int = value.toInt()
                    .coerceIn(firstPage, totalPages - 1)
                onPageChange(newPage)
            },
            valueRange = 0f..(totalPages - 1).toFloat(),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 40.dp, vertical = 4.dp).then(
                    Modifier.rotate(0f)
                )
        )

        // Next Page Button.
        PageSwitcherButton(
            onClick = {
                val newPage: Int = (currentPage + 1)
                    .coerceAtMost(totalPages - 1)
                onPageChange(newPage)
            },
            enabled = currentPage < totalPages - 1,
            modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp),
            label = ">"
        )
    }
}
