package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

interface Platform {
    val type: PlatformType
}

expect fun getPlatform(): Platform
