package com.turskyi.malaknyzhka.models

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.os.LocaleListCompat
import java.util.Locale

private const val PREF_APP_LANGUAGE_CODE = "app_language_code"
private const val PREF_APP_LANGUAGE_USER_SET = "app_language_user_set"

class AndroidAppLocaleManager(
    private val context: Context,
) : AppLocaleManager {
    // Use default SharedPreferences for these app-wide flags.
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(
            context,
        )

    override fun getLocale(): String {
        // Check if user has ever set a language for this app.
        val userHasSetLanguage: Boolean = sharedPreferences.getBoolean(
            PREF_APP_LANGUAGE_USER_SET,
            false,
        )

        if (!userHasSetLanguage) {
            // First launch or language never explicitly set by app/user:
            // default to Ukrainian.
            return AppLang.Ukraine.code
        }

        // User has set a language before, or we are setting it, so try to
        // load it.
        val savedLangCode: String? = sharedPreferences.getString(
            PREF_APP_LANGUAGE_CODE,
            AppLang.Ukraine.code,
        )
        if (savedLangCode != null) {
            // Use app's saved preference.
            return savedLangCode
        }

        // Fallback to system language if somehow flag is set but code is not (shouldn't happen with correct setLocale)
        // Or if you want system to be the initial source AFTER the first default override
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager: LocaleManager? =
                context.getSystemService<LocaleManager>()
            val locales: LocaleList? = localeManager?.applicationLocales
            if (locales == null || locales.isEmpty) AppLang.Ukraine.code
            else
                locales[0]?.toLanguageTag()?.split("-")?.firstOrNull()
                    ?: AppLang.Ukraine.code
        } else {
            AppCompatDelegate.getApplicationLocales()
                .toLanguageTags().split(",")
                .firstOrNull()?.split("-")?.firstOrNull()
                ?: AppLang.Ukraine.code
        }
    }

    override fun setLocale(appLang: AppLang) {
        // Save the chosen language.
        sharedPreferences.edit {
            putString(PREF_APP_LANGUAGE_CODE, appLang.code)
                .putBoolean(
                    PREF_APP_LANGUAGE_USER_SET,
                    // Mark that language has been set.
                    true,
                )
        }
        val locale: Locale = Locale.forLanguageTag(appLang.code)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager: LocaleManager? =
                context.getSystemService<LocaleManager>()
            // It's crucial that this context is an Activity or Application context
            // that can handle the configuration change.
            localeManager?.applicationLocales = LocaleList(locale)
        } else {
            // For older versions, use LocaleListCompat with AppCompatDelegate
            val localeListCompat: LocaleListCompat = LocaleListCompat.create(
                locale,
            )
            AppCompatDelegate.setApplicationLocales(localeListCompat)
        }
    }

    override fun hasUserEverSetLanguage(): Boolean {
        return sharedPreferences.getBoolean(PREF_APP_LANGUAGE_USER_SET, false)
    }
}

@Composable
actual fun rememberAppLocaleManager(): AppLocaleManager {
    val context: Context = LocalContext.current
    return remember { AndroidAppLocaleManager(context) }
}