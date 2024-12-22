import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DraggableDividerWithButton(
    modifier: Modifier = Modifier,
    dividerPosition: Float,
    screenHeightPx: Float,
    dividerHeightPx: Float,
    onPositionChange: (Float) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset {
                val offsetY = (dividerPosition * screenHeightPx).coerceIn(
                    0f,
                    // Prevent from going beyond the screen.
                    screenHeightPx - dividerHeightPx
                )
                IntOffset(0, offsetY.toInt() - (dividerHeightPx / 2).toInt())
            }
            // Combined height for button + divider.
            .height(50.dp)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    // Calculate the new position, ensuring it stays within the 0 to 1 range
                    val newPosition =
                        (dividerPosition + delta / screenHeightPx).coerceIn(
                            0f,
                            1f
                        )
                    onPositionChange(newPosition)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Divider Line
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colors.primary)
        )

        // Draggable Button
        Button(onClick = { /* No-op */ }) {
            Text(
                "⤒⤓",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }
}