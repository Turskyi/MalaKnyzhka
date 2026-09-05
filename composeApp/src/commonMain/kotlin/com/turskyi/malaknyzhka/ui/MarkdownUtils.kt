package com.turskyi.malaknyzhka.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * A basic Markdown-to-AnnotatedString parser.
 * Handles:
 * 1. Bold: **text** and __text__
 * 2. Italic: *text* and _text_
 */
fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            when {
                // Bold: **text** or __text__
                text.startsWith("**", i) || text.startsWith("__", i) -> {
                    val marker = if (text.startsWith("**", i)) "**" else "__"
                    val closingIndex = text.indexOf(marker, i + 2)
                    if (closingIndex != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(parseMarkdown(text.substring(i + 2, closingIndex)))
                        }
                        i = closingIndex + 2
                    } else {
                        append(text[i])
                        i++
                    }
                }
                // Italic: *text* or _text_
                text.startsWith("*", i) || text.startsWith("_", i) -> {
                    val marker = if (text.startsWith("*", i)) "*" else "_"
                    val closingIndex = text.indexOf(marker, i + 1)
                    if (closingIndex != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            append(parseMarkdown(text.substring(i + 1, closingIndex)))
                        }
                        i = closingIndex + 1
                    } else {
                        append(text[i])
                        i++
                    }
                }
                else -> {
                    append(text[i])
                    i++
                }
            }
        }
    }
}
