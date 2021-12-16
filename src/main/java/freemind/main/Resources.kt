/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 */
/*
 * Created on 12.07.2005
 * Copyright (C) 2005-2008 Dimitri Polivaev, Christian Foltin
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
import java.awt.Color
import java.util.StringTokenizer
import java.util.LinkedList
import java.lang.StringBuffer
import java.awt.GraphicsEnvironment
import java.util.Locale
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
import freemind.common.NamedObject
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
import java.text.MessageFormat
import freemind.modes.FreeMindFileDialog
import freemind.modes.FreeMindJFileDialog
import freemind.modes.FreeMindAwtFileDialog
import tests.freemind.FreeMindMainMock
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
import java.net.*
import java.util.HashMap
import java.util.logging.*
import javax.swing.filechooser.FileFilter

/**
 * @author Dimitri Polivaev 12.07.2005
 */
class Resources private constructor(private val main: FreeMindMain) : TextTranslator {
    private var countryMap: HashMap<String, String>? = null
    private var logger: Logger? = null

    init {
        if (logger == null) {
            logger = main.getLogger(this.javaClass.name)
        }
    }

    fun getResource(resource: String): URL? {
        return main.getResource(resource)
    }

    fun getResourceString(resource: String?): String? {
        return main.getResourceString(resource)
    }

    fun getResourceString(key: String?, resource: String?): String? {
        return main.getResourceString(key, resource)
    }

    val freemindDirectory: String?
        get() = main.freemindDirectory
    val freemindBaseDir: String?
        get() = main.freemindBaseDir
    val freemindVersion: VersionInformation?
        get() = main.freemindVersion
    val freeMindClassLoader: ClassLoader?
        get() = main.freeMindClassLoader

    fun getIntProperty(key: String?, defaultValue: Int): Int {
        return main.getIntProperty(key, defaultValue)
    }

    fun getLongProperty(key: String?, defaultValue: Long): Long {
        return try {
            getProperty(key)!!.toLong()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    /**
     * @param key
     * Property key
     * @return the boolean value of the property resp. the default.
     */
    fun getBoolProperty(key: String?): Boolean {
        val boolProperty = getProperty(key)
        return Tools.safeEquals("true", boolProperty)
    }

    val properties: Properties?
        get() = main.properties

    fun getProperty(key: String?): String? {
        return main.getProperty(key)
    }

    val resources: ResourceBundle?
        get() = main.resources

    fun getCountryMap(): HashMap<String, String> {
        if (countryMap == null) {
            val countryMapArray = arrayOf("de", "DE", "en", "UK",
                    "en", "US", "es", "ES", "es", "MX", "fi", "FI", "fr", "FR",
                    "hu", "HU", "it", "CH", "it", "IT", "nl", "NL", "no", "NO",
                    "pt", "PT", "ru", "RU", "sl", "SI", "uk", "UA", "zh", "CN")
            countryMap = HashMap()
            var i = 0
            while (i < countryMapArray.size) {
                countryMap!![countryMapArray[i]] = countryMapArray[i + 1]
                i = i + 2
            }
        }
        return countryMap!!
    }

    /* To obtain a logging element, ask here. */
    fun getLogger(forClass: String?): Logger? {
        return main.getLogger(forClass)
    }

    @JvmOverloads
    fun logException(e: Throwable?, comment: String = "") {
        logger!!.log(Level.SEVERE, "An exception occured: $comment", e)
    }

    fun format(resourceKey: String?, messageArguments: Array<Any?>?): String {
        val formatter = MessageFormat(getResourceString(resourceKey))
        return formatter.format(messageArguments)
    }

    fun createTranslatedString(key: String?): NamedObject {
        val fs = getResourceString(key)
        return NamedObject(key, fs)
    }

    override fun getText(pKey: String?): String {
        return getResourceString(pKey)!!
    }

    fun getStandardFileChooser(filter: FileFilter?): FreeMindFileDialog {
        val chooser: FreeMindFileDialog
        chooser = if (!Tools.isMacOsX()) {
            FreeMindJFileDialog()
        } else {
            // only for mac
            FreeMindAwtFileDialog()
        }
        if (filter != null) {
            chooser.addChoosableFileFilterAsDefault(filter)
        }
        return chooser
    }

    /**
     * @param baseFileName
     * @return
     */
    fun createThumbnailFileName(baseFileName: File): String {
        return (baseFileName.parent
                + File.separatorChar
                + "." // hidden
                + baseFileName.name.replaceFirst((
                FreeMindCommon.Companion.FREEMIND_FILE_EXTENSION + "$").toRegex(),
                ".png"))
    }

    companion object {
        var resourcesInstance: Resources? = null
        fun createInstance(frame: FreeMindMain) {
            if (resourcesInstance == null) {
                resourcesInstance = Resources(frame)
            }
        }

        val instance: Resources?
            get() {
                if (resourcesInstance == null) {
                    createInstance(FreeMindMainMock())
                    System.err.println("Resources without FreeMind called.")
                }
                return resourcesInstance
            }
    }
}