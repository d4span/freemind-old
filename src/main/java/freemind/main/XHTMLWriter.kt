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
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
import java.util.logging.SimpleFormatter
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
import kotlin.jvm.JvmStatic
import javax.swing.JLayeredPane
import freemind.main.StdFormatter.StdOutErrLevel
import javax.swing.JApplet
import freemind.main.FreeMindApplet
import java.util.PropertyResourceBundle
import freemind.main.FreeMindCommon.FreeMindResourceBundle
import java.net.URLClassLoader
import javax.swing.text.MutableAttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import java.net.PasswordAuthentication
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.main.FreeMindSplashModern.FeedBackImpl
import javax.swing.JProgressBar
import freemind.view.ImageFactory
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import java.lang.Exception
import javax.swing.text.html.*

/*
 * XHTMLWriter -- A simple XHTML document writer
 * 
 * (C) 2004 Richard "Shred" Koerber
 *   http://www.shredzone.net/
 *
 * This is free software. You can modify and use it at will.
 */ /**
 * Create a new XHTMLWriter which is able to save a HTMLDocument as XHTML.
 *
 *
 * The result will be a valid XML file, but it is not granted that the file will
 * really be XHTML 1.0 transitional conform. The basic purpose of this class
 * is to give an XSL processor access to plain HTML files.
 *
 * @author Richard "Shred" K�rber
 */
class XHTMLWriter @JvmOverloads constructor(writer: Writer?, doc: HTMLDocument, pos: Int = 0, len: Int = doc.length) : FixedHTMLWriter(XHTMLFilterWriter(writer), doc, pos, len) {
    private var writeLineSeparatorEnabled = true
    /**
     * Create a new XHTMLWriter that will write a part of a HTMLDocument.
     *
     * @param writer
     * Writer to write to
     * @param doc
     * Source document
     * @param pos
     * Starting position
     * @param len
     * Length
     */
    /**
     * Create a new XHTMLWriter that will write the entire HTMLDocument.
     *
     * @param writer
     * Writer to write to
     * @param doc
     * Source document
     */
    init {
        lineLength = Int.MAX_VALUE
    }

    /**
     * Start the writing process. An XML and DOCTYPE header will be written
     * prior to the XHTML output.
     */
    @Throws(IOException::class, BadLocationException::class)
    override fun write() {
        // fc, 17.5.06: no special tags, as they are wrong inside XML tags like
        // <content>...<html>...
        // write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
        // writeLineSeparator();
        // write( "<!DOCTYPE html PUBLIC \"-//W3C//"
        // + "DTD XHTML 1.0 Transitional//EN\" "
        // + "\"DTD/xhtml1-transitional.dtd\">" );
        // writeLineSeparator();
        super.write()
    }

    @Throws(IOException::class)
    override fun writeOption(option: Option) {
        writeLineSeparatorEnabled = false
        super.writeOption(option)
        writeLineSeparatorEnabled = true
        write("</option>")
        writeLineSeparator()
    }

    @Throws(IOException::class)
    override fun writeLineSeparator() {
        if (writeLineSeparatorEnabled) super.writeLineSeparator()
    }

    /**
     * This FilterWriter will convert the output of Swing's HTMLWriter to XHTML
     * format. This is done by converting tags like &lt;br&gt; to
     * &lt;br&nbsp;/&gt;. Also, special characters in tag attributes are
     * escaped.
     *
     *
     * This filter relies on known flaws of the HTMLWriter. It is known to work
     * with Java 1.4, but might not work with future Java releases.
     */
    class XHTMLFilterWriter
    /**
     * Create a new XHTMLFilterWriter.
     *
     * @param writer
     * Writer to write to
     */
    (writer: Writer?) : FilterWriter(writer) {
        private var insideTag = false // We're inside a tag
        private var insideValue = false // We're inside an attribute value
        private var readTag = false // We're reading the tag name
        private var tag = "" // Collector for the tag name

        /**
         * Write a single char to the Writer.
         *
         * @param c
         * Char to be written
         */
        @Throws(IOException::class)
        override fun write(c: Int) {
            if (insideValue) {
                // We're currently within a tag attribute's value.
                // Take care for proper HTML escaping.
                if (c == '&'.code) {
                    super.write("&amp;", 0, 5)
                    return
                } else if (c == '<'.code) {
                    super.write("&lt;", 0, 4)
                    return
                } else if (c == '>'.code) {
                    super.write("&gt;", 0, 4)
                    return
                } else if (c == '"'.code) { // leaving the value
                    insideValue = false
                }
            } else if (insideTag) {
                // We're inside a tag. Add a slash to the closing tag bracket
                // for
                // certain tags (like img, br, hr, input, ... ).
                if (readTag) {
                    if (c == ' '.code || c == '>'.code) { // tag name ends
                        readTag = false
                    } else {
                        tag += c.toChar() // collect tag name here
                    }
                }
                if (c == '"'.code) { // attribute value begins
                    insideValue = true
                } else if (c == '>'.code) { // check if this is a "certain tag"
                    if (tag == "img" || tag == "br" || tag == "hr" || tag == "input" || tag == "meta" || tag == "link" || tag == "area" || tag == "base" || tag == "basefont" || tag == "frame" || tag == "iframe" || tag == "col") {
                        super.write(" /") // add slash to the closing bracket
                    }
                    insideTag = false
                    readTag = false
                }
            } else if (c == '<'.code) {
                // We're just at the very beginning of a tag.
                tag = ""
                insideTag = true
                readTag = true
            }
            super.write(c)
        }

        /**
         * Write a char array to the Writer.
         *
         * @param cbuf
         * Char array to be written
         * @param off
         * Start offset within the array
         * @param len
         * Number of chars to be written
         */
        @Throws(IOException::class)
        override fun write(cbuf: CharArray, off: Int, len: Int) {
            var off = off
            var len = len
            while (len-- > 0) {
                write(cbuf[off++].code)
            }
        }

        /**
         * Write a String to the Writer.
         *
         * @param str
         * String to be written
         * @param off
         * Start offset within the String
         * @param len
         * Number of chars to be written
         */
        @Throws(IOException::class)
        override fun write(str: String, off: Int, len: Int) {
            write(str.toCharArray(), off, len)
        }
    }

    companion object {
        /**
         * Read HTML from the Reader, and send XHTML to the writer. Common mistakes
         * in the HTML code will also be corrected. The result is pretty-printed.
         *
         * @param reader
         * HTML source
         * @param writer
         * XHTML target
         */
        @Throws(IOException::class, BadLocationException::class)
        fun html2xhtml(reader: Reader?, writer: Writer?) {
            // --- Create a HTML document ---
            val kit = HTMLEditorKit()
            val doc = kit.createDefaultDocument()

            // --- Read the HTML source ---
            kit.read(reader, doc, doc.length)

            // --- Write the content ---
            val xhw = XHTMLWriter(writer, doc as HTMLDocument)
            xhw.write()
        }

        /**
         * External call to convert a source HTML file to a target XHTML file.
         *
         *
         * Usage: <tt>java XHTMLWriter &lt;source file&gt; &lt;target file&gt;</tt>
         *
         * @param args
         * Shell arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val reader = FileReader(args[0])
                val writer = FileWriter(args[1])
                html2xhtml(reader, writer)
                writer.close()
                reader.close()
            } catch (e: Exception) {
                Resources.Companion.getInstance().logException(e)
            }
        }
    }
}