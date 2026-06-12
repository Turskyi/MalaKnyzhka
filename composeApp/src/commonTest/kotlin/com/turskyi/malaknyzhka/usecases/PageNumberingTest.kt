package com.turskyi.malaknyzhka.usecases

import kotlin.test.Test
import kotlin.test.assertEquals

class PageNumberingTest {

    @Test
    fun testToUserPageNumber() {
        // Internal index 0 should be Page 1
        assertEquals(1, 0.toUserPageNumber())
        // Internal index 192 should be Page 193
        assertEquals(193, 192.toUserPageNumber())
    }

    @Test
    fun testToInternalPageIndex() {
        // Page 1 should be internal index 0
        assertEquals(0, 1.toInternalPageIndex())
        // Page 193 should be internal index 192
        assertEquals(192, 193.toInternalPageIndex())
    }

    @Test
    fun testInternalPageIndexLowerBound() {
        // Page numbers less than 1 should be coerced to index 0
        assertEquals(0, 0.toInternalPageIndex())
        assertEquals(0, (-1).toInternalPageIndex())
    }
}
