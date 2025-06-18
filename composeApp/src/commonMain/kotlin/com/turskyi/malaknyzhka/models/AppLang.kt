package com.turskyi.malaknyzhka.models

import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.en
import malaknyzhka.composeapp.generated.resources.english
import malaknyzhka.composeapp.generated.resources.uk
import malaknyzhka.composeapp.generated.resources.ukrainian
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class AppLang(
    val code: String,
    val stringRes: StringResource,
    val imageRes: DrawableResource
) {
    English(code = "en", Res.string.en, Res.drawable.english),
    Ukraine(code = "uk", Res.string.uk, Res.drawable.ukrainian);

    companion object {
        fun fromLanguageTag(tag: String?): AppLang {
            val parsedTag: String? = tag?.lowercase()?.split("-")?.firstOrNull()
            return entries.find { it.code == parsedTag } ?: Ukraine
        }
    }
}
