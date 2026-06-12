package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType

class JVMPlatform: Platform {
    override val type: PlatformType = PlatformType.DESKTOP
    override val initialRoute: String? = null
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

actual fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat(
        "dd.MM.yyyy HH:mm",
        java.util.Locale.getDefault(),
    )
    return format.format(date)
}