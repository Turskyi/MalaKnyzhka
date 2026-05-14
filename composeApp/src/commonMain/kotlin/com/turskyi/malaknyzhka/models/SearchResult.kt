package com.turskyi.malaknyzhka.models

/**
 * Represents a single search match.
 *
 * @property pageIndex The index of the page in the book.
 * @property pageLabel The displayable label of the page (e.g., "Page 13").
 * @property snippet A short piece of text surrounding the match for context.
 * @property matchedRange The range within the [snippet] where the query was found.
 */
data class SearchResult(
    val pageIndex: Int,
    val pageLabel: String,
    val snippet: String,
    val matchedRange: IntRange,
)
