import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.dawn
import malaknyzhka.composeapp.generated.resources.thoughts
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextPages(
    currentPage: Int
) {
    val texts = listOf(
        Res.string.thoughts,
        Res.string.dawn,
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(texts[currentPage]))
    }
}
