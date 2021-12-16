/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
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
 *
 * Created on 10.01.2006
 */
/*$Id: FreeMindCommon.java,v 1.1.2.2.2.39 2009/05/18 19:47:57 christianfoltin Exp $*/
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
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.logging.Logger

/**
 * @author foltin
 */
class FreeMindCommon(private val mFreeMindMain: FreeMindMain) {
    private inner class FreeMindResourceBundle internal constructor() : ResourceBundle() {
        private var languageResources: PropertyResourceBundle? = null
        private var defaultResources: PropertyResourceBundle? = null

        init {
            try {
                var lang = getProperty(RESOURCE_LANGUAGE)
                if (lang == null || lang == "automatic") {
                    lang = (Locale.getDefault().language + "_"
                            + Locale.getDefault().country)
                    if (getLanguageResources(lang) == null) {
                        lang = Locale.getDefault().language
                        if (getLanguageResources(lang) == null) {
                            // default is english.
                            lang = DEFAULT_LANGUAGE
                        }
                    }
                }
                if ("no" == lang) {
                    // Bugs item #1935818
                    lang = "nb"
                }
                languageResources = getLanguageResources(lang)
                /*
				 * fc, 26.4.2008. the following line is a bug, as the
				 * defaultResources are used, even, when a single string is
				 * missing inside a bundle and not only, when the complete
				 * bundle is missing.
				 */
                // if(languageResources == null)
                defaultResources = getLanguageResources(DEFAULT_LANGUAGE)
            } catch (ex: Exception) {
                Resources.Companion.getInstance().logException(ex)
                logger!!.severe("Error loading Resources")
            }
            // printResourceTable();
        }
        // /** This is useful, if you want to see all resource strings in a HTML
        // table.
        // * Just rename the log file to log.0.html, open in a browser and set
        // the
        // * coding to UTF-8 */
        // private void printResourceTable() {
        // StringBuffer b = new StringBuffer("<html><body><table>");
        // Set keySet = languageResources.keySet();
        // Vector keys = new Vector(keySet);
        // Collections.sort(keys);
        // for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
        // String key = (String) iterator.next();
        // b.append("<tr><td>" + key + "</td><td>" +
        // languageResources.getString(key)+"</td></tr>\n");
        // }
        // b.append("</table></body></html>");
        // logger.info(b.toString());
        // }
        /**
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun getLanguageResources(lang: String?): PropertyResourceBundle? {
            val systemResource = mFreeMindMain.getResource("Resources_" + lang
                    + ".properties") ?: return null
            val `in` = systemResource.openStream() ?: return null
            val bundle = PropertyResourceBundle(`in`)
            `in`.close()
            return bundle
        }

        override fun handleGetObject(key: String): Any {
            return try {
                languageResources!!.getString(key)
            } catch (ex: Exception) {
                if (key != null && key.startsWith("__")) {
                    // private string, only translate on demand
                    key
                } else {
                    logger!!.severe("Warning - resource string not found:\n$key")
                    defaultResources!!.getString(key) + POSTFIX_TRANSLATE_ME
                }
            }
        }

        override fun getKeys(): Enumeration<String> {
            return defaultResources!!.keys
        }

        fun getResourceString(key: String?): String? {
            return try {
                getString(key)
            } catch (ex: Exception) {
                key
            }
        }

        fun getResourceString(key: String?, pDefault: String?): String? {
            return try {
                try {
                    languageResources!!.getString(key)
                } catch (ex: Exception) {
                    (defaultResources!!.getString(key)
                            + POSTFIX_TRANSLATE_ME)
                }
            } catch (e: Exception) {
                // logger.info(key+" not found.");
                pDefault
            }
        }
    }

    private var baseDir: String? = null
    private var resources: FreeMindResourceBundle? = null

    /**
     *
     */
    init {
        // TODO Auto-generated constructor stub
        if (logger == null) logger = mFreeMindMain.getLogger(this.javaClass.name)
    }

    fun getProperty(key: String?): String? {
        return mFreeMindMain.getProperty(key)
    }

    private fun setDefaultProperty(key: String, value: String) {
        mFreeMindMain.setDefaultProperty(key, value)
    }

    /** Returns the ResourceBundle with the current language  */
    fun getResources(): ResourceBundle {
        if (resources == null) {
            resources = FreeMindResourceBundle()
        }
        return resources!!
    }

    fun getResourceString(key: String?): String? {
        return (getResources() as FreeMindResourceBundle).getResourceString(key)
    }

    fun getResourceString(key: String?, pDefault: String?): String? {
        return (getResources() as FreeMindResourceBundle).getResourceString(key,
                pDefault)
    }

    fun clearLanguageResources() {
        resources = null
    }

    val freeMindClassLoader: ClassLoader
        get() {
            val classLoader = this.javaClass.classLoader
            return try {
                URLClassLoader(arrayOf(Tools.fileToUrl(File(
                        freemindBaseDir))), classLoader)
            } catch (e: MalformedURLException) {
                Resources.Companion.getInstance().logException(e)
                classLoader
            }
        }// if freemind.jar is not found in the class path use user.dir as
    // Basedir
    /*
* fc: Now, if freemind.jar is the first, firstpos == -1. This
* results in bad results in the substring method, or not??
*/
    /*
* I suppose, that here, the freemind.jar is removed together with
* the last path. Example: /home/foltin/freemindapp/lib/freemind.jar
* gives /home/foltin/freemindapp
*/
    /**
     * Old version using String manipulation out of the classpath to find the
     * base dir.
     */
    val freemindBaseDirOld: String?
        get() {
            if (baseDir == null) {
                val classPath = System.getProperty("java.class.path")
                val mainJarFile = "freemind.jar"
                var lastpos = classPath.indexOf(mainJarFile)
                var firstpos = 0
                // if freemind.jar is not found in the class path use user.dir as
                // Basedir
                if (lastpos == -1) {
                    baseDir = System.getProperty("user.dir")
                    logger!!.info("Basedir is user.dir: $baseDir")
                    return baseDir
                }
                /*
      * fc: Now, if freemind.jar is the first, firstpos == -1. This
      * results in bad results in the substring method, or not??
      */firstpos = classPath.lastIndexOf(File.pathSeparator, lastpos) + 1
                lastpos -= 1
                baseDir = if (lastpos > firstpos) {
                    logger!!.info("First " + firstpos + " and last " + lastpos
                            + " and string " + classPath)
                    classPath.substring(firstpos, lastpos)
                } else ""
                val basePath = File(baseDir)
                baseDir = basePath.absolutePath
                logger!!.info("First basedir is: $baseDir")
                /*
      * I suppose, that here, the freemind.jar is removed together with
      * the last path. Example: /home/foltin/freemindapp/lib/freemind.jar
      * gives /home/foltin/freemindapp
      */lastpos = baseDir.lastIndexOf(File.separator)
                if (lastpos > -1) baseDir = baseDir.substring(0, lastpos)
                logger!!.info("Basedir is: $baseDir")
            }
            return baseDir
        }// Property isn't set, we try to find the

    // base directory ourselves.
    // System.err.println("property not set");
    // We locate first the current class.
    // then we create a file out of it, after
    // removing file: and jar:, removing everything
    // after !, as well as the class name part.
    // Finally we decode everything (e.g. %20)
    // TODO: is UTF-8 always the right value?
    // if it's a file, we take its parent, a dir
    /*
* Now, we remove the lib directory: Example:
* /home/foltin/freemindapp/lib/freemind.jar gives
* /home/foltin/freemindapp
*/
    // then we check if the directory exists and is really
    // a directory.
    // set the member variable
    // return the value of the cache variable
    /*
	 * We define the base dir of FreeMind as the directory where accessories,
	 * plugins and other things are to be found. We expect it to be either the
	 * directory where the main jar file is (freemind.jar), or the root of the
	 * class hierarchy (if no jar file is used), after any 'lib' directory is
	 * removed. One can overwrite this definition by setting the
	 * freemind.base.dir property.
	 */
    val freemindBaseDir: String?
        get() {
            if (baseDir == null) {
                try {
                    var file: File
                    val dir = System.getProperty("freemind.base.dir")
                    if (dir == null) {
                        // Property isn't set, we try to find the
                        // base directory ourselves.
                        // System.err.println("property not set");
                        // We locate first the current class.
                        val classname = this.javaClass.name
                        val url = this.javaClass.getResource(classname.replaceFirst(("^"
                                + this.javaClass.getPackage().name
                                + ".").toRegex(), "")
                                + ".class")
                        // then we create a file out of it, after
                        // removing file: and jar:, removing everything
                        // after !, as well as the class name part.
                        // Finally we decode everything (e.g. %20)
                        // TODO: is UTF-8 always the right value?
                        file = File(URLDecoder.decode(
                                url.path
                                        .replaceFirst("^(file:|jar:)+".toRegex(), "")
                                        .replaceFirst("!.*$".toRegex(), "")
                                        .replaceFirst((
                                                classname.replace('.', '/')
                                                        + ".class$").toRegex(), ""), "UTF-8"))
                        // if it's a file, we take its parent, a dir
                        if (file.isFile) file = file.parentFile
                        /*
					 * Now, we remove the lib directory: Example:
					 * /home/foltin/freemindapp/lib/freemind.jar gives
					 * /home/foltin/freemindapp
					 */if (file.name == "lib") file = file.parentFile
                    } else {
                        file = File(dir)
                    }
                    // then we check if the directory exists and is really
                    // a directory.
                    require(file.exists()) {
                        ("FreeMind base dir '"
                                + file + "' does not exist.")
                    }
                    require(file.isDirectory) {
                        ("FreeMind base dir (!) '" + file
                                + "' is not a directory.")
                    }
                    // set the member variable
                    baseDir = file.canonicalPath
                    logger!!.info("Basedir is: $baseDir")
                } catch (e: Exception) {
                    Resources.Companion.getInstance().logException(e)
                    throw IllegalArgumentException(
                            "FreeMind base dir can't be determined.")
                }
            }
            // return the value of the cache variable
            return baseDir
        }

    fun getAdjustableProperty(label: String): String? {
        var value = getProperty(label)
        if (value == null) {
            return value
        }
        if (value.startsWith("?") && value != "?") {
            // try to look in the language specific properties
            val localValue = (getResources() as FreeMindResourceBundle)
                    .getResourceString(LOCAL_PROPERTIES + label, null)
            value = localValue ?: value.substring(1).trim { it <= ' ' }
            setDefaultProperty(label, value)
        }
        return value
    }

    fun loadUIProperties(props: Properties?) {
        // props.put(FreeMind.RESOURCES_BACKGROUND_COLOR,
        // Tools.colorToXml(UIManager.getColor("text")));
        // props.put(FreeMind.RESOURCES_NODE_TEXT_COLOR,
        // Tools.colorToXml(UIManager.getColor("textText")));
        // props.put(FreeMind.RESOURCES_SELECTED_NODE_COLOR,
        // Tools.colorToXml(UIManager.getColor("textHighlight")));
        // props.put(FreeMind.RESOURCES_SELECTED_NODE_TEXT_COLOR,
        // Tools.colorToXml(UIManager.getColor("textHighlightText")));
    }

    companion object {
        const val FREEMIND_FILE_EXTENSION_WITHOUT_DOT = "mm"
        const val FREEMIND_FILE_EXTENSION = ("."
                + FREEMIND_FILE_EXTENSION_WITHOUT_DOT)
        const val POSTFIX_TRANSLATE_ME = "[translate me]"
        const val RESOURCE_LANGUAGE = "language"
        const val CHECK_SPELLING = "check_spelling"
        const val RESOURCE_ANTIALIAS = "antialias"
        const val DEFAULT_LANGUAGE = "en"
        const val LOCAL_PROPERTIES = "LocalProperties."
        const val THUMBNAIL_SIZE = "thumbnail_size"

        /**
         * Holds the last opened map.
         */
        const val ON_START_IF_NOT_SPECIFIED = "onStartIfNotSpecified"
        const val LOAD_LAST_MAP = "loadLastMap"
        const val LOAD_LAST_MAPS_AND_LAYOUT = "load_last_maps_and_layout"
        const val SAVE_ONLY_INTRISICALLY_NEEDED_IDS = "save_only_intrisically_needed_ids"
        const val LOAD_NEW_MAP = "load_new_map_when_no_other_is_specified"

        /**
         * Load event occurred during startup
         *
         *
         * If FreeMind is not started and you double-click a .mm file on Mac OS X
         * the .mm file is not passed to Java's main method but handleOpenFile is
         * called which happens during startup where it is not safe to already load
         * the map. Therefore the event is stored in this property and later
         * processed by loadMaps.
         *
         *
         * Related issues
         *
         *  *
         * http://sourceforge.net/tracker/?func=detail&aid=2908045&group_id=7118&
         * atid=107118
         *  * http://sourceforge.net/tracker/index.php?func=detail&aid=1980423&
         * group_id=7118&atid=107118
         *
         */
        const val LOAD_EVENT_DURING_STARTUP = "loadEventDuringStartup"
        const val MINDMAP_LAST_STATE_MAP_STORAGE = "mindmap_last_state_map_storage"
        const val CREATE_THUMBNAIL_ON_SAVE = "create_thumbnail_on_save"
        const val TIME_MANAGEMENT_MARKING_XML = "time_management_marking_xml"
        private var logger: Logger? = null
    }
}