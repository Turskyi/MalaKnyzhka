package com.turskyi.malaknyzhka.ui.book

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turskyi.malaknyzhka.formatTimestamp
import com.turskyi.malaknyzhka.infrastructure.BookContentRegistry
import com.turskyi.malaknyzhka.models.Bookmark
import com.turskyi.malaknyzhka.models.BookmarkRepository
import com.turskyi.malaknyzhka.usecases.toUserPageNumber
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.back_button_description
import malaknyzhka.composeapp.generated.resources.bookmarks
import malaknyzhka.composeapp.generated.resources.no_bookmarks
import malaknyzhka.composeapp.generated.resources.page_label
import malaknyzhka.composeapp.generated.resources.remove_bookmark
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookmarksPage(
    bookmarkRepository: BookmarkRepository,
    onBookmarkClick: (Int) -> Unit,
    onBack: () -> Unit,
) {
    var bookmarks: List<Bookmark> by remember {
        mutableStateOf(bookmarkRepository.getBookmarks())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back_button_description),
                    tint = MaterialTheme.colors.primary
                )
            }
            Text(
                text = stringResource(Res.string.bookmarks),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.no_bookmarks),
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(bookmarks) { bookmark ->
                    val pageText =
                        if (bookmark.pageNumber < BookContentRegistry.allPoemPages.size) {
                            stringResource(BookContentRegistry.allPoemPages[bookmark.pageNumber])
                        } else {
                            ""
                        }
                    val firstLine = pageText.lineSequence()
                        .map { it.trim() }
                        .firstOrNull { it.isNotEmpty() } ?: ""

                    BookmarkItem(
                        bookmark = bookmark,
                        previewText = firstLine,
                        onClick = { onBookmarkClick(bookmark.pageNumber) },
                        onDelete = {
                            bookmarkRepository.removeBookmark(bookmark.pageNumber)
                            bookmarks = bookmarkRepository.getBookmarks()
                        }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun BookmarkItem(
    bookmark: Bookmark,
    previewText: String,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val userPageNumber: Int = bookmark.pageNumber.toUserPageNumber()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = userPageNumber.toString(),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = stringResource(
                    Res.string.page_label,
                    userPageNumber.toString(),
                ),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium
            )
            if (previewText.isNotEmpty()) {
                Text(
                    text = previewText,
                    style = MaterialTheme.typography.body2,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = formatTimestamp(bookmark.timestamp),
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(Res.string.remove_bookmark),
                tint = Color.Gray
            )
        }
    }
}
