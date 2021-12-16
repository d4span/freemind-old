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
import java.lang.Exception

/**
 * Long running tasks inside FreeMind should derive from this class.
 *
 * @author foltin
 * @date 08.04.2013
 */
abstract class FreeMindTask(private val mFrame: RootPaneContainer, var amountOfSteps: Int, pName: String?) : Thread(pName) {
    private var mInterrupted = false
    var isFinished = false
    private var mProgressMonitor: FreeMindProgressMonitor? = null
    var rounds = 0
        private set
    protected var mProgressDescription: ProgressDescription? = null
    private val mGlass: JPanel
    private val mOldGlassPane: Component

    protected inner class ProgressDescription
    /**
     * @param pProgressString
     * @param pProgressParameters
     */(var mProgressString: String,
        /**
         * To be inserted into mProgressString;
         */
        var mProgressParameters: Array<Any>)

    init {
        mProgressMonitor = FreeMindProgressMonitor(name)
        mGlass = JPanel(GridLayout(0, 1))
        val padding = JLabel()
        mGlass.isOpaque = false
        mGlass.add(padding)

        // trap both mouse and key events. Could provide a smarter
        // key handler if you wanted to allow things like a keystroke
        // that would cancel the long-running operation.
        mGlass.addMouseListener(object : MouseAdapter() {})
        mGlass.addMouseMotionListener(object : MouseMotionAdapter() {})
        mGlass.addKeyListener(object : KeyAdapter() {})

        // make sure the focus won't leave the glass pane
        mGlass.isFocusCycleRoot = true // 1.4
        mOldGlassPane = mFrame.glassPane
        mFrame.glassPane = mGlass
        mGlass.isVisible = true
        padding.requestFocus() // required to trap key events
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
    override fun run() {
        val startTime = System.currentTimeMillis()
        rounds = 0
        var again = true
        while (again) {
            try {
                again = processAction()
                rounds++
                if (!again) {
                    // already ready!!
                    rounds = amountOfSteps
                }
                if (rounds == amountOfSteps) {
                    again = false
                }
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
                again = false
            }
            if (isInterrupted) {
                again = false
            }
            if (System.currentTimeMillis() - startTime > TIME_TO_DISPLAY_PROGRESS_BAR_IN_MILLIS) {
                // mProgressMonitor.setModal(true);
                EventQueue.invokeLater { mProgressMonitor!!.isVisible = true }
            }
            if (mProgressMonitor!!.isVisible) {
                var progressDescription = mProgressDescription
                if (mProgressDescription == null) {
                    progressDescription = ProgressDescription(
                            "FreeMindTask.Default", arrayOf(
                            rounds))
                }
                val canceled = mProgressMonitor!!.showProgress(rounds,
                        amountOfSteps, progressDescription!!.mProgressString,
                        progressDescription.mProgressParameters)
                if (canceled) {
                    mInterrupted = true
                    again = false
                }
            }
        }
        isFinished = true
        EventQueue.invokeLater {
            mGlass.isVisible = false
            mFrame.glassPane = mOldGlassPane
            mProgressMonitor!!.dismiss()
        }
    }

    /**
     * Subclasses should process one single action out of the set of its actions
     * and then return. The method is directly called again by the task
     * controller until it returns false.
     *
     * @return true, if further actions follow. False, if done.
     */
    @Throws(Exception::class)
    protected abstract fun processAction(): Boolean
    override fun isInterrupted(): Boolean {
        return mInterrupted
    }

    fun setInterrupted(pInterrupted: Boolean) {
        mInterrupted = pInterrupted
    }

    companion object {
        private const val TIME_TO_DISPLAY_PROGRESS_BAR_IN_MILLIS: Long = 1000
    }
}