package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turskyi.malaknyzhka.infrastructure.SearchIndex
import com.turskyi.malaknyzhka.models.SearchResult
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.clear_search
import malaknyzhka.composeapp.generated.resources.no_results
import malaknyzhka.composeapp.generated.resources.search_description
import malaknyzhka.composeapp.generated.resources.search_hint
import malaknyzhka.composeapp.generated.resources.search_results
import org.jetbrains.compose.resources.stringResource

/**
 * A search panel that allows users to search through the transcribed
 * manuscript.
 */
@Composable
fun SearchPanel(
    onClose: () -> Unit,
    onResultClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchIndex = remember { SearchIndex() }
    var query by remember { mutableStateOf("") }
    var results by remember {
        mutableStateOf<List<SearchResult>>(
            emptyList()
        )
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(query) {
        results = searchIndex.search(query)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(
                        Res.string.search_description,
                    ),
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.search_hint),
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(
                            MaterialTheme.colors.primary,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                        )
                    )
                }

                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { query = "" },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(
                                Res.string.clear_search,
                            ),
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Results List
            Box(modifier = Modifier.weight(1f)) {
                if (query.isNotEmpty()) {
                    if (results.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.no_results),
                                style = MaterialTheme.typography.body1,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Text(
                                    text = stringResource(
                                        Res.string.search_results,
                                        results.size
                                    ),
                                    style = MaterialTheme.typography.caption,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                    color = Color.Gray
                                )
                            }
                            items(results) { result ->
                                SearchResultItem(
                                    result = result,
                                    onClick = {
                                        onResultClick(result.pageIndex)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Close Button at Bottom (optional, or rely on Top bar)
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}
