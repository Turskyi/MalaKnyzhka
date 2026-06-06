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
    val shortName: String,
    val stringRes: StringResource,
    val imageRes: DrawableResource,
) {
    English(
        code = "en",
        shortName = "EN",
        stringRes = Res.string.en,
        imageRes = Res.drawable.english,
    ),
    Ukraine(
        code = "uk",
        shortName = "УК",
        stringRes = Res.string.uk,
        imageRes = Res.drawable.ukrainian,
    );

    fun toggle(): AppLang {
        return if (this == English) Ukraine else English
    }

    companion object {
        val DEFAULT: AppLang = Ukraine

        fun fromLanguageTag(tag: String?): AppLang {
            val parsedTag: String? = tag?.lowercase()?.split("-")?.firstOrNull()
            return entries.find { it.code == parsedTag } ?: DEFAULT
        }
    }
}
