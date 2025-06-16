package com.turskyi.malaknyzhka.models // Or your actual package, must match 'expect'

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.w3c.dom.get
import org.w3c.dom.set

private const val PREFERRED_LOCALE_KEY = "app_preferred_locale"
private const val PREFERRED_WASM_LOCALE_USER_SET_KEY =
    "app_preferred_wasm_locale_user_set"

// Actual implementation of AppLocaleManager for wasmJs.
class WasmJsAppLocaleManager : AppLocaleManager {

    override fun getLocale(): String {
        // 1. Check if user has ever set a language for this app
        val userHasSetLanguage: Boolean =
            localStorage[PREFERRED_WASM_LOCALE_USER_SET_KEY] == "true"

        if (!userHasSetLanguage) {
            // First launch or language never explicitly set by app/user: default to Ukrainian.
            // You might also want to consult browser language here as a *first-time default*
            // before any user interaction, but Ukrainian as a hard default is also valid.
            // For this example, let's stick to your explicit default for "not user set".
            return AppLang.Ukraine.code
        }

        // 2. User has set a language before (or we are programmatically setting it), so try to load it.
        val storedLocale: String? = localStorage[PREFERRED_LOCALE_KEY]
        if (!storedLocale.isNullOrBlank()) {
            return storedLocale.split("-").firstOrNull()
                ?: AppLang.Ukraine.code
        }

        // 3. Fallback: If flag is true but code is missing (should be rare).
        //    Or, if you decided above that !userHasSetLanguage should still check browser
        //    before falling to hardcoded default.
        //    For now, if userHasSetLanguage is true, we expect a storedLocale.
        //    If it's missing, falling back to a hardcoded default like Ukraine.code is safest.
        val browserLang: String = window.navigator.language
        return browserLang.split("-").firstOrNull()
            ?: AppLang.Ukraine.code
    }

    //    FIXME: this does not work.
    override fun setLocale(appLang: AppLang) {
        // 1. Store the preference in localStorage.
        localStorage[PREFERRED_LOCALE_KEY] = appLang.code
        // 2. Mark that language has been explicitly set by the user/app.
        localStorage[PREFERRED_WASM_LOCALE_USER_SET_KEY] = "true"
        // 3. Optionally, set the 'lang' attribute on the HTML document's root
        // element.
        document.documentElement?.setAttribute(
            "lang",
            appLang.code,
        )
    }

    override fun hasUserEverSetLanguage(): Boolean {
        // localStorage stores everything as strings.
        // Check if the value is exactly "true".
        return localStorage[PREFERRED_WASM_LOCALE_USER_SET_KEY] == "true"
    }
}

@Composable
actual fun rememberAppLocaleManager(): AppLocaleManager {
    return remember { WasmJsAppLocaleManager() }
}