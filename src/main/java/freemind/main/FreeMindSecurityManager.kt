/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2008 Christian Foltin and others.
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
 * Created on 06.03.2008
 */
/*$Id: FreeMindSecurityManager.java,v 1.1.2.1 2008/03/14 21:15:22 christianfoltin Exp $*/
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
import java.io.IOException
import java.util.zip.Inflater
import java.util.zip.DataFormatException
import java.io.UnsupportedEncodingException
import java.lang.RuntimeException
import freemind.main.Tools.BooleanHolder
import javax.swing.JDialog
import java.awt.Dimension
import java.awt.Insets
import kotlin.Throws
import java.lang.Runnable
import freemind.main.HtmlTools
import java.io.FileNotFoundException
import java.io.BufferedReader
import java.io.FileReader
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
import java.io.PrintWriter
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
import java.io.FileInputStream
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
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import freemind.view.mindmapview.MapView
import java.awt.Desktop
import java.awt.Cursor
import java.util.ResourceBundle
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
import java.util.logging.SimpleFormatter
import java.io.PrintStream
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
import java.io.DataInputStream
import java.net.ServerSocket
import java.io.FileWriter
import java.util.TreeMap
import freemind.main.XMLElement
import java.util.Enumeration
import java.lang.ClassCastException
import java.io.CharArrayReader
import freemind.main.FixedHTMLWriter
import freemind.main.XHTMLWriter.XHTMLFilterWriter
import java.io.FilterWriter
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
import java.io.FileDescriptor
import java.lang.UnsupportedOperationException
import java.security.Permission

/**
 * By default, everything is allowed. But you can install a different security
 * controller once, until you install it again. Thus, the code executed in
 * between is securely controlled by that different security manager. Moreover,
 * only by double registering the manager is removed. So, no malicious code can
 * remove the active security manager.
 *
 * @author foltin
 */
class FreeMindSecurityManager : SecurityManager() {
    private var mFinalSecurityManager: SecurityManager? = null

    /**
     * @param pFinalSecurityManager
     * set twice the same to remove it.
     */
    fun setFinalSecurityManager(pFinalSecurityManager: SecurityManager) {
        if (pFinalSecurityManager === mFinalSecurityManager) {
            mFinalSecurityManager = null
            return
        }
        if (mFinalSecurityManager != null) {
            throw SecurityException(
                    "There is a SecurityManager installed already.")
        }
        mFinalSecurityManager = pFinalSecurityManager
    }

    override fun checkAccept(pHost: String, pPort: Int) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkAccept(pHost, pPort)
    }

    override fun checkAccess(pT: Thread) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkAccess(pT)
    }

    override fun checkAccess(pG: ThreadGroup) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkAccess(pG)
    }

    fun checkAwtEventQueueAccess() {
        if (mFinalSecurityManager == null) return
        //mFinalSecurityManager.checkAwtEventQueueAccess();
    }

    override fun checkConnect(pHost: String, pPort: Int, pContext: Any) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkConnect(pHost, pPort, pContext)
    }

    override fun checkConnect(pHost: String, pPort: Int) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkConnect(pHost, pPort)
    }

    override fun checkCreateClassLoader() {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkCreateClassLoader()
    }

    override fun checkDelete(pFile: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkDelete(pFile)
    }

    override fun checkExec(pCmd: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkExec(pCmd)
    }

    override fun checkExit(pStatus: Int) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkExit(pStatus)
    }

    override fun checkLink(pLib: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkLink(pLib)
    }

    override fun checkListen(pPort: Int) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkListen(pPort)
    }

    fun checkMemberAccess(arg0: Class<*>?, arg1: Int) {
        if (mFinalSecurityManager == null) return
        //mFinalSecurityManager.checkMemberAccess(arg0, arg1);
    }

    override fun checkMulticast(pMaddr: InetAddress, pTtl: Byte) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkMulticast(pMaddr, pTtl)
    }

    override fun checkMulticast(pMaddr: InetAddress) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkMulticast(pMaddr)
    }

    override fun checkPackageAccess(pPkg: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPackageAccess(pPkg)
    }

    override fun checkPackageDefinition(pPkg: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPackageDefinition(pPkg)
    }

    override fun checkPermission(pPerm: Permission, pContext: Any) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPermission(pPerm, pContext)
    }

    override fun checkPermission(pPerm: Permission) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPermission(pPerm)
    }

    override fun checkPrintJobAccess() {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPrintJobAccess()
    }

    override fun checkPropertiesAccess() {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPropertiesAccess()
    }

    override fun checkPropertyAccess(pKey: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkPropertyAccess(pKey)
    }

    override fun checkRead(pFd: FileDescriptor) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkRead(pFd)
    }

    override fun checkRead(pFile: String, pContext: Any) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkRead(pFile, pContext)
    }

    override fun checkRead(pFile: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkRead(pFile)
    }

    override fun checkSecurityAccess(pTarget: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkSecurityAccess(pTarget)
    }

    override fun checkSetFactory() {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkSetFactory()
    }

    fun checkSystemClipboardAccess() {
        if (mFinalSecurityManager == null) return
        //mFinalSecurityManager.checkSystemClipboardAccess();
    }

    fun checkTopLevelWindow(pWindow: Any?): Boolean {
        if (mFinalSecurityManager == null) return true
        throw UnsupportedOperationException()
        //return mFinalSecurityManager.checkTopLevelWindow(pWindow);
    }

    override fun checkWrite(pFd: FileDescriptor) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkWrite(pFd)
    }

    override fun checkWrite(pFile: String) {
        if (mFinalSecurityManager == null) return
        mFinalSecurityManager!!.checkWrite(pFile)
    }

    override fun getSecurityContext(): Any {
        return if (mFinalSecurityManager == null) super.getSecurityContext() else mFinalSecurityManager!!.securityContext
    }
}