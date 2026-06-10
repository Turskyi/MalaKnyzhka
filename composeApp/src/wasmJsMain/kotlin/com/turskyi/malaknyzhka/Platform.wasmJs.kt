package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
    override val initialRoute: String
        get() = window.location.hash.removePrefix("#")
}

actual fun getPlatform(): Platform = WasmPlatform()
