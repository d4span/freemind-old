/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2006  Christian Foltin <christianfoltin@users.sourceforge.net>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: HtmlTools.java,v 1.1.2.28 2010/12/04 21:07:23 christianfoltin Exp $*/
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
import java.awt.Color
import java.util.StringTokenizer
import java.util.LinkedList
import java.lang.StringBuffer
import java.awt.GraphicsEnvironment
import java.util.Locale
import java.net.MalformedURLException
import freemind.main.Base64Coding
import java.util.zip.Deflater
import java.util.zip.Inflater
import java.util.zip.DataFormatException
import java.lang.RuntimeException
import freemind.main.Tools.BooleanHolder
import javax.swing.JDialog
import java.awt.Dimension
import java.awt.Insets
import kotlin.Throws
import java.lang.Runnable
import freemind.main.HtmlTools
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.KeyStroke
import freemind.main.FreeMindCommon
import javax.swing.SwingUtilities
import javax.swing.AbstractButton
import freemind.main.Tools.ButtonHolder
import freemind.main.Tools.ActionHolder
import freemind.main.Tools.NameMnemonicHolder
import java.net.URISyntaxException
import java.awt.Graphics2D
import java.awt.RenderingHints
import freemind.modes.MindMapNode
import java.net.InetAddress
import freemind.controller.actions.generated.instance.XmlAction
import freemind.common.XmlBindingTools
import java.lang.SecurityException
import java.lang.IllegalAccessException
import java.lang.NoSuchFieldException
import freemind.modes.mindmapmode.MindMapController
import java.awt.datatransfer.Clipboard
import freemind.controller.MindMapNodesSelection
import java.awt.event.ActionListener
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent
import javax.swing.InputMap
import javax.swing.UIManager
import java.awt.print.Paper
import freemind.controller.actions.generated.instance.CompoundAction
import java.lang.reflect.InvocationTargetException
import java.lang.InterruptedException
import freemind.main.FreeMindStarter
import java.net.URLDecoder
import java.util.Properties
import freemind.modes.EdgeAdapter
import freemind.modes.MindIcon
import java.nio.file.attribute.DosFileAttributes
import java.awt.GraphicsDevice
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
import javax.swing.JFrame
import freemind.main.FreeMindMain
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTabbedPane
import javax.swing.ImageIcon
import freemind.main.FreeMindMain.StartupDoneListener
import freemind.main.EditServer
import freemind.main.FreeMindSecurityManager
import java.util.Collections
import freemind.main.FeedBack
import java.awt.BorderLayout
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import freemind.view.mindmapview.MapView
import java.awt.Desktop
import java.awt.Cursor
import java.util.ResourceBundle
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
import com.inet.jortho.SpellChecker
import freemind.main.FreeMindStarter.ProxyAuthenticator
import java.net.Socket
import java.awt.event.InputEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import freemind.modes.ModeController
import freemind.controller.LastStateStorageManagement
import freemind.view.MapModule
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JPanel
import freemind.main.IFreeMindSplash
import freemind.main.FreeMindSplashModern
import java.awt.event.WindowFocusListener
import javax.swing.JOptionPane
import freemind.main.XHTMLWriter
import javax.swing.text.BadLocationException
import freemind.main.HtmlTools.IndexPair
import freemind.main.HtmlTools.NodeCreator
import org.jsoup.select.NodeTraversor
import freemind.main.HtmlTools.HtmlNodeVisitor
import org.jsoup.Jsoup
import kotlin.jvm.JvmOverloads
import freemind.common.TextTranslator
import java.text.MessageFormat
import freemind.modes.FreeMindFileDialog
import freemind.modes.FreeMindJFileDialog
import freemind.modes.FreeMindAwtFileDialog
import tests.freemind.FreeMindMainMock
import java.net.ServerSocket
import java.util.TreeMap
import freemind.main.XMLElement
import java.util.Enumeration
import java.lang.ClassCastException
import freemind.main.FixedHTMLWriter
import freemind.main.XHTMLWriter.XHTMLFilterWriter
import javax.swing.text.html.HTMLEditorKit
import kotlin.jvm.JvmStatic
import javax.swing.JLayeredPane
import freemind.main.StdFormatter.StdOutErrLevel
import javax.swing.JApplet
import freemind.main.FreeMindApplet
import java.util.PropertyResourceBundle
import freemind.main.FreeMindCommon.FreeMindResourceBundle
import java.net.URLClassLoader
import javax.swing.text.html.HTMLWriter
import javax.swing.text.MutableAttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.html.HTML
import javax.swing.text.StyleConstants
import javax.swing.text.html.CSS
import java.net.PasswordAuthentication
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.main.FreeMindSplashModern.FeedBackImpl
import javax.swing.JProgressBar
import freemind.view.ImageFactory
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeVisitor
import org.xml.sax.InputSource
import org.xml.sax.SAXParseException
import org.xml.sax.helpers.DefaultHandler
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.ArrayList
import java.util.logging.*
import java.util.regex.Pattern
import javax.xml.parsers.SAXParserFactory

/**  */
class HtmlTools private constructor() {
    /**
     *
     */
    init {
        logger = Resources.Companion.getInstance().getLogger(HtmlTools::class.java.name)
    }

    fun toXhtml(htmlText: String): String? {
        if (!isHtmlNode(htmlText)) {
            return null
        }
        logger!!.fine("Enter toXhtml with $htmlText")
        val reader = StringReader(htmlText)
        val writer = StringWriter()
        try {
            XHTMLWriter.Companion.html2xhtml(reader, writer)
            val resultXml = writer.toString()
            // for safety:
            if (isWellformedXml(resultXml)) {
                logger!!.fine("Leave toXhtml with $resultXml")
                return resultXml
            }
        } catch (e: IOException) {
            Resources.Companion.getInstance().logException(e)
        } catch (e: BadLocationException) {
            Resources.Companion.getInstance().logException(e)
        }
        // fallback:
        val fallbackText = removeAllTagsFromString(htmlText)
        logger!!.fine("Leave toXhtml with fallback $fallbackText")
        return fallbackText
    }

    fun toHtml(xhtmlText: String?): String {
        // Remove '/' from <.../> of elements that do not have '/' there in HTML
        return SLASHED_TAGS_PATTERN.matcher(xhtmlText).replaceAll("<$1>")
    }

    class IndexPair
    /**
     * @param pIsTag
     * TODO
     */(var originalStart: Int, var originalEnd: Int,
        var replacedStart: Int, var replacedEnd: Int, var mIsTag: Boolean) {
        var mIsAlreadyAppended = false

        /**
         * generated by CodeSugar http://sourceforge.net/projects/codesugar
         */
        override fun toString(): String {
            val buffer = StringBuffer()
            buffer.append("[IndexPair:")
            buffer.append(" originalStart: ")
            buffer.append(originalStart)
            buffer.append(" originalEnd: ")
            buffer.append(originalEnd)
            buffer.append(" replacedStart: ")
            buffer.append(replacedStart)
            buffer.append(" replacedEnd: ")
            buffer.append(replacedEnd)
            buffer.append(" is a tag: ")
            buffer.append(mIsTag)
            buffer.append("]")
            return buffer.toString()
        }
    }

    /**
     * Replaces text in node content without replacing tags. fc, 19.12.06: This
     * method is very difficult. If you have a simplier method, please supply
     * it. But look that it complies with FindTextTests!!!
     */
    fun getReplaceResult(pattern: Pattern, replacement: String?,
                         text: String): String {
        val splittedStringList = ArrayList<IndexPair>()

        // remove tags and denote their positions:
        run {
            val sb = StringBuffer()
            val matcher = FIND_TAGS_PATTERN.matcher(text)
            var lastMatchEnd = 0
            while (matcher.find()) {
                val textWithoutTag = matcher.group(1)
                // Append text without tags:
                var replStart = sb.length
                matcher.appendReplacement(sb, "$1")
                var indexPair: IndexPair
                if (textWithoutTag.length > 0) {
                    indexPair = IndexPair(lastMatchEnd, matcher.end(1),
                            replStart, sb.length, false)
                    lastMatchEnd = matcher.end(1)
                    // System.out.println(sb.toString()
                    // + ", "
                    // + input.substring(indexPair.originalStart,
                    // indexPair.originalEnd) + ", " + indexPair);
                    splittedStringList.add(indexPair)
                }
                // String tag = matcher.group(2);
                replStart = sb.length
                indexPair = IndexPair(lastMatchEnd, matcher.end(2),
                        replStart, sb.length, true)
                lastMatchEnd = matcher.end(2)
                // System.out.println(sb.toString() + ", " +
                // input.substring(indexPair.originalStart,
                // indexPair.originalEnd)+ ", " + indexPair);
                splittedStringList.add(indexPair)
            }
            val replStart = sb.length
            matcher.appendTail(sb)
            // append tail only if there is a tail
            if (sb.length != replStart) {
                val indexPair = IndexPair(lastMatchEnd,
                        text.length, replStart, sb.length, false)
                // System.out.println(sb.toString() + ", " + indexPair);
                splittedStringList.add(indexPair)
            }
        }

        // // give it out:
        // for (Iterator iter = splittedStringList.iterator(); iter.hasNext();)
        // {
        // IndexPair pair = (IndexPair) iter.next();
        // System.out.println(text.substring(pair.originalStart,
        // pair.originalEnd) + ", " + pair);
        // }
        /**
         * For each pair which is not a tag we find concurrences and replace
         * them, if pair is a tag then we just append
         */
        val sbResult = StringBuffer()
        for (pair in splittedStringList) {
            if (pair.mIsTag) append(sbResult, text, pair.originalStart, pair.originalEnd) else {
                val matcher = pattern.matcher(text.substring(
                        pair.originalStart, pair.originalEnd))
                var mStart = 0
                var mEnd = 0
                var mEndOld = 0
                while (matcher.find()) {
                    mStart = matcher.start()
                    mEnd = matcher.end()
                    append(sbResult, text, pair.originalStart + mEndOld,
                            pair.originalStart + mStart)
                    /**
                     * If it's a first iteration then we append text between
                     * start and first concurrence, and when it's not first
                     * iteration (mEndOld != 0) we append text between two
                     * concurrences
                     */

                    // sbResult.append(text, pair.originalStart + mStart,
                    // pair.originalStart + mEnd);
                    // original text
                    sbResult.append(replacement)
                    mEndOld = mEnd
                }
                append(sbResult, text, pair.originalStart + mEndOld,
                        pair.originalEnd)
                // append tail
            }
        }
        // System.out.println("Result:'"+sbResult.toString()+"'");
        return sbResult.toString()
    }

    /**
     * Need to program this, as the stringbuffer method appears in java 1.5
     * first.
     */
    private fun append(pSbResult: StringBuffer, pText: String, pStart: Int,
                       pEnd: Int) {
        pSbResult.append(pText.substring(pStart, pEnd))
    }

    fun getMinimalOriginalPosition(pI: Int, pListOfIndices: ArrayList<IndexPair>): Int {
        for (pair in pListOfIndices) {
            if (pI >= pair.replacedStart && pI <= pair.replacedEnd) {
                return pair.originalStart + pI - pair.replacedStart
            }
        }
        throw IllegalArgumentException("Position $pI not found.")
    }

    /**
     * @return the maximal index i such that pI is mapped to i by removing all
     * tags from the original input.
     */
    fun getMaximalOriginalPosition(pI: Int, pListOfIndices: ArrayList<IndexPair>): Int {
        for (i in pListOfIndices.indices.reversed()) {
            val pair = pListOfIndices[i]
            if (pI >= pair.replacedStart) {
                return if (!pair.mIsTag) {
                    pair.originalStart + pI - pair.replacedStart
                } else {
                    pair.originalEnd
                }
            }
        }
        throw IllegalArgumentException("Position $pI not found.")
    }

    /**
     * @return true, if well formed XML.
     */
    fun isWellformedXml(xml: String): Boolean {
        try {
            // Create a builder factory
            val factory = SAXParserFactory.newInstance()
            factory.isValidating = false

            // Create the builder and parse the file
            factory.newSAXParser().parse(
                    InputSource(StringReader(xml)),
                    DefaultHandler())
            return true
        } catch (e: SAXParseException) {
            logger!!.log(
                    Level.SEVERE,
                    "XmlParseError on line " + e.lineNumber + " of " + xml,
                    e)
        } catch (e: Exception) {
            logger!!.log(Level.SEVERE, "XmlParseError", e)
        }
        return false
    }

    interface NodeCreator {
        fun createChild(pParent: MindMapNode?): MindMapNode?
        fun setText(pText: String?, pNode: MindMapNode?)
        fun setLink(pLink: String?, pNode: MindMapNode?)
    }

    /**
     * @author foltin
     * @date 10.12.2014
     */
    private inner class HtmlNodeVisitor
    /**
     * @param pParentNode
     * @param pCreator
     */(private var mParentNode: MindMapNode?, private val mCreator: NodeCreator) : NodeVisitor {
        var isNewline = true
        var mLevel = 0
        private var mCurrentNode: MindMapNode? = null
        private var mFirstUl = true
        private var mLink: String? = null
        override fun head(node: Node, depth: Int) {
            try {
                if (node is TextNode) {
                    val text = node.text().replace('\u00A0', ' ').trim { it <= ' ' }
                    if (!text.isEmpty()) {
                        if (mCurrentNode == null) {
                            // create a new sibling:
                            mCurrentNode = mCreator.createChild(mParentNode)
                        }
                        //						System.out.println("TEXT+: " + text);
                        mCreator.setText(mCurrentNode!!.text + text, mCurrentNode)
                        if (mLink != null) {
                            mCreator.setLink(mLink, mCurrentNode)
                            mLink = null
                        }
                        isNewline = false
                    }
                } else if (node is Element) {
                    val element = node
                    if ("a" == element.tagName()) {
                        mLink = element.attr("href")
                    }
                    if (element.tagName() == "ul") {
                        createChild()
                    } else {
                        val matcher = LEVEL_PATTERN.matcher(element.attr("style"))
                        if (element.tagName() == "p" && matcher.find()) {
                            // special handling for outlook 
                            val newLevel = Integer.valueOf(matcher.group(1))
                            while (newLevel > mLevel) {
//								System.out.println("Level increase from: " + mLevel + " to " + newLevel);
                                createChild()
                                mLevel++
                            }
                            // test for other direction
                            while (newLevel < mLevel) {
//								System.out.println("Level decrease from: " + mLevel + " to " + newLevel);
                                backToParent()
                                mLevel--
                            }
                            isNewline = false
                        }
                        if (!isNewline) {
                            if (element.isBlock
                                    || element.tagName() == "br" || element
                                            .tagName() == "li") {
                                isNewline = true
                                if (mCurrentNode == null || !mCurrentNode!!.text.isEmpty()) {
                                    // next sibling, same parent, only if already content present.
                                    mCurrentNode = null
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Resources.Companion.getInstance().logException(e)
            }
        }

        override fun tail(node: Node, depth: Int) {
            try {
                if (node is Element) {
                    if (node.tagName() == "ul") {
                        backToParent()
                    }
                }
            } catch (e: Exception) {
                Resources.Companion.getInstance().logException(e)
            }
        }

        private fun createChild() {
            if (mFirstUl) {
                // the first ul is ignored.
                mFirstUl = false
            } else {
                if (mCurrentNode == null) {
                    // create one:
                    mCurrentNode = mCreator.createChild(mParentNode)
                }
                mParentNode = mCurrentNode
                mCurrentNode = null
            }
        }

        private fun backToParent() {
            mParentNode = mParentNode!!.parentNode
        }
    }

    /**
     * Uses JSoup to parse HTML
     */
    fun insertHtmlIntoNodes(pText: String?, pParentNode: MindMapNode?, pCreator: NodeCreator) {
        NodeTraversor(HtmlNodeVisitor(pParentNode, pCreator)).traverse(Jsoup.parse(pText))
    }

    companion object {
        const val NBSP = "\u00A0"
        private var logger: Logger?
        val instance = HtmlTools()
        private val HTML_PATTERN = Pattern
                .compile("(?s).*<\\s*html.*?>.*")
        private val FIND_TAGS_PATTERN = Pattern
                .compile("([^<]*)(<[^>]+>)")
        private val SLASHED_TAGS_PATTERN = Pattern.compile("<(("
                + "br|area|base|basefont|" + "bgsound|button|col|colgroup|embed|hr"
                + "|img|input|isindex|keygen|link|meta"
                + "|object|plaintext|spacer|wbr" + ")(\\s[^>]*)?)/>")
        private val TAGS_PATTERN = Pattern.compile("(?s)<[^><]*>")
        private val LEVEL_PATTERN = Pattern.compile("level([0-9]+)")
        const val SP = "&#160;"

        /**
         * Searches for <html> tag in text.
        </html> */
        fun isHtmlNode(text: String): Boolean {
            return HTML_PATTERN.matcher(text.lowercase()).matches()
        }

        /**
         * Changes all unicode characters into &#xxx values.
         * Opposite to [HtmlTools.unescapeHTMLUnicodeEntity]
         */
        fun unicodeToHTMLUnicodeEntity(text: String, pPreserveNewlines: Boolean): String {
            /*
		 * Heuristic reserve for expansion : factor 1.2
		 */
            val result = StringBuffer((text.length * 1.2).toInt())
            var intValue: Int
            var myChar: Char
            for (i in 0 until text.length) {
                myChar = text[i]
                intValue = text[i].code
                var outOfRange = intValue < 32 || intValue > 126
                if (pPreserveNewlines && myChar == '\n') {
                    outOfRange = false
                }
                if (pPreserveNewlines && myChar == '\r') {
                    outOfRange = false
                }
                if (outOfRange) {
                    result.append("&#x").append(Integer.toString(intValue, 16))
                            .append(';')
                } else {
                    result.append(myChar)
                }
            }
            return result.toString()
        }

        /**
         * Converts XML unicode entity-encoded characters into plain Java unicode
         * characters; for example, ''&amp;#xff;'' gets converted. Removes all
         * XML-invalid entity characters, such as &amp;#xb;.
         *
         * Opposite to [HtmlTools.unicodeToHTMLUnicodeEntity]
         *
         * @param text
         * input
         * @return the converted output.
         */
        fun unescapeHTMLUnicodeEntity(text: String): String {
            val result = StringBuffer(text.length)
            val entity = StringBuffer()
            var readingEntity = false
            var myChar: Char
            var entityChar: Char
            for (i in 0 until text.length) {
                myChar = text[i]
                if (readingEntity) {
                    if (myChar == ';') {
                        if (entity[0] == '#') {
                            try {
                                entityChar = if (entity[1] == 'x') {
                                    // Hexadecimal
                                    entity.substring(2).toInt(16).toChar()
                                } else {
                                    // Decimal
                                    entity.substring(1).toInt(10).toChar()
                                }
                                if (isXMLValidCharacter(entityChar)) result.append(entityChar)
                            } catch (e: NumberFormatException) {
                                result.append('&').append(entity).append(';')
                            }
                        } else {
                            result.append('&').append(entity).append(';')
                        }
                        entity.setLength(0)
                        readingEntity = false
                    } else {
                        if (isXMLValidCharacter(myChar)) entity.append(myChar)
                    }
                } else {
                    if (myChar == '&') {
                        readingEntity = true
                    } else {
                        if (isXMLValidCharacter(myChar)) result.append(myChar)
                    }
                }
            }
            if (entity.length > 0) {
                result.append('&').append(entity).append(';')
            }
            return result.toString()
        }

        /**
         * Removes all tags (<..>) from a string if it starts with "<html>..." to
         * make it compareable.
        </html> */
        fun removeHtmlTagsFromString(text: String): String {
            return if (isHtmlNode(text)) {
                removeAllTagsFromString(text) // (?s) enables that . matches
                // newline.
            } else {
                text
            }
        }

        fun removeAllTagsFromString(text: String?): String {
            return TAGS_PATTERN.matcher(text).replaceAll("")
        }

        @JvmOverloads
        fun htmlToPlain(text: String, strictHTMLOnly: Boolean =  /* strictHTMLOnly= */true): String {
            // 0. remove all newlines
            // 1. replace newlines, paragraphs, and table rows
            // 2. remove XML tags
            // 3. replace HTML entities including &nbsp;
            // 4. unescape unicode entities
            // This is a very basic conversion, fixing the most annoying
            // inconvenience. You can imagine much better conversion of
            // HTML to plain text. Most of HTML tags can be handled
            // sensibly, like web browsers do it.
            if (strictHTMLOnly && !isHtmlNode(text)) {
                return text
            }
            // System.err.println("base:"+text);
            var intermediate: String = text
                    .replace("(?ims)[\n\t]".toRegex(), "")
                    .replace // Remove newlines
            "(?ims) +".toRegex(), " ")
            .replace // Condense spaces
            "(?ims)<br.*?>".toRegex(), "\n")
            .replace("(?ims)<p.*?>".toRegex(), "\n\n")
                    .replace // Paragraph
            "(?ims)<div.*?>".toRegex(), "\n")
            .replace // Div - block
            "(?ims)<tr.*?>".toRegex(), "\n")
            .replace("(?ims)<dt.*?>".toRegex(), "\n")
                    .replace // Defined term
            "(?ims)<dd.*?>".toRegex(), "\n   ")
            .replace // Definition of defined term
            "(?ims)<td.*?>".toRegex(), " ")
            .replace("(?ims)<[uo]l.*?>".toRegex(), "\n")
                    .replace // Beginning of a list
            "(?ims)<li.*?>".toRegex(), "\n   * ")
            .replace("(?ims) *</[^>]*>".toRegex(), "").replace // Remaining closing HTML
            // tags
            "(?ims)<[^/][^>]*> *".toRegex(), "").replace // Remaining opening HTML
            // tags
            // FIXME Dimitry: is removing of all new lines at the begin a
            // good idea?
            "^\n+".toRegex(), "").trim // fc: to remove start and end spaces.
            { it <= ' ' }
            intermediate = unescapeHTMLUnicodeEntity(intermediate)

            // Entities, with the exception of &.
            intermediate = intermediate.replace("(?ims)&lt;".toRegex(), "<")
                    .replace("(?ims)&gt;".toRegex(), ">").replace("(?ims)&quot;".toRegex(), "\"")
                    .replace("(?ims)&nbsp;".toRegex(), " ")
            // System.err.println("intermediate:"+intermediate);
            return intermediate.replace("(?ims)&amp;".toRegex(), "&")
        }

        fun plainToHTML(text: String): String {
            var myChar: Char
            val textTabsExpanded = text.replace("\t".toRegex(), "         ") // Use
            // eight
            // spaces
            // as
            // tab
            // width.
            val result = StringBuffer(textTabsExpanded.length) // Heuristic
            val lengthMinus1 = textTabsExpanded.length - 1
            result.append("<html><body><p>")
            for (i in 0 until textTabsExpanded.length) {
                myChar = textTabsExpanded[i]
                when (myChar) {
                    '&' -> result.append("&amp;")
                    '<' -> result.append("&lt;")
                    '>' -> result.append("&gt;")
                    ' ' -> if (i > 0 && i < lengthMinus1 && textTabsExpanded[i - 1].code > 32 && textTabsExpanded[i + 1].code > 32) {
                        result.append(' ')
                    } else {
                        result.append("&nbsp;")
                    }
                    '\n' -> result.append("<br>")
                    else -> result.append(myChar)
                }
            }
            return result.toString()
        }

        fun toXMLUnescapedText(text: String): String {
            return text.replace("&lt;".toRegex(), "<").replace("&gt;".toRegex(), ">")
                    .replace("&quot;".toRegex(), "\"").replace("&amp;".toRegex(), "&")
        }

        fun toXMLEscapedTextExpandingWhitespace(text: String): String {
            // Spaces and tabs are handled
            var text = text
            text = text.replace("\t".toRegex(), "         ") // Use eight spaces as tab
            // width.
            val len = text.length
            val result = StringBuffer(len)
            var myChar: Char
            for (i in 0 until len) {
                myChar = text[i]
                when (myChar) {
                    '&' -> result.append("&amp;")
                    '<' -> result.append("&lt;")
                    '>' -> result.append("&gt;")
                    ' ' -> if (i > 0 && i < len - 1 && text[i - 1].code > 32 && text[i + 1].code > 32) {
                        result.append(' ')
                    } else {
                        result.append("&nbsp;")
                    }
                    else -> result.append(myChar)
                }
            }
            return result.toString()
        }

        fun toXMLEscapedText(text: String?): String {
            return text?.replace("&".toRegex(), "&amp;")?.replace("<".toRegex(), "&lt;")?.replace(">".toRegex(), "&gt;")?.replace("\"".toRegex(), "&quot;")
                    ?: "ERROR: none"
        }

        /** \0 is not allowed:  */
        fun makeValidXml(pXmlNoteText: String): String {
            return pXmlNoteText.replace("\u0000".toRegex(), "").replace("&#0;".toRegex(), "")
        }

        fun replaceIllegalXmlCharacters(fileContents: String): String {
            // replace &xa; by newline.
            var fileContents = fileContents
            fileContents = fileContents.replace("&#x0*[Aa];".toRegex(), "\n")
            /*
		 * &#xb; is illegal, but sometimes occurs in 0.8.x maps. Thus, we
		 * exclude all from 0 - 1f and replace them by nothing. TODO: Which more
		 * are illegal??
		 */fileContents = fileContents.replace("&#x0*1?[0-9A-Fa-f];".toRegex(), "")
            // decimal: 0-31
            fileContents = fileContents.replace("&#0*[1-2]?[0-9];".toRegex(), "")
            fileContents = fileContents.replace("&#0*3[0-1];".toRegex(), "")
            return fileContents
        }

        /**
         * Determines whether the character is valid in XML. Invalid characters
         * include most of the range x00-x1F, and more.
         *
         * @see http://www.w3.org/TR/2000/REC-xml-20001006.NT-Char.
         */
        fun isXMLValidCharacter(character: Char): Boolean {
            // Order the tests in such a sequence that the most probable
            // conditions are tested first.
            return character.code >= 0x20 && character.code <= 0xD7FF || character.code == 0x9 || character.code == 0xA || character.code == 0xD || character.code >= 0xE000 && character.code <= 0xFFFD || character.code >= 0x10000 && character.code <= 0x10FFFF
        }

        /** Precondition: The input text contains XML unicode entities rather
         * than Java unicode text.
         *
         * The algorithm:
         * Search the string for XML entities. For each XML entity inspect
         * whether it is valid. If valid, append it. To be on the safe side,
         * also inspect for no-entity unicode whether it is XML-valid, and
         * pass on only XML-valid characters.
         *
         * This method uses the method isXMLValidCharacter, which makes use
         * of http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char.  */
        fun removeInvalidXmlCharacters(text: String?): String {
            val result = StringBuffer(text!!.length)
            val entity = StringBuffer()
            var readingEntity = false
            var myChar: Char
            var entityChar: Char
            for (i in 0 until text.length) {
                myChar = text[i]
                if (readingEntity) {
                    if (myChar == ';') {
                        if (entity[0] == '#') {
                            try {
                                entityChar = if (entity[1] == 'x') {
                                    // Hexadecimal
                                    entity.substring(2).toInt(16).toChar()
                                } else {
                                    // Decimal
                                    entity.substring(1).toInt(10).toChar()
                                }
                                if (isXMLValidCharacter(entityChar)) result.append('&').append(entity).append(';')
                            } catch (e: NumberFormatException) {
                                result.append('&').append(entity).append(';')
                            }
                        } else {
                            result.append('&').append(entity).append(';')
                        }
                        entity.setLength(0)
                        readingEntity = false
                    } else {
                        entity.append(myChar)
                    }
                } else {
                    if (myChar == '&') {
                        readingEntity = true
                    } else {
                        // The following test is superfluous under the assumption
                        // that the string only contains unicode in XML entities.
                        // Removing this test could significantly speed up this
                        // method; maybe.
                        if (isXMLValidCharacter(myChar)) result.append(myChar)
                    }
                }
            }
            if (entity.length > 0) {
                result.append('&').append(entity).append(';')
            }
            return result.toString()
        }

        fun extractHtmlBody(output: String): String {
            var output = output
            if (output.startsWith("<html")) {
                output = output.substring(6) // do not write
            }
            var start = output.indexOf("<body")
            start = if (start == -1) {
                output.indexOf('>') + 1
            } else {
                output.indexOf('>', start + 5) + 1
            }
            var end = output.indexOf("</body>")
            if (end == -1) {
                end = output.indexOf("</html>")
            }
            if (end == -1) {
                end = output.length
            }
            output = output.substring(start, end)
            return output
        }

        /**
         * Is used from XSLT! Don't change, unless you change the freemind_version_updater.xslt, too.
         * @param input
         * @return
         */
        fun replaceSpacesToNonbreakableSpaces(input: String): String {
            val result = StringBuffer(input.length)
            var readingSpaces = false
            var myChar: Char
            for (i in 0 until input.length) {
                myChar = input[i]
                if (myChar == ' ') {
                    if (readingSpaces) {
                        result.append(NBSP)
                    } else {
                        result.append(myChar)
                        readingSpaces = true
                    }
                } else {
                    readingSpaces = false
                    result.append(myChar)
                }
            }
            return result.toString()
        }
    }
}