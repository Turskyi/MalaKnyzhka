package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class AndroidPlatform(override val initialRoute: String? = null) : Platform {
    override val type: PlatformType = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

actual fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat(
        "dd.MM.yyyy HH:mm",
        java.util.Locale.getDefault(),
    )
    return format.format(date)
}