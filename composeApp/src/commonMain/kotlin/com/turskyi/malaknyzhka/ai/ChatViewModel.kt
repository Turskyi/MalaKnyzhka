package com.turskyi.malaknyzhka.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.ai.models.ChatMessage
import com.turskyi.malaknyzhka.ai.models.MessageRole
import com.turskyi.malaknyzhka.share.ShareManager
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

    private val _isExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded.asStateFlow()

    fun toggleExpanded() {
        _isExpanded.value = !_isExpanded.value
    }

    fun setExpanded(expanded: Boolean) {
        _isExpanded.value = expanded
    }

    fun sendMessage(
        text: String,
        pageNumber: Int?,
        pageText: String?,
    ) {
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.sendMessage(text, pageNumber, pageText)
            } catch (e: Exception) {
                // If the repository's internal catch failed for some reason, 
                // we catch it here to ensure the UI doesn't hang.
                println("ViewModel caught error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun shareConversation(
        shareManager: ShareManager,
        title: String,
        userName: String,
        tarasName: String,
        fromLabel: String
    ) {
        val formattedText =
            formatConversation(title, userName, tarasName, fromLabel)
        if (formattedText.isNotEmpty()) {
            shareManager.shareText(formattedText, title)
        }
    }

    fun copyConversation(
        shareManager: ShareManager,
        title: String,
        userName: String,
        tarasName: String,
        fromLabel: String,
        toastMessage: String
    ) {
        val formattedText =
            formatConversation(title, userName, tarasName, fromLabel)
        if (formattedText.isNotEmpty()) {
            shareManager.copyToClipboard(formattedText, toastMessage)
        }
    }

    private fun formatConversation(
        title: String,
        userName: String,
        tarasName: String,
        fromLabel: String
    ): String {
        val history = messages.value
        if (history.isEmpty()) return ""

        return buildString {
            appendLine(title)
            appendLine()
            history.forEach { msg ->
                val roleName =
                    if (msg.role == MessageRole.USER) userName else tarasName
                appendLine(roleName)
                appendLine(msg.text)
                appendLine()
            }
            appendLine(fromLabel)
        }
    }
}
