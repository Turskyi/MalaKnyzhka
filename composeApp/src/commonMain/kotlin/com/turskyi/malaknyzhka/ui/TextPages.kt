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
import malaknyzhka.composeapp.generated.resources._002
import malaknyzhka.composeapp.generated.resources._003
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextPages(
    currentPage: Int
) {
    val texts: List<StringResource> = listOf(
        Res.string._002,
        Res.string._003,
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