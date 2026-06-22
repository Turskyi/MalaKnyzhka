package com.turskyi.malaknyzhka.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.getCurrentTimeMillis
import com.turskyi.malaknyzhka.infrastructure.BookContentRegistry
import com.turskyi.malaknyzhka.infrastructure.BookSpreadsRegistry
import com.turskyi.malaknyzhka.infrastructure.StringResourceResolver
import com.turskyi.malaknyzhka.infrastructure.TextToSpeech
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.BookRepository
import com.turskyi.malaknyzhka.models.Bookmark
import com.turskyi.malaknyzhka.models.BookmarkRepository
import com.turskyi.malaknyzhka.share.ShareManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

class BookViewModel(
    private val bookRepository: BookRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val textToSpeech: TextToSpeech,
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

    val isSpeaking: StateFlow<Boolean> = textToSpeech.isSpeaking

    // Constants for divider limits.
    private val maxTopFraction = 0.025f
    private val minBottomFraction = 0.94f

    fun onNewPage(newPage: Int) {
        textToSpeech.stop()
        bookRepository.saveCurrentPage(newPage)
        _currentPage.value = newPage
        _isBookmarked.value = bookmarkRepository.isBookmarked(
            pageNumber = newPage,
        )
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

    fun toggleSpeech(text: String, languageCode: String) {
        if (isSpeaking.value) {
            textToSpeech.stop()
        } else {
            textToSpeech.speak(text, languageCode)
        }
    }

    fun stopSpeech() {
        textToSpeech.stop()
    }

    fun isTtsAvailable(): Boolean {
        return textToSpeech.isLanguageAvailable()
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

    fun shareTranscription(
        shareManager: ShareManager,
        pageNumber: Int,
        transcription: String,
        appLang: AppLang,
        title: String,
        pageLabel: String,
        fromLabel: String,
        ukrainianLabel: String,
        englishLabel: String,
        copiedLabel: String,
    ) {
        viewModelScope.launch {
            val formattedText = if (appLang == AppLang.English) {
                // If English, include both Ukrainian and English
                val ukrainianText = StringResourceResolver.getStringInUkrainian(
                    BookContentRegistry.allPoemPages[pageNumber]
                )
                buildString {
                    appendLine(title)
                    appendLine()
                    appendLine(pageLabel)
                    appendLine()
                    appendLine(ukrainianLabel)
                    appendLine("“$ukrainianText”")
                    appendLine()
                    appendLine(englishLabel)
                    appendLine("“$transcription”")
                    appendLine()
                    appendLine(fromLabel)
                }
            } else {
                buildString {
                    appendLine(title)
                    appendLine()
                    appendLine(pageLabel)
                    appendLine()
                    appendLine("“$transcription”")
                    appendLine()
                    appendLine(fromLabel)
                }
            }
            // For desktop/wasm fallback, passing the toast message
            shareManager.shareText(formattedText, title, copiedLabel)
        }
    }

    override fun onCleared() {
        textToSpeech.stop()
        super.onCleared()
    }
}
