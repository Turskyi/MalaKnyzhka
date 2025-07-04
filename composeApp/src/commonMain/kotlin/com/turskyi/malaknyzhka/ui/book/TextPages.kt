package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.turskyi.malaknyzhka.infrastructure.BookContentRegistry
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TextPages(currentPage: Int) {
    val poemPages: List<StringResource> = BookContentRegistry.allPoemPages

    val scrollState: ScrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        SelectionContainer {
            Text(
                text = stringResource(poemPages[currentPage]),
                textAlign = TextAlign.Justify
            )
        }
    }
}

