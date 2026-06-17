package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turskyi.malaknyzhka.models.ThemeMode

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDark: Boolean = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val lightColors: Colors = lightColors(
        primary = Color(0xFFFF6F00), // Amber
        primaryVariant = Color(0xFFEF5100), // Deep orange
        secondary = Color(0xFFFF7043), // Soft Coral
        secondaryVariant = Color(0xFFDD2C00), // Darker Coral
        background = Color(0xFFFAF1E6), // Light beige
        surface = Color(0xFFFFFFFF), // White surface
        error = Color(0xFFD32F2F), // Red for errors
        onPrimary = Color.White, // White text on primary
        onSecondary = Color.Black, // Black text on secondary
        onBackground = Color(0xFF5D4037), // Dark brown text
        onSurface = Color(0xFF5D4037), // Dark brown text
        onError = Color.White // White text on errors
    )

    val darkColors: Colors = darkColors(
        primary = Color(0xFFFFB300), // Lighter Amber
        primaryVariant = Color(0xFFFF6F00), // Amber
        secondary = Color(0xFFFF8A65), // Lighter Coral
        background = Color(0xFF212121), // Dark grey
        surface = Color(0xFF333333), // Slightly lighter grey
        error = Color(0xFFCF6679), // Standard dark error color
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color(0xFFE0E0E0), // Light grey text
        onSurface = Color(0xFFE0E0E0), // Light grey text
        onError = Color.Black
    )

    val customColors: Colors = if (isDark) darkColors else lightColors

    val customTypography = Typography(
        h1 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp),
        h2 = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
        body1 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
        body2 = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp)
    )

    val customShapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(32.dp)
    )

    MaterialTheme(
        colors = customColors,
        typography = customTypography,
        shapes = customShapes,
        content = content
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
fun AppThemePreview() {
    AppTheme {
        // Preview content
    }
}
