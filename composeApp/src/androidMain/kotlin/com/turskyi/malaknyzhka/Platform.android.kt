package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class AndroidPlatform : Platform {
    override val type: PlatformType = PlatformType.MOBILE
}

actual fun getPlatform(): Platform = AndroidPlatform()