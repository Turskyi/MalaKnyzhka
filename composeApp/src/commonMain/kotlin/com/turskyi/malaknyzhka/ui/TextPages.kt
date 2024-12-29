import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources._001
import malaknyzhka.composeapp.generated.resources._002
import malaknyzhka.composeapp.generated.resources._003
import malaknyzhka.composeapp.generated.resources._004
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TextPages(currentPage: Int) {
    val texts: List<StringResource> = listOf(
        Res.string._001,
        Res.string._002,
        Res.string._003,
        Res.string._004,
    )

    val scrollState: ScrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        SelectionContainer {
            Text(
                text = stringResource(texts[currentPage]),
                textAlign = TextAlign.Justify
            )
        }
    }
}

