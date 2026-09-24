package org.kimplify.deci

import org.kimplify.deci.exception.DeciOverflowException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeciDoubleConstructorIosTest {
    @Test
    fun `rejects a double above the Apple decimal range`() {
        assertFailsWith<DeciOverflowException> { Deci(Double.MAX_VALUE) }
    }

    @Test
    fun `rejects a double below the Apple decimal range`() {
        assertFailsWith<DeciOverflowException> { Deci(Double.MIN_VALUE) }
    }

    @Test
    fun `rejects a valid decimal above the Apple decimal range`() {
        assertFailsWith<DeciOverflowException> { Deci("1" + "0".repeat(308)) }
    }

    @Test
    fun `accepts ordinary scientific notation on iOS`() {
        assertEquals(Deci("29638000"), Deci(29_638_000.0))
    }
}
