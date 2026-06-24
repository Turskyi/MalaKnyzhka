package com.turskyi.malaknyzhka.infrastructure

import platform.Foundation.NSLog

var onUpdateWidgetsCallback: (() -> Unit)? = null

actual fun updateWidgets() {
    NSLog("WidgetUpdate: Triggering widget reload")
    onUpdateWidgetsCallback?.invoke()
}
