package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun ZoomableImage(imageResource: DrawableResource) {
    // State for zoom level and offset.
    val scale: MutableState<Float> = remember { mutableStateOf(1f) }
    val offset: MutableState<Offset> = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    // Define minimum and maximum scale factors.
    // Prevent zooming out further than screen width.
    val minScale = 1f
    // Maximum zoom level.
    val maxScale = 20f

    val transformableState: TransformableState =
        rememberTransformableState { zoomChange: Float, panChange: Offset, _ ->
            val newScale =
                (scale.value * zoomChange).coerceIn(minScale, maxScale)
            scale.value = newScale
            offset.value = if (newScale == minScale || newScale == maxScale) {
                Offset.Zero
            } else {
                offset.value + panChange
            }
        }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFf0e7d8))
            // Handles zoom and pan gestures.
            .transformable(state = transformableState)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        // Reset zoom on double-tap.
                        scale.value = 1f
                        offset.value = Offset.Zero
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = "Розворот книги",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                )
                .fillMaxSize()
        )
    }
}
