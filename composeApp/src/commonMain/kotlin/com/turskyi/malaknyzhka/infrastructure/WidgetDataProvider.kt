package com.turskyi.malaknyzhka.infrastructure

import com.turskyi.malaknyzhka.models.BookRepository
import com.turskyi.malaknyzhka.models.WidgetData
import com.turskyi.malaknyzhka.usecases.toUserPageNumber

object WidgetDataProvider {
    suspend fun getWidgetData(bookRepository: BookRepository): WidgetData? {
        val currentPageIndex: Int = bookRepository.getCurrentPage()
        if (currentPageIndex < 0 || currentPageIndex >= BookContentRegistry.allPoemPages.size) {
            return null
        }
        val userPageNumber: Int = currentPageIndex.toUserPageNumber()
        val textResource = BookContentRegistry.allPoemPages[currentPageIndex]
        val fullText: String =
            StringResourceResolver.getStringInUkrainian(textResource)

        // Truncate to a reasonable excerpt length for the widget
        val excerpt: String = fullText.take(150).trim()
            .let { if (it.length < fullText.length) "$it…" else it }

        return WidgetData(
            pageNumber = userPageNumber,
            excerpt = excerpt
        )
    }
}
