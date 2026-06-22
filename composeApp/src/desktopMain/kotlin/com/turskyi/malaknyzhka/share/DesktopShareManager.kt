package com.turskyi.malaknyzhka.share

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class DesktopShareManager : ShareManager {
    private val _toastMessages =
        MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val toastMessages: SharedFlow<String> =
        _toastMessages.asSharedFlow()

    override fun shareText(
        text: String,
        title: String?,
        toastMessage: String?
    ) {
        // Desktop sharing fallback to clipboard
        copyToClipboard(text, toastMessage ?: "Copied to clipboard")
    }

    override fun copyToClipboard(text: String, toastMessage: String?) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }
}
