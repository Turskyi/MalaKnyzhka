package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
    override val initialRoute: String
        get() = window.location.hash.removePrefix("#")
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
