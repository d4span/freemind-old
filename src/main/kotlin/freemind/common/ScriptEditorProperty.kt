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
 * Created on 25.02.2006
 */
/*$Id: ScriptEditorProperty.java,v 1.1.2.6 2008/07/04 20:44:02 christianfoltin Exp $*/
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
import java.io.PushbackInputStream
import java.io.IOException
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
import java.util.logging.Logger

class ScriptEditorProperty(override var description: String, override var label: String,
                           private val mMindMapController: MindMapController) : PropertyBean(), PropertyControl, ActionListener {
    interface ScriptEditorStarter : MindMapControllerPlugin {
        fun startEditor(scriptInput: String?): String?
    }

    var script: String
    var mButton: JButton
    val menu = JPopupMenu()

    /**
     */
    init {
        if (logger == null) {
            logger = mMindMapController.frame.getLogger(
                    this.javaClass.name)
        }
        mButton = JButton()
        mButton.addActionListener(this)
        script = ""
    }

    override fun getDescription(): String? {
        return description
    }

    override fun getLabel(): String? {
        return label
    }

    override var value: String?
        get() = HtmlTools.unicodeToHTMLUnicodeEntity(HtmlTools
                .toXMLEscapedText(script), false)
        set(value) {
            setScriptValue(value)
        }

    override fun layout(builder: DefaultFormBuilder, pTranslator: TextTranslator) {
        val label = builder.append(pTranslator.getText(getLabel()), mButton)
        label.toolTipText = pTranslator.getText(getDescription())
    }

    override fun actionPerformed(arg0: ActionEvent) {
        // search for plugin that handles the script editor.
        for (plugin in mMindMapController.plugins) {
            if (plugin is ScriptEditorStarter) {
                val resultScript = plugin.startEditor(script)
                if (resultScript != null) {
                    script = resultScript
                    firePropertyChangeEvent()
                }
            }
        }
    }

    /**
     */
    private fun setScriptValue(result: String?) {
        var result = result
        if (result == null) {
            result = ""
        }
        script = HtmlTools.toXMLUnescapedText(HtmlTools
                .unescapeHTMLUnicodeEntity(result))
        logger!!.fine("Setting script to $script")
        mButton.text = script
    }

    // /**
    // */
    // private Color getColorValue() {
    // return color;
    // }
    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }

    companion object {
        private var logger: Logger? = null
    }
}