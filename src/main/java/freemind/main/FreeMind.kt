/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
/*$Id: FreeMind.java,v 1.32.14.28.2.147 2011/01/09 21:03:13 christianfoltin Exp $*/
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
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
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
import java.awt.*
import java.io.*
import java.lang.Exception
import java.net.*
import java.util.*
import java.util.logging.*
import javax.swing.*
import javax.swing.Timer

class FreeMind(pDefaultPreferences: Properties,
               pUserPreferences: Properties, pAutoPropertiesFile: File) : JFrame("FreeMind"), FreeMindMain, ActionListener {
    private var logger: Logger? = null
    override var freeMindMenuBar: MenuBar? = null
        private set
    private var status: JLabel? = null
    private var mStatusMessageDisplayTimer: Timer? = null

    // used to open different file types
    private val autoPropertiesFile: File
    override var patternsFile: File? = null
        private set
    override var controller // the one and only controller
            : Controller? = null
    private val mFreeMindCommon: FreeMindCommon

    /**
     * The main map's scroll pane.
     */
    override var scrollPane: JScrollPane? = null
        private set
    private var mSplitPane: JSplitPane? = null
    override var contentComponent: JComponent? = null
        private set
    private var mTabbedPane: JTabbedPane? = null
    private var mWindowIcon: ImageIcon? = null
    private var mStartupDone = false
    private val mStartupDoneListeners: MutableList<StartupDoneListener> = Vector()
    private var mEditServer: EditServer? = null
    private val mLoggerList = Vector<Logger>()

    init {
        // Focus searcher
        System.setSecurityManager(FreeMindSecurityManager())
        defProps = pDefaultPreferences
        props = pUserPreferences
        autoPropertiesFile = pAutoPropertiesFile
        if (logger == null) {
            logger = getLogger(FreeMind::class.java.name)
            val info = StringBuffer()
            info.append("freemind_version = ")
            info.append(Companion.freemindVersion)
            info.append("; freemind_xml_version = ")
            info.append(XML_VERSION)
            try {
                val propsLoc = "version.properties"
                val versionUrl = this.javaClass.classLoader
                        .getResource(propsLoc)
                val buildNumberPros = Properties()
                val stream = versionUrl.openStream()
                buildNumberPros.load(stream)
                info.append("""
    
    Build: ${buildNumberPros.getProperty("build.number")}
    
    """.trimIndent())
                stream.close()
            } catch (e: Exception) {
                info.append("Problems reading build number file: $e")
            }
            info.append("\njava_version = ")
            info.append(System.getProperty("java.version"))
            info.append("; os_name = ")
            info.append(System.getProperty("os.name"))
            info.append("; os_version = ")
            info.append(System.getProperty("os.version"))
            logger.info(info.toString())
        }
        try {
            val b = StringBuffer()
            // print all java/sun properties
            val properties = System.getProperties()
            val list: MutableList<String> = ArrayList()
            for (key in properties.keys) {
                list.add(key as String)
            }
            Collections.sort(list)
            for (key in list) {
                if (key.startsWith("java") || key.startsWith("sun")) {
                    b.append("""Environment key $key = ${properties.getProperty(key)}
""")
                }
            }
            logger.info(b.toString())
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
        }
        mFreeMindCommon = FreeMindCommon(this)
        Resources.Companion.createInstance(this)
    }

    fun init(feedback: FeedBack?) {
        /* This is only for apple but does not harm for the others. */
        System.setProperty("apple.laf.useScreenMenuBar", "true")
        patternsFile = File(freemindDirectory,
                getDefaultProperty("patternsfile"))
        feedback!!.increase("FreeMind.progress.updateLookAndFeel", null)
        updateLookAndFeel()
        feedback.increase("FreeMind.progress.createController", null)
        iconImage = mWindowIcon!!.image
        // Layout everything
        contentPane.layout = BorderLayout()
        controller = Controller(this)
        controller!!.init()
        feedback.increase("FreeMind.progress.settingPreferences", null)
        // add a listener for the controller, resource bundle:
        Controller.addPropertyChangeListener { propertyName, newValue, oldValue ->
            if (propertyName == FreeMindCommon.Companion.RESOURCE_LANGUAGE) {
                // re-read resources:
                mFreeMindCommon.clearLanguageResources()
                resources
            }
        }
        // fc, disabled with purpose (see java look and feel styleguides).
        // http://java.sun.com/products/jlf/ed2/book/index.html
        // // add a listener for the controller, look and feel:
        // Controller.addPropertyChangeListener(new FreemindPropertyListener() {
        //
        // public void propertyChanged(String propertyName, String newValue,
        // String oldValue) {
        // if (propertyName.equals(RESOURCE_LOOKANDFEEL)) {
        // updateLookAndFeel();
        // }
        // }
        // });
        controller!!.optionAntialiasAction
                .changeAntialias(getProperty(FreeMindCommon.Companion.RESOURCE_ANTIALIAS))
        setupSpellChecking()
        setupProxy()
        feedback.increase("FreeMind.progress.propageteLookAndFeel", null)
        SwingUtilities.updateComponentTreeUI(this) // Propagate LookAndFeel to
        feedback.increase("FreeMind.progress.buildScreen", null)
        setScreenBounds()

        // JComponents
        feedback.increase("FreeMind.progress.createInitialMode", null)
        controller!!.createNewMode(getProperty("initial_mode")!!)
        //		EventQueue eventQueue = Toolkit.getDefaultToolkit()
//				.getSystemEventQueue();
//		eventQueue.push(new MyEventQueue());
    } // Constructor

    /**
     *
     */
    private fun updateLookAndFeel() {
        // set Look&Feel
        try {
            val lookAndFeel = props.getProperty(RESOURCE_LOOKANDFEEL)
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
                logger!!.info("Default (System) Look & Feel: "
                        + UIManager.getSystemLookAndFeelClassName())
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName())
            }
        } catch (ex: Exception) {
            System.err.println("Unable to set Look & Feel.")
        }
        mFreeMindCommon.loadUIProperties(defProps)
    }

    override val isApplet: Boolean
        get() = false

    // maintain this methods to keep the last state/size of the window (PN)
    override val winHeight: Int
        get() = height
    override val winWidth: Int
        get() = width
    override val winX: Int
        get() = x
    override val winY: Int
        get() = y
    override val winState: Int
        get() = extendedState

    override fun getResource(name: String): URL? {
        return this.javaClass.classLoader.getResource(name)
    }

    override fun getProperty(key: String?): String? {
        return props.getProperty(key)
    }

    override fun getIntProperty(key: String?, defaultValue: Int): Int {
        return try {
            getProperty(key)!!.toInt()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    override val properties: Properties?
        get() = props

    override fun setProperty(key: String?, value: String?) {
        props.setProperty(key, value)
    }

    fun getDefaultProperty(key: String?): String {
        return defProps.getProperty(key)
    }

    override fun setDefaultProperty(key: String?, value: String?) {
        defProps.setProperty(key, value)
    }

    override val freemindDirectory: String?
        get() = (System.getProperty("user.home") + File.separator
                + getProperty("properties_folder"))

    override fun saveProperties(pIsShutdown: Boolean) {
        try {
            val out: OutputStream = FileOutputStream(autoPropertiesFile)
            val outputStreamWriter = OutputStreamWriter(
                    out, "8859_1")
            outputStreamWriter.write("#FreeMind ")
            outputStreamWriter.write(Companion.freemindVersion.toString())
            outputStreamWriter.write('\n'.code)
            outputStreamWriter.flush()
            //to save as few props as possible.
            val toBeStored = Tools.copyChangedProperties(props, defProps)
            toBeStored!!.store(out, null)
            out.close()
        } catch (ex: Exception) {
            Resources.Companion.getInstance().logException(ex)
        }
        controller!!.filterController!!.saveConditions()
        if (pIsShutdown && mEditServer != null) {
            mEditServer!!.stopServer()
        }
    }

    override var view: MapView?
        get() = controller!!.view
        set(view) {
            scrollPane!!.setViewportView(view)
        }

    override fun out(msg: String?) {
        if (status != null) {
            status!!.text = msg
            // Automatically remove old messages after a certain time.
            mStatusMessageDisplayTimer!!.restart()
            // logger.info(msg);
        }
    }

    override fun err(msg: String) {
        out(msg)
    }

    /* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    override fun actionPerformed(pE: ActionEvent) {
        out("")
        mStatusMessageDisplayTimer!!.stop()
    }

    /**
     * Open URL in system browser
     *
     *
     * Opens the specified URL in the default browser for the operating system.
     *
     * @param url a url pointing to where the browser should open
     * @see URL
     */
    @Throws(Exception::class)
    override fun openDocument(url: URL) {
        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                // fix for https://sourceforge.net/p/freemind/discussion/22102/thread/cf032151/?limit=25#c631
                val uri = URI(url.toString().replace("^file:////".toRegex(), "file://"))
                desktop.browse(uri)
            } catch (e: Exception) {
                logger!!.severe("Caught: $e")
            }
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

    /** Returns the ResourceBundle with the current language  */
    override val resources: ResourceBundle?
        get() = mFreeMindCommon.resources

    override fun getResourceString(resource: String?): String? {
        return mFreeMindCommon.getResourceString(resource)
    }

    override fun getResourceString(key: String?, pDefault: String?): String? {
        return mFreeMindCommon.getResourceString(key, pDefault)
    }

    override fun getLogger(forClass: String?): Logger {
        val loggerForClass = Logger.getLogger(forClass)
        mLoggerList.add(loggerForClass)
        if (mFileHandler == null && !mFileHandlerError) {
            // initialize handlers using an old System.err:
            val parentLogger = loggerForClass.parent
            val handlers = parentLogger.handlers
            for (i in handlers.indices) {
                val handler = handlers[i]
                if (handler is ConsoleHandler) {
                    parentLogger.removeHandler(handler)
                }
            }
            try {
                mFileHandler = FileHandler(freemindDirectory
                        + File.separator + LOG_FILE_NAME, 1400000, 5, false)
                mFileHandler!!.formatter = StdFormatter()
                mFileHandler!!.level = Level.INFO
                parentLogger.addHandler(mFileHandler)
                val stdConsoleHandler = ConsoleHandler()
                stdConsoleHandler.formatter = StdFormatter()
                stdConsoleHandler.level = Level.WARNING
                parentLogger.addHandler(stdConsoleHandler)
                sLogFileHandler = LogFileLogHandler()
                sLogFileHandler!!.formatter = SimpleFormatter()
                sLogFileHandler!!.level = Level.INFO
                var los: LoggingOutputStream
                var logger = Logger.getLogger(StdFormatter.Companion.STDOUT.getName())
                los = LoggingOutputStream(logger, StdFormatter.Companion.STDOUT)
                System.setOut(PrintStream(los, true))
                logger = Logger.getLogger(StdFormatter.Companion.STDERR.getName())
                los = LoggingOutputStream(logger, StdFormatter.Companion.STDERR)
                System.setErr(PrintStream(los, true))
            } catch (e: Exception) {
                System.err.println("Error creating logging File Handler")
                e.printStackTrace()
                mFileHandlerError = true
                // to avoid infinite recursion.
                // freemind.main.Resources.getInstance().logExecption(e);
            }
        }
        if (sLogFileHandler != null) {
            loggerForClass.addHandler(sLogFileHandler)
        }
        return loggerForClass
    }

    private fun setupSpellChecking() {
        val checkSpelling =  //			Resources.getInstance().getBoolProperty(FreeMindCommon.CHECK_SPELLING);
                Tools.safeEquals("true", props.getProperty(FreeMindCommon.Companion.CHECK_SPELLING))
        if (checkSpelling) {
            try {
                // TODO filter languages in dictionaries.properties like this:
//				String[] languages = "en,de,es,fr,it,nl,pl,ru,ar".split(",");
//				for (int i = 0; i < languages.length; i++) {
//					System.out.println(new File("dictionary_" + languages[i] + ".ortho").exists());
//				}
                val decodedPath = Tools.getFreeMindBasePath()
                var url: URL? = null
                if (File(decodedPath).exists()) {
                    url = URL("file", null, decodedPath)
                }
                SpellChecker.registerDictionaries(url, Locale.getDefault().language)
            } catch (e: MalformedURLException) {
                Resources.Companion.getInstance().logException(e)
            } catch (e: UnsupportedEncodingException) {
                Resources.Companion.getInstance().logException(e)
            }
        }
    }

    private fun setupProxy() {
        // proxy settings
        if ("true" == props.getProperty(PROXY_USE_SETTINGS)) {
            if ("true" == props.getProperty(PROXY_IS_AUTHENTICATED)) {
                Authenticator.setDefault(ProxyAuthenticator(props
                        .getProperty(PROXY_USER), Tools.decompress(props
                        .getProperty(PROXY_PASSWORD))))
            }
            System.setProperty("http.proxyHost", props.getProperty(PROXY_HOST))
            System.setProperty("http.proxyPort", props.getProperty(PROXY_PORT))
            System.setProperty("https.proxyHost", props.getProperty(PROXY_HOST))
            System.setProperty("https.proxyPort", props.getProperty(PROXY_PORT))
            System.setProperty("http.nonProxyHosts", props.getProperty(PROXY_EXCEPTION))
        }
    }

    private fun initServer() {
        val portFile = portFile ?: return
        mEditServer = EditServer(portFile, this)
        mEditServer!!.start()
    }

    private fun checkForAnotherInstance(pArgs: Array<String>) {
        val portFile = portFile ?: return
        // {{{ Try connecting to another running FreeMind instance
        if (portFile != null && File(portFile).exists()) {
            try {
                val `in` = BufferedReader(FileReader(portFile))
                val check = `in`.readLine()
                if (check != "b") throw Exception("Wrong port file format")
                val port = `in`.readLine().toInt()
                val key = `in`.readLine().toInt()
                val socket = Socket(InetAddress.getByName("127.0.0.1"),
                        port)
                val out = DataOutputStream(
                        socket.getOutputStream())
                out.writeInt(key)
                val script: String?
                // Put url to open here
                script = Tools.arrayToUrls(pArgs)
                out.writeUTF(script)
                logger!!.info("Waiting for server")
                // block until its closed
                try {
                    socket.getInputStream().read()
                } catch (e: Exception) {
                }
                `in`.close()
                out.close()
                System.exit(0)
            } catch (e: Exception) {
                // ok, this one seems to confuse newbies
                // endlessly, so log it as NOTICE, not
                // ERROR
                logger!!.info("An error occurred"
                        + " while connecting to the FreeMind server instance."
                        + " This probably means that"
                        + " FreeMind crashed and/or exited abnormally"
                        + " the last time it was run." + " If you don't"
                        + " know what this means, don't worry. Exception: " + e)
            }
        }
    }

    /**
     * @return null, if no port should be opened.
     */
    private val portFile: String?
        private get() = if (mEditServer == null
                && Resources.Companion.getInstance().getBoolProperty(
                        RESOURCES_DON_T_OPEN_PORT)) {
            null
        } else freemindDirectory + File.separator + getProperty(PORT_FILE)

    private fun fireStartupDone() {
        mStartupDone = true
        for (listener in mStartupDoneListeners) {
            listener.startupDone()
        }
    }

    private fun setScreenBounds() {
        // Create the MenuBar
        freeMindMenuBar = MenuBar(controller!!)
        jMenuBar = freeMindMenuBar

        // Create the scroll pane
        scrollPane = MapView.ScrollPane()
        if (Resources.Companion.getInstance().getBoolProperty("no_scrollbar")) {
            scrollPane
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER)
            scrollPane
                    .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        } else {
            scrollPane
                    .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS)
            scrollPane
                    .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS)
        }
        status = JLabel("!")
        status!!.preferredSize = status!!.preferredSize
        status!!.text = ""
        mStatusMessageDisplayTimer = Timer(TIME_TO_DISPLAY_MESSAGES, this)
        contentComponent = scrollPane
        val shouldUseTabbedPane: Boolean = Resources.Companion.getInstance().getBoolProperty(
                RESOURCES_USE_TABBED_PANE)
        if (shouldUseTabbedPane) {
            // tabbed panes eat control up. This is corrected here.
            val map: InputMap
            map = UIManager.get("TabbedPane.ancestorInputMap") as InputMap
            val keyStrokeCtrlUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP,
                    InputEvent.CTRL_DOWN_MASK)
            map.remove(keyStrokeCtrlUp)
            mTabbedPane = JTabbedPane()
            mTabbedPane!!.isFocusable = false
            controller!!.addTabbedPane(mTabbedPane)
            contentPane.add(mTabbedPane, BorderLayout.CENTER)
        } else {
            // don't use tabbed panes.
            contentPane.add(contentComponent, BorderLayout.CENTER)
        }
        contentPane.add(status, BorderLayout.SOUTH)

        // Disable the default close button, instead use windowListener
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                controller!!.quit
                        .actionPerformed(ActionEvent(this, 0, "quit"))
            } /*
			 * fc, 14.3.2008: Completely removed, as it damaged the focus if for
			 * example the note window was active.
			 */
            // public void windowActivated(WindowEvent e) {
            // // This doesn't work the first time, it's called too early to
            // // get Focus
            // logger.info("windowActivated");
            // if ((getView() != null) && (getView().getSelected() != null)) {
            // getView().getSelected().requestFocus();
            // }
            // }
        })
        if (Tools.safeEquals(getProperty("toolbarVisible"), "false")) {
            controller!!.setToolbarVisible(false)
        }
        if (Tools.safeEquals(getProperty("leftToolbarVisible"), "false")) {
            controller!!.setLeftToolbarVisible(false)
        }

        // first define the final layout of the screen:
        focusTraversalKeysEnabled = false
        // and now, determine size, position and state.
        pack()
        // set the default size (PN)
        var win_width = getIntProperty("appwindow_width", 0)
        var win_height = getIntProperty("appwindow_height", 0)
        var win_x = getIntProperty("appwindow_x", 0)
        var win_y = getIntProperty("appwindow_y", 0)
        win_width = if (win_width > 0) win_width else 640
        win_height = if (win_height > 0) win_height else 440
        val defaultToolkit = Toolkit.getDefaultToolkit()
        val screenInsets = defaultToolkit
                .getScreenInsets(graphicsConfiguration)
        val screenSize = defaultToolkit.screenSize
        val screenWidth = (screenSize.width - screenInsets.left
                - screenInsets.right)
        win_width = Math.min(win_width, screenWidth)
        val screenHeight = (screenSize.height - screenInsets.top
                - screenInsets.bottom)
        win_height = Math.min(win_height, screenHeight)
        win_x = Math.max(screenInsets.left, win_x)
        win_x = Math.min(screenWidth + screenInsets.left - win_width, win_x)
        win_y = Math.max(screenInsets.top, win_y)
        win_y = Math.min(screenWidth + screenInsets.top - win_height, win_y)
        setBounds(win_x, win_y, win_width, win_height)
        // set the default state (normal/maximized) (PN)
        // (note: this must be done later when partucular
        // initalizations of the windows are ready,
        // perhaps after setVisible is it enough... :-?
        var win_state = props.getProperty(
                "appwindow_state", "0").toInt()
        win_state = if (win_state and ICONIFIED != 0) NORMAL else win_state
        extendedState = win_state
    }

    private fun createModeController(args: Array<String>): ModeController? {
        val ctrl = controller!!.modeController
        // try to load mac module:
        try {
            val macClass = Class.forName("accessories.plugins.MacChanges")
            // lazy programming. the mac class has exactly one
            // constructor
            // with a modeController.
            macClass.constructors[0].newInstance(*arrayOf<Any>(this))
        } catch (e1: Exception) {
            // freemind.main.Resources.getInstance().logExecption(e1);
        }
        return ctrl
    }

    private fun getMaximumNumberOfMapsToLoad(args: Array<String>): Int {
        val management = lastStateStorageManagement
        return Math.max(args.size + management.lastOpenList.size, 1)
    }

    private fun loadMaps(args: Array<String>, pModeController: ModeController?,
                         pFeedBack: FeedBack?) {
        var fileLoaded = false
        if (Tools.isPreferenceTrue(getProperty(FreeMindCommon.Companion.LOAD_LAST_MAPS_AND_LAYOUT))) {
            var index = 0
            var mapToFocus: MapModule? = null
            val management = lastStateStorageManagement
            for (store in management.lastOpenList) {
                val restorable = store!!.restorableName
                pFeedBack!!.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME, arrayOf<Any>(restorable!!.replace(".*/".toRegex(), "")))
                try {
                    if (controller!!.lastOpenedList!!.open(restorable)) {
                        if (index == management.lastFocussedTab) {
                            mapToFocus = controller!!.mapModule
                        }
                    }
                    fileLoaded = true
                } catch (e: Exception) {
                    Resources.Companion.getInstance().logException(e)
                }
                index++
            }
            if (mapToFocus != null) {
                controller!!.mapModuleManager!!.changeToMapModule(
                        mapToFocus.displayName)
            }
        }
        for (i in args.indices) {
            var fileArgument = args[i]
            pFeedBack!!.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME, arrayOf<Any>(fileArgument.replace(".*/".toRegex(), "")))
            if (fileArgument.lowercase(Locale.getDefault()).endsWith(
                            FreeMindCommon.Companion.FREEMIND_FILE_EXTENSION)) {
                if (!Tools.isAbsolutePath(fileArgument)) {
                    fileArgument = (System.getProperty("user.dir")
                            + System.getProperty("file.separator")
                            + fileArgument)
                }
                try {
                    pModeController!!.load(File(fileArgument))
                    fileLoaded = true
                    // logger.info("Attempting to load: " +
                    // args[i]);
                } catch (ex: Exception) {
                    System.err.println("File " + fileArgument
                            + " not found error")
                }
            }
        }
        if (!fileLoaded) {
            fileLoaded = processLoadEventFromStartupPhase()
        }
        if (!fileLoaded) {
            val restoreable = getProperty(FreeMindCommon.Companion.ON_START_IF_NOT_SPECIFIED)
            if (Tools.isPreferenceTrue(getProperty(FreeMindCommon.Companion.LOAD_LAST_MAP))
                    && restoreable != null && restoreable.length > 0) {
                pFeedBack!!.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME, arrayOf<Any>(restoreable.replace(".*/".toRegex(), "")))
                try {
                    controller!!.lastOpenedList!!.open(restoreable)
                    controller!!.modeController!!.view.moveToRoot()
                    fileLoaded = true
                } catch (e: Exception) {
                    Resources.Companion.getInstance().logException(e)
                    out("An error occured on opening the file: " + restoreable
                            + ".")
                }
            }
        }
        if (!fileLoaded
                && Tools.isPreferenceTrue(getProperty(FreeMindCommon.Companion.LOAD_NEW_MAP))) {
            /*
			 * nothing loaded so far. Perhaps, we should display a new map...
			 * According to Summary: On first start FreeMind should show new map
			 * to newbies
			 * https://sourceforge.net/tracker/?func=detail&atid=107118
			 * &aid=1752516&group_id=7118
			 */
            pModeController!!.newMap()
            pFeedBack!!.increase(FREE_MIND_PROGRESS_LOAD_MAPS, null)
        }
    }

    private val lastStateStorageManagement: LastStateStorageManagement
        private get() {
            val lastStateMapXml = getProperty(FreeMindCommon.Companion.MINDMAP_LAST_STATE_MAP_STORAGE)
            return LastStateStorageManagement(
                    lastStateMapXml)
        }

    /**
     * Iterates over the load events from the startup phase
     *
     *
     * More than one file can be opened during startup. The filenames are stored
     * in numbered properties, i.e.
     *
     * loadEventDuringStartup0=/Users/alex/Desktop/test1.mm
     * loadEventDuringStartup1=/Users/alex/Desktop/test2.mm
     *
     *
     * @return true if at least one file has been loaded
     */
    private fun processLoadEventFromStartupPhase(): Boolean {
        var atLeastOneFileHasBeenLoaded = false
        var count = 0
        while (true) {
            val propertyKey: String = (FreeMindCommon.Companion.LOAD_EVENT_DURING_STARTUP
                    + count)
            if (getProperty(propertyKey) == null) {
                break
            } else {
                if (processLoadEventFromStartupPhase(propertyKey)) atLeastOneFileHasBeenLoaded = true
                ++count
            }
        }
        return atLeastOneFileHasBeenLoaded
    }

    private fun processLoadEventFromStartupPhase(propertyKey: String): Boolean {
        val filename = getProperty(propertyKey)
        return try {
            if (logger!!.isLoggable(Level.INFO)) {
                logger!!.info("Loading $filename")
            }
            controller!!.modeController!!.load(
                    Tools.fileToUrl(File(filename)))
            // remove temporary property because we do not want to store in a
            // file and survive restart
            properties!!.remove(propertyKey)
            true
        } catch (e: Exception) {
            Resources.Companion.getInstance().logException(e)
            out("An error occured on opening the file: $filename.")
            false
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.main.FreeMindMain#getJFrame()
	 */
    override val jFrame: JFrame
        get() = this
    override val freeMindClassLoader: ClassLoader?
        get() = mFreeMindCommon.freeMindClassLoader
    override val freemindBaseDir: String?
        get() = mFreeMindCommon.freemindBaseDir

    override fun getAdjustableProperty(label: String): String? {
        return mFreeMindCommon.getAdjustableProperty(label)
    }

    override fun insertComponentIntoSplitPane(pMindMapComponent: JComponent): JSplitPane? {
        if (mSplitPane != null) {
            // already present:
            return mSplitPane
        }
        removeContentComponent()
        var splitType = JSplitPane.VERTICAL_SPLIT
        val splitProperty = getProperty(J_SPLIT_PANE_SPLIT_TYPE)
        if (Tools.safeEquals(splitProperty, HORIZONTAL_SPLIT_RIGHT)) {
            splitType = JSplitPane.HORIZONTAL_SPLIT
        } else if (Tools.safeEquals(splitProperty, VERTICAL_SPLIT_BELOW)) {
            // default
        } else {
            logger!!.warning("Split type not known: $splitProperty")
        }
        mSplitPane = JSplitPane(splitType, scrollPane, pMindMapComponent)
        mSplitPane!!.isContinuousLayout = true
        mSplitPane!!.isOneTouchExpandable = false
        /*
		 * This means that the mind map area gets all the space that results
		 * from resizing the window.
		 */mSplitPane!!.resizeWeight = 1.0
        // split panes eat F8 and F6. This is corrected here.
        Tools.correctJSplitPaneKeyMap()
        contentComponent = mSplitPane
        setContentComponent()
        // set divider position:
        setSplitLocation()
        // after making this window visible, the size is adjusted. To get the right split location, we postpone this.
        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(pE: ComponentEvent) {
                setSplitLocation()
                removeComponentListener(this)
            }
        })
        return mSplitPane
    }

    private fun setSplitLocation() {
        val splitPanePosition = getIntProperty(SPLIT_PANE_POSITION, -1)
        val lastSplitPanePosition = getIntProperty(SPLIT_PANE_LAST_POSITION, -1)
        if (mSplitPane != null && splitPanePosition != -1 && lastSplitPanePosition != -1) {
            mSplitPane!!.dividerLocation = splitPanePosition
            mSplitPane!!.lastDividerLocation = lastSplitPanePosition
        }
    }

    override fun removeSplitPane() {
        if (mSplitPane != null) {
            setProperty(SPLIT_PANE_POSITION,
                    "" + mSplitPane!!.dividerLocation)
            setProperty(SPLIT_PANE_LAST_POSITION,
                    "" + mSplitPane!!.lastDividerLocation)
            removeContentComponent()
            contentComponent = scrollPane
            setContentComponent()
            mSplitPane = null
        }
    }

    private fun removeContentComponent() {
        if (mTabbedPane != null) {
            if (mTabbedPane!!.selectedIndex >= 0) {
                mTabbedPane!!.setComponentAt(mTabbedPane!!.selectedIndex,
                        JPanel())
            }
        } else {
            contentPane.remove(contentComponent)
            getRootPane().revalidate()
        }
    }

    private fun setContentComponent() {
        if (mTabbedPane != null) {
            if (mTabbedPane!!.selectedIndex >= 0) {
                mTabbedPane!!.setComponentAt(mTabbedPane!!.selectedIndex,
                        contentComponent)
            }
        } else {
            contentPane.add(contentComponent, BorderLayout.CENTER)
            getRootPane().revalidate()
        }
    }

    override fun registerStartupDoneListener(
            pStartupDoneListener: StartupDoneListener) {
        if (!mStartupDone) mStartupDoneListeners.add(pStartupDoneListener)
    }

    override val loggerList: List<Logger>
        get() = Collections.unmodifiableList(mLoggerList)

    companion object {
        const val J_SPLIT_PANE_SPLIT_TYPE = "JSplitPane.SPLIT_TYPE"
        const val VERTICAL_SPLIT_BELOW = "vertical_split_below"
        const val HORIZONTAL_SPLIT_RIGHT = "horizontal_split_right"
        const val LOG_FILE_NAME = "log"
        private const val PORT_FILE = "portFile"
        private const val FREE_MIND_PROGRESS_LOAD_MAPS = "FreeMind.progress.loadMaps"
        private const val FREE_MIND_PROGRESS_LOAD_MAPS_NAME = "FreeMind.progress.loadNamedMaps"
        private const val SPLIT_PANE_POSITION = "split_pane_position"
        private const val SPLIT_PANE_LAST_POSITION = "split_pane_last_position"
        const val RESOURCE_LOOKANDFEEL = "lookandfeel"
        const val RESOURCES_SELECTION_METHOD = "selection_method"
        const val RESOURCES_NODE_STYLE = "standardnodestyle"
        const val RESOURCES_ROOT_NODE_STYLE = "standardrootnodestyle"
        const val RESOURCES_NODE_TEXT_COLOR = "standardnodetextcolor"
        const val RESOURCES_SELECTED_NODE_COLOR = "standardselectednodecolor"
        const val RESOURCES_SELECTED_NODE_RECTANGLE_COLOR = "standardselectednoderectanglecolor"
        const val RESOURCE_DRAW_RECTANGLE_FOR_SELECTION = "standarddrawrectangleforselection"
        const val RESOURCES_EDGE_COLOR = "standardedgecolor"
        const val RESOURCES_EDGE_STYLE = "standardedgestyle"
        const val RESOURCES_CLOUD_COLOR = "standardcloudcolor"
        const val RESOURCES_LINK_COLOR = "standardlinkcolor"
        const val RESOURCES_BACKGROUND_COLOR = "standardbackgroundcolor"
        const val RESOURCE_PRINT_ON_WHITE_BACKGROUND = "printonwhitebackground"
        const val RESOURCES_WHEEL_VELOCITY = "wheel_velocity"
        const val RESOURCES_USE_TABBED_PANE = "use_tabbed_pane"
        const val RESOURCES_SHOW_NOTE_PANE = "use_split_pane"
        const val RESOURCES_SHOW_ATTRIBUTE_PANE = "show_attribute_pane"
        const val RESOURCES_DELETE_NODES_WITHOUT_QUESTION = "delete_nodes_without_question"
        const val RESOURCES_RELOAD_FILES_WITHOUT_QUESTION = "reload_files_without_question"
        val freemindVersion = VersionInformation("1.1.0 Beta 2")
            get() = Companion.field
        const val XML_VERSION = "1.1.0"
        const val RESOURCES_REMIND_USE_RICH_TEXT_IN_NEW_LONG_NODES = "remind_use_rich_text_in_new_long_nodes"
        const val RESOURCES_EXECUTE_SCRIPTS_WITHOUT_ASKING = "resources_execute_scripts_without_asking"
        const val RESOURCES_EXECUTE_SCRIPTS_WITHOUT_FILE_RESTRICTION = "resources_execute_scripts_without_file_restriction"
        const val RESOURCES_EXECUTE_SCRIPTS_WITHOUT_NETWORK_RESTRICTION = "resources_execute_scripts_without_network_restriction"
        const val RESOURCES_EXECUTE_SCRIPTS_WITHOUT_EXEC_RESTRICTION = "resources_execute_scripts_without_exec_restriction"
        const val RESOURCES_SCRIPT_USER_KEY_NAME_FOR_SIGNING = "resources_script_user_key_name_for_signing"
        const val RESOURCES_CONVERT_TO_CURRENT_VERSION = "resources_convert_to_current_version"
        const val RESOURCES_CUT_NODES_WITHOUT_QUESTION = "resources_cut_nodes_without_question"
        const val RESOURCES_DON_T_SHOW_NOTE_ICONS = "resources_don_t_show_note_icons"
        const val RESOURCES_USE_COLLABORATION_SERVER_WITH_DIFFERENT_VERSION = "resources_use_collaboration_server_with_different_version"
        const val RESOURCES_REMOVE_NOTES_WITHOUT_QUESTION = "resources_remove_notes_without_question"
        const val RESOURCES_SAVE_FOLDING_STATE = "resources_save_folding_state"
        const val RESOURCES_SIGNED_SCRIPT_ARE_TRUSTED = "resources_signed_script_are_trusted"
        const val RESOURCES_USE_DEFAULT_FONT_FOR_NOTES_TOO = "resources_use_default_font_for_notes_too"
        const val RESOURCES_USE_MARGIN_TOP_ZERO_FOR_NOTES = "resources_use_margin_top_zero_for_notes"
        const val RESOURCES_DON_T_SHOW_CLONE_ICONS = "resources_don_t_show_clone_icons"
        const val RESOURCES_DON_T_OPEN_PORT = "resources_don_t_open_port"
        const val KEYSTROKE_MOVE_MAP_LEFT = "keystroke_MoveMapLeft"
        const val KEYSTROKE_MOVE_MAP_RIGHT = "keystroke_MoveMapRight"
        const val KEYSTROKE_PREVIOUS_MAP = "keystroke_previousMap"
        const val KEYSTROKE_NEXT_MAP = "keystroke_nextMap"
        const val RESOURCES_SEARCH_IN_NOTES_TOO = "resources_search_in_notes_too"
        const val RESOURCES_DON_T_SHOW_NOTE_TOOLTIPS = "resources_don_t_show_note_tooltips"
        const val RESOURCES_SEARCH_FOR_NODE_TEXT_WITHOUT_QUESTION = "resources_search_for_node_text_without_question"
        const val RESOURCES_COMPLETE_CLONING = "complete_cloning"
        const val RESOURCES_CLONE_TYPE_COMPLETE_CLONE = "COMPLETE_CLONE"
        const val TOOLTIP_DISPLAY_TIME = "tooltip_display_time"
        const val PROXY_PORT = "proxy.port"
        const val PROXY_HOST = "proxy.host"
        const val PROXY_PASSWORD = "proxy.password"
        const val PROXY_USER = "proxy.user"
        const val PROXY_IS_AUTHENTICATED = "proxy.is_authenticated"
        const val PROXY_USE_SETTINGS = "proxy.use_settings"
        const val RESOURCES_DISPLAY_FOLDING_BUTTONS = "resources_display_folding_buttons"
        private const val TIME_TO_DISPLAY_MESSAGES = 10000
        const val ICON_BAR_COLUMN_AMOUNT = "icon_bar_column_amount"
        const val RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION = "resources_optional_split_divider_position"
        const val RESOUCES_PASTE_HTML_STRUCTURE = "paste_html_structure"
        const val PROXY_EXCEPTION = "proxy.exception"
        const val SCALING_FACTOR_PROPERTY = "scaling_factor_property"
        const val RESOURCES_CALENDAR_FONT_SIZE = "calendar_font_size"

        // public static final String defaultPropsURL = "freemind.properties";
        // public static Properties defaultProps;
        var props: Properties
        private var defProps: Properties
        private var mFileHandler: FileHandler? = null
        private var mFileHandlerError = false
        private var sLogFileHandler: LogFileLogHandler? = null
        fun main(args: Array<String>,
                 pDefaultPreferences: Properties, pUserPreferences: Properties,
                 pAutoPropertiesFile: File) {
            try {
                val frame = FreeMind(pDefaultPreferences,
                        pUserPreferences, pAutoPropertiesFile)
                val scale = frame.getIntProperty(SCALING_FACTOR_PROPERTY, 100)
                if (scale != 100) {
                    Tools.scaleAllFonts(scale / 100f)
                    val SEGOE_UI_PLAIN_12 = Font("Segoe UI", Font.PLAIN,
                            12 * scale / 100)
                    UIManager.put("MenuItem.acceleratorFont", SEGOE_UI_PLAIN_12)
                    UIManager.put("Menu.acceleratorFont", SEGOE_UI_PLAIN_12)
                    UIManager.put("CheckBoxMenuItem.acceleratorFont",
                            SEGOE_UI_PLAIN_12)
                    UIManager.put("RadioButtonMenuItem.acceleratorFont",
                            SEGOE_UI_PLAIN_12)
                }
                var splash: IFreeMindSplash? = null
                frame.checkForAnotherInstance(args)
                frame.initServer()
                val feedBack: FeedBack?
                splash = FreeMindSplashModern(frame)
                splash.setVisible(true)
                feedBack = splash.getFeedBack()
                frame.mWindowIcon = splash.getWindowIcon()
                feedBack.setMaximumValue(10 + frame.getMaximumNumberOfMapsToLoad(args))
                frame.init(feedBack)
                feedBack.increase("FreeMind.progress.startCreateController", null)
                val ctrl = frame.createModeController(args)
                feedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS, null)
                frame.loadMaps(args, ctrl, feedBack)
                Tools.waitForEventQueue()
                feedBack.increase("FreeMind.progress.endStartup", null)
                // focus fix after startup.
                frame.addWindowFocusListener(object : WindowFocusListener {
                    override fun windowLostFocus(e: WindowEvent) {}
                    override fun windowGainedFocus(e: WindowEvent) {
                        frame.controller!!.obtainFocusForSelected()
                        frame.removeWindowFocusListener(this)
                    }
                })
                frame.isVisible = true
                if (splash != null) {
                    splash.setVisible(false)
                }
                frame.fireStartupDone()
            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showMessageDialog(null,
                        """
                            FreeMind can't be started: ${e.localizedMessage}
                            ${Tools.getStacktrace(e)}
                            """.trimIndent(),
                        "Startup problem", JOptionPane.ERROR_MESSAGE)
                System.exit(1)
            }
        }
    }
}