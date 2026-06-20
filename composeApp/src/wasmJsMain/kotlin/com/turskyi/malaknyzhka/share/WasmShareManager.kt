package com.turskyi.malaknyzhka.share

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WasmShareManager : ShareManager {
    private val _toastMessages =
        MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val toastMessages: SharedFlow<String> =
        _toastMessages.asSharedFlow()

    override fun shareText(
        text: String,
        title: String?,
        toastMessage: String?
    ) {
        if (canShare()) {
            shareNative(title ?: "", text)
            toastMessage?.let { _toastMessages.tryEmit(it) }
        } else {
            copyToClipboard(text, toastMessage ?: "Copied to clipboard")
        }
    }

    override fun copyToClipboard(text: String, toastMessage: String?) {
        copyToClipboardJs(text)
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => !!navigator.share")
private external fun canShare(): Boolean

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(title, text) => { navigator.share({ title: title, text: text }).catch(() => {}); }")
private external fun shareNative(title: String, text: String)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(text) => { navigator.clipboard.writeText(text).catch(() => {}); }")
private external fun copyToClipboardJs(text: String)
