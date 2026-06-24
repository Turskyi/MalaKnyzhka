package com.turskyi.malaknyzhka.infrastructure

import androidx.glance.appwidget.updateAll
import com.turskyi.malaknyzhka.AppContext
import com.turskyi.malaknyzhka.widget.ContinueReadingWidget
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Trigger update for Android Glance widgets.
 */
actual fun updateWidgets() {
    AppContext.context?.let { context ->
        MainScope().launch {
            ContinueReadingWidget().updateAll(context)
        }
    }
}
