package org.kimplify.deci

import kotlin.test.Test
import kotlin.test.assertEquals

class DeciDoubleConstructorIosTest {
    @Test
    fun `largest finite double does not become NaN`() {
        assertEquals(Double.MAX_VALUE, Deci(Double.MAX_VALUE).toDouble())
    }
}
