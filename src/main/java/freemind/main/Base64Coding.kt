/* AbstractPreferences -- Partial implementation of a Preference node
   Copyright (C) 2001, 2003, 2004, 2006  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */
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
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import java.util.logging.Logger

/**
 * Partial implementation of a Preference node.
 *
 * @since 1.4
 * @author Mark Wielaard (mark@klomp.org)
 *
 * 22.3.2008: FC: Changed name from AbstractPreferences to the current,
 * Removed all but base64 coding.
 */
object Base64Coding {
    private var logger: Logger? = null

    init {
        logger = Resources.Companion.getInstance()
                .getLogger("Base64Coding")
    }

    /**
     * Helper method for decoding a Base64 string as an byte array. Returns null
     * on encoding error. This method does not allow any other characters
     * present in the string then the 65 special base64 chars.
     */
    fun decode64(s: String): ByteArray? {
        val bs = ByteArrayOutputStream(
                s.length / 4 * 3)
        val c = CharArray(s.length)
        s.toCharArray(c, 0, 0, s.length)

        // Convert from base64 chars
        var endchar = -1
        var j = 0
        while (j < c.size && endchar == -1) {
            if (c[j] >= 'A' && c[j] <= 'Z') {
                (c[j] -= 'A').toChar()
            } else if (c[j] >= 'a' && c[j] <= 'z') {
                c[j] = (c[j].code + 26 - 'a'.code).toChar()
            } else if (c[j] >= '0' && c[j] <= '9') {
                c[j] = (c[j].code + 52 - '0'.code).toChar()
            } else if (c[j] == '+') {
                c[j] = 62.toChar()
            } else if (c[j] == '/') {
                c[j] = 63.toChar()
            } else if (c[j] == '=') {
                endchar = j
            } else {
                logger!!.severe("Found illegal character in base64 coding: '"
                        + c[j] + "'")
                return null // encoding exception
            }
            j++
        }
        var remaining = if (endchar == -1) c.size else endchar
        var i = 0
        while (remaining > 0) {
            // Four input chars (6 bits) are decoded as three bytes as
            // 000000 001111 111122 222222
            var b0 = (c[i].code shl 2).toByte()
            if (remaining >= 2) {
                (b0 += (c[i + 1].code and 0x30 shr 4).toByte()).toByte()
            }
            bs.write(b0.toInt())
            if (remaining >= 3) {
                var b1 = (c[i + 1].code and 0x0F shl 4).toByte()
                (b1 += (c[i + 2].code and 0x3C shr 2).toByte()).toByte()
                bs.write(b1.toInt())
            }
            if (remaining >= 4) {
                var b2 = (c[i + 2].code and 0x03 shl 6).toByte()
                (b2 += c[i + 3].code.toByte()).toByte()
                bs.write(b2.toInt())
            }
            i += 4
            remaining -= 4
        }
        return bs.toByteArray()
    }

    /**
     * Helper method for encoding an array of bytes as a Base64 String.
     */
    fun encode64(b: ByteArray): String {
        val sb = StringBuffer(b.size / 3 * 4)
        var i = 0
        var remaining = b.size
        val c = CharArray(4)
        while (remaining > 0) {
            // Three input bytes are encoded as four chars (6 bits) as
            // 00000011 11112222 22333333
            c[0] = (b[i] and 0xFC shr 2).toChar()
            c[1] = (b[i] and 0x03 shl 4).toChar()
            if (remaining >= 2) {
                c[1] += (b[i + 1] and 0xF0 shr 4).toChar()
                c[2] = (b[i + 1] and 0x0F shl 2).toChar()
                if (remaining >= 3) {
                    c[2] += (b[i + 2] and 0xC0 shr 6).toChar()
                    c[3] = (b[i + 2] and 0x3F).toChar()
                } else {
                    c[3] = 64.toChar()
                }
            } else {
                c[2] = 64.toChar()
                c[3] = 64.toChar()
            }

            // Convert to base64 chars
            for (j in 0..3) {
                if (c[j].code < 26) {
                    c[j] += 'A'
                } else if (c[j].code < 52) {
                    c[j] = (c[j].code - 26 + 'a'.code).toChar()
                } else if (c[j].code < 62) {
                    c[j] = (c[j].code - 52 + '0'.code).toChar()
                } else if (c[j].code == 62) {
                    c[j] = '+'
                } else if (c[j].code == 63) {
                    c[j] = '/'
                } else {
                    c[j] = '='
                }
            }
            sb.append(c)
            i += 3
            remaining -= 3
        }
        return sb.toString()
    }
}