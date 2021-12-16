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
import java.net.PasswordAuthentication
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.main.FreeMindSplashModern.FeedBackImpl
import javax.swing.JProgressBar
import freemind.view.ImageFactory
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import javax.swing.text.*
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
 * really be XHTML 1.0 transitional conformous. The basic purpose of this class
 * is to give an XSL processor access to plain HTML files.
 *
 * @author Richard "Shred" K�rber
 */
open class FixedHTMLWriter
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
@JvmOverloads constructor(writer: Writer?, doc: HTMLDocument, pos: Int = 0, len: Int = doc.length) : HTMLWriter(writer, doc, pos, len) {
    private val convAttr: MutableAttributeSet = SimpleAttributeSet()

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.html.HTMLWriter#writeAttributes(javax.swing.text.
	 * AttributeSet)
	 */
    @Throws(IOException::class)
    override fun writeAttributes(attr: AttributeSet) {
        // translate css attributes to html
        if (attr is Element) {
            val elem = attr as Element
            if (elem.isLeaf || elem.name.equals("p-implied", ignoreCase = true)) {
                super.writeAttributes(attr)
                return
            }
        }
        convAttr.removeAttributes(convAttr)
        convertToHTML(attr, convAttr)
        val names = convAttr.attributeNames
        while (names.hasMoreElements()) {
            val name = names.nextElement()
            if (name is HTML.Tag || name is StyleConstants
                    || name === HTML.Attribute.ENDTAG) {
                continue
            }
            write(" " + name + "=\"" + convAttr.getAttribute(name) + "\"")
        }
    }

    companion object {
        /**
         * Create an older style of HTML attributes. This will convert character
         * level attributes that have a StyleConstants mapping over to an HTML
         * tag/attribute. Other CSS attributes will be placed in an HTML style
         * attribute.
         */
        private fun convertToHTML(from: AttributeSet?, to: MutableAttributeSet) {
            if (from == null) {
                return
            }
            val keys = from.attributeNames
            var value = ""
            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                if (key is CSS.Attribute) {
                    // default is to store in a HTML style attribute
                    if (value.length > 0) {
                        value = "$value; "
                    }
                    value = value + key + ": " + from.getAttribute(key)
                } else {
                    to.addAttribute(key, from.getAttribute(key))
                }
            }
            if (value.length > 0) {
                to.addAttribute(HTML.Attribute.STYLE, value)
            }
        }
    }
}