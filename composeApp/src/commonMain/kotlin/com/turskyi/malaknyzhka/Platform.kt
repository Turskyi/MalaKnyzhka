package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.PlatformType

interface Platform {
    val type: PlatformType
    val initialRoute: String?
    val hostname: String?
    fun syncLauncherIcon(experience: Experience, immediate: Boolean = false)
}

expect fun getPlatform(): Platform

expect fun getCurrentTimeMillis(): Long

expect fun formatTimestamp(timestamp: Long): String
