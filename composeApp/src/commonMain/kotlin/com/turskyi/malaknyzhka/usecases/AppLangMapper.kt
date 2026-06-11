package com.turskyi.malaknyzhka.usecases

import com.turskyi.malaknyzhka.models.AppLang

/**
 * Maps a language tag to [AppLang].
 * Handling potential null strings by returning [AppLang.DEFAULT].
 */
fun String?.toApLang(): AppLang {
    return AppLang.fromLanguageTag(this)
}
