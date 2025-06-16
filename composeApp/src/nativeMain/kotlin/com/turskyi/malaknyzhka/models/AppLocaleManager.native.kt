package com.turskyi.malaknyzhka.models // Or your actual package, must match 'expect'

import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages
import platform.Foundation.setValue

// Key for storing the user's preferred language in NSUserDefaults.
private const val PREFERRED_LOCALE_KEY = "app_preferred_locale"

// Key for storing whether the user has ever explicitly set the language.
private const val PREFERRED_NATIVE_LOCALE_USER_SET_KEY =
    "app_preferred_native_locale_user_set"


// Actual implementation of AppLocaleManager for native (iOS, macOS, etc.).
class NativeAppLocaleManager : AppLocaleManager {

    override fun getLocale(): String {
        // 1. Check NSUserDefaults for a user-set preference within the app.
        val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
        val storedLocale: String? = userDefaults.stringForKey(
            PREFERRED_LOCALE_KEY,
        )
        if (!storedLocale.isNullOrBlank()) {
            return storedLocale.split("-").firstOrNull() ?: AppLang.Ukraine.code
        }

        // 2. Get the app's current effective language based on its Info.plist localizations
        //    and the system's preferred languages.
        //    NSBundle.mainBundle.preferredLocalizations.firstOrNull() gives the best match
        //    from the app's available localizations.
        val appPreferredLocalization: String? =
            NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
        if (appPreferredLocalization != null && appPreferredLocalization != "Base") {
            return appPreferredLocalization.split("-").firstOrNull()
                ?: AppLang.Ukraine.code
        }

        // 3. Fallback: Get the system's preferred language
        //    NSLocale.preferredLanguages.firstOrNull() gives the device's top preferred language.
        val systemPreferredLanguage: String? =
            NSLocale.preferredLanguages.firstOrNull() as? String
        return systemPreferredLanguage?.split("-")?.firstOrNull()
        // Default.
            ?: AppLang.Ukraine.code
    }

    override fun setLocale(appLang: AppLang) {
        val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

        // 1. Store the preference in NSUserDefaults.
        //    This allows the app to remember the choice for next launch.
        userDefaults.setValue(appLang.code, forKey = PREFERRED_LOCALE_KEY)
        // Ensure it's written to disk.
        userDefaults.synchronize()

        // 2. IMPORTANT: Changing the language for an iOS/macOS app on the fly to make
        //    NSLocalizedString / system resource loading pick it up is complex.
        //    Simply setting a value in NSUserDefaults will NOT make the currently running
        //    app instance magically switch its resource language for system-level APIs
        //    like NSLocalizedString or Storyboard/XIB localization.
        //
        //    To make system resources reload, the app typically needs to be relaunched.
        //    The standard way iOS handles this is by the user changing the language in
        //    the device Settings, which then relaunches apps with the new locale.
        //
        //    For Compose Multiplatform resources (`stringResource` from org.jetbrains.compose.resources):
        //    a) Your Compose UI needs to recompose with the new `AppLang` state.
        //    b) The `org.jetbrains.compose.resources` library for native targets needs to be
        //       able to detect this change. It might honor the "AppleLanguages" user default
        //       upon next resource bundle lookup or require a more explicit re-initialization
        //       of its resource loading mechanism if you're not restarting the app.
        //
        //    The common pattern for in-app language switching on iOS without a full app restart
        //    (if you want immediate effect for your *own* resource system, like Compose resources) is:
        //    - Manage your own resource loading based on the selected `AppLang`.
        //    - The `OverrideLocale` or similar mechanism from the JetBrains article (if applicable
        //      to native targets) would be how you tell Compose's resource system which
        //      language to use from your available . kres files.
        //
        //    Setting "AppleLanguages" in NSUserDefaults is a hint for the system for
        //    future launches or for how bundles might be loaded.
        //    `userDefaults.setObject(listOf(appLang.lang), forKey = "AppleLanguages")`
        //    However, modifying "AppleLanguages" directly is often discouraged for sandboxed apps
        //    and might not have the immediate effect you want on the current session without a restart.

        // For Compose Multiplatform resources, the most reliable way to make `stringResource`
        // update is to ensure your `OverrideLocale` (or equivalent) logic receives the new
        // `AppLang`, and the resource loader underlying Compose resources for native
        // is set up to respect that.
        // The value stored in PREFERRED_LOCALE_KEY is primarily for your app to pick up
        // on next launch or if your `getLocale()` is called again.
    }

    override fun hasUserEverSetLanguage(): Boolean {
        // Retrieve the boolean flag from NSUserDefaults.
        // If the key doesn't exist, boolForKey defaults to 'false', which is correct.
        return NSUserDefaults.standardUserDefaults.boolForKey(
            PREFERRED_NATIVE_LOCALE_USER_SET_KEY,
        )
    }
}