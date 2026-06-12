package com.turskyi.malaknyzhka.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.infrastructure.SearchIndex
import com.turskyi.malaknyzhka.models.SearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchViewModel : ViewModel() {
    private val searchIndex: SearchIndex = SearchIndex()

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results: MutableStateFlow<List<SearchResult>> =
        MutableStateFlow(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()

    init {
        _query.onEach { newQuery ->
            _results.value = searchIndex.search(newQuery)
        }.launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun clearQuery() {
        _query.value = ""
    }
}
