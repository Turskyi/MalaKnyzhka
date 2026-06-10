package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class AndroidPlatform : Platform {
    override val type: PlatformType = PlatformType.ANDROID
    override val initialRoute: String? = null
}

actual fun getPlatform(): Platform = AndroidPlatform()