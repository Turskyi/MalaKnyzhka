package com.turskyi.malaknyzhka.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DraggableDividerWithButton(
    modifier: Modifier = Modifier,
    dividerPosition: Float,
    screenHeightOrWidthPx: Float,
    dragOrientation: Orientation,
    onPositionChange: (Float) -> Unit,
    currentPage: Int,
    totalPages: Int,
) {
    val dividerSizePx: Float = with(LocalDensity.current) { 50.dp.toPx() }

    val offset: IntOffset = if (dragOrientation == Orientation.Vertical) {
        IntOffset(
            0, (dividerPosition * screenHeightOrWidthPx).coerceIn(
                0f,
                screenHeightOrWidthPx - dividerSizePx
            ).toInt() - (dividerSizePx / 2).toInt()
        )
    } else {
        IntOffset(
            (dividerPosition * screenHeightOrWidthPx).coerceIn(
                0f,
                screenHeightOrWidthPx - dividerSizePx
            ).toInt() - (dividerSizePx / 2).toInt(), 0
        )
    }

    Box(
        modifier = modifier
            .then(
                if (dragOrientation == Orientation.Vertical)
                    Modifier.fillMaxWidth().height(50.dp)
                else
                    Modifier.fillMaxHeight().width(50.dp)
            )
            .offset {
                offset
            }
            .draggable(
                orientation = dragOrientation,
                state = rememberDraggableState { delta: Float ->
                    val newPosition: Float =
                        (dividerPosition + delta / screenHeightOrWidthPx)
                            .coerceIn(
                                0f,
                                1f,
                            )
                    onPositionChange(newPosition)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Divider line
        Box(
            Modifier
                .then(
                    if (dragOrientation == Orientation.Vertical)
                        Modifier.fillMaxWidth().height(2.dp)
                    else
                        Modifier.fillMaxHeight().width(2.dp)
                )
                .background(MaterialTheme.colors.primary)
        )

        // Draggable Button
        Button(
            onClick = { /* No-op: purely decorative */ },
            modifier = Modifier.then(
                if (dragOrientation == Orientation.Horizontal)
                    Modifier.rotate(90f)
                else
                    Modifier.rotate(0f)
            )
        ) {
            Text(
                if (dragOrientation == Orientation.Horizontal)
                    "="
                else
                    "$currentPage / $totalPages",
                style = if (dragOrientation == Orientation.Horizontal)
                    MaterialTheme.typography.h6
                else
                    MaterialTheme.typography.button,
                color = Color.White,
            )
        }
    }
}



