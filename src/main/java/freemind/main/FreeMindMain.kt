/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: FreeMindMain.java,v 1.12.14.5.2.12 2008/07/17 19:16:33 christianfoltin Exp $*/
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
import javax.swing.JDialog
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
import javax.swing.InputMap
import javax.swing.UIManager
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
import freemind.controller.Controller
import freemind.controller.MenuBar
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
import javax.swing.JLayeredPane
import freemind.main.StdFormatter.StdOutErrLevel
import javax.swing.JApplet
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
import javax.swing.JProgressBar
import freemind.view.ImageFactory
import java.awt.*
import javax.swing.JRootPane
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.*
import java.util.*
import java.util.logging.Logger

interface FreeMindMain {
    interface StartupDoneListener {
        fun startupDone()
    }

    val jFrame: JFrame
    val isApplet: Boolean
    var view: MapView?
    val controller: Controller?
    fun setWaitingCursor(waiting: Boolean)
    val patternsFile: File?
    val freeMindMenuBar: MenuBar?

    /** Returns the ResourceBundle with the current language  */
    val resources: ResourceBundle?
    fun getResourceString(key: String?): String?
    fun getResourceString(key: String?, defaultResource: String?): String?
    val contentPane: Container?
    fun out(msg: String?)
    fun err(msg: String)

    /**
     * Open url in WWW browser. This method hides some differences between
     * operating systems.
     */
    @Throws(Exception::class)
    fun openDocument(location: URL)

    /** remove this!  */
    fun repaint()
    fun getResource(name: String): URL?
    fun getIntProperty(key: String?, defaultValue: Int): Int

    /** @return returns the list of all properties.
     */
    val properties: Properties?

    /**
     * Properties are stored in freemind.properties (internally) and
     * ~/.freemind/auto.properties for user changed values. This method returns
     * the user value (if changed) or the original.
     *
     * @param key
     * The property key as specified in freemind.properties
     * @return the value of the property or null, if not found.
     */
    fun getProperty(key: String?): String?
    fun setProperty(key: String?, value: String?)
    fun saveProperties(pIsShutdown: Boolean)

    /**
     * Returns the path to the directory the freemind auto properties are in, or
     * null, if not present.
     */
    val freemindDirectory: String?
    val layeredPane: JLayeredPane?
    fun setTitle(title: String?)

    // to keep last win size (PN)
    val winHeight: Int
    val winWidth: Int
    val winState: Int
    val winX: Int
    val winY: Int

    class VersionInformation {
        var mMaj = 0
        var mMid = 9
        var mMin = 0
        var mType = VERSION_TYPE_BETA
        var mNum = 17

        constructor(pMaj: Int, pMid: Int, pMin: Int, pType: Int,
                    pNum: Int) : super() {
            mMaj = pMaj
            mMid = pMid
            mMin = pMin
            mType = pType
            mNum = pNum
        }

        /**
         * Sets the version number from a string.
         *
         * @param pString
         * : The version number coding. Example "0.9.0 Beta 1"
         * Keywords are "Alpha", "Beta", "RC". Separation by " " or
         * by ".".
         */
        constructor(pString: String) {
            val t = StringTokenizer(pString, ". ", false)
            val info = arrayOfNulls<String>(t.countTokens())
            var i = 0
            while (t.hasMoreTokens()) {
                info[i++] = t.nextToken()
            }
            require(!(info.size != 3 && info.size != 5)) {
                ("Wrong number of tokens for version information: "
                        + pString)
            }
            mMaj = info[0]!!.toInt()
            mMid = info[1]!!.toInt()
            mMin = info[2]!!.toInt()
            if (info.size == 3) {
                // release.
                mType = VERSION_TYPE_RELEASE
                mNum = 0
                return
            }
            // here,we have info.length == 5!
            val types = Vector<String?>()
            types.add("Alpha")
            types.add("Beta")
            types.add("RC")
            val typeIndex = types.indexOf(info[3])
            require(typeIndex >= 0) {
                ("Wrong version type for version information: "
                        + info[4])
            }
            mType = typeIndex
            mNum = info[4]!!.toInt()
        }

        override fun toString(): String {
            val buf = StringBuffer()
            buf.append(mMaj)
            buf.append('.')
            buf.append(mMid)
            buf.append('.')
            buf.append(mMin)
            when (mType) {
                VERSION_TYPE_ALPHA -> {
                    buf.append(' ')
                    buf.append("Alpha")
                }
                VERSION_TYPE_BETA -> {
                    buf.append(' ')
                    buf.append("Beta")
                }
                VERSION_TYPE_RC -> {
                    buf.append(' ')
                    buf.append("RC")
                }
                VERSION_TYPE_RELEASE -> {}
                else -> throw IllegalArgumentException("Unknown version type "
                        + mType)
            }
            if (mType != VERSION_TYPE_RELEASE) {
                buf.append(' ')
                buf.append(mNum)
            }
            return buf.toString()
        }
    }

    /** version info:  */
    val freemindVersion: VersionInformation

    /** To obtain a logging element, ask here.  */
    fun getLogger(forClass: String?): Logger

    /**
     * Inserts a (south) component into the split pane. If the screen isn't
     * split yet, a split pane should be created on the fly.
     * @param pMindMapComponent
     *
     * @return the split pane in order to move the dividers.
     */
    fun insertComponentIntoSplitPane(pMindMapComponent: JComponent): JSplitPane?

    /**
     * Indicates that the south panel should be made invisible.
     */
    fun removeSplitPane()

    /**
     * @return a ClassLoader derived from the standard, with freeminds base dir
     * included.
     */
    val freeMindClassLoader: ClassLoader?

    /**
     * @return default ".", but on different os this differs.
     */
    val freemindBaseDir: String?

    /**
     * Makes it possible to have a property different for different
     * localizations. Common example is to put keystrokes to different keys as
     * some are better reachable than others depending on the locale.
     */
    fun getAdjustableProperty(label: String): String?
    fun setDefaultProperty(key: String?, value: String?)
    val contentComponent: JComponent?
    val scrollPane: JScrollPane?
    fun registerStartupDoneListener(
            pStartupDoneListener: StartupDoneListener)

    /**
     * @return a list of all loggers. Used for example for the log file viewer.
     */
    val loggerList: List<Logger>

    companion object {
        const val VERSION_TYPE_ALPHA = 0
        const val VERSION_TYPE_BETA = 1
        const val VERSION_TYPE_RC = 2
        const val VERSION_TYPE_RELEASE = 3
        const val ENABLE_NODE_MOVEMENT = "enable_node_movement"
    }
}