package com.turskyi.malaknyzhka.models

import com.russhwolf.settings.Settings

data class Bookmark(
    val pageNumber: Int,
    val timestamp: Long
)

interface BookmarkRepository {
    fun getBookmarks(): List<Bookmark>
    fun addBookmark(bookmark: Bookmark)
    fun removeBookmark(pageNumber: Int)
    fun isBookmarked(pageNumber: Int): Boolean
}

class SettingsBookmarkRepository(private val settings: Settings) :
    BookmarkRepository {
    private val separator = ";"
    private val pairSeparator = ":"

    override fun getBookmarks(): List<Bookmark> {
        val saved: String = settings.getString(SettingsKeys.BOOKMARKS, "")
        if (saved.isEmpty()) return emptyList()
        return saved.split(separator).filter { it.contains(pairSeparator) }
            .mapNotNull {
                val parts = it.split(pairSeparator)
                if (parts.size == 2) {
                    try {
                        Bookmark(parts[0].toInt(), parts[1].toLong())
                    } catch (e: Exception) {
                        println("Error parsing bookmark: $it. ${e.message}")
                        null
                    }
                } else {
                    null
                }
            }.sortedByDescending { it.timestamp }
    }

    override fun addBookmark(bookmark: Bookmark) {
        val bookmarks: MutableList<Bookmark> = getBookmarks().toMutableList()
        bookmarks.removeAll { it.pageNumber == bookmark.pageNumber }
        bookmarks.add(bookmark)
        saveBookmarks(bookmarks)
    }

    override fun removeBookmark(pageNumber: Int) {
        val bookmarks: MutableList<Bookmark> = getBookmarks().toMutableList()
        bookmarks.removeAll { it.pageNumber == pageNumber }
        saveBookmarks(bookmarks)
    }

    override fun isBookmarked(pageNumber: Int): Boolean {
        return getBookmarks().any { it.pageNumber == pageNumber }
    }

    private fun saveBookmarks(bookmarks: List<Bookmark>) {
        val string: String =
            bookmarks.joinToString(separator) { "${it.pageNumber}${pairSeparator}${it.timestamp}" }
        settings.putString(SettingsKeys.BOOKMARKS, string)
    }
}
