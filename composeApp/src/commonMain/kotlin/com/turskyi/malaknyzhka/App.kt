import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.Page
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    settings: Settings
) {
    val customColors = lightColors(
        // Amber (bright and energetic).
        primary = Color(0xFFFF6F00),
        // Deep orange (more intense).
        primaryVariant = Color(0xFFEF5100),
        // Soft Coral (harmonizes with amber).
        secondary = Color(0xFFFF7043),
        // Darker Coral.
        secondaryVariant = Color(0xFFDD2C00),
        // Light beige background (soft, warm).
        background = Color(0xFFFAF1E6),
        // Clean white for surface elements.
        surface = Color(0xFFFFFFFF),
        // Red for error (stands out).
        error = Color(0xFFD32F2F),
        // White text on primary.
        onPrimary = Color.White,
        // Black text on secondary.
        onSecondary = Color.Black,
        // Dark brown text on light background.
        onBackground = Color(0xFF5D4037),
        // Dark brown text on white surface.
        onSurface = Color(0xFF5D4037),
        // White text on error color.
        onError = Color.White,
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
        shapes = customShapes
    ) {
        Page(settings)
    }
}