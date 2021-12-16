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
/*$Id: ComboProperty.java,v 1.1.2.5.2.2 2006/07/25 20:28:19 christianfoltin Exp $*/
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
import java.util.*

open class ComboProperty : PropertyBean, PropertyControl {
    override var description: String
    override var label: String
    protected var mComboBox = JComboBox<String?>()
    protected var possibleValues: Vector<String?>? = null

    /**
     * @param pTranslator
     * TODO
     */
    constructor(description: String, label: String, possibles: Array<String>,
                pTranslator: TextTranslator) : super() {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        val possibleTranslations = Vector<String?>()
        for (key in possibleValues!!) {
            possibleTranslations.add(pTranslator.getText(key))
        }
        mComboBox.model = DefaultComboBoxModel(possibleTranslations)
        addActionListener()
    }

    protected fun addActionListener() {
        mComboBox.addActionListener { firePropertyChangeEvent() }
    }

    constructor(description: String, label: String, possibles: Array<String>,
                possibleTranslations: List<String?>?) {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        mComboBox.model = DefaultComboBoxModel(Vector(
                possibleTranslations))
        addActionListener()
    }

    constructor(description: String, label: String, possibles: List<String?>,
                possibleTranslations: List<String?>?) {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        mComboBox.model = DefaultComboBoxModel(Vector(
                possibleTranslations))
    }

    /**
     */
    private fun fillPossibleValues(possibles: Array<String>) {
        fillPossibleValues(Arrays.asList(*possibles))
    }

    /**
     */
    private fun fillPossibleValues(possibles: List<String?>) {
        possibleValues = Vector()
        possibleValues!!.addAll(possibles)
    }

    /**
     * If your combo base changes, call this method to update the values. The
     * old selected value is not selected, but the first in the list. Thus, you
     * should call this method only shortly before setting the value with
     * setValue.
     */
    fun updateComboBoxEntries(possibles: List<String?>, possibleTranslations: List<String?>?) {
        mComboBox.model = DefaultComboBoxModel(Vector(
                possibleTranslations))
        fillPossibleValues(possibles)
        if (possibles.size > 0) {
            mComboBox.selectedIndex = 0
        }
    }

    override fun getDescription(): String? {
        return description
    }

    override fun getLabel(): String? {
        return label
    }

    override var value: String?
        get() = possibleValues!![mComboBox.selectedIndex]
        set(value) {
            if (possibleValues!!.contains(value)) {
                mComboBox.setSelectedIndex(possibleValues!!.indexOf(value))
            } else {
                System.err.println("Can't set the value:" + value
                        + " into the combo box " + getLabel() + "/"
                        + getDescription())
                if (mComboBox.model.size > 0) {
                    mComboBox.selectedIndex = 0
                }
            }
        }

    override fun layout(builder: DefaultFormBuilder, pTranslator: TextTranslator) {
        val label = builder.append(pTranslator.getText(getLabel()),
                mComboBox)
        label.toolTipText = pTranslator.getText(getDescription())
    }

    override fun setEnabled(pEnabled: Boolean) {
        mComboBox.isEnabled = pEnabled
    }
}