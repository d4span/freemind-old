package freemind.common

import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PushbackInputStream
import java.io.Reader

/**
 *
 * fc, 2010-12-23 Taken from http://koti.mbnet.fi/akini/java/unicodereader/UnicodeReader.java.txt
 * asuming public domain.
 *
 * version: 1.1 / 2007-01-25
 * - changed BOM recognition ordering (longer boms first)
 *
 * Original pseudocode   : Thomas Weidenfeller
 * Implementation tweaked: Aki Nieminen
 *
 * http://www.unicode.org/unicode/faq/utf_bom.html
 * BOMs:
 * 00 00 FE FF    = UTF-32, big-endian
 * FF FE 00 00    = UTF-32, little-endian
 * EF BB BF       = UTF-8,
 * FE FF          = UTF-16, big-endian
 * FF FE          = UTF-16, little-endian
 *
 * Win2k Notepad:
 * Unicode format = UTF-16LE
 */
/**
 * Generic unicode textreader, which will use BOM mark to identify the encoding
 * to be used. If BOM is not found then use a given default or system encoding.
 */
class UnicodeReader(`in`: InputStream?, defaultEnc: String) : Reader() {
    var internalIn: PushbackInputStream
    var internalIn2: InputStreamReader? = null
    var defaultEncoding: String

    /**
     *
     * @param in
     * inputstream to be read
     * @param defaultEnc
     * default encoding if stream does not have BOM marker. Give NULL
     * to use system-level default.
     */
    init {
        internalIn = PushbackInputStream(`in`, BOM_SIZE)
        defaultEncoding = defaultEnc
    }

    /**
     * Get stream encoding or NULL if stream is uninitialized. Call init() or
     * read() method to initialize it.
     */
    val encoding: String?
        get() = if (internalIn2 == null) null else internalIn2!!.encoding

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are unread
     * back to the stream, only BOM bytes are skipped.
     */
    @Throws(IOException::class)
    protected fun init() {
        if (internalIn2 != null) return
        // val encoding: String
        val bom = ByteArray(BOM_SIZE)
        val n: Int
        val unread: Int
        n = internalIn.read(bom, 0, bom.size)
        if (bom[0] == 0x00.toByte() && bom[1] == 0x00.toByte()
            && bom[2] == 0xFE.toByte() && bom[3] == 0xFF.toByte()
        ) {
            // encoding = "UTF-32BE"
            unread = n - 4
        } else if (bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte()
            && bom[2] == 0x00.toByte() && bom[3] == 0x00.toByte()
        ) {
            // encoding = "UTF-32LE"
            unread = n - 4
        } else if (bom[0] == 0xEF.toByte() && bom[1] == 0xBB.toByte()
            && bom[2] == 0xBF.toByte()
        ) {
            // encoding = "UTF-8"
            unread = n - 3
        } else if (bom[0] == 0xFE.toByte() && bom[1] == 0xFF.toByte()) {
            // encoding = "UTF-16BE"
            unread = n - 2
        } else if (bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte()) {
            // encoding = "UTF-16LE"
            unread = n - 2
        } else {
            // Unicode BOM mark not found, unread all bytes
            // encoding = defaultEncoding
            unread = n
        }
        // System.out.println("read=" + n + ", unread=" + unread);
        if (unread > 0) internalIn.unread(bom, n - unread, unread)

        // Use given encoding
//        internalIn2 = if (encoding == null) {
            InputStreamReader(internalIn)
//        } else {
//            InputStreamReader(internalIn, encoding)
//        }
    }

    @Throws(IOException::class)
    override fun close() {
        init()
        internalIn2!!.close()
    }

    @Throws(IOException::class)
    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        init()
        return internalIn2!!.read(cbuf, off, len)
    }

    companion object {
        private const val BOM_SIZE = 4
    }
}