package com.turskyi.malaknyzhka.usecases

/**
 * Converts internal 0-based page index to user-facing 1-based page number.
 */
fun Int.toUserPageNumber(): Int = this + 1

/**
 * Converts user-facing 1-based page number back to internal 0-based index.
 */
fun Int.toInternalPageIndex(): Int = (this - 1).coerceAtLeast(0)
