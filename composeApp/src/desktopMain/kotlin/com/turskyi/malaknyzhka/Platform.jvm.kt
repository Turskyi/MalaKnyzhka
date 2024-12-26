package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class JVMPlatform: Platform {
    override val type: PlatformType = PlatformType.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()