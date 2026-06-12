package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

interface Platform {
    val type: PlatformType
    val initialRoute: String?
}

expect fun getPlatform(): Platform

expect fun getCurrentTimeMillis(): Long

expect fun formatTimestamp(timestamp: Long): String
