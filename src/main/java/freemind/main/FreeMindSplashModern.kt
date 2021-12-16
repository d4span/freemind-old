/***************************************************************************
 *
 * FreeMindSplash, taken from GanttSplash.java.
 *
 * Copyright (C) 2002 by Thomas Alexandre (alexthomas(at)ganttproject.org)
 * Copyright (C) 2005-2008 by Christian Foltin and Daniel Polansky
 *
 */
/***************************************************************************
 * *
 * This program is free software; you can redistribute it and/or modify  *
 * it under the terms of the GNU General Public License as published by  *
 * the Free Software Foundation; either version 2 of the License, or     *
 * (at your option) any later version.                                   *
 * *
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
import java.util.StringTokenizer
import java.util.LinkedList
import java.lang.StringBuffer
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
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import freemind.view.mindmapview.MapView
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
import java.awt.*
import javax.swing.JRootPane
import java.io.FileDescriptor
import java.util.logging.Logger

/**
 * Class to put a splash during launching the application.
 */
class FreeMindSplashModern(private val frame: FreeMindMain) : JFrame("FreeMind"), IFreeMindSplash {
    private inner class FeedBackImpl : FeedBack {
        private var mActualValue = 0
        private var mActualTimeStamp = System.currentTimeMillis()
        private var mTotalTime: Long = 0
        private var lastTaskId: String? = null
        private var mImageJLabel: JLabel? = null
        override fun progress(act: Int, messageId: String, pMessageParameters: Array<Any>?) {
            val formatter = MessageFormat(
                    frame.getResourceString(messageId))
            val progressString = formatter.format(pMessageParameters)
            logger!!.fine(progressString)
            mActualValue = act
            val timeDifference = System.currentTimeMillis() - mActualTimeStamp
            mActualTimeStamp = System.currentTimeMillis()
            mTotalTime += timeDifference
            logger!!.fine("""Task: $lastTaskId ($act) last ${timeDifference / 1000.0} seconds.
Total: ${mTotalTime / 1000.0}
""")
            SwingUtilities.invokeLater {
                mProgressBar.value = act
                val percent = act * 1.0 / mProgressBar.maximum
                mProgressBar.string = progressString
                if (mImageJLabel != null) {
                    mImageJLabel!!.putClientProperty("progressString",
                            progressString)
                    mImageJLabel!!.putClientProperty("progressPercent", percent)
                    mImageJLabel!!.repaint()
                }
            }
            logger!!.fine("Beginnig task:$messageId")
            lastTaskId = messageId
            // this is not nice, as other windows are probably more important!
//			// make it the top most window.
//			FreeMindSplashModern.this.toFront();
        }

        override fun getActualValue(): Int {
            return mActualValue
        }

        override fun setMaximumValue(max: Int) {
            mProgressBar.maximum = max
            mProgressBar.isIndeterminate = false
        }

        override fun increase(messageId: String, pMessageParameters: Array<Any>?) {
            progress(actualValue + 1, messageId, pMessageParameters)
        }

        fun setImageJLabel(imageJLabel: JLabel?) {
            mImageJLabel = imageJLabel
        }
    }

    private val feedBack // !
            : FeedBackImpl
    private val mProgressBar: JProgressBar
    private val mIcon: ImageIcon
    override fun getFeedBack(): FeedBack {
        return feedBack
    }

    init {
        if (logger == null) {
            logger = frame.getLogger(this.javaClass.name)
        }
        feedBack = FeedBackImpl()

        // http://www.kde-look.org/content/show.php?content=76812
        // License GPLV2+
        val imageFactory = ImageFactory.getInstance()
        mIcon = imageFactory.createIcon(
                frame.getResource("images/76812-freemind_v0.4.png"))
        iconImage = mIcon.image // Set the icon
        setDefaultLookAndFeelDecorated(false)
        isUndecorated = true
        getRootPane().windowDecorationStyle = JRootPane.NONE // Set no border
        // lamentablemente since 1.5: setAlwaysOnTop(true);
        val splashImage = imageFactory.createIcon(frame.getResource(FREEMIND_SPLASH))
        val splashImageLabel: JLabel = object : JLabel(splashImage) {
            private var mWidth: Int? = null
            private val progressFont = Font("SansSerif", Font.PLAIN, 10)
            private val versionTextFont = if (Tools.isAvailableFontFamily("Century Gothic")) Font("Century Gothic", Font.BOLD, 14) else Font("Arial", Font.BOLD, 12)
            private fun calcYRelative(y: Int): Int {
                return (y.toFloat() / SPLASH_HEIGHT * splashImage.iconHeight).toInt()
            }

            private fun calcXRelative(x: Int): Int {
                return (x.toFloat() / SPLASH_WIDTH * splashImage.iconWidth).toInt()
            }

            override fun paint(g: Graphics) {
                super.paint(g)
                val g2 = g as Graphics2D
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
                g2.font = versionTextFont
                // Determine width of string to center it
                val freemindVersion = frame.freemindVersion.toString()
                if (mWidth == null) {
                    mWidth = g2.fontMetrics.stringWidth(
                            freemindVersion)
                }
                val yCoordinate = calcYRelative(58)
                val xCoordinate = (size.getWidth() / 2 - mWidth
                        .toInt() / 2).toInt()
                g2.color = Color(0x4d, 0x63, 0xb4)
                g2.drawString(freemindVersion, xCoordinate, yCoordinate)
                // Draw progress bar
                val progressString = getClientProperty("progressString") as String
                if (progressString != null) {
                    val percent = getClientProperty("progressPercent") as Double
                    val xBase = calcXRelative(7)
                    val yBase = calcYRelative(185)
                    val width = calcXRelative(281)
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
                    g2.font = progressFont
                    // g2.setColor(new Color(0x80,0x80,0x80));
                    g2.color = Color(0xff, 0xff, 0xff)
                    g2.drawString(progressString, xBase + calcXRelative(1), yBase - calcYRelative(4))
                    g2.color = Color(0xc8, 0xdf, 0x8b)
                    g2.draw(Rectangle(xBase + calcXRelative(2), yBase, width, calcYRelative(3)))
                    // g2.setColor(new Color(0xd0,0xd0,0xd0));
                    // g2.draw(new Rectangle(xBase+1, yBase+1, width, 2));
                    // g2.setColor(new Color(0xf4,0xf4,0xf4));
                    // g2.fill(new Rectangle(xBase+1, yBase+1, width-1, 2));
                    // g2.setColor(new Color(0x4d,0x63,0xb4));
                    g2.color = Color(0xff, 0xff, 0xff)
                    g2.fill(Rectangle(xBase + calcXRelative(1), yBase + calcYRelative(1), (width * percent).toInt(), calcYRelative(2)))
                }
            }
        }
        feedBack.setImageJLabel(splashImageLabel)
        contentPane.add(splashImageLabel, BorderLayout.CENTER)
        mProgressBar = JProgressBar()
        mProgressBar.isIndeterminate = true
        mProgressBar.isStringPainted = true
        pack()
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val labelSize = splashImageLabel.preferredSize

        // Put image at the middle of the screen
        setLocation(screenSize.width / 2 - labelSize.width / 2,
                screenSize.height / 2 - labelSize.height / 2)
    }

    override fun close() {
        isVisible = false
        dispose()
    }

    override fun getWindowIcon(): ImageIcon {
        return mIcon
    }

    companion object {
        private const val FREEMIND_SPLASH = "images/Freemind_Splash_Butterfly_Modern.png"
        private const val SPLASH_HEIGHT = 200
        private const val SPLASH_WIDTH = 300
        private var logger: Logger? = null
    }
}