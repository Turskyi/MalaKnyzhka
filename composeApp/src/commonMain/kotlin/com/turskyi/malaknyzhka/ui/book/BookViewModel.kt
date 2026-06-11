package com.turskyi.malaknyzhka.ui.book

import androidx.lifecycle.ViewModel
import com.turskyi.malaknyzhka.getCurrentTimeMillis
import com.turskyi.malaknyzhka.infrastructure.BookSpreadsRegistry
import com.turskyi.malaknyzhka.models.BookRepository
import com.turskyi.malaknyzhka.models.Bookmark
import com.turskyi.malaknyzhka.models.BookmarkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource

class BookViewModel(
    private val bookRepository: BookRepository,
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {
    private val _currentPage: MutableStateFlow<Int> =
        MutableStateFlow(bookRepository.getCurrentPage())
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    val bookSpreads: List<DrawableResource> = BookSpreadsRegistry.allBookSpreads

    private val _isDrawerOpen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    private val _isSearchOpen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _dividerPosition: MutableStateFlow<Float> =
        MutableStateFlow(0.5f)
    val dividerPosition: StateFlow<Float> = _dividerPosition.asStateFlow()

    private val _isBookmarked: MutableStateFlow<Boolean> =
        MutableStateFlow(bookmarkRepository.isBookmarked(_currentPage.value))
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    // Constants for divider limits.
    private val maxTopFraction = 0.025f
    private val minBottomFraction = 0.94f

    fun onNewPage(newPage: Int) {
        bookRepository.saveCurrentPage(newPage)
        _currentPage.value = newPage
        _isBookmarked.value = bookmarkRepository.isBookmarked(newPage)
    }

    fun setDrawerOpen(isOpen: Boolean) {
        _isDrawerOpen.value = isOpen
    }

    fun setSearchOpen(isOpen: Boolean) {
        _isSearchOpen.value = isOpen
    }

    fun toggleBookmark() {
        val page: Int = _currentPage.value
        if (bookmarkRepository.isBookmarked(page)) {
            bookmarkRepository.removeBookmark(page)
        } else {
            bookmarkRepository.addBookmark(
                Bookmark(
                    page,
                    getCurrentTimeMillis(),
                ),
            )
        }
        _isBookmarked.value = bookmarkRepository.isBookmarked(page)
    }

    fun onDividerPositionChange(newPosition: Float) {
        _dividerPosition.value = newPosition.coerceIn(
            maxTopFraction,
            minBottomFraction,
        )
    }

    fun onLanguageChange(langCode: String) {
        bookRepository.saveCurrentLanguage(langCode)
    }
}
