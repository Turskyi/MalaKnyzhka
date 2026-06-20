package com.turskyi.malaknyzhka.share

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard
import platform.UIKit.UIWindow

class IosShareManager : ShareManager {
    private val _toastMessages =
        MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val toastMessages: SharedFlow<String> =
        _toastMessages.asSharedFlow()

    override fun shareText(
        text: String,
        title: String?,
        toastMessage: String?
    ) {
        val window = UIApplication.sharedApplication.keyWindow
            ?: UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow
        val rootViewController = window?.rootViewController
        val activityViewController =
            UIActivityViewController(listOf(text), null)
        rootViewController?.presentViewController(
            activityViewController,
            true,
            null
        )
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }

    override fun copyToClipboard(text: String, toastMessage: String?) {
        UIPasteboard.generalPasteboard.string = text
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }
}
