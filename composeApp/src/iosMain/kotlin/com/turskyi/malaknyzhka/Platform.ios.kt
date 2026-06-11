package com.turskyi.malaknyzhka

import com.turskyi.malaknyzhka.models.PlatformType
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

class IOSPlatform: Platform {
    override val type: PlatformType = PlatformType.IOS
    override val initialRoute: String? = null
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun getCurrentTimeMillis(): Long =
    (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun formatTimestamp(timestamp: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd.MM.yyyy HH:mm"
    return formatter.stringFromDate(date)
}