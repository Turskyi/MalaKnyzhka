package com.turskyi.malaknyzhka.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AndroidShareManager(private val context: Context) : ShareManager {
    private val _toastMessages =
        MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val toastMessages: SharedFlow<String> =
        _toastMessages.asSharedFlow()

    override fun shareText(
        text: String,
        title: String?,
        toastMessage: String?
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            title?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val shareIntent = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }

    override fun copyToClipboard(text: String, toastMessage: String?) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Mala Knyzhka", text)
        clipboard.setPrimaryClip(clip)
        toastMessage?.let { _toastMessages.tryEmit(it) }
    }
}
