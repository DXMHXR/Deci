package org.kimplify.deci

import org.kimplify.deci.exception.DeciParseException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Kotlin prints a [Double] in scientific notation once its magnitude reaches `1e7`, or falls below
 * `1e-3`. Those literals are outside the decimal grammar the [String] constructor validates
 * against, so the [Double] constructor used to reject ordinary values — ten million of any currency
 * is not an edge case.
 */
class DeciDoubleConstructorTest {
    @Test
    fun `accepts magnitudes at and above 1e7`() {
        assertEquals(Deci("10000000"), Deci(1.0e7))
        assertEquals(Deci("29638000"), Deci(29_638_000.0))
        assertEquals(Deci("150000000"), Deci(1.5e8))
        assertEquals(Deci("1000000000000000000000"), Deci(1.0e21))
    }

    @Test
    fun `accepts magnitudes below 1e-3`() {
        assertEquals(Deci("0.0001"), Deci(1.0e-4))
        assertEquals(Deci("0.00012345"), Deci(0.00012345))
    }

    @Test
    fun `keeps the sign`() {
        assertEquals(Deci("-150000000"), Deci(-1.5e8))
        assertEquals(Deci("-0.0001"), Deci(-1.0e-4))
    }

    @Test
    fun `still accepts the magnitudes that never needed expanding`() {
        assertEquals(Deci("0"), Deci(0.0))
        assertEquals(Deci("9999999"), Deci(9_999_999.0))
        assertEquals(Deci("0.001"), Deci(0.001))
        assertEquals(Deci("-12.5"), Deci(-12.5))
    }

    @Test
    fun `arithmetic on an expanded value is exact`() {
        assertEquals(Deci("29638000"), Deci(21_170_000.0) + Deci(8_468_000.0))
        assertEquals(Deci("59276000"), Deci(29_638_000.0) * Deci(2))
    }

    @Test
    fun `non-finite values are still rejected`() {
        assertFailsWith<DeciParseException> { Deci(Double.NaN) }
        assertFailsWith<DeciParseException> { Deci(Double.POSITIVE_INFINITY) }
        assertFailsWith<DeciParseException> { Deci(Double.NEGATIVE_INFINITY) }
    }
}
