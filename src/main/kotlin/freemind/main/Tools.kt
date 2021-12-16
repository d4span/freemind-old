/*
 * FreeMind - a program for creating and viewing mindmaps
 * Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * See COPYING for details
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.main

import freemind.controller.actions.generated.instance.CompoundAction.listChoiceList
import freemind.controller.Controller.init
import freemind.controller.Controller.OptionAntialiasAction.changeAntialias
import freemind.controller.Controller.createNewMode
import freemind.controller.Controller.filterController
import freemind.controller.filter.FilterController.saveConditions
import freemind.controller.Controller.view
import freemind.controller.Controller.obtainFocusForSelected
import freemind.controller.Controller.addTabbedPane
import freemind.controller.Controller.modeController
import freemind.controller.LastStateStorageManagement.lastOpenList
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.restorableName
import freemind.controller.Controller.lastOpenedList
import freemind.controller.LastOpenedList.open
import freemind.controller.LastStateStorageManagement.lastFocussedTab
import freemind.controller.Controller.mapModule
import freemind.controller.Controller.mapModuleManager
import freemind.controller.MapModuleManager.changeToMapModule
import freemind.controller.Controller.errorMessage
import freemind.main.Tools
import java.lang.StringBuffer
import freemind.main.Base64Coding
import java.util.zip.Deflater
import java.util.zip.Inflater
import java.util.zip.DataFormatException
import java.lang.RuntimeException
import freemind.main.Tools.BooleanHolder
import kotlin.Throws
import java.lang.Runnable
import freemind.main.HtmlTools
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import java.awt.event.ActionEvent
import freemind.main.FreeMindCommon
import freemind.main.Tools.ButtonHolder
import freemind.main.Tools.ActionHolder
import freemind.main.Tools.NameMnemonicHolder
import freemind.modes.MindMapNode
import freemind.controller.actions.generated.instance.XmlAction
import freemind.common.XmlBindingTools
import java.lang.SecurityException
import java.lang.IllegalAccessException
import java.lang.NoSuchFieldException
import freemind.modes.mindmapmode.MindMapController
import java.awt.datatransfer.Clipboard
import freemind.controller.MindMapNodesSelection
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.print.Paper
import freemind.controller.actions.generated.instance.CompoundAction
import java.lang.reflect.InvocationTargetException
import java.lang.InterruptedException
import freemind.main.FreeMindStarter
import freemind.modes.EdgeAdapter
import freemind.modes.MindIcon
import java.nio.file.attribute.DosFileAttributes
import freemind.main.FreeMind
import javax.crypto.Cipher
import java.security.spec.KeySpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.PBEParameterSpec
import java.security.InvalidAlgorithmParameterException
import java.security.spec.InvalidKeySpecException
import javax.crypto.NoSuchPaddingException
import java.security.NoSuchAlgorithmException
import freemind.main.Tools.DesEncrypter
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import freemind.main.Tools.ReaderCreator
import freemind.main.FreeMindMain
import freemind.main.FreeMindMain.StartupDoneListener
import freemind.main.EditServer
import freemind.main.FreeMindSecurityManager
import freemind.main.FeedBack
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import freemind.view.mindmapview.MapView
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
import java.util.logging.SimpleFormatter
import com.inet.jortho.SpellChecker
import freemind.main.FreeMindStarter.ProxyAuthenticator
import java.awt.event.InputEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import freemind.modes.ModeController
import freemind.controller.LastStateStorageManagement
import freemind.view.MapModule
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import freemind.main.IFreeMindSplash
import freemind.main.FreeMindSplashModern
import java.awt.event.WindowFocusListener
import freemind.main.XHTMLWriter
import javax.swing.text.BadLocationException
import freemind.main.HtmlTools.IndexPair
import freemind.main.HtmlTools.NodeCreator
import org.jsoup.select.NodeTraversor
import freemind.main.HtmlTools.HtmlNodeVisitor
import org.jsoup.Jsoup
import kotlin.jvm.JvmOverloads
import freemind.common.TextTranslator
import freemind.common.UnicodeReader
import java.text.MessageFormat
import freemind.modes.FreeMindFileDialog
import freemind.modes.FreeMindJFileDialog
import freemind.modes.FreeMindAwtFileDialog
import tests.freemind.FreeMindMainMock
import freemind.main.XMLElement
import java.lang.ClassCastException
import freemind.main.FixedHTMLWriter
import freemind.main.XHTMLWriter.XHTMLFilterWriter
import javax.swing.text.html.HTMLEditorKit
import kotlin.jvm.JvmStatic
import freemind.main.StdFormatter.StdOutErrLevel
import freemind.main.FreeMindApplet
import freemind.main.FreeMindCommon.FreeMindResourceBundle
import javax.swing.text.html.HTMLWriter
import javax.swing.text.MutableAttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.html.HTML
import javax.swing.text.StyleConstants
import javax.swing.text.html.CSS
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.main.FreeMindSplashModern.FeedBackImpl
import freemind.view.ImageFactory
import freemind.view.mindmapview.NodeView
import java.awt.*
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.*
import java.nio.file.Files
import java.security.InvalidKeyException
import java.util.*
import java.util.logging.Logger
import javax.swing.*
import javax.swing.Timer
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

//maybe move this class to another package like tools or something...
/**
 * @author foltin
 */
object Tools {
    /**
     *
     */
    const val FREEMIND_LIB_FREEMIND_JAR = "lib/freemind.jar"
    private var logger: Logger? = null

    init {
        logger = Resources.Companion.getInstance().getLogger("Tools")
    }

    const val CONTENTS_JAVA_FREEMIND_JAR = "Contents/Java/freemind.jar"
    const val FREE_MIND_APP_CONTENTS_RESOURCES_JAVA = "Contents/Resources/Java/"

    // public static final Set executableExtensions = new HashSet ({ "exe",
    // "com", "vbs" });
    // The Java programming language provides a shortcut syntax for creating and
    // initializing an array. Here's an example of this syntax:
    // boolean[] answers = { true, false, true, true, false };
    val executableExtensions: MutableSet<String> = HashSet(5)

    init {
        executableExtensions.add("exe")
        executableExtensions.add("com")
        executableExtensions.add("vbs")
        executableExtensions.add("bat")
        executableExtensions.add("lnk")
    }

    private var sEnvFonts: Array<String>? = null

    // bug fix from Dimitri.
    var ran = Random()
    fun executableByExtension(file: String): Boolean {
        return executableExtensions.contains(getExtension(file))
    }

    fun colorToXml(col: Color?): String? {
        // if (col == null) throw new IllegalArgumentException("Color was
        // null");
        if (col == null) return null
        var red = Integer.toHexString(col.red)
        if (col.red < 16) red = "0$red"
        var green = Integer.toHexString(col.green)
        if (col.green < 16) green = "0$green"
        var blue = Integer.toHexString(col.blue)
        if (col.blue < 16) blue = "0$blue"
        return "#$red$green$blue"
    }

    fun xmlToColor(string: String?): Color? {
        var string = string ?: return null
        string = string.trim { it <= ' ' }
        return if (string.length == 7) {
            val red = string.substring(1, 3).toInt(16)
            val green = string.substring(3, 5).toInt(16)
            val blue = string.substring(5, 7).toInt(16)
            Color(red, green, blue)
        } else {
            throw IllegalArgumentException("No xml color given by '"
                    + string + "'.")
        }
    }

    fun PointToXml(col: Point?): String? {
        if (col == null) return null // throw new IllegalArgumentException("Point was
        // null");
        val l = Vector<String>()
        l.add(Integer.toString(col.x))
        l.add(Integer.toString(col.y))
        return listToString(l)
    }

    fun xmlToPoint(string: String?): Point? {
        var string = string ?: return null
        // fc, 3.11.2004: bug fix for alpha release of FM
        if (string.startsWith("java.awt.Point")) {
            string = string.replace(
                    "java\\.awt\\.Point\\[x=(-*[0-9]*),y=(-*[0-9]*)\\]".toRegex(),
                    "$1;$2")
        }
        val l = stringToList(string)
        val it = l.listIterator(0)
        require(l.size == 2) {
            ("A point must consist of two numbers (and not: '" + string
                    + "').")
        }
        val x: Int = it.next() as String?. toInt ()
        val y: Int = it.next() as String?. toInt ()
        return Point(x, y)
    }

    fun BooleanToXml(col: Boolean): String {
        return if (col) "true" else "false"
    }

    fun xmlToBoolean(string: String): Boolean {
        return if ("true" == string) true else false
    }

    /**
     * Converts a String in the format "value;value;value" to a List with the
     * values (as strings)
     */
    fun stringToList(string: String?): List<String> {
        val tok = StringTokenizer(string, ";")
        val list: MutableList<String> = LinkedList()
        while (tok.hasMoreTokens()) {
            list.add(tok.nextToken())
        }
        return list
    }

    fun listToString(list: List<*>): String {
        val it = list.listIterator(0)
        val str = StringBuffer()
        while (it.hasNext()) {
            str.append(it.next().toString() + ";")
        }
        return str.toString()
    }

    /**
     * Replaces a ~ in a filename with the users home directory
     */
    fun expandFileName(file: String): String {
        // replace ~ with the users home dir
        var file = file
        if (file.startsWith("~")) {
            file = System.getProperty("user.home") + file.substring(1)
        }
        return file
    }

    /**
     */
    val availableFonts: Array<String>?
        get() {
            if (sEnvFonts == null) {
                val gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment()
                sEnvFonts = gEnv.availableFontFamilyNames
            }
            return sEnvFonts
        }

    fun isAvailableFontFamily(name: String): Boolean {
        for (s in availableFonts!!) {
            if (s == name) return true
        }
        return false
    }

    /**
     * Returns the lowercase of the extension of a file. Example:
     * getExtension("fork.pork.MM") ==
     * freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT
     */
    fun getExtension(f: File): String {
        return getExtension(f.toString())
    }

    /**
     * Returns the lowercase of the extension of a file name. Example:
     * getExtension("fork.pork.MM") ==
     * freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT
     */
    fun getExtension(s: String): String {
        val i = s.lastIndexOf('.')
        return if (i > 0 && i < s.length - 1) s.substring(i + 1).lowercase(Locale.getDefault())
                .trim { it <= ' ' } else ""
    }

    fun removeExtension(s: String): String {
        val i = s.lastIndexOf('.')
        return if (i > 0 && i < s.length - 1) s.substring(0, i) else ""
    }

    fun isAbsolutePath(path: String): Boolean {
        // On Windows, we cannot just ask if the file name starts with file
        // separator.
        // If path contains ":" at the second position, then it is not relative,
        // I guess.
        // However, if it starts with separator, then it is absolute too.

        // Possible problems: Not tested on Macintosh, but should work.
        // Koh, 1.4.2004: Resolved problem: I tested on Mac OS X 10.3.3 and
        // worked.
        val osNameStart = System.getProperty("os.name").substring(0, 3)
        val fileSeparator = System.getProperty("file.separator")
        return if (osNameStart == "Win") {
            (path.length > 1 && path.substring(1, 2) == ":"
                    || path.startsWith(fileSeparator))
        } else if (osNameStart == "Mac") {
            // Koh:Panther (or Java 1.4.2) may change file path rule
            path.startsWith(fileSeparator)
        } else {
            path.startsWith(fileSeparator)
        }
    }

    /**
     * This is a correction of a method getFile of a class URL. Namely, on
     * Windows it returned file paths like /C: etc., which are not valid on
     * Windows. This correction is heuristic to a great extend. One of the
     * reasons is that file:// is basically no protocol at all, but rather
     * something every browser and every system uses slightly differently.
     */
    fun urlGetFile(url: URL): String {
        return if (isWindows && isFile(url)) {
            val fileName = url.toString().replaceFirst("^file:".toRegex(), "")
                    .replace('/', '\\')
            if (fileName.indexOf(':') >= 0) fileName.replaceFirst(
                    "^\\\\*".toRegex(), "") else fileName
        } // Network path
        else {
            url.file
        }
    }

    val isWindows: Boolean
        get() = System.getProperty("os.name").substring(0, 3) == "Win"

    fun isFile(url: URL): Boolean {
        return url.protocol == "file"
    }

    /**
     * @return "/" for absolute file names under Unix, "c:\\" or similar under
     * windows, null otherwise
     */
    fun getPrefix(pFileName: String): String? {
        if (isWindows) {
            if (pFileName.matches("^[a-zA-Z]:\\\\.*")) {
                return pFileName.substring(0, 3)
            }
        } else {
            if (pFileName.startsWith(File.separator)) {
                return File.separator
            }
        }
        return null
    }

    /**
     * This method converts an absolute url to an url relative to a given
     * base-url. Something like this should be included in the librarys, but I
     * couldn't find it. You can create a new absolute url with
     * "new URL(URL context, URL relative)".
     */
    fun toRelativeURL(base: URL?, target: URL?): String {
        // Precondition: If URL is a path to folder, then it must end with '/'
        // character.
        if (base == null || base.protocol != target!!.protocol
                || base.host != target.host) {
            return target.toString()
        }
        var baseString = base.file
        var targetString = target!!.file
        var result = ""
        // remove filename from URL
        targetString = targetString.substring(0,
                targetString.lastIndexOf("/") + 1)
        // remove filename from URL
        baseString = baseString.substring(0, baseString.lastIndexOf("/") + 1)

        // Algorithm
        // look for same start:
        var index = targetString.length - 1
        while (!baseString.startsWith(targetString.substring(0, index + 1))) {
            // remove last part:
            index = targetString.lastIndexOf("/", index - 1)
            if (index < 0) {
                // no common part. This is strange, as both should start with /,
                // but...
                break
            }
        }

        // now, baseString is targetString + "/" + rest. we determine
        // rest=baseStringRest now.
        val baseStringRest = baseString
                .substring(index, baseString.length)

        // Maybe this causes problems under windows
        val baseTokens = StringTokenizer(baseStringRest, "/")

        // Maybe this causes problems under windows
        val targetTokens = StringTokenizer(
                targetString.substring(index + 1), "/")
        var nextTargetToken = ""
        while (baseTokens.hasMoreTokens()) {
            result = "$result../"
            baseTokens.nextToken()
        }
        while (targetTokens.hasMoreTokens()) {
            nextTargetToken = targetTokens.nextToken()
            result = "$result$nextTargetToken/"
        }
        val temp = target.file
        result = result + temp.substring(temp.lastIndexOf("/") + 1,
                temp.length)
        return result
    }

    /**
     * If the preferences say, that links should be relative, a relative url is
     * returned.
     *
     * @param input
     * the file that is treated
     * @param pMapFile
     * the file, that input is made relative to
     * @return in case of trouble the absolute path.
     */
    fun fileToRelativeUrlString(input: File, pMapFile: File?): String {
        val link: URL?
        var relative: String
        try {
            link = fileToUrl(input)
            relative = link.toString()
            if ("relative" == Resources.Companion.getInstance().getProperty("links")) {
                // Create relative URL
                relative = toRelativeURL(fileToUrl(pMapFile), link)
            }
            return relative
        } catch (ex: MalformedURLException) {
            Resources.Companion.getInstance().logException(ex)
        }
        return input.absolutePath
    }

    /**
     * Tests a string to be equals with "true".
     *
     * @return true, iff the String is "true".
     */
    fun isPreferenceTrue(option: String?): Boolean {
        return safeEquals(option, "true")
    }

    /**
     * @param string1
     * input (or null)
     * @param string2
     * input (or null)
     * @return true, if equal (that means: same text or both null)
     */
    fun safeEquals(string1: String?, string2: String?): Boolean {
        return (string1 != null && string2 != null && string1 == string2
                || string1 == null && string2 == null)
    }

    fun safeEquals(obj1: Any?, obj2: Any?): Boolean {
        return (obj1 != null && obj2 != null && obj1 == obj2
                || obj1 == null && obj2 == null)
    }

    fun safeEqualsIgnoreCase(string1: String?, string2: String?): Boolean {
        return (string1 != null && string2 != null && (string1.lowercase(Locale.getDefault())
                == string2.lowercase(Locale.getDefault()))
                || string1 == null && string2 == null)
    }

    fun safeEquals(color1: Color?, color2: Color?): Boolean {
        return (color1 != null && color2 != null && color1 == color2
                || color1 == null && color2 == null)
    }

    fun firstLetterCapitalized(text: String?): String? {
        return if (text == null || text.length == 0) {
            text
        } else text.substring(0, 1).uppercase(Locale.getDefault())
                + text.substring(1, text.length)
    }

    fun setHidden(file: File, hidden: Boolean,
                  synchronously: Boolean) {
        // According to Web articles, UNIX systems do not have attribute hidden
        // in general, rather, they consider files starting with . as hidden.
        val osNameStart = System.getProperty("os.name").substring(0, 3)
        if (osNameStart == "Win") {
            try {
                Runtime.getRuntime().exec(
                        "attrib " + (if (hidden) "+" else "-") + "H \""
                                + file.absolutePath + "\"")
                // Synchronize the effect, because it is asynchronous in
                // general.
                if (!synchronously) {
                    return
                }
                var timeOut = 10
                while (file.isHidden != hidden && timeOut > 0) {
                    Thread.sleep(10 /* miliseconds */)
                    timeOut--
                }
            } catch (e: Exception) {
                Resources.Companion.getInstance().logException(e)
            }
        }
    }

    /**
     * Example: expandPlaceholders("Hello $1.","Dolly"); => "Hello Dolly."
     */
    fun expandPlaceholders(message: String, s1: String?): String {
        var s1 = s1
        var result = message
        if (s1 != null) {
            s1 = s1.replace("\\\\".toRegex(), "\\\\\\\\") // Replace \ with \\
            result = result.replace("\\$1".toRegex(), s1)
        }
        return result
    }

    fun expandPlaceholders(message: String, s1: String?, s2: String?): String {
        var result = message
        if (s1 != null) {
            result = result.replace("\\$1".toRegex(), s1)
        }
        if (s2 != null) {
            result = result.replace("\\$2".toRegex(), s2)
        }
        return result
    }

    fun expandPlaceholders(message: String, s1: String?,
                           s2: String?, s3: String?): String {
        var result = message
        if (s1 != null) {
            result = result.replace("\\$1".toRegex(), s1)
        }
        if (s2 != null) {
            result = result.replace("\\$2".toRegex(), s2)
        }
        if (s3 != null) {
            result = result.replace("\\$3".toRegex(), s3)
        }
        return result
    }

    /**
     */
    fun toBase64(byteBuffer: ByteArray): String {
        return String(Base64Coding.encode64(byteBuffer))
    }

    /** Method to be called from XSLT  */
    fun toBase64(text: String): String {
        return toBase64(text.toByteArray())
    }

    /**
     * @throws IOException
     */
    fun fromBase64(base64String: String): ByteArray? {
        return Base64Coding.decode64(base64String)
    }

    fun compress(message: String): String {
        val input = uTF8StringToByteArray(message)
        // Create the compressor with highest level of compression
        val compressor = Deflater()
        compressor.setLevel(Deflater.BEST_COMPRESSION)

        // Give the compressor the data to compress
        compressor.setInput(input)
        compressor.finish()

        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        val bos = ByteArrayOutputStream(input.size)

        // Compress the data
        val buf = ByteArray(1024)
        while (!compressor.finished()) {
            val count = compressor.deflate(buf)
            bos.write(buf, 0, count)
        }
        try {
            bos.close()
        } catch (e: IOException) {
        }

        // Get the compressed data
        val compressedData = bos.toByteArray()
        return toBase64(compressedData)
    }

    fun decompress(compressedMessage: String?): String {
        val compressedData = fromBase64(compressedMessage)
        // Create the decompressor and give it the data to compress
        val decompressor = Inflater()
        decompressor.setInput(compressedData)

        // Create an expandable byte array to hold the decompressed data
        val bos = ByteArrayOutputStream(
                compressedData!!.size)

        // Decompress the data
        val buf = ByteArray(1024)
        var errorOccured = false
        while (!decompressor.finished() && !errorOccured) {
            try {
                val count = decompressor.inflate(buf)
                bos.write(buf, 0, count)
            } catch (e: DataFormatException) {
                errorOccured = true
            }
        }
        try {
            bos.close()
        } catch (e: IOException) {
        }

        // Get the decompressed data
        val decompressedData = bos.toByteArray()
        return byteArrayToUTF8String(decompressedData)
    }

    /**
     */
    fun byteArrayToUTF8String(compressedData: ByteArray?): String {
        // Decode using utf-8
        return try {
            String(compressedData!!, "UTF8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("UTF8 packing not allowed")
        }
    }

    /**
     */
    fun uTF8StringToByteArray(uncompressedData: String): ByteArray {
        // Code using utf-8
        return try {
            uncompressedData.toByteArray(charset("UTF8"))
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("UTF8 packing not allowed")
        }
    }

    /**
     * Extracts a long from xml. Only useful for dates.
     */
    fun xmlToDate(xmlString: String?): Date {
        return try {
            Date(java.lang.Long.valueOf(xmlString).toLong())
        } catch (e: Exception) {
            Date(System.currentTimeMillis())
        }
    }

    fun dateToString(date: Date): String {
        return java.lang.Long.toString(date.time)
    }

    fun safeEquals(holder: BooleanHolder?, holder2: BooleanHolder?): Boolean {
        return (holder == null && holder2 == null
                || holder != null && holder2 != null && holder.value == holder2
                .value)
    }

    fun setDialogLocationRelativeTo(dialog: JDialog?, c: Component?) {
        var c = c
                ?: // perhaps, the component is not yet existing.
                return
        if (c is NodeView) {
            val nodeView = c
            nodeView.map.scrollNodeToVisible(nodeView)
            c = nodeView.mainView
        }
        val compLocation = c.locationOnScreen
        val cw = c.width
        val ch = c.height
        val parent = dialog.parent
        val parentLocation = parent.locationOnScreen
        val pw = parent.width
        val ph = parent.height
        val dw = dialog.width
        val dh = dialog.height
        val defaultToolkit = Toolkit.getDefaultToolkit()
        val screenSize = defaultToolkit.screenSize
        val screenInsets = defaultToolkit.getScreenInsets(dialog
                .graphicsConfiguration)
        val minX = Math.max(parentLocation.x, screenInsets.left)
        val minY = Math.max(parentLocation.y, screenInsets.top)
        val maxX = Math.min(parentLocation.x + pw, screenSize.width
                - screenInsets.right)
        val maxY = Math.min(parentLocation.y + ph, screenSize.height
                - screenInsets.bottom)
        val dx: Int
        val dy: Int
        dx = if (compLocation.x + cw < minX) {
            minX
        } else if (compLocation.x > maxX) {
            maxX - dw
        } else  // component X on screen
        {
            val leftSpace = compLocation.x - minX
            val rightSpace = maxX - (compLocation.x + cw)
            if (leftSpace > rightSpace) {
                if (leftSpace > dw) {
                    compLocation.x - dw
                } else {
                    minX
                }
            } else {
                if (rightSpace > dw) {
                    compLocation.x + cw
                } else {
                    maxX - dw
                }
            }
        }
        dy = if (compLocation.y + ch < minY) {
            minY
        } else if (compLocation.y > maxY) {
            maxY - dh
        } else  // component Y on screen
        {
            val topSpace = compLocation.y - minY
            val bottomSpace = maxY - (compLocation.y + ch)
            if (topSpace > bottomSpace) {
                if (topSpace > dh) {
                    compLocation.y - dh
                } else {
                    minY
                }
            } else {
                if (bottomSpace > dh) {
                    compLocation.y + ch
                } else {
                    maxY - dh
                }
            }
        }
        dialog.setLocation(dx, dy)
    }

    /**
     * Creates a reader that pipes the input file through a XSLT-Script that
     * updates the version to the current.
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getUpdateReader(pReader: Reader, xsltScript: String): Reader {
        var writer: StringWriter? = null
        var inputStream: InputStream? = null
        val logger: Logger = Resources.Companion.getInstance().getLogger(Tools::class.java
                .name)
        logger.info("Updating the reader " + pReader
                + " to the current version.")
        var successful = false
        var errorMessage: String? = null
        try {
            // try to convert map with xslt:
            var updaterUrl: URL? = null
            updaterUrl = Resources.Companion.getInstance().getResource(xsltScript)
            requireNotNull(updaterUrl) { "$xsltScript not found." }
            inputStream = updaterUrl.openStream()
            val xsltSource: Source = StreamSource(inputStream)
            // get output:
            writer = StringWriter()
            val result: Result = StreamResult(writer)
            var fileContents = getFile(pReader)
            if (fileContents!!.length > 10) {
                logger.info("File start before UTF8 replacement: '"
                        + fileContents.substring(0, 9) + "'")
            }
            fileContents = replaceUtf8AndIllegalXmlChars(fileContents)
            if (fileContents.length > 10) {
                logger.info("File start after UTF8 replacement: '"
                        + fileContents.substring(0, 9) + "'")
            }
            val sr = StreamSource(StringReader(
                    fileContents))

            // Dimitry: to avoid a memory leak and properly release resources
            // after the XSLT transformation
            // everything should run in own thread. Only after the thread dies
            // the resources are released.
            class TransformerRunnable : Runnable {
                private val successful = false
                private val errorMessage: String? = null
                override fun run() {
                    // create an instance of TransformerFactory
                    val transFact = TransformerFactory
                            .newInstance()
                    logger.info("TransformerFactory class: "
                            + transFact.javaClass)
                    val trans: Transformer
                    try {
                        trans = transFact.newTransformer(xsltSource)
                        trans.transform(sr, result)
                        successful = true
                    } catch (ex: Exception) {
                        Resources.Companion.getInstance().logException(ex)
                        errorMessage = ex.toString()
                    }
                }

                fun isSuccessful(): Boolean {
                    return successful
                }

                fun getErrorMessage(): String? {
                    return errorMessage
                }
            }

            val transformer = TransformerRunnable()
            val transformerThread = Thread(transformer, "XSLT")
            transformerThread.start()
            transformerThread.join()
            logger.info("Updating the reader " + pReader
                    + " to the current version. Done.") // +
            // writer.getBuffer().toString());
            successful = transformer.isSuccessful()
            errorMessage = transformer.getErrorMessage()
        } catch (ex: Exception) {
            Resources.Companion.getInstance().logException(ex, xsltScript)
            errorMessage = ex.localizedMessage
        } finally {
            inputStream?.close()
            writer?.close()
        }
        return if (successful) {
            val content = writer!!.buffer.toString()
            // logger.info("Content before transformation: " + content);
            val replacedContent = replaceUtf8AndIllegalXmlChars(content)
            // logger.info("Content after transformation: " + replacedContent);
            StringReader(replacedContent)
        } else {
            StringReader("<map><node TEXT='"
                    + HtmlTools.Companion.toXMLEscapedText(errorMessage) + "'/></map>")
        }
    }

    fun replaceUtf8AndIllegalXmlChars(fileContents: String?): String {
        return HtmlTools.Companion.removeInvalidXmlCharacters(fileContents)
    }

    /**
     * Creates a default reader that just reads the given file.
     *
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun getActualReader(pReader: Reader?): Reader {
        return BufferedReader(pReader)
    }

    /**
     * In case of trouble, the method returns null.
     *
     * @param pInputFile
     * the file to read.
     * @return the complete content of the file. or null if an exception has
     * occured.
     */
    fun getFile(pInputFile: File?): String? {
        return try {
            getFile(getReaderFromFile(pInputFile))
        } catch (e: FileNotFoundException) {
            Resources.Companion.getInstance().logException(e)
            null
        }
    }

    @Throws(FileNotFoundException::class)
    fun getReaderFromFile(pInputFile: File?): Reader {
        return FileReader(pInputFile)
    }

    fun getFile(pReader: Reader?): String? {
        val lines = StringBuffer()
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = BufferedReader(pReader)
            val endLine = System.getProperty("line.separator")
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                lines.append(line).append(endLine)
            }
            bufferedReader.close()
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
            if (bufferedReader != null) {
                try {
                    bufferedReader.close()
                } catch (ex: Exception) {
                    Resources.Companion.getInstance().logException(ex)
                }
            }
            return null
        }
        return lines.toString()
    }

    fun logTransferable(t: Transferable) {
        System.err.println()
        System.err.println("BEGIN OF Transferable:\t$t")
        val dataFlavors = t.transferDataFlavors
        for (i in dataFlavors.indices) {
            println("  Flavor:\t" + dataFlavors[i])
            println("    Supported:\t"
                    + t.isDataFlavorSupported(dataFlavors[i]))
            try {
                println("    Content:\t"
                        + t.getTransferData(dataFlavors[i]))
            } catch (e: Exception) {
            }
        }
        System.err.println("END OF Transferable")
        System.err.println()
    }

    fun addEscapeActionToDialog(dialog: JDialog) {
        class EscapeAction : AbstractAction() {
            private static
            val serialVersionUID = 238333614987438806L
            override fun actionPerformed(e: ActionEvent) {
                dialog.dispose()
            }
        }
        addEscapeActionToDialog(dialog, EscapeAction())
    }

    fun addEscapeActionToDialog(dialog: JDialog?, action: Action) {
        addKeyActionToDialog(dialog, action, "ESCAPE", "end_dialog")
    }

    fun addKeyActionToDialog(dialog: JDialog, action: Action,
                             keyStroke: String?, actionId: String?) {
        action.putValue(Action.NAME, actionId)
        // Register keystroke
        dialog.rootPane
                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyStroke),
                        action.getValue(Action.NAME))

        // Register action
        dialog.rootPane.actionMap
                .put(action.getValue(Action.NAME), action)
    }

    /**
     * Removes the "TranslateMe" sign from the end of not translated texts.
     */
    fun removeTranslateComment(inputString: String?): String? {
        var inputString = inputString
        if (inputString != null
                && inputString.endsWith(FreeMindCommon.Companion.POSTFIX_TRANSLATE_ME)) {
            // remove POSTFIX_TRANSLATE_ME:
            inputString = inputString.substring(0, inputString.length
                    - FreeMindCommon.Companion.POSTFIX_TRANSLATE_ME.length)
        }
        return inputString
    }

    /**
     * Returns the same URL as input with the addition, that the reference part
     * "#..." is filtered out.
     *
     * @throws MalformedURLException
     */
    @Throws(MalformedURLException::class)
    fun getURLWithoutReference(input: URL): URL {
        return URL(input.toString().replaceFirst("#.*".toRegex(), ""))
    }

    @Throws(IOException::class)
    fun copyStream(`in`: InputStream, out: OutputStream,
                   pCloseOutput: Boolean) {
        val buf = ByteArray(1024)
        var len: Int
        while (`in`.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        `in`.close()
        if (pCloseOutput) {
            out.close()
        }
    }

    fun convertPointToAncestor(c: Component, p: Point,
                               destination: Component): Point {
        var c = c
        var x: Int
        var y: Int
        while (c !== destination) {
            x = c.x
            y = c.y
            p.x += x
            p.y += y
            c = c.parent
        }
        return p
    }

    fun convertPointFromAncestor(source: Component, p: Point,
                                 c: Component) {
        var c = c
        var x: Int
        var y: Int
        while (c !== source) {
            x = c.x
            y = c.y
            p.x -= x
            p.y -= y
            c = c.parent
        }
    }

    fun convertPointToAncestor(source: Component, point: Point,
                               ancestorClass: Class<*>?) {
        val destination: Component = SwingUtilities.getAncestorOfClass(
                ancestorClass, source)
        convertPointToAncestor(source, point, destination)
    }

    /**
     * Ampersand indicates that the character after it is a mnemo, unless the
     * character is a space. In "Find & Replace", ampersand does not label
     * mnemo, while in "&About", mnemo is "Alt + A".
     */
    fun setLabelAndMnemonic(btn: AbstractButton?, inLabel: String?) {
        setLabelAndMnemonic(ButtonHolder(btn), inLabel)
    }

    /**
     * Ampersand indicates that the character after it is a mnemo, unless the
     * character is a space. In "Find & Replace", ampersand does not label
     * mnemo, while in "&About", mnemo is "Alt + A".
     */
    fun setLabelAndMnemonic(action: Action, inLabel: String) {
        setLabelAndMnemonic(ActionHolder(action), inLabel)
    }

    private fun setLabelAndMnemonic(item: NameMnemonicHolder,
                                    inLabel: String) {
        var rawLabel: String? = inLabel
        if (rawLabel == null) rawLabel = item.text
        if (rawLabel == null) return
        item.text = removeMnemonic(rawLabel)
        val mnemoSignIndex = rawLabel.indexOf("&")
        if (mnemoSignIndex >= 0 && mnemoSignIndex + 1 < rawLabel.length) {
            val charAfterMnemoSign = rawLabel[mnemoSignIndex + 1]
            if (charAfterMnemoSign != ' ') {
                // no mnemonics under Mac OS:
                if (!isMacOsX) {
                    item.setMnemonic(charAfterMnemoSign)
                    // sets the underline to exactly this character.
                    item.setDisplayedMnemonicIndex(mnemoSignIndex)
                }
            }
        }
    }

    val isMacOsX: Boolean
        get() {
            var underMac = false
            val osName = System.getProperty("os.name")
            if (osName.startsWith("Mac OS")) {
                underMac = true
            }
            return underMac
        }
    val isLinux: Boolean
        get() {
            var underLinux = false
            val osName = System.getProperty("os.name")
            if (osName.startsWith("Linux")) {
                underLinux = true
            }
            return underLinux
        }

    fun removeMnemonic(rawLabel: String): String {
        return rawLabel.replaceFirst("&([^ ])".toRegex(), "$1")
    }

    fun getKeyStroke(keyStrokeDescription: String?): KeyStroke? {
        if (keyStrokeDescription == null) {
            return null
        }
        val keyStroke = KeyStroke
                .getKeyStroke(keyStrokeDescription)
        return keyStroke ?: KeyStroke.getKeyStroke("typed $keyStrokeDescription")
    }

    val JAVA_VERSION = System
            .getProperty("java.version")

    @Throws(MalformedURLException::class)
    fun fileToUrl(pFile: File?): URL? {
        return pFile?.toURI()?.toURL()
    }

    val isBelowJava6: Boolean
        get() = JAVA_VERSION.compareTo("1.6.0") < 0
    val isAboveJava4: Boolean
        get() = JAVA_VERSION.compareTo("1.4.0") > 0

    @Throws(URISyntaxException::class)
    fun urlToFile(pUrl: URL): File {
        // fix for java1.4 and java5 only.
        return if (isBelowJava6) {
            File(urlGetFile(pUrl))
        } else File(URI(pUrl.toString()))
    }

    fun restoreAntialiasing(g: Graphics2D, renderingHint: Any?) {
        if (RenderingHints.KEY_ANTIALIASING.isCompatibleValue(renderingHint)) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint)
        }
    }

    fun getFileNameProposal(node: MindMapNode): String {
        var rootText = node.plainTextContent
        rootText = rootText.replace("[&:/\\\\\u0000%$#~\\?\\*]+".toRegex(), "")
        return rootText
    }

    fun waitForEventQueue() {
        try {
            // wait until AWT thread starts
            // final Exception e = new IllegalArgumentException("HERE");
            if (!EventQueue.isDispatchThread()) {
                EventQueue.invokeAndWait {
                    // logger.info("Waited for event queue.");
                    // e.printStackTrace();
                }
            } else {
                logger!!.warning("Can't wait for event queue, if I'm inside this queue!")
            }
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
        }
    }

    /**
     * Logs the stacktrace via a dummy exception.
     */
    fun printStackTrace() {
        Resources.Companion.getInstance().logException(
                IllegalArgumentException("HERE"))
    }

    /**
     * Logs the stacktrace into a string.
     */
    val stackTrace: String
        get() {
            val ex = IllegalArgumentException("HERE")
            return getStacktrace(ex)
        }

    /**
     * Adapts the font size inside of a component to the zoom
     *
     * @param c
     * component
     * @param zoom
     * zoom factor
     * @param normalFontSize
     * "unzoomed" normal font size.
     * @return a copy of the input font (if the size was effectively changed)
     * with the correct scale.
     */
    fun updateFontSize(font: Font?, zoom: Float, normalFontSize: Int): Font? {
        var font = font
        if (font != null) {
            val oldFontSize = font.size2D
            val newFontSize = normalFontSize * zoom
            if (oldFontSize != newFontSize) {
                font = font.deriveFont(newFontSize)
            }
        }
        return font
    }

    fun compareText(pText1: String?, pText2: String?): String {
        if (pText1 == null || pText2 == null) {
            return "One of the Strings is null $pText1, $pText2"
        }
        val b = StringBuffer()
        if (pText1.length > pText2.length) {
            b.append("""
    First string is longer :${pText1.substring(pText2.length)}
    
    """.trimIndent())
        }
        if (pText1.length < pText2.length) {
            b.append("""
    Second string is longer :${pText2.substring(pText1.length)}
    
    """.trimIndent())
        }
        for (i in 0 until Math.min(pText1.length, pText2.length)) {
            if (pText1[i] != pText2[i]) {
                b.append("""
    Difference at $i: ${pText1[i]}!=${pText2[i]}
    
    """.trimIndent())
            }
        }
        return b.toString()
    }

    val hostName: String
        get() {
            var hostname = "UNKNOWN"
            try {
                val addr = InetAddress.getLocalHost()
                hostname = addr.hostName
            } catch (e: UnknownHostException) {
            }
            return hostname
        }

    // Get host name
    val userName: String
        get() {
            // Get host name
            val hostname = hostName
            return System.getProperty("user.name") + "@" + hostname
        }

    fun marshall(action: XmlAction?): String {
        return XmlBindingTools.getInstance().marshall(action)
    }

    fun unMarshall(inputString: String?): XmlAction {
        return XmlBindingTools.getInstance().unMarshall(inputString)
    }

    fun getFileNameFromRestorable(restoreable: String?): String? {
        val token = StringTokenizer(restoreable, ":")
        val fileName: String?
        fileName = if (token.hasMoreTokens()) {
            token.nextToken()
            // fix for windows (??, fc, 25.11.2005).
            token.nextToken("").substring(1)
        } else {
            null
        }
        return fileName
    }

    fun getModeFromRestorable(restoreable: String?): String? {
        val token = StringTokenizer(restoreable, ":")
        val mode: String?
        mode = if (token.hasMoreTokens()) {
            token.nextToken()
        } else {
            null
        }
        return mode
    }

    fun <T> getVectorWithSingleElement(obj: T): Vector<T> {
        val nodes = Vector<T>()
        nodes.add(obj)
        return nodes
    }

    fun <T> swapVectorPositions(pVector: Vector<T>, src: Int, dst: Int) {
        require(!(src >= pVector.size || dst >= pVector.size || src < 0 || dst < 0)) {
            ("One index is out of bounds "
                    + src + ", " + dst + ", size= " + pVector.size)
        }
        pVector[dst] = pVector.set(src, pVector[dst])
    }

    @Throws(IllegalArgumentException::class, SecurityException::class, IllegalAccessException::class, NoSuchFieldException::class)
    fun getField(pObjects: Array<Any>, pField: String?): Any? {
        for (i in pObjects.indices) {
            val `object` = pObjects[i]
            for (j in `object`.javaClass.fields.indices) {
                val f = `object`.javaClass.fields[j]
                if (safeEquals(pField, f.name)) {
                    return `object`.javaClass.getField(pField)[`object`]
                }
            }
        }
        return null
    }

    val isUnix: Boolean
        get() = File.separatorChar == '/' || isMacOsX
    // {{{ setPermissions() method
    /**
     * Sets numeric permissions of a file. On non-Unix platforms, does nothing.
     * From jEdit
     */
    fun setPermissions(path: String, permissions: Int) {
        if (permissions != 0) {
            if (isUnix) {
                val cmdarray = arrayOf("chmod",
                        Integer.toString(permissions, 8), path)
                try {
                    val process = Runtime.getRuntime().exec(cmdarray)
                    process.inputStream.close()
                    process.outputStream.close()
                    process.errorStream.close()
                    // Jun 9 2004 12:40 PM
                    // waitFor() hangs on some Java
                    // implementations.
                    /*
					 * int exitCode = process.waitFor(); if(exitCode != 0)
					 * Log.log
					 * (Log.NOTICE,FileVFS.class,"chmod exited with code " +
					 * exitCode);
					 */
                } // Feb 4 2000 5:30 PM
                // Catch Throwable here rather than Exception.
                // Kaffe's implementation of Runtime.exec throws
                // java.lang.InternalError.
                catch (t: Throwable) {
                }
            }
        }
    } // }}}

    fun arrayToUrls(pArgs: Array<String>): String {
        val b = StringBuffer()
        for (i in pArgs.indices) {
            val fileName = pArgs[i]
            try {
                b.append(fileToUrl(File(fileName)))
                b.append('\n')
            } catch (e: MalformedURLException) {
                Resources.Companion.getInstance().logException(e)
            }
        }
        return b.toString()
    }

    fun urlStringToUrls(pUrls: String): Vector<URL> {
        val urls = pUrls.split("\n").toTypedArray()
        val ret = Vector<URL>()
        for (i in urls.indices) {
            val url = urls[i]
            try {
                ret.add(URL(url))
            } catch (e: MalformedURLException) {
                Resources.Companion.getInstance().logException(e)
            }
        }
        return ret
    }

    /**
     * @return
     */
    val isHeadless: Boolean
        get() = GraphicsEnvironment.isHeadless()

    /**
     * @param pNode
     * @param pMindMapController
     * @return
     */
    fun getNodeTextHierarchy(pNode: MindMapNode,
                             pMindMapController: MindMapController?): String {
        return (pNode.getShortText(pMindMapController)
                + if (pNode.isRoot) "" else " <- " + getNodeTextHierarchy(
                pNode.parentNode, pMindMapController))
    }

    /**
     */
    val clipboard: Clipboard
        get() = Toolkit.getDefaultToolkit().systemClipboard

    /**
     * @return a list of MindMapNode s if they are currently contained in the clipboard. An empty list otherwise.
     */
    fun getMindMapNodesFromClipboard(pMindMapController: MindMapController): Vector<MindMapNode> {
        val mindMapNodes = Vector<MindMapNode>()
        val clipboardContents = pMindMapController.clipboardContents
        if (clipboardContents != null) {
            try {
                val transferData = clipboardContents.getTransferData(MindMapNodesSelection.copyNodeIdsFlavor) as List<String>
                val it = transferData.iterator()
                while (it.hasNext()) {
                    val node: MindMapNode = pMindMapController.getNodeFromID(it.next())
                    mindMapNodes.add(node)
                }
            } catch (e: Exception) {
                // e.printStackTrace();
                // freemind.main.Resources.getInstance().logException(e);
            }
        }
        return mindMapNodes
    }

    fun addFocusPrintTimer() {
        val timer = Timer(1000) {
            logger!!.info("Component: "
                    + KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .focusOwner
                    + ", Window: "
                    + KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .focusedWindow)
        }
        timer.start()
    }

    /**
     * copied from HomePane.java 15 mai 2006
     *
     * Sweet Home 3D, Copyright (c) 2006 Emmanuel PUYBARET / eTeks
     * <info></info>@eteks.com>
     *
     * - This listener manages accelerator keys that may require the use of
     * shift key depending on keyboard layout (like + - or ?)
     */
    fun invokeActionsToKeyboardLayoutDependantCharacters(
            pEvent: KeyEvent, specialKeyActions: Array<Action>, pObject: Any?) {
        // on purpose without shift.
        val modifiersMask = (KeyEvent.ALT_MASK or KeyEvent.CTRL_MASK
                or KeyEvent.META_MASK)
        for (i in specialKeyActions.indices) {
            val specialKeyAction = specialKeyActions[i]
            val actionKeyStroke = specialKeyAction
                    .getValue(Action.ACCELERATOR_KEY) as KeyStroke
            if (pEvent.keyChar == actionKeyStroke.keyChar && pEvent.modifiers and modifiersMask == actionKeyStroke
                            .modifiers and modifiersMask && specialKeyAction.isEnabled) {
                specialKeyAction.actionPerformed(ActionEvent(pObject,
                        ActionEvent.ACTION_PERFORMED, specialKeyAction
                        .getValue(Action.ACTION_COMMAND_KEY) as String))
                pEvent.consume()
            }
        }
    }

    /**
     * @param pString
     * @param pSearchString
     * @return the amount of occurrences of pSearchString in pString.
     */
    fun countOccurrences(pString: String, pSearchString: String): Int {
        var pString = pString
        var amount = 0
        while (true) {
            val index = pString.indexOf(pSearchString)
            if (index < 0) {
                break
            }
            amount++
            pString = pString.substring(index + pSearchString.length)
        }
        return amount
    }

    fun correctJSplitPaneKeyMap() {
        val map = UIManager.get("SplitPane.ancestorInputMap") as InputMap
        val keyStrokeF6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0)
        val keyStrokeF8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0)
        map.remove(keyStrokeF6)
        map.remove(keyStrokeF8)
    }

    /**
     * @param pPageFormat
     * @param pPageFormatProperty
     */
    fun setPageFormatFromString(pPaper: Paper,
                                pPageFormatProperty: String) {
        try {
            // parse string:
            val tokenizer = StringTokenizer(
                    pPageFormatProperty, ";")
            if (tokenizer.countTokens() != 6) {
                logger!!.warning("Page format property has not the correct format:"
                        + pPageFormatProperty)
                return
            }
            pPaper.setSize(nt(tokenizer), nt(tokenizer))
            pPaper.setImageableArea(nt(tokenizer), nt(tokenizer),
                    nt(tokenizer), nt(tokenizer))
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
        }
    }

    /**
     * @param pTokenizer
     * @return
     */
    private fun nt(pTokenizer: StringTokenizer): Double {
        val nextToken = pTokenizer.nextToken()
        try {
            return nextToken.toDouble()
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
        }
        return 0
    }

    /**
     * @param pPageFormat
     * @return
     */
    fun getPageFormatAsString(pPaper: Paper): String {
        return (pPaper.width.toString() + ";" + pPaper.height + ";"
                + pPaper.imageableX + ";" + pPaper.imageableY + ";"
                + pPaper.imageableWidth + ";"
                + pPaper.imageableHeight)
    }

    /**
     * @return
     */
    val hostIpAsString: String?
        get() {
            try {
                return InetAddress.getLocalHost().hostAddress
            } catch (e: UnknownHostException) {
                Resources.Companion.getInstance().logException(e)
            }
            return null
        }

    fun printXmlAction(pAction: XmlAction): String {
        val classString = pAction.javaClass.name
                .replace(".*\\.".toRegex(), "")
        if (pAction is CompoundAction) {
            val buf = StringBuffer("[")
            val it: Iterator<XmlAction> = pAction.listChoiceList.iterator()
            while (it
                            .hasNext()) {
                if (buf.length > 1) {
                    buf.append(',')
                }
                val subAction = it.next()
                buf.append(printXmlAction(subAction))
            }
            buf.append(']')
            return "$classString $buf"
        }
        return classString
    }

    fun deepCopy(action: XmlAction?): XmlAction {
        return unMarshall(marshall(action))
    }

    fun generateID(proposedID: String?, map: Map<*, *>,
                   prefix: String): String {
        var myProposedID = proposedID ?: ""
        var returnValue: String
        do {
            if (!myProposedID.isEmpty()) {
                // there is a proposal:
                returnValue = myProposedID
                // this string is tried only once:
                myProposedID = ""
            } else {
                /*
				 * The prefix is to enable the id to be an ID in the sense of
				 * XML/DTD.
				 */
                returnValue = (prefix
                        + Integer.toString(ran.nextInt(2000000000)))
            }
        } while (map.containsKey(returnValue))
        return returnValue
    }

    /**
     * Call this method, if you don't know, if you are in the event thread or
     * not. It checks this and calls the invokeandwait or the runnable directly.
     *
     * @param pRunnable
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    @Throws(InvocationTargetException::class, InterruptedException::class)
    fun invokeAndWait(pRunnable: Runnable) {
        if (EventQueue.isDispatchThread()) {
            pRunnable.run()
        } else {
            EventQueue.invokeAndWait(pRunnable)
        }
    }

    @get:Throws(UnsupportedEncodingException::class)
    val freeMindBasePath: String
        get() {
            val path = FreeMindStarter::class.java.protectionDomain
                    .codeSource.location.path
            var decodedPath = URLDecoder.decode(path, "UTF-8")
            logger!!.info("Path: $decodedPath")
            if (decodedPath.endsWith(CONTENTS_JAVA_FREEMIND_JAR)) {
                decodedPath = decodedPath.substring(0, decodedPath.length
                        - CONTENTS_JAVA_FREEMIND_JAR.length)
                decodedPath = decodedPath + FREE_MIND_APP_CONTENTS_RESOURCES_JAVA
                logger!!.info("macPath: $decodedPath")
            } else if (decodedPath.endsWith(FREEMIND_LIB_FREEMIND_JAR)) {
                decodedPath = decodedPath.substring(0, decodedPath.length
                        - FREEMIND_LIB_FREEMIND_JAR.length)
                logger!!.info("reducded Path: $decodedPath")
            }
            return decodedPath
        }

    fun copyChangedProperties(props2: Properties,
                              defProps2: Properties): Properties {
        val toBeStored = Properties()
        val it: Iterator<*> = props2.keys.iterator()
        while (it.hasNext()) {
            val key = it.next() as String
            if (!safeEquals(props2[key], defProps2[key])) {
                toBeStored[key] = props2[key]
            }
        }
        return toBeStored
    }

    /**
     * Returns pMinimumLength bytes of the files content.
     *
     * @return an empty string buffer, if something fails.
     */
    fun readFileStart(pReader: Reader?, pMinimumLength: Int): StringBuffer {
        var `in`: BufferedReader? = null
        val buffer = StringBuffer()
        try {
            // get the file start into the memory:
            `in` = BufferedReader(pReader)
            var str: String?
            while (`in`.readLine().also { str = it } != null) {
                buffer.append(str)
                if (buffer.length >= pMinimumLength) break
            }
            `in`.close()
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
            return StringBuffer()
        }
        return buffer
    }

    /**
     */
    fun edgeWidthStringToInt(value: String?): Int {
        if (value == null) {
            return EdgeAdapter.DEFAULT_WIDTH
        }
        return if (value == EdgeAdapter.EDGE_WIDTH_THIN_STRING) {
            EdgeAdapter.WIDTH_THIN
        } else Integer.valueOf(value).toInt()
    }

    fun iconFirstIndex(node: MindMapNode, iconName: String): Int {
        val icons = node.icons
        val i: ListIterator<MindIcon> = icons.listIterator()
        while (i.hasNext()) {
            val nextIcon = i.next()
            if (iconName == nextIcon.name) return i.previousIndex()
        }
        return -1
    }

    fun iconLastIndex(node: MindMapNode, iconName: String): Int {
        val icons = node.icons
        val i: ListIterator<MindIcon> = icons.listIterator(icons.size)
        while (i.hasPrevious()) {
            val nextIcon = i.previous()
            if (iconName == nextIcon.name) return i.nextIndex()
        }
        return -1
    }

    // http://stackoverflow.com/questions/1149703/stacktrace-to-string-in-java
    fun getStacktrace(e: Exception): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        return sw.toString()
    }

    fun makeFileHidden(file: File, setHidden: Boolean) {
        try {
            if (!file.exists() || !isWindows) {
                return
            }
            val path = file.toPath()
            val attrs = Files.readAttributes(path, DosFileAttributes::class.java)
            if (setHidden != attrs.isHidden) {
                Files.setAttribute(path, "dos:hidden", setHidden)
            }
        } catch (e: IOException) {
            Resources.Companion.getInstance().logException(e)
        }
    }

    val isRetina: Boolean
        get() {
            val env = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
            val device = env.defaultScreenDevice
            try {
                val field = device.javaClass.getDeclaredField("scale")
                if (field != null) {
                    field.isAccessible = true
                    val scale = field[device]
                    if (scale is Int
                            && scale.toInt() == 2) {
                        return true
                    }
                }
            } catch (ignore: Exception) {
            }
            return false
        }

    fun scaleAllFonts(pScale: Float) {
        for (next in UIManager.getLookAndFeelDefaults().keys) {
            if (next is String) {
                val key = next
                if (key.endsWith(".font")) {
                    val font = UIManager.getFont(key)
                    val biggerFont = font.deriveFont(pScale * font.size2D)
                    // change ui default to bigger font
                    UIManager.put(key, biggerFont)
                }
            }
        }
    }

    val scalingFactor: Float
        get() = scalingFactorPlain / 100.0f
    val scalingFactorPlain: Int
        get() = Resources.Companion.getInstance().getIntProperty(FreeMind.Companion.SCALING_FACTOR_PROPERTY, 100)

    class IntHolder {
        var value = 0

        constructor() {}
        constructor(value: Int) {
            this.value = value
        }

        override fun toString(): String {
            return "IntHolder($value)"
        }

        fun increase() {
            value++
        }
    }

    class BooleanHolder {
        var value = false

        constructor() {}
        constructor(initialValue: Boolean) {
            value = initialValue
        }
    }

    class ObjectHolder {
        var `object`: Any? = null
            set(object) {
                field = `object`
            }
    }

    class Pair(var first: Any, var second: Any)

    /** from: http://javaalmanac.com/egs/javax.crypto/PassKey.html  */
    open class DesEncrypter(pPassPhrase: StringBuffer, pAlgorithm: String) {
        var ecipher: Cipher? = null
        var dcipher: Cipher? = null

        // 8-byte default Salt
        var salt = byteArrayOf(0xA9.toByte(), 0x9B.toByte(), 0xC8.toByte(), 0x32.toByte(), 0x56.toByte(), 0x35.toByte(), 0xE3.toByte(), 0x03.toByte())

        // Iteration count
        var iterationCount = 19
        private val passPhrase: CharArray
        private val mAlgorithm: String

        init {
            passPhrase = CharArray(pPassPhrase.length)
            pPassPhrase.getChars(0, passPhrase.size, passPhrase, 0)
            mAlgorithm = pAlgorithm
        }

        /**
         */
        private fun init(mSalt: ByteArray?) {
            if (mSalt != null) {
                salt = mSalt
            }
            if (ecipher == null) {
                try {
                    // Create the key
                    val keySpec: KeySpec = PBEKeySpec(passPhrase, salt,
                            iterationCount)
                    val key = SecretKeyFactory.getInstance(mAlgorithm)
                            .generateSecret(keySpec)
                    ecipher = Cipher.getInstance(mAlgorithm)
                    dcipher = Cipher.getInstance(mAlgorithm)

                    // Prepare the parameter to the ciphers
                    val paramSpec: AlgorithmParameterSpec = PBEParameterSpec(
                            salt, iterationCount)

                    // Create the ciphers
                    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec)
                    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec)
                } catch (e: InvalidAlgorithmParameterException) {
                } catch (e: InvalidKeySpecException) {
                } catch (e: NoSuchPaddingException) {
                } catch (e: NoSuchAlgorithmException) {
                } catch (e: InvalidKeyException) {
                }
            }
        }

        fun encrypt(str: String): String? {
            try {
                // Encode the string into bytes using utf-8
                val utf8 = str.toByteArray(charset("UTF8"))
                // determine salt by random:
                val newSalt = ByteArray(SALT_LENGTH)
                for (i in newSalt.indices) {
                    newSalt[i] = (Math.random() * 256L - 128L).toByte()
                }
                init(newSalt)
                // Encrypt
                val enc = ecipher!!.doFinal(utf8)

                // Encode bytes to base64 to get a string
                return (toBase64(newSalt) + SALT_PRESENT_INDICATOR
                        + toBase64(enc))
            } catch (e: BadPaddingException) {
            } catch (e: IllegalBlockSizeException) {
            } catch (e: UnsupportedEncodingException) {
            }
            return null
        }

        fun decrypt(str: String?): String? {
            var str = str ?: return null
            try {
                var salt: ByteArray? = null
                // test if salt exists:
                val indexOfSaltIndicator = str.indexOf(SALT_PRESENT_INDICATOR)
                if (indexOfSaltIndicator >= 0) {
                    val saltString = str.substring(0, indexOfSaltIndicator)
                    str = str.substring(indexOfSaltIndicator + 1)
                    salt = fromBase64(saltString)
                }
                // Decode base64 to get bytes
                str = str.replace("\\s".toRegex(), "")
                val dec = fromBase64(str)
                init(salt)

                // Decrypt
                val utf8 = dcipher!!.doFinal(dec)

                // Decode using utf-8
                return String(utf8, "UTF8")
            } catch (e: BadPaddingException) {
            } catch (e: IllegalBlockSizeException) {
            } catch (e: UnsupportedEncodingException) {
            }
            return null
        }

        companion object {
            private const val SALT_PRESENT_INDICATOR = " "
            private const val SALT_LENGTH = 8
        }
    }

    class SingleDesEncrypter(pPassPhrase: StringBuffer) : DesEncrypter(pPassPhrase, "PBEWithMD5AndDES")
    class TripleDesEncrypter(pPassPhrase: StringBuffer) : DesEncrypter(pPassPhrase, "PBEWithMD5AndTripleDES")
    internal interface NameMnemonicHolder {
        /**
         */
        /**
         */
        var text: String?

        /**
         */
        fun setMnemonic(charAfterMnemoSign: Char)

        /**
         */
        fun setDisplayedMnemonicIndex(mnemoSignIndex: Int)
    }

    private class ButtonHolder(private val btn: AbstractButton) : NameMnemonicHolder {
        /*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#getText()
		 *//*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#setText(java.lang.String)
		 */
        override var text: String?
            get() = btn.text
            set(text) {
                btn.text = text
            }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * freemind.main.Tools.IAbstractButton#setDisplayedMnemonicIndex(int)
		 */
        override fun setDisplayedMnemonicIndex(mnemoSignIndex: Int) {
            btn.displayedMnemonicIndex = mnemoSignIndex
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#setMnemonic(char)
		 */
        override fun setMnemonic(charAfterMnemoSign: Char) {
            btn.setMnemonic(charAfterMnemoSign)
        }
    }

    private class ActionHolder(private val action: Action) : NameMnemonicHolder {
        /*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#getText()
		 *//*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#setText(java.lang.String)
		 */
        override var text: String?
            get() = action.getValue(Action.NAME).toString()
            set(text) {
                action.putValue(Action.NAME, text)
            }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * freemind.main.Tools.IAbstractButton#setDisplayedMnemonicIndex(int)
		 */
        override fun setDisplayedMnemonicIndex(mnemoSignIndex: Int) {}

        /*
		 * (non-Javadoc)
		 * 
		 * @see freemind.main.Tools.IAbstractButton#setMnemonic(char)
		 */
        override fun setMnemonic(charAfterMnemoSign: Char) {
            var vk = charAfterMnemoSign.code
            if (vk >= 'a'.code && vk <= 'z'.code) vk -= 'a' - 'A'
            action.putValue(Action.MNEMONIC_KEY, vk)
        }
    }

    class MindMapNodePair(var corresponding: MindMapNode, var cloneNode: MindMapNode)
    class FileReaderCreator(private val mFile: File) : ReaderCreator {
        @Throws(FileNotFoundException::class)
        override fun createReader(): Reader {
            return UnicodeReader(FileInputStream(mFile), "UTF-8")
        }

        override fun toString(): String {
            return mFile.name
        }
    }

    class StringReaderCreator(private val mString: String) : ReaderCreator {
        @Throws(FileNotFoundException::class)
        override fun createReader(): Reader {
            return StringReader(mString)
        }

        override fun toString(): String {
            return mString
        }
    }

    interface ReaderCreator {
        @Throws(FileNotFoundException::class)
        fun createReader(): Reader
    }
}