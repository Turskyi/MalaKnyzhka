package com.turskyi.malaknyzhka.share

import kotlinx.coroutines.flow.SharedFlow

interface ShareManager {
    val toastMessages: SharedFlow<String>
    fun shareText(
        text: String,
        title: String? = null,
        toastMessage: String? = null
    )

    fun copyToClipboard(text: String, toastMessage: String? = null)
}
