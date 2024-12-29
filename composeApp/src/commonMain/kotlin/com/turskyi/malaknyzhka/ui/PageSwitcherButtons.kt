package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun PageSwitcherButtons(
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    // TODO: Adjust as needed for the number of book spreads
    val totalPages = 4
    val firstPage = 0

    Box(Modifier.fillMaxSize()) {
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
