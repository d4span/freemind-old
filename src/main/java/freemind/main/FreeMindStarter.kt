/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Dimitri Polivaev, Christian Foltin and others.
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
 * Created on 06.07.2006
 */
/*$Id: FreeMindStarter.java,v 1.1.2.11 2009/03/29 19:37:23 christianfoltin Exp $*/
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
import java.util.StringTokenizer
import java.util.LinkedList
import java.lang.StringBuffer
import java.util.Locale
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
import java.util.Properties
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
import java.util.Collections
import freemind.main.FeedBack
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import freemind.view.mindmapview.MapView
import java.util.ResourceBundle
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
import java.awt.*
import javax.swing.JRootPane
import java.io.*
import java.lang.Exception
import java.net.*

/**
 * This class should check the java version and start freemind. In order to be
 * able to check, it must be startable with java versions < 1.4. We have
 * therefore a section in the build.xml that explicitly compiles this class for
 * java 1.1 compatibility. Currently, it is unclear, if this works as expected.
 * But in any case, almost no dependencies to other FreeMind sources should be
 * used here.
 *
 * @author foltin
 */
class FreeMindStarter {
    private fun checkJavaVersion() {
        println("Checking Java Version...")
        if (JAVA_VERSION.compareTo("1.6.0") < 0) {
            val message = ("Warning: FreeMind requires version Java 1.6.0 or higher (your version: "
                    + JAVA_VERSION
                    + ", installed in "
                    + System.getProperty("java.home") + ").")
            System.err.println(message)
            JOptionPane.showMessageDialog(null, message, "FreeMind",
                    JOptionPane.WARNING_MESSAGE)
            System.exit(1)
        }
        println("Checking Java Version done.")
    }

    private fun createUserDirectory(pDefaultProperties: Properties) {
        val userPropertiesFolder = File(
                getFreeMindDirectory(pDefaultProperties))
        try {
            // create user directory:
            if (!userPropertiesFolder.exists()) {
                userPropertiesFolder.mkdir()
            }
        } catch (e: Exception) {
            // exception is logged to console as we don't have a logger
            e.printStackTrace()
            System.err.println("Cannot create folder for user properties and logging: '"
                    + userPropertiesFolder.absolutePath + "'")
        }
    }

    /**
     * @param pProperties
     */
    private fun setDefaultLocale(pProperties: Properties) {
        val lang = pProperties.getProperty(FreeMindCommon.Companion.RESOURCE_LANGUAGE) ?: return
        var localeDef: Locale? = null
        localeDef = when (lang.length) {
            2 -> Locale(lang)
            5 -> Locale(lang.substring(0, 1), lang.substring(3, 4))
            else -> return
        }
        Locale.setDefault(localeDef)
    }

    private fun readUsersPreferences(defaultPreferences: Properties): Properties {
        var auto: Properties? = null
        auto = Properties(defaultPreferences)
        try {
            var `in`: InputStream? = null
            val autoPropertiesFile = getUserPreferencesFile(defaultPreferences)
            `in` = FileInputStream(autoPropertiesFile)
            auto.load(`in`)
            `in`.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
            System.err.println("Panic! Error while loading default properties.")
        }
        return auto
    }

    private fun getUserPreferencesFile(defaultPreferences: Properties?): File {
        if (defaultPreferences == null) {
            System.err.println("Panic! Error while loading default properties.")
            System.exit(1)
        }
        val freemindDirectory = getFreeMindDirectory(defaultPreferences)
        val userPropertiesFolder = File(freemindDirectory)
        return File(userPropertiesFolder,
                defaultPreferences!!.getProperty("autoproperties"))
    }

    private fun getFreeMindDirectory(defaultPreferences: Properties?): String {
        return (System.getProperty("user.home") + File.separator
                + defaultPreferences!!.getProperty("properties_folder"))
    }

    fun readDefaultPreferences(): Properties {
        val propsLoc = "freemind.properties"
        val defaultPropsURL = this.javaClass.classLoader.getResource(propsLoc)
        val props = Properties()
        try {
            val `in` = defaultPropsURL.openStream()
            props.load(`in`)
            `in`.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
            System.err.println("Panic! Error while loading default properties.")
        }
        return props
    }

    class ProxyAuthenticator(private val user: String, private val password: String?) : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(user, password!!.toCharArray())
        }
    }

    companion object {
        /** Doubled variable on purpose. See header of this class.  */
        val JAVA_VERSION = System.getProperty("java.version")
        @JvmStatic
        fun main(args: Array<String>) {
            val starter = FreeMindStarter()
            // First check version of Java
            starter.checkJavaVersion()
            val defaultPreferences = starter.readDefaultPreferences()
            starter.createUserDirectory(defaultPreferences)
            val userPreferences = starter.readUsersPreferences(defaultPreferences)
            starter.setDefaultLocale(userPreferences)

            // Christopher Robin Elmersson: set
            val xToolkit = Toolkit.getDefaultToolkit()

            // workaround for java bug http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7075600
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true")
            try {
                val awtAppClassNameField = xToolkit.javaClass.getDeclaredField("awtAppClassName")
                awtAppClassNameField.isAccessible = true
                try {
                    awtAppClassNameField[xToolkit] = "FreeMind"
                } catch (ex: IllegalAccessException) {
                    System.err.println("Could not set window name")
                }
            } catch (ex: NoSuchFieldException) {
                // System.err.println("Could not get awtAppClassName");
            }

            // use reflection to call :
            // FreeMind.main(args, defaultPreferences, userPreferences,
            // starter.getUserPreferencesFile(defaultPreferences));
            try {
                val mainClass = Class.forName("freemind.main.FreeMind")
                val mainMethod = mainClass.getMethod("main", *arrayOf<Class<*>>(
                        Array<String>::class.java, Properties::class.java, Properties::class.java,
                        File::class.java))
                mainMethod.invoke(null, *arrayOf<Any>(
                        args,
                        defaultPreferences,
                        userPreferences,
                        starter.getUserPreferencesFile(defaultPreferences)))
            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showMessageDialog(null,
                        """
                            freemind.main.FreeMind can't be started: ${e.localizedMessage}
                            ${Tools.getStacktrace(e)}
                            """.trimIndent(),
                        "Startup problem", JOptionPane.ERROR_MESSAGE)
                System.exit(1)
            }
        }
    }
}