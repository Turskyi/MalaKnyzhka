package com.turskyi.malaknyzhka.infrastructure

import com.turskyi.malaknyzhka.models.SearchResult
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

/**
 * Handles full-text search across the transcribed pages of the manuscript.
 */
class SearchIndex {
    private val pages: List<StringResource> = BookContentRegistry.allPoemPages
    private val pageCache = mutableMapOf<Int, String>()

    /**
     * Searches for the given [query] in all transcribed pages.
     *
     * @param query The text to search for.
     * @return A list of [SearchResult] objects.
     */
    suspend fun search(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()

        val results = mutableListOf<SearchResult>()
        val trimmedQuery = query.trim()

        pages.forEachIndexed { index, resource ->
            val content = getPageContent(index, resource)
            val matches = findAllMatches(content, trimmedQuery)

            matches.forEach { matchIndex ->
                results.add(
                    createSearchResult(
                        index,
                        content,
                        matchIndex,
                        trimmedQuery.length
                    )
                )
            }
        }

        return results
    }

    private suspend fun getPageContent(
        index: Int,
        resource: StringResource
    ): String {
        return pageCache.getOrPut(index) {
            getString(resource)
        }
    }

    private fun findAllMatches(content: String, query: String): List<Int> {
        val matches = mutableListOf<Int>()
        var index = content.indexOf(query, ignoreCase = true)
        while (index != -1) {
            matches.add(index)
            index = content.indexOf(query, index + 1, ignoreCase = true)
        }
        return matches
    }

    private fun createSearchResult(
        pageIndex: Int,
        content: String,
        matchIndex: Int,
        queryLength: Int
    ): SearchResult {
        val snippetRadius = 40
        val start = (matchIndex - snippetRadius).coerceAtLeast(0)
        val end =
            (matchIndex + queryLength + snippetRadius).coerceAtMost(
                content.length,
            )

        val rawSnippet =
            content.substring(start, end).replace(
                '\n', ' ',
            ).replace('\r', ' ')
        val snippet = rawSnippet.trim()
        val leadingTrimmed = rawSnippet.length - rawSnippet.trimStart().length

        // Adjust snippet if it was truncated
        val prefix = if (start > 0) "..." else ""
        val suffix = if (end < content.length) "..." else ""

        val finalSnippet = prefix + snippet + suffix

        // Recalculate match range in the snippet
        val matchInSnippetStart =
            prefix.length + (matchIndex - start) - leadingTrimmed
        val matchRange =
            matchInSnippetStart until (matchInSnippetStart + queryLength)

        return SearchResult(
            pageIndex = pageIndex,
            // Using 1-based page numbering
            pageLabel = (pageIndex + 1).toString(),
            snippet = finalSnippet,
            matchedRange = matchRange
        )
    }
}
