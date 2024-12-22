import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var currentPage by remember { mutableStateOf(0) }
    var dividerPosition by remember { mutableStateOf(0.5f) } // Initial position in the middle

    val totalPages = 2 // Adjust as needed for the number of book spreads

    MaterialTheme {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()) {
                BookSpreads(
                    dividerPosition = dividerPosition,
                    currentPage = currentPage,
                    onPageChange = { newPage -> currentPage = newPage },
                    onDividerPositionChange = { newPosition ->
                        dividerPosition = newPosition.coerceIn(0.1f, 0.9f)
                    }
                )

                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth() // Ensure the box fills max width
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
}
