/*
 * EditServer.java - FreeMind server
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999, 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import java.lang.StringBuffer
import java.awt.GraphicsEnvironment
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
import java.util.*
import java.util.logging.Logger

//{{{ Imports
/**
 * Inter-process communication.
 *
 *
 *
 * The edit server protocol is very simple. `$HOME/.jedit/server` is
 * an ASCII file containing two lines, the first being the port number, the
 * second being the authorization key.
 *
 *
 *
 * You connect to that port on the local machine, sending the authorization key
 * as four bytes in network byte order, followed by the length of the BeanShell
 * script as two bytes in network byte order, followed by the script in UTF8
 * encoding. After the socked is closed, the BeanShell script will be executed
 * by FreeMind.
 *
 *
 *
 * The snippet is executed in the AWT thread. None of the usual BeanShell
 * variables (view, buffer, textArea, editPane) are set so the script has to
 * figure things out by itself.
 *
 *
 *
 * In most cases, the script will call the static
 * [.handleClient] method, but of course more
 * complicated stuff can be done too.
 *
 * @author Slava Pestov
 * @version $Id: EditServer.java 19384 2011-02-23 16:50:37Z k_satoda $
 */
class EditServer internal constructor(portFile: String, private val mFrame: FreeMindMain) : Thread("FreeMind server daemon [$portFile]") {
    // {{{ run() method
    override fun run() {
        while (true) {
            if (abort) return
            var client: Socket? = null
            try {
                client = socket!!.accept()

                // Stop script kiddies from opening the edit
                // server port and just leaving it open, as a
                // DoS
                client.soTimeout = 1000
                logger!!.info("$client: connected")
                val `in` = DataInputStream(
                        client.getInputStream())
                if (!handleClient(client, `in`)) abort = true
            } catch (e: Exception) {
                if (!abort) logger!!.info("" + e)
                abort = true
            } finally {
                /*
				 * if(client != null) { try { client.close(); } catch(Exception
				 * e) { logger.info(e); }
				 * 
				 * client = null; }
				 */
            }
        }
    } // }}}

    // {{{ getPort method
    // }}}
    val port: Int
        get() = socket!!.localPort

    // {{{ stopServer() method
    fun stopServer() {
        abort = true
        try {
            socket!!.close()
        } catch (io: IOException) {
        }
        File(portFile).delete()
    } // }}}

    // {{{ Private members
    // {{{ Instance variables
    private val portFile: String
    private var socket: ServerSocket? = null
    private var authKey = 0

    // {{{ isOK() method
    // }}}
    var isOK = false
    private var abort = false

    // {{{ EditServer constructor
    init {
        if (logger == null) {
            logger = Resources.Companion.getInstance().getLogger(
                    this.javaClass.name)
        }
        isDaemon = true
        this.portFile = portFile
        try {
            // On Unix, set permissions of port file to rw-------,
            // so that on broken Unices which give everyone read
            // access to user home dirs, people can't see your
            // port file (and hence send arbitriary BeanShell code
            // your way. Nasty.)
            if (Tools.isUnix()) {
                File(portFile).createNewFile()
                Tools.setPermissions(portFile, 384)
            }

            // Bind to any port on localhost; accept 2 simultaneous
            // connection attempts before rejecting connections
            socket = ServerSocket(0, 2, InetAddress.getByName("127.0.0.1"))
            authKey = Random().nextInt(Int.MAX_VALUE)
            val port = socket.getLocalPort()
            val out = FileWriter(portFile)
            try {
                out.write("b\n")
                out.write(port.toString())
                out.write("\n")
                out.write(authKey.toString())
                out.write("\n")
            } finally {
                out.close()
            }
            isOK = true
            logger!!.info("FreeMind server started on port "
                    + socket.getLocalPort())
            logger!!.info("Authorization key is $authKey")
        } catch (io: IOException) {
            /*
			 * on some Windows versions, connections to localhost fail if the
			 * network is not running. To avoid confusing newbies with weird
			 * error messages, log errors that occur while starting the server
			 * as NOTICE, not ERROR
			 */
            logger!!.info("" + io)
        }
    } // }}}

    // }}}
    // {{{ handleClient() method
    @Throws(Exception::class)
    private fun handleClient(client: Socket?, `in`: DataInputStream): Boolean {
        val key = `in`.readInt()
        return if (key != authKey) {
            logger!!.info(client.toString() + ": wrong" + " authorization key (got " + key
                    + ", expected " + authKey + ")")
            `in`.close()
            client!!.close()
            false
        } else {
            // Reset the timeout
            client!!.soTimeout = 0
            logger!!.info("$client: authenticated successfully")
            val script = `in`.readUTF()
            logger!!.info(script)
            SwingUtilities.invokeLater {
                try {
                    val urls = Tools.urlStringToUrls(script)
                    for (urli in urls!!) {
                        mFrame.controller.modeController
                                .load(urli)
                    }
                } catch (e: MalformedURLException) {
                    Resources.Companion.getInstance().logException(e)
                } catch (e: Exception) {
                    Resources.Companion.getInstance().logException(e)
                }
            }
            `in`.close()
            client.close()
            true
        }
    } // }}}

    // }}}
    companion object {
        protected var logger: Logger? = null
    }
}