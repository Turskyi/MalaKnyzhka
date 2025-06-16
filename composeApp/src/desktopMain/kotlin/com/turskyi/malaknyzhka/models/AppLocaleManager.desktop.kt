package com.turskyi.malaknyzhka.models


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.Locale
import java.util.prefs.Preferences

// Preference key for storing the user's chosen language code on Desktop.
private const val PREFERRED_DESKTOP_LOCALE_CODE_KEY =
    "app_preferred_desktop_locale_code"

// Preference key for storing whether the user has ever explicitly set the language on Desktop.
private const val PREFERRED_DESKTOP_LOCALE_USER_SET_KEY =
    "app_preferred_desktop_locale_user_set"

// Actual implementation of the AppLocaleManager for Desktop.
class DesktopAppLocaleManager : AppLocaleManager {
    // Get a Preferences node unique to this application class (or a specific path).
    // This helps avoid collisions with other applications.
    private val prefs: Preferences =
        Preferences.userNodeForPackage(DesktopAppLocaleManager::class.java)

    override fun getLocale(): String {
        // 1. Check if user has ever set a language within the app
        val userHasSetLanguage = prefs.getBoolean(
            PREFERRED_DESKTOP_LOCALE_USER_SET_KEY,
            false,
        )

        if (userHasSetLanguage) {
            // 2. If yes, try to get their stored preference
            // The second argument to get() is the default value if the key is not found.
            val storedLocale: String? = prefs.get(
                PREFERRED_DESKTOP_LOCALE_CODE_KEY,
                AppLang.Ukraine.code,
            )
            if (!storedLocale.isNullOrBlank()) {
                return storedLocale.split("-").firstOrNull()
                    ?: AppLang.Ukraine.code
            }
            // If flag is true but code is missing/blank (should be rare if setLocale is correct),
            // fall through to JVM default.
        }

        // Get the JVM's default locale's language tag.
        return Locale.getDefault().language.split("-").firstOrNull()
            ?: AppLang.Ukraine.code
    }

    override fun setLocale(appLang: AppLang) {
        // 1. Store the user's chosen language code.
        prefs.put(PREFERRED_DESKTOP_LOCALE_CODE_KEY, appLang.code)

        // 2. Store the flag indicating the user has explicitly made a choice.
        prefs.putBoolean(PREFERRED_DESKTOP_LOCALE_USER_SET_KEY, true)

        // Set the JVM's default locale.
        // This affects new Locale.getDefault() calls within the current application instance.
        // Note: Changing this might have broader effects than just Compose Resources on Desktop,
        // as it changes the default Locale for the entire JVM process.
        val newLocale: Locale? = Locale.forLanguageTag(appLang.code)
        if (newLocale != null) { // Check if forLanguageTag returned a valid Locale
            Locale.setDefault(newLocale)
        } else {
            // Fallback or log if the language code couldn't be resolved to a
            // Locale.
            Locale.setDefault(Locale.forLanguageTag(AppLang.Ukraine.code))
        }
    }

    override fun hasUserEverSetLanguage(): Boolean {
        // Retrieve the boolean flag from Preferences.
        // The second argument to getBoolean is the default value if the key is
        // not found.
        return prefs.getBoolean(PREFERRED_DESKTOP_LOCALE_USER_SET_KEY, false)
    }
}

@Composable
actual fun rememberAppLocaleManager(): AppLocaleManager {
    return remember { DesktopAppLocaleManager() }
}