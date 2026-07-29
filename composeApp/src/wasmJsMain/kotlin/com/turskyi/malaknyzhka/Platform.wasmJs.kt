package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.PlatformType

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
    override val initialRoute: String
        get() = window.location.pathname.removePrefix("/")
    override fun syncLauncherIcon(experience: Experience, immediate: Boolean) {
        // WASM does not support dynamic icons via activity aliases.
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
