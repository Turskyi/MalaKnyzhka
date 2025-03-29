package com.turskyi.malaknyzhka.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ScreenshotCarousel(
    screenshots: List<DrawableResource>,
    ratio: Float,
    height: Dp,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(screenshots.size) { index: Int ->
            val screenshotResId: DrawableResource = screenshots[index]
            Image(
                painter = painterResource(screenshotResId),
                contentDescription = "Screenshot",
                modifier = Modifier
                    .height(height = height)
                    .aspectRatio(ratio = ratio)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    )
            )
        }
    }
}