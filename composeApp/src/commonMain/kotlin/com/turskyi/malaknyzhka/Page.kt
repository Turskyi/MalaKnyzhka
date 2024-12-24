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
import com.russhwolf.settings.Settings

@Composable
fun Page(
    settings: Settings
) {

    val firstPage = 0

    // Function to get the current page from Settings
    fun getCurrentPage(): Int {
        return settings.getInt("currentPage", firstPage)
    }

    // Use state to manage the current page
    var currentPage by remember { mutableStateOf(getCurrentPage()) }

    val initialPositionInTheMiddle = 0.5f
    var dividerPosition by remember { mutableStateOf(initialPositionInTheMiddle) }

    // Constants for divider limits
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    // TODO: Adjust as needed for the number of book spreads
    val totalPages = 2

    // Extracted function
    fun onNewPage(newPage: Int) {
        settings.putInt("currentPage", newPage) // Save new page in Settings
        currentPage = newPage // Update state with new page
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
                        minBottomFraction
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
