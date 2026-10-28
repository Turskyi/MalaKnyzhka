package com.turskyi.malaknyzhka.infrastructure

import kotlin.test.Test
import kotlin.test.assertEquals

class WidgetTextFormattingTest {

    @Test
    fun testTrimIndentAlignsFirstLineWithSubsequentLines() {
        val rawXmlString = "\n        Зоре моя вечірняя,\n        Зійди над горою,\n        Поговорим на чужині\n        Тихенько з тобою."

        val wrongTrimmed = rawXmlString.trim()
        val wrongLines = wrongTrimmed.lines()
        // With .trim(), line 1 loses leading spaces while line 2 keeps 8 leading spaces
        assertEquals("Зоре моя вечірняя,", wrongLines[0])
        assertEquals("        Зійди над горою,", wrongLines[1])

        val correctlyTrimmed = rawXmlString.trimIndent().trim()
        val correctLines = correctlyTrimmed.lines()
        // With .trimIndent().trim(), all lines lose the common XML indentation and align cleanly
        assertEquals("Зоре моя вечірняя,", correctLines[0])
        assertEquals("Зійди над горою,", correctLines[1])
        assertEquals("Поговорим на чужині", correctLines[2])
        assertEquals("Тихенько з тобою.", correctLines[3])
    }
}
