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
/*$Id: ColorProperty.java,v 1.1.2.4.2.2 2008/07/24 03:10:36 christianfoltin Exp $*/
package freemind.common

import freemind.common.TextTranslator
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import javax.swing.JComboBox
import java.awt.GraphicsEnvironment
import javax.swing.DefaultComboBoxModel
import com.jgoodies.forms.builder.DefaultFormBuilder
import javax.swing.JLabel
import javax.swing.RootPaneContainer
import freemind.common.FreeMindProgressMonitor
import freemind.common.FreeMindTask.ProgressDescription
import javax.swing.JPanel
import java.awt.GridLayout
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
import freemind.common.BooleanProperty
import javax.swing.JCheckBox
import java.util.Locale
import freemind.common.ScalableJButton
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
import freemind.controller.Controller
import java.awt.event.*

class ColorProperty(override var description: String, override var label: String, private val defaultColor: String,
                    private val mTranslator: TextTranslator) : PropertyBean(), PropertyControl, ActionListener {
    /**
     */
    private var colorValue: Color?
    var mButton: JButton
    val menu = JPopupMenu()

    /**
     * @param defaultColor
     * TODO
     * @param pTranslator
     * TODO
     */
    init {
        mButton = JButton()
        mButton.addActionListener(this)
        colorValue = Color.BLACK
    }

    override fun getDescription(): String? {
        return description
    }

    override fun getLabel(): String? {
        return label
    }

    override var value: String?
        get() = Tools.colorToXml(colorValue)
        set(value) {
            setColorValue(Tools.xmlToColor(value))
        }

    override fun layout(builder: DefaultFormBuilder, pTranslator: TextTranslator) {
        val label = builder.append(pTranslator.getText(getLabel()), mButton)
        label.toolTipText = pTranslator.getText(getDescription())
        // add "reset to standard" popup:

        // Create and add a menu item
        val item = JMenuItem(
                mTranslator.getText("ColorProperty.ResetColor"))
        item.addActionListener { value = defaultColor }
        menu.add(item)

        // Set the component to show the popup menu
        mButton.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(evt: MouseEvent) {
                if (evt.isPopupTrigger) {
                    menu.show(evt.component, evt.x, evt.y)
                }
            }

            override fun mouseReleased(evt: MouseEvent) {
                if (evt.isPopupTrigger) {
                    menu.show(evt.component, evt.x, evt.y)
                }
            }
        })
    }

    override fun actionPerformed(arg0: ActionEvent) {
        val result = Controller.showCommonJColorChooserDialog(
                mButton.rootPane, mTranslator.getText(getLabel()),
                colorValue)
        if (result != null) {
            setColorValue(result)
            firePropertyChangeEvent()
        }
    }

    /**
     */
    private fun setColorValue(result: Color) {
        var result: Color? = result
        colorValue = result
        if (result == null) {
            result = Color.WHITE
        }
        mButton.background = result
        mButton.text = Tools.colorToXml(result)
    }

    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }
}