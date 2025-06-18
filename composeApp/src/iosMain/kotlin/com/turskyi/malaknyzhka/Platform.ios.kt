package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class IOSPlatform: Platform {
    override val type: PlatformType = PlatformType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()