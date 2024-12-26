package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class WasmPlatform : Platform {
    override val type: PlatformType = PlatformType.WEB
}

actual fun getPlatform(): Platform = WasmPlatform()
