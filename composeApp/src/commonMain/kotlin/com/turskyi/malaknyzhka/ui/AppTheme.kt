package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AppTheme(content: @Composable () -> Unit) {
    val customColors: Colors = lightColors(
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