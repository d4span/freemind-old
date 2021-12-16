/*
 * Copyright (C) 2009 Archie L. Cobbs. All rights reserved.
 * Provided as is, as stated in http://jira.codehaus.org/browse/JIBX-346
 */
package de.foltin

import java.lang.StringBuilder
import de.foltin.CompileXsdStart.XsdHandler
import java.util.TreeSet
import java.lang.StringBuffer
import de.foltin.CompileXsdStart.ElementTypes
import kotlin.Throws
import de.foltin.CompileXsdStart
import java.io.FileOutputStream
import java.io.IOException
import de.foltin.CompileXsdStart.ComplexTypeHandler
import de.foltin.CompileXsdStart.ComplexContentHandler
import de.foltin.CompileXsdStart.SchemaHandler
import de.foltin.CompileXsdStart.SequenceHandler
import de.foltin.CompileXsdStart.ChoiceHandler
import de.foltin.CompileXsdStart.AttributeHandler
import de.foltin.CompileXsdStart.EnumerationHandler
import de.foltin.CompileXsdStart.ChoiceElementHandler
import java.util.Locale
import de.foltin.CompileXsdStart.SequenceElementHandler
import java.util.StringTokenizer
import kotlin.jvm.JvmStatic
import java.io.BufferedInputStream
import java.io.FileInputStream

/**
 * Encodes/decodes XML-invalid characters in Java strings so they may be
 * included as XML text.
 */
object StringEncoder {
    private const val HEXDIGITS = "0123456789abcdef"

    /**
     * Encode a string, escaping any invalid XML characters.
     *
     *
     *
     * Invalid characters are escaped using `&#92;uNNNN` notation
     * like Java unicode characters, e.g., `0x001f` would appear in
     * the encoded string as `&#92;u001f`. Backslash characters are
     * themselves encoded with a double backslash.
     *
     * @param value
     * string to encode (possibly null)
     * @return the encoded version of `value`, or `null` if
     * `value` was `null`
     * @see .decode
     */
    fun encode(value: String?): String? {
        if (value == null) return value
        val buf = StringBuilder(value.length + 4)
        val limit = value.length
        for (i in 0 until limit) {
            val ch = value[i]

            // Handle escape character
            if (ch == '\\') {
                buf.append('\\')
                buf.append('\\')
                continue
            }

            // If character is an otherwise valid XML character, pass it through
            // unchanged
            if (isValidXMLChar(ch)) {
                buf.append(ch)
                continue
            }

            // Escape it
            buf.append('\\')
            buf.append('u')
            var shift = 12
            while (shift >= 0) {
                buf.append(HEXDIGITS[ch.code shr shift and 0x0f])
                shift -= 4
            }
        }
        return buf.toString()
    }

    /**
     * Decode a string encoded by [.encode].
     *
     *
     *
     * The parsing is strict; any ill-formed backslash escape sequence (i.e.,
     * not of the form `&#92;uNNNN` or `\\`) will cause an
     * exception to be thrown.
     *
     * @param text
     * string to decode (possibly null)
     * @return the decoded version of `text`, or `null` if
     * `text` was `null`
     * @throws IllegalArgumentException
     * if `text` contains an invalid escape sequence
     * @see .encode
     */
    fun decode(text: String?): String? {
        if (text == null) return null
        val buf = StringBuilder(text.length)
        val limit = text.length
        var i = 0
        while (i < limit) {
            var ch = text[i]

            // Handle unescaped characters
            if (ch != '\\') {
                buf.append(ch)
                i++
                continue
            }

            // Get next char
            require(++i < limit) { "illegal trailing '\\' in encoded string" }
            ch = text[i]

            // Check for backslash escape
            if (ch == '\\') {
                buf.append(ch)
                i++
                continue
            }

            // Must be unicode escape
            require(ch == 'u') {
                ("illegal escape sequence '\\" + ch
                        + "' in encoded string")
            }

            // Decode hex value
            var value = 0
            for (j in 0..3) {
                require(++i < limit) { "illegal truncated '\\u' escape sequence in encoded string" }
                val nibble = text[i].digitToIntOrNull(16) ?: -1
                require(nibble != -1) {
                    ("illegal escape sequence '"
                            + text.substring(i - j - 2, i - j + 4)
                            + "' in encoded string")
                }
                // assert nibble >= 0 && nibble <= 0xf;
                value = value shl 4 or nibble
            }

            // Append decodec character
            buf.append(value.toChar())
            i++
        }
        return buf.toString()
    }

    /**
     * Determine if the given character is a valid XML character according to
     * the XML 1.0 specification.
     *
     * @see [The XML 1.0
     * Specification](http://www.w3.org/TR/REC-xml/.charsets)
     */
    fun isValidXMLChar(ch: Char): Boolean {
        return ch >= '\u0020' && ch <= '\ud7ff' || ch >= '\ue000' && ch <= '\ufffd'
    }
}