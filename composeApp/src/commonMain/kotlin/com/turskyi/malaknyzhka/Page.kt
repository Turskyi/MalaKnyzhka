package com.turskyi.malaknyzhka

import BookSpreads
import PageSwitcherButtons
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun Page(
    prefs: DataStore<Preferences>
) {
    val firstPage = 0

    val counterKey = intPreferencesKey("currentPage")

    // Get the current page from DataStore
    val currentPage by prefs.data
        .map { preferences -> preferences[counterKey] ?: firstPage }
        .collectAsState(firstPage)

    // Function to update the current page in DataStore
    val scope = rememberCoroutineScope()
    fun savePageToDataStore(newPage: Int) {
        scope.launch {
            prefs.edit { preferences ->
                preferences[counterKey] = newPage
            }
        }
    }

    val initialPositionInTheMiddle = 0.5f
    var dividerPosition by remember { mutableStateOf(initialPositionInTheMiddle) }

    // Constants for divider limits
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    //TODO: Adjust as needed for the number of book spreads
    val totalPages = 2

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            BookSpreads(
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onPageChange = { newPage ->
                    savePageToDataStore(newPage)
                },
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
                    onPageChange = { newPage ->
                        savePageToDataStore(newPage)
                    },
                    totalPages = totalPages
                )
            }
        }
    }
}