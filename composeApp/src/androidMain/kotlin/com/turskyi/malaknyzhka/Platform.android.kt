package com.turskyi.malaknyzhka

import android.content.ComponentName
import android.content.pm.PackageManager
import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.PlatformType

class AndroidPlatform(override val initialRoute: String? = null) : Platform {
    override val type: PlatformType = PlatformType.ANDROID

    companion object {
        var pendingExperience: Experience? = null
    }

    override fun syncLauncherIcon(experience: Experience, immediate: Boolean) {
        if (!immediate) {
            pendingExperience = experience
            return
        }

        val context = AppContext.context ?: return
        val packageManager = context.packageManager
        val packageName = context.packageName

        val bookAlias = ComponentName(packageName, "$packageName.MainActivityBook")
        val tarasAlias = ComponentName(packageName, "$packageName.MainActivityTaras")

        val (enable, disable) = when (experience) {
            Experience.BOOK -> bookAlias to tarasAlias
            Experience.TARAS -> tarasAlias to bookAlias
        }

        // Only update if the target component is not already enabled
        val currentState = packageManager.getComponentEnabledSetting(enable)
        if (currentState != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // Enable the new alias first to avoid having no launcher entry
            packageManager.setComponentEnabledSetting(
                enable,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            packageManager.setComponentEnabledSetting(
                disable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            // Clear pending after immediate sync
            pendingExperience = null
        }
    }
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
