package com.turskyi.malaknyzhka.ui

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
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.util.rememberWindowSize

@Composable
fun Page(pageSettings: PageSettings) {
    val initialPositionInTheMiddle = 0.5f
    var dividerPosition: Float by remember {
        mutableStateOf(
            initialPositionInTheMiddle
        )
    }

    // Constants for divider limits.
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    // State for current page initialized with value from settings
    var currentPage: Int by remember {
        mutableStateOf(pageSettings.getCurrentPage())
    }

    // Function to handle page changes.
    fun onNewPage(newPage: Int) {
        pageSettings.saveCurrentPage(newPage)
        currentPage = newPage
    }

    // Screen width detection
    val windowInfo: WindowInfo = rememberWindowSize()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            BookSpreads(
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onDividerPositionChange = { newPosition ->
                    dividerPosition = newPosition.coerceIn(
                        maxTopFraction,
                        minBottomFraction,
                    )
                },
                screenWidth = windowInfo.screenWidth
            )

            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                PageSwitcherButtons(
                    currentPage = currentPage,
                    onPageChange = ::onNewPage
                )
            }
        }
    }
}
