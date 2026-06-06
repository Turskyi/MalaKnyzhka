package com.turskyi.malaknyzhka.models

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WindowInfo(val screenWidth: Dp)

val LocalWindowInfo: ProvidableCompositionLocal<WindowInfo> =
    compositionLocalOf { WindowInfo(0.dp) }
