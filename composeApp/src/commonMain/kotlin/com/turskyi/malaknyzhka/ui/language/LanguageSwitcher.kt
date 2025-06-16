package com.turskyi.malaknyzhka.ui.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSwitcher(
    currentLanguage: AppLang,
    onLanguageChange: (AppLang) -> Unit
) {
    Column {
        Text(
            text = "Мова / Language",
            fontWeight = Bold,
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .background(
                    // Semi-transparent black background.
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
        Spacer(Modifier.height(8.dp))
        Row {
            LanguageChip(
                label = stringResource(AppLang.Ukraine.stringRes),
                icon = AppLang.Ukraine.imageRes,
                selected = currentLanguage == AppLang.Ukraine,
                onClick = { onLanguageChange(AppLang.Ukraine) }
            )
            Spacer(Modifier.width(8.dp))
            LanguageChip(
                label = stringResource(AppLang.English.stringRes),
                icon = AppLang.English.imageRes,
                selected = currentLanguage == AppLang.English,
                onClick = { onLanguageChange(AppLang.English) }
            )
        }
    }
}