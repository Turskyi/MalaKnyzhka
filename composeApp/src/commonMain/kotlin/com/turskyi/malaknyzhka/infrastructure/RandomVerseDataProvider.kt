package com.turskyi.malaknyzhka.infrastructure

import com.turskyi.malaknyzhka.getCurrentTimeMillis
import com.turskyi.malaknyzhka.models.RandomVerseData
import com.turskyi.malaknyzhka.usecases.toUserPageNumber
import kotlin.math.abs
import kotlin.random.Random

/**
 * Dataset generated from BookContentRegistry.allPoemPages (Ukrainian poem text of Mala Knyzhka).
 * Each entry pairs a 1-based user page number with a 2-4 line meaningful snippet extracted
 * from that page verbatim, ending at line/sentence boundary with ellipsis if truncated.
 * 
 * NOTE: Generated from BookContentRegistry.allPoemPages string resources.
 * To regenerate if book text changes, run the snippet extraction utility over allPoemPages.
 */
object RandomVerseDataProvider {
    private var cachedDataset: List<RandomVerseData>? = null

    suspend fun getDataset(): List<RandomVerseData> {
        cachedDataset?.let { return it }
        val list = mutableListOf<RandomVerseData>()
        for (i in BookContentRegistry.allPoemPages.indices) {
            val res = BookContentRegistry.allPoemPages[i]
            val text = StringResourceResolver.getStringInUkrainian(res).trim()
            if (text.length > 10) {
                val snippet = extractSnippet(text)
                val pageNumber = i.toUserPageNumber()
                list.add(RandomVerseData(pageNumber = pageNumber, snippet = snippet))
            }
        }
        cachedDataset = list
        return list
    }

    private fun extractSnippet(fullText: String): String {
        val lines = fullText.lines().filter { it.isNotBlank() }
        val selectedLines = lines.take(3)
        val combined = selectedLines.joinToString("\n")
        val truncated = if (combined.length > 280) {
            combined.take(280).substringBeforeLast(" ", combined.take(280)) + "…"
        } else if (lines.size > 3) {
            "$combined…"
        } else {
            combined
        }
        return truncated
    }

    suspend fun getRandomVerseData(currentTimeMillis: Long = getCurrentTimeMillis()): RandomVerseData? {
        val dataset = getDataset()
        if (dataset.isEmpty()) return null
        
        val daysSinceEpoch = currentTimeMillis / (1000 * 60 * 60 * 24)
        val random = Random(daysSinceEpoch)
        val index = abs(random.nextInt()) % dataset.size
        
        val prevIndex = abs(Random(daysSinceEpoch - 1).nextInt()) % dataset.size
        val finalIndex = if (dataset.size > 1 && index == prevIndex) {
            (index + 1) % dataset.size
        } else {
            index
        }
        return dataset[finalIndex]
    }
}
