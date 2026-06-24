package com.turskyi.malaknyzhka.infrastructure

import com.turskyi.malaknyzhka.createDataStore
import com.turskyi.malaknyzhka.models.SettingsBookRepository
import com.turskyi.malaknyzhka.models.WidgetData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * A bridge to provide widget data to Swift code.
 */
object WidgetBridge {
    fun getWidgetData(completion: (WidgetData?) -> Unit) {
        MainScope().launch {
            val bookRepository = SettingsBookRepository(createDataStore())
            val data = WidgetDataProvider.getWidgetData(bookRepository)
            completion(data)
        }
    }
}
