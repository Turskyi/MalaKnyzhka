package com.turskyi.malaknyzhka.models

import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.en
import malaknyzhka.composeapp.generated.resources.uk
import org.jetbrains.compose.resources.StringResource

enum class AppLang(
    val code: String,
    val stringRes: StringResource
) {
    English("en", Res.string.en),
    Ukraine("uk", Res.string.uk)
}
