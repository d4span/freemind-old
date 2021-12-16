/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2013 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.common

import freemind.common.TextTranslator
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import javax.swing.JComboBox
import javax.swing.DefaultComboBoxModel
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.DefaultFormBuilder
import javax.swing.JLabel
import javax.swing.RootPaneContainer
import freemind.common.FreeMindProgressMonitor
import freemind.common.FreeMindTask.ProgressDescription
import javax.swing.JPanel
import java.awt.event.MouseAdapter
import java.awt.event.MouseMotionAdapter
import java.awt.event.KeyAdapter
import freemind.common.FreeMindTask
import java.lang.Runnable
import kotlin.Throws
import freemind.modes.MindIcon
import javax.swing.JButton
import freemind.modes.IconInformation
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import javax.swing.JPopupMenu
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
import javax.swing.JOptionPane
import freemind.controller.actions.generated.instance.XmlAction
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.BindingDirectory
import javax.swing.JPasswordField
import javax.swing.JComponent
import javax.swing.JSplitPane
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import javax.swing.JFrame
import freemind.common.JOptionalSplitPane
import freemind.common.ThreeCheckBoxProperty
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import freemind.common.ScriptEditorProperty
import freemind.common.ScriptEditorProperty.ScriptEditorStarter
import javax.swing.Icon
import javax.swing.ImageIcon
import freemind.controller.BlindIcon
import javax.swing.JProgressBar
import java.lang.InterruptedException
import freemind.common.OptionalDontShowMeAgainDialog.DontShowPropertyHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.main.*
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

/**
 * @author foltin
 * @date 01.04.2013
 */
class FreeMindProgressMonitor(pTitle: String?) : JDialog() {
    private val mLabel: JLabel
    private val mProgressBar: JProgressBar
    private val mCancelButton: JButton
    protected var mCanceled = false

    /**
     *
     */
    init {
        title = getString(pTitle)
        mLabel = JLabel("!")
        mProgressBar = JProgressBar()
        mCancelButton = JButton()
        Tools.setLabelAndMnemonic(mCancelButton, getString("cancel"))
        mCancelButton.addActionListener { mCanceled = true }
        layout = GridBagLayout()
        val constraints = GridBagConstraints(0, 0,
                GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                Insets(0, 5, 0, 5), 0, 0)
        add(mLabel, constraints)
        constraints.gridy = 1
        add(mProgressBar, constraints)
        constraints.gridy = 2
        constraints.fill = GridBagConstraints.NONE
        constraints.anchor = GridBagConstraints.EAST
        add(mCancelButton, constraints)
        // Tools.addEscapeActionToDialog(this);
        pack()
        size = Dimension(600, 200)
        val marshaled = Resources.getInstance().getProperty(
                PROGRESS_MONITOR_WINDOW_CONFIGURATION_STORAGE)
        if (marshaled != null) {
            XmlBindingTools.Companion.getInstance().decorateDialog(marshaled, this)
        }
    }

    protected fun getString(resource: String?): String {
        return Resources.getInstance().getResourceString(resource)
    }

    /**
     * @param pCurrent
     * @param pMax
     * @param pName
     * resource string to be displayed as progress string (maybe with
     * parameters pParameters)
     * @param pParameters
     * objects to be put in the resource string for pName
     * @return
     */
    fun showProgress(pCurrent: Int, pMax: Int, pName: String?,
                     pParameters: Array<Any>?): Boolean {
        EventQueue.invokeLater { mProgressBar.maximum = pMax }
        return showProgress(pCurrent, pName, pParameters)
    }

    fun showProgress(pCurrent: Int, pName: String?, pParameters: Array<Any>?): Boolean {
        val format = Resources.getInstance().format(pName, pParameters)
        EventQueue.invokeLater { mLabel.text = format }
        return setProgress(pCurrent)
    }

    fun setProgress(pCurrent: Int): Boolean {
        EventQueue.invokeLater { mProgressBar.value = pCurrent }
        return mCanceled
    }

    fun dismiss() {
        val storage = WindowConfigurationStorage()
        val marshalled: String = XmlBindingTools.Companion.getInstance().storeDialogPositions(
                storage, this)
        Resources
                .getInstance()
                .properties
                .setProperty(PROGRESS_MONITOR_WINDOW_CONFIGURATION_STORAGE,
                        marshalled)
        this.isVisible = false
    }

    companion object {
        /**
         *
         */
        private const val PROGRESS_MONITOR_WINDOW_CONFIGURATION_STORAGE = "progress_monitor_window_configuration_storage"

        /**
         * Test method for this dialog.
         */
        @Throws(InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val mock = FreeMindMainMock()
            Resources.createInstance(mock)
            val progress = FreeMindProgressMonitor("title")
            progress.isVisible = true
            for (i in 0..9) {
                val canceled = progress.showProgress(i, 10, "inhalt {0}", arrayOf(Integer.valueOf(i)))
                if (canceled) {
                    progress.dismiss()
                    System.exit(1)
                }
                Thread.sleep(1000L)
            }
            progress.dismiss()
            System.exit(0)
        }
    }
}