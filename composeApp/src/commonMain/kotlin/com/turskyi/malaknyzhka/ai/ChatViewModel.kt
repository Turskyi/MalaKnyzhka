package com.turskyi.malaknyzhka.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.ai.models.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val messages: StateFlow<List<ChatMessage>> = repository.messages
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var currentPageNumber: Int? = null
    var currentPageText: String? = null

    fun sendMessage(
        text: String,
        pageNumber: Int?,
        pageText: String?,
    ) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            repository.sendMessage(text, pageNumber, pageText)
            _isLoading.value = false
        }
    }
}
