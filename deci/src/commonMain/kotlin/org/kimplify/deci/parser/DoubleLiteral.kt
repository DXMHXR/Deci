package org.kimplify.deci.parser

/**
 * Renders a [Double] as a plain decimal literal, expanding scientific notation if
 * [Double.toString] produced any.
 *
 * Kotlin switches to scientific notation for magnitudes at or above `1e7` and below `1e-3`, so
 * `29638000.0` prints as `"2.9638E7"` and `0.0001` as `"1.0E-4"`. That form is not part of the
 * decimal grammar [DECIMAL_REGEX] accepts, so feeding it straight to the [String] constructor
 * rejects ordinary values. Expanding it first keeps the `Double` constructor total for every
 * finite input.
 *
 * `NaN` and the infinities carry no exponent marker and are returned unchanged, so they still
 * fail validation as before.
 */
internal fun Double.toDecimalLiteral(): String = toString().expandScientificNotation()

/**
 * Rewrites a decimal string in scientific notation as a plain one, preserving every significant
 * digit: `"2.9638E7"` becomes `"29638000"` and `"1.0E-4"` becomes `"0.00010"`.
 *
 * Strings without an exponent marker, and those whose exponent is not an integer, are returned
 * unchanged.
 */
internal fun String.expandScientificNotation(): String {
    val exponentIndex = indexOfFirst { it == 'e' || it == 'E' }
    if (exponentIndex < 0) return this

    val exponent = substring(exponentIndex + 1).toIntOrNull() ?: return this
    val mantissa = substring(0, exponentIndex)
    val isNegative = mantissa.startsWith('-')
    val unsigned = mantissa.removePrefix("-").removePrefix("+")

    val digits = unsigned.filterNot { it == '.' }
    val separatorIndex = unsigned.indexOf('.')
    val integerLength = if (separatorIndex < 0) unsigned.length else separatorIndex
    val pointPosition = integerLength + exponent

    val expanded =
        when {
            pointPosition <= 0 -> "0." + "0".repeat(-pointPosition) + digits
            pointPosition >= digits.length -> digits + "0".repeat(pointPosition - digits.length)
            else -> digits.substring(0, pointPosition) + "." + digits.substring(pointPosition)
        }

    return if (isNegative) "-$expanded" else expanded
}
