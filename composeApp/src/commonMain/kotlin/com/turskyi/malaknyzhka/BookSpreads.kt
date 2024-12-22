import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources._002
import malaknyzhka.composeapp.generated.resources._003
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookSpreads(
    dividerPosition: Float,
    currentPage: Int,
    onPageChange: (Int) -> Unit,
    onDividerPositionChange: (Float) -> Unit
) {
    val dividerHeightPx = with(LocalDensity.current) { 50.dp.toPx() }
    val bookSpreads = listOf(
        Res.drawable._002,
        Res.drawable._003,
    )

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val screenHeightPx = with(LocalDensity.current) { maxHeight.toPx() }

        Box(Modifier.fillMaxSize()) {
            // Detect swipe gestures to switch pages
            Box(
                Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount < 0) { // Swiping left
                                onPageChange(
                                    (currentPage + 1).coerceAtMost(bookSpreads.size - 1)
                                )
                            } else if (dragAmount > 0) { // Swiping right
                                onPageChange((currentPage - 1).coerceAtLeast(0))
                            }
                        }
                    }
            ) {
                // Top section: Book spreads with background matching the page
                Box(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = dividerPosition)
                        .background(Color(0xFFf0e7d8)),
                    contentAlignment = Alignment.Center
                ) {
                    // Show the current book spread image
                    Image(
                        painter = painterResource(bookSpreads[currentPage]),
                        contentDescription = "Book Spread",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Position TextPages at the bottom
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxHeight(fraction = 1f - dividerPosition)
                ) {
                    TextPages(
                        currentPage = currentPage
                    )
                }

                // Draggable Divider with Button
                DraggableDividerWithButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    dividerPosition = dividerPosition,
                    screenHeightPx = screenHeightPx,
                    dividerHeightPx = dividerHeightPx,
                    onPositionChange = onDividerPositionChange
                )
            }
        }
    }
}
