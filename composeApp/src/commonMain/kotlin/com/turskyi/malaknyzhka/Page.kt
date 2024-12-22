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
fun Page() {
    val firstPage = 0
    var currentPage by remember { mutableStateOf(firstPage) }
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
                onPageChange = { newPage -> currentPage = newPage },
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
                    onPageChange = { newPage -> currentPage = newPage },
                    totalPages = totalPages
                )
            }
        }
    }
}