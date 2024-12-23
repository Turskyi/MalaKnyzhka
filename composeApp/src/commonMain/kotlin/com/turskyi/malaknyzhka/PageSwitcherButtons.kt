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
    onPageChange: (Int) -> Unit,
    totalPages: Int
) {
    val firstPage = 0
    Box(Modifier.fillMaxSize()) {
        // Previous com.turskyi.malaknyzhka.Page Button on the left side
        Button(
            onClick = {
                onPageChange((currentPage - 1).coerceAtLeast(firstPage))
            },
            modifier = Modifier
                .align(Alignment.BottomStart).padding(all = 4.dp)
                // Makes the button circular.
                .size(50.dp),
            shape = CircleShape,
            // Disable button if it's the first page.
            enabled = currentPage != firstPage
        ) {
            Text("<")
        }

        // Next com.turskyi.malaknyzhka.Page Button on the right side
        Button(
            onClick = {
                onPageChange((currentPage + 1).coerceAtMost(totalPages - 1))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd).padding(all = 4.dp)
                // Makes the button circular.
                .size(50.dp),
            shape = CircleShape,
            // Disable button if it's the last page.
            enabled = currentPage < totalPages - 1,
        ) {
            Text(">")
        }
    }
}
