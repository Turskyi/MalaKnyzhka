package com.turskyi.malaknyzhka.util

import com.turskyi.malaknyzhka.models.AppLang

// Make it nullable to handle potential null strings.
fun String?.toApLang(): AppLang {
    return AppLang.fromLanguageTag(this)
}