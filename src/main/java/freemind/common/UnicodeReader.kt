package freemind.common

import freemind.common.TextTranslator
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import javax.swing.JComboBox
import java.awt.GraphicsEnvironment
import javax.swing.DefaultComboBoxModel
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.DefaultFormBuilder
import javax.swing.JLabel
import javax.swing.RootPaneContainer
import freemind.common.FreeMindProgressMonitor
import freemind.common.FreeMindTask.ProgressDescription
import javax.swing.JPanel
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseMotionAdapter
import java.awt.event.KeyAdapter
import freemind.common.FreeMindTask
import java.lang.Runnable
import kotlin.Throws
import freemind.main.FreeMindMain
import freemind.modes.MindIcon
import javax.swing.JButton
import freemind.modes.IconInformation
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import java.awt.Color
import javax.swing.JPopupMenu
import freemind.main.Tools
import javax.swing.JMenuItem
import java.util.Arrays
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import java.lang.NumberFormatException
import javax.swing.JTable
import freemind.main.FreeMind
import javax.swing.JTextField
import java.awt.event.KeyEvent
import freemind.common.BooleanProperty
import javax.swing.JCheckBox
import java.awt.event.ItemListener
import java.awt.event.ItemEvent
import java.util.Locale
import java.awt.event.ComponentListener
import freemind.common.ScalableJButton
import java.awt.event.ComponentEvent
import org.jibx.runtime.IMarshallingContext
import freemind.common.XmlBindingTools
import org.jibx.runtime.JiBXException
import org.jibx.runtime.IUnmarshallingContext
import javax.swing.JDialog
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import java.awt.Dimension
import javax.swing.JOptionPane
import freemind.controller.actions.generated.instance.XmlAction
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.BindingDirectory
import javax.swing.JPasswordField
import javax.swing.JComponent
import java.awt.BorderLayout
import javax.swing.JSplitPane
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import javax.swing.JFrame
import freemind.common.JOptionalSplitPane
import freemind.common.ThreeCheckBoxProperty
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import freemind.common.ScriptEditorProperty
import freemind.main.HtmlTools
import freemind.common.ScriptEditorProperty.ScriptEditorStarter
import javax.swing.Icon
import javax.swing.ImageIcon
import freemind.controller.BlindIcon
import javax.swing.JProgressBar
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.lang.InterruptedException
import freemind.common.OptionalDontShowMeAgainDialog.DontShowPropertyHandler
import freemind.common.OptionalDontShowMeAgainDialog
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.*

/**
 *
 * fc, 2010-12-23 Taken from http://koti.mbnet.fi/akini/java/unicodereader/UnicodeReader.java.txt
 * asuming public domain.
 *
 * version: 1.1 / 2007-01-25
 * - changed BOM recognition ordering (longer boms first)
 *
 * Original pseudocode   : Thomas Weidenfeller
 * Implementation tweaked: Aki Nieminen
 *
 * http://www.unicode.org/unicode/faq/utf_bom.html
 * BOMs:
 * 00 00 FE FF    = UTF-32, big-endian
 * FF FE 00 00    = UTF-32, little-endian
 * EF BB BF       = UTF-8,
 * FE FF          = UTF-16, big-endian
 * FF FE          = UTF-16, little-endian
 *
 * Win2k Notepad:
 * Unicode format = UTF-16LE
 */
/**
 * Generic unicode textreader, which will use BOM mark to identify the encoding
 * to be used. If BOM is not found then use a given default or system encoding.
 */
class UnicodeReader(`in`: InputStream?, defaultEnc: String) : Reader() {
    var internalIn: PushbackInputStream
    var internalIn2: InputStreamReader? = null
    var defaultEncoding: String

    /**
     *
     * @param in
     * inputstream to be read
     * @param defaultEnc
     * default encoding if stream does not have BOM marker. Give NULL
     * to use system-level default.
     */
    init {
        internalIn = PushbackInputStream(`in`, BOM_SIZE)
        defaultEncoding = defaultEnc
    }

    /**
     * Get stream encoding or NULL if stream is uninitialized. Call init() or
     * read() method to initialize it.
     */
    val encoding: String?
        get() = if (internalIn2 == null) null else internalIn2!!.encoding

    /**
     * Read-ahead four bytes and check for BOM marks. Extra bytes are unread
     * back to the stream, only BOM bytes are skipped.
     */
    @Throws(IOException::class)
    protected fun init() {
        if (internalIn2 != null) return
        val encoding: String
        val bom = ByteArray(BOM_SIZE)
        val n: Int
        val unread: Int
        n = internalIn.read(bom, 0, bom.size)
        if (bom[0] == 0x00.toByte() && bom[1] == 0x00.toByte()
                && bom[2] == 0xFE.toByte() && bom[3] == 0xFF.toByte()) {
            encoding = "UTF-32BE"
            unread = n - 4
        } else if (bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte()
                && bom[2] == 0x00.toByte() && bom[3] == 0x00.toByte()) {
            encoding = "UTF-32LE"
            unread = n - 4
        } else if (bom[0] == 0xEF.toByte() && bom[1] == 0xBB.toByte()
                && bom[2] == 0xBF.toByte()) {
            encoding = "UTF-8"
            unread = n - 3
        } else if (bom[0] == 0xFE.toByte() && bom[1] == 0xFF.toByte()) {
            encoding = "UTF-16BE"
            unread = n - 2
        } else if (bom[0] == 0xFF.toByte() && bom[1] == 0xFE.toByte()) {
            encoding = "UTF-16LE"
            unread = n - 2
        } else {
            // Unicode BOM mark not found, unread all bytes
            encoding = defaultEncoding
            unread = n
        }
        // System.out.println("read=" + n + ", unread=" + unread);
        if (unread > 0) internalIn.unread(bom, n - unread, unread)

        // Use given encoding
        internalIn2 = if (encoding == null) {
            InputStreamReader(internalIn)
        } else {
            InputStreamReader(internalIn, encoding)
        }
    }

    @Throws(IOException::class)
    override fun close() {
        init()
        internalIn2!!.close()
    }

    @Throws(IOException::class)
    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        init()
        return internalIn2!!.read(cbuf, off, len)
    }

    companion object {
        private const val BOM_SIZE = 4
    }
}