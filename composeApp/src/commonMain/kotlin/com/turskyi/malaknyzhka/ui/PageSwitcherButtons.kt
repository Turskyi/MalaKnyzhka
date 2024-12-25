package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageSwitcherButtons(
    currentPage: Int,
    onPageChange: (Int) -> Unit
) {
    // TODO: Adjust as needed for the number of book spreads
    val totalPages = 3
    val firstPage = 0

    Box(Modifier.fillMaxSize()) {
        // Previous Page Button
        Button(
            onClick = {
                val newPage: Int = (currentPage - 1).coerceAtLeast(firstPage)
                onPageChange(newPage)
            },
            modifier = Modifier.align(Alignment.BottomStart).padding(4.dp)
                .size(50.dp),
            shape = CircleShape,
            enabled = currentPage != firstPage
        ) {
            Text("<")
        }

        // Next Page Button.
        Button(
            onClick = {
                val newPage: Int = (currentPage + 1).coerceAtMost(
                    totalPages - 1,
                )
                onPageChange(newPage)
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                .size(50.dp),
            shape = CircleShape,
            // Disable button if it's the last page.
            enabled = currentPage < totalPages - 1,
        ) {
            Text(">")
        }
    }
}
