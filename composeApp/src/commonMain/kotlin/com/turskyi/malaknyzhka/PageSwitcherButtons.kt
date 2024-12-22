import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageSwitcherButtons(
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    totalPages: Int
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                onPageChange((currentPage - 1).coerceAtLeast(0))
            }
        ) {
            Text("Попередня сторінка")
        }

        Button(onClick = {
            onPageChange((currentPage + 1).coerceAtMost(totalPages - 1))
        }) {
            Text("Наступна сторінка")
        }
    }
}
