package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class AndroidPlatform : Platform {
    override val type: PlatformType = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()