package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turskyi.malaknyzhka.models.SearchResult
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.page_label
import org.jetbrains.compose.resources.stringResource

/**
 * Displays a single search result with highlighted matching text.
 */
@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.page_label, result.pageLabel),
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = buildHighlightedSnippet(
                snippet = result.snippet,
                range = result.matchedRange,
                highlightColor = MaterialTheme.colors.primary.copy(alpha = 0.3f)
            ),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(top = 4.dp),
            lineHeight = 20.sp,
            color = MaterialTheme.colors.onSurface
        )
    }
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
}

@Composable
private fun buildHighlightedSnippet(
    snippet: String,
    range: IntRange,
    highlightColor: Color
): AnnotatedString {
    return buildAnnotatedString {
        if (range.isEmpty() || range.first >= snippet.length || range.last < 0) {
            append(snippet)
            return@buildAnnotatedString
        }

        val safeStart: Int = range.firstOrNull()?.coerceAtLeast(
            0,
        ) ?: 0
        val safeEnd = (range.last + 1).coerceAtMost(
            snippet.length,
        )

        if (safeStart > 0) {
            append(snippet.substring(0, safeStart))
        }

        withStyle(
            style = SpanStyle(
                background = highlightColor,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(snippet.substring(safeStart, safeEnd))
        }

        if (safeEnd < snippet.length) {
            append(snippet.substring(safeEnd))
        }
    }
}
