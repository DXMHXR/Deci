package org.kimplify.deci.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DoubleLiteralTest {
    @Test
    fun `expand scientific notation various cases`() {
        val cases =
            listOf(
                "123" to "123",
                "1.5" to "1.5",
                "-0.25" to "-0.25",
                "2.9638E7" to "29638000",
                "2.9638e7" to "29638000",
                "1.0E7" to "10000000",
                "1.2345E2" to "123.45",
                "1E2" to "100",
                "1.0E-4" to "0.00010",
                "5.0E-1" to "0.50",
                "-1.5E8" to "-150000000",
                "-1.0E-4" to "-0.00010",
                "1e+21" to "1000000000000000000000",
                "NaN" to "NaN",
                "Infinity" to "Infinity",
                "-Infinity" to "-Infinity",
            )

        cases.forEach { (input, expected) ->
            assertEquals(expected, input.expandScientificNotation(), "input: $input")
        }
    }

    @Test
    fun `expand scientific notation leaves a non-integer exponent alone`() {
        assertEquals("1.5E", "1.5E".expandScientificNotation())
        assertEquals("1.5Ex", "1.5Ex".expandScientificNotation())
    }

    @Test
    fun `double literal never carries an exponent marker`() {
        val values =
            listOf(
                0.0,
                0.001,
                9_999_999.0,
                29_638_000.0,
                -150_000_000.0,
                0.00012345,
                1.0e21,
                1.0e-10,
                Double.MAX_VALUE,
                Double.MIN_VALUE,
            )

        values.forEach { value ->
            val literal = value.toDecimalLiteral()
            assertFalse(literal.any { it == 'e' || it == 'E' }, "literal for $value: $literal")
        }
    }
}
