package com.turskyi.malaknyzhka

import BookSpreads
import PageSwitcherButtons
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Page(
    pageSettings: PageSettings
) {
    val initialPositionInTheMiddle = 0.5f
    var dividerPosition by remember { mutableStateOf(initialPositionInTheMiddle) }

    // Constants for divider limits
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    // TODO: Adjust as needed for the number of book spreads
    val totalPages = 2

    // State for current page initialized with value from settings
    var currentPage: Int by remember { mutableStateOf(pageSettings.getCurrentPage()) }

    // Function to handle page changes
    fun onNewPage(newPage: Int) {
        pageSettings.saveCurrentPage(newPage)
        // Update state with new page
        currentPage = newPage
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            BookSpreads(
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onPageChange = ::onNewPage,
                onDividerPositionChange = { newPosition ->
                    dividerPosition = newPosition.coerceIn(
                        maxTopFraction,
                        minBottomFraction,
                    )
                }
            )

            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                PageSwitcherButtons(
                    currentPage = currentPage,
                    onPageChange = ::onNewPage,
                    totalPages = totalPages
                )
            }
        }
    }
}
