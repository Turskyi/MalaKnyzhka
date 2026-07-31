@file:OptIn(ExperimentalWasmJsInterop::class)

package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.PlatformType

import kotlinx.browser.window

@JsFun("(iconUrl) => updateFavicon(iconUrl)")
external fun updateFavicon(iconUrl: String)

class WasmPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
    override val initialRoute: String
        get() = window.location.pathname.removePrefix("/")

    override val hostname: String
        get() = window.location.hostname

    override fun syncLauncherIcon(
        experience: Experience,
        immediate: Boolean,
    ) {
        val iconUrl = when (experience) {
            Experience.BOOK -> "logo_book.png"
            Experience.TARAS -> "logo_taras.png"
        }
        updateFavicon(iconUrl)
    }
}

actual fun getPlatform(): Platform = WasmPlatform()

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => Date.now()")
external fun jsDateNow(): Double

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(timestamp) => new Date(timestamp).toLocaleString('uk-UA')")
external fun jsFormatDate(timestamp: Double): String

actual fun getCurrentTimeMillis(): Long = jsDateNow().toLong()

actual fun formatTimestamp(timestamp: Long): String {
    return jsFormatDate(timestamp.toDouble())
}
