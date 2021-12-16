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
/*$Id: FreeMindApplet.java,v 1.18.14.13.2.25 2009/04/19 19:44:01 christianfoltin Exp $*/
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
import java.lang.StringBuffer
import java.awt.GraphicsEnvironment
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
import java.awt.Graphics2D
import java.awt.RenderingHints
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
import java.awt.KeyboardFocusManager
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
import freemind.main.FeedBack
import java.awt.BorderLayout
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import freemind.view.mindmapview.MapView
import java.awt.Desktop
import java.awt.Cursor
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
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.net.*
import java.util.*
import java.util.logging.Logger

class FreeMindApplet : JApplet(), FreeMindMain {
    // public static final String defaultPropsURL;
    var defaultPropsURL: URL? = null
    private override val scrollPane: JScrollPane = MapView.ScrollPane()
    override var freeMindMenuBar: MenuBar? = null
        private set
    private var status: JLabel? = null
    override var controller // the one and only controller
            : Controller? = null
    private val mFreeMindCommon: FreeMindCommon

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.main.FreeMindMain#getSouthPanel()
	 */  var southPanel: JPanel? = null
        private set
    private var mComponentInSplitPane: JComponent? = null
    override val isApplet: Boolean
        get() = true
    override val patternsFile: File?
        get() = null
    override var view: MapView?
        get() = controller!!.view
        set(view) {
            scrollPane.setViewportView(view)
        }

    // "dummy" implementation of the interface (PN)
    override val winHeight: Int
        get() = getRootPane().height
    override val winWidth: Int
        get() = getRootPane().width
    override val winState: Int
        get() = 6
    override val winX: Int
        get() = 0
    override val winY: Int
        get() = 0

    /**
     * Returns the ResourceBundle with the current language
     */
    override val resources: ResourceBundle?
        get() = mFreeMindCommon.resources

    override fun getResourceString(resource: String?): String? {
        return mFreeMindCommon.getResourceString(resource)
    }

    override fun getResourceString(key: String?, resource: String?): String? {
        return mFreeMindCommon.getResourceString(key, resource)
    }

    override fun getProperty(key: String?): String? {
        return Companion.properties!!.getProperty(key)
    }

    override fun getIntProperty(key: String?, defaultValue: Int): Int {
        return try {
            getProperty(key)!!.toInt()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    override fun setProperty(key: String?, value: String?) {}
    override fun setDefaultProperty(key: String?, value: String?) {
        Companion.properties!!.setProperty(key, value)
    }

    override val freemindDirectory: String?
        get() = null

    init {
        mFreeMindCommon = FreeMindCommon(this)
        Resources.Companion.createInstance(this)
    } // Constructor

    override fun saveProperties(pIsShutdown: Boolean) {}
    override fun setTitle(title: String?) {}
    override fun out(msg: String?) {
        status!!.text = msg
    }

    override fun err(msg: String) {
        status!!.text = "ERROR: $msg"
    }

    @Throws(Exception::class)
    override fun openDocument(doc: URL) {
        appletContext.showDocument(doc, "_blank")
    }

    override fun start() {
        // Make sure the map is centered at the very beginning.
        try {
            if (view != null) {
                view!!.moveToRoot()
            } else {
                System.err.println("View is null.")
            }
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
        }
    }

    override fun setWaitingCursor(waiting: Boolean) {
        if (waiting) {
            getRootPane().glassPane.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
            getRootPane().glassPane.isVisible = true
        } else {
            getRootPane().glassPane.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            getRootPane().glassPane.isVisible = false
        }
    }

    override fun getResource(name: String): URL? {
        val resourceURL = this.javaClass.getResource("/$name")
        return if (resourceURL == null || resourceURL.protocol != "jar"
                && System.getProperty("freemind.debug", null) == null) null else resourceURL
    }

    override fun getLogger(forClass: String?): Logger {
        /*
		 * Applet logging is anonymous due to security reasons. (Calling a named
		 * logger is answered with a security exception).
		 */
        return Logger.getAnonymousLogger()
    }

    override fun init() {
        createRootPane()
        // load properties
        defaultPropsURL = getResource("freemind.properties")
        try {
            // load properties
            defaultProps = Properties()
            val `in` = defaultPropsURL!!.openStream()
            defaultProps!!.load(`in`)
            `in`.close()
            Companion.properties = defaultProps
        } catch (ex: Exception) {
            System.err.println("Could not load properties.")
        }
        updateLookAndFeel()

        // try to overload some properties with given command-line (html tag)
        // Arguments
        val allKeys = Companion.properties!!.propertyNames()
        while (allKeys.hasMoreElements()) {
            val key = allKeys.nextElement() as String
            setPropertyByParameter(key)
        }

        // Layout everything
        contentPane.layout = BorderLayout()
        controller = Controller(this)
        controller!!.init()
        controller!!.optionAntialiasAction
                .changeAntialias(getProperty(FreeMindCommon.Companion.RESOURCE_ANTIALIAS))

        // Create the MenuBar
        freeMindMenuBar = MenuBar(controller!!) // new MenuBar(c);
        jMenuBar = freeMindMenuBar
        controller!!.setToolbarVisible(false)
        controller!!.setMenubarVisible(false)

        // Create the scroll pane.
        contentPane.add(scrollPane, BorderLayout.CENTER)
        // taken from Lukasz Pekacki, NodeText version:
        southPanel = JPanel(BorderLayout())
        status = JLabel()
        southPanel!!.add(status, BorderLayout.SOUTH)
        contentPane.add(southPanel, BorderLayout.SOUTH)
        // end taken.
        SwingUtilities.updateComponentTreeUI(this) // Propagate LookAndFeel to
        // JComponents

        // wait until AWT thread starts
        Tools.waitForEventQueue()
        controller!!.createNewMode(getProperty("initial_mode")!!)
        var initialMapName = getProperty("browsemode_initial_map")
        if (initialMapName != null && initialMapName.startsWith(".")) {
            /* new handling for relative urls. fc, 29.10.2003. */
            initialMapName = try {
                val documentBaseUrl = URL(documentBase, initialMapName)
                documentBaseUrl.toString()
            } catch (e: MalformedURLException) {
                controller!!.errorMessage(
                        "Could not open relative URL " + initialMapName
                                + ". It is malformed.")
                System.err.println(e)
                return
            }
            /* end: new handling for relative urls. fc, 29.10.2003. */
        }
        if (initialMapName !== "") {
            try {
                // get URL:
                val mapUrl = URL(initialMapName)
                controller!!.modeController!!.load(mapUrl)
            } catch (e: Exception) {
                Resources.Companion.getInstance().logException(e)
            }
        }
    }

    private fun setPropertyByParameter(key: String) {
        val `val` = getParameter(key)
        // System.out.println("Got prop:"+key+":"+val);
        if (`val` != null && `val` !== "") {
            Companion.properties!!.setProperty(key, `val`)
        }
    }

    private fun updateLookAndFeel() {
        // set Look&Feel
        var lookAndFeel = ""
        try {
            setPropertyByParameter("lookandfeel")
            lookAndFeel = Companion.properties!!.getProperty("lookandfeel")
            if (lookAndFeel == "windows") {
                UIManager
                        .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
            } else if (lookAndFeel == "motif") {
                UIManager
                        .setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel")
            } else if (lookAndFeel == "mac") {
                // Only available on macOS
                UIManager.setLookAndFeel("javax.swing.plaf.mac.MacLookAndFeel")
            } else if (lookAndFeel == "metal") {
                UIManager
                        .setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel")
            } else if (lookAndFeel == "gtk") {
                UIManager
                        .setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
            } else if (lookAndFeel == "nothing") {
            } else if (lookAndFeel.indexOf('.') != -1) { // string contains a
                // dot
                UIManager.setLookAndFeel(lookAndFeel)
                // we assume class name
            } else {
                // default.
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName())
            }
        } catch (ex: Exception) {
            System.err.println("Error while setting Look&Feel$lookAndFeel")
        }
        mFreeMindCommon.loadUIProperties(Companion.properties)
        Companion.properties!![FreeMind.Companion.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION] = Tools.BooleanToXml(true)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.main.FreeMindMain#getJFrame()
	 */
    override val jFrame: JFrame
        get() {
            throw IllegalArgumentException("The applet has no frames")
        }
    override val freeMindClassLoader: ClassLoader?
        get() = mFreeMindCommon.freeMindClassLoader
    override val freemindBaseDir: String?
        get() = mFreeMindCommon.freemindBaseDir

    override fun getAdjustableProperty(label: String): String? {
        return mFreeMindCommon.getAdjustableProperty(label)
    }

    override fun insertComponentIntoSplitPane(pMindMapComponent: JComponent): JSplitPane? {
        if (mComponentInSplitPane === pMindMapComponent) {
            return null
        }
        removeSplitPane()
        mComponentInSplitPane = pMindMapComponent
        southPanel!!.add(pMindMapComponent, BorderLayout.CENTER)
        southPanel!!.revalidate()
        return null
    }

    override fun removeSplitPane() {
        if (mComponentInSplitPane != null) {
            southPanel!!.remove(mComponentInSplitPane)
            southPanel!!.revalidate()
            mComponentInSplitPane = null
        }
    }

    // TODO: Is that correct?
    override val contentComponent: JComponent?
        get() =// TODO: Is that correct?
            if (mComponentInSplitPane != null) {
                mComponentInSplitPane
            } else southPanel

    override fun getScrollPane(): JScrollPane? {
        return scrollPane
    }

    override fun registerStartupDoneListener(
            pStartupDoneListener: StartupDoneListener) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see freemind.main.FreeMindMain#getLoggerList()
	 */
    override val loggerList: List<Logger>
        get() = Vector<Any>()

    companion object {
        val freemindVersion: VersionInformation = FreeMind.Companion.VERSION
            get() = Companion.field
        var defaultProps: Properties? = null
        var properties: Properties? = null
            get() = Companion.field
        var iMaxNodeWidth = 0
        val maxNodeWidth: Int
            get() {
                if (iMaxNodeWidth == 0) {
                    try {
                        iMaxNodeWidth = properties
                                .getProperty("max_node_width").toInt()
                    } catch (nfe: NumberFormatException) {
                        iMaxNodeWidth = properties
                                .getProperty("el__max_default_window_width").toInt()
                    }
                }
                return iMaxNodeWidth
            }
    }
}