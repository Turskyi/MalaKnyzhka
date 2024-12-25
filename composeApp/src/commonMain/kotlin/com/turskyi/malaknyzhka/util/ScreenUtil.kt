package com.turskyi.malaknyzhka.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.WindowInfo


@Composable
fun rememberWindowSize(): WindowInfo {
    val density: Density = LocalDensity.current
    var windowInfo: WindowInfo by remember { mutableStateOf(WindowInfo(0.dp)) }

    BoxWithConstraints {
        with(density) {
            windowInfo = WindowInfo(
                screenWidth = constraints.maxWidth.toDp()
            )
        }
    }

    return windowInfo
}








