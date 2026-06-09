package com.turskyi.malaknyzhka.ui.book

import androidx.lifecycle.ViewModel
import com.turskyi.malaknyzhka.models.BookSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookViewModel(private val bookSettings: BookSettingsRepository) :
    ViewModel() {
    private val _currentPage: MutableStateFlow<Int> =
        MutableStateFlow(bookSettings.getCurrentPage())
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _isDrawerOpen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    private val _isSearchOpen: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isSearchOpen: StateFlow<Boolean> = _isSearchOpen.asStateFlow()

    private val _dividerPosition: MutableStateFlow<Float> =
        MutableStateFlow(0.5f)
    val dividerPosition: StateFlow<Float> = _dividerPosition.asStateFlow()

    // Constants for divider limits.
    private val maxTopFraction = 0.025f
    private val minBottomFraction = 0.94f

    fun onNewPage(newPage: Int) {
        bookSettings.saveCurrentPage(newPage)
        _currentPage.value = newPage
    }

    fun setDrawerOpen(isOpen: Boolean) {
        _isDrawerOpen.value = isOpen
    }

    fun setSearchOpen(isOpen: Boolean) {
        _isSearchOpen.value = isOpen
    }

    fun onDividerPositionChange(newPosition: Float) {
        _dividerPosition.value = newPosition.coerceIn(
            maxTopFraction,
            minBottomFraction,
        )
    }

    fun onLanguageChange(langCode: String) {
        bookSettings.saveCurrentLanguage(langCode)
    }
}
