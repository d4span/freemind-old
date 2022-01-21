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

import freemind.main.Resources
import java.awt.Component
import java.awt.EventQueue
import java.awt.GridLayout
import java.awt.event.KeyAdapter
import java.awt.event.MouseAdapter
import java.awt.event.MouseMotionAdapter
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.RootPaneContainer

/**
 * Long running tasks inside FreeMind should derive from this class.
 *
 * @author foltin
 * @date 08.04.2013
 */
abstract class FreeMindTask(private val mFrame: RootPaneContainer, var amountOfSteps: Int, pName: String?) :
    Thread(pName) {
    private var mInterrupted = false
    var isFinished = false
    private var mProgressMonitor: FreeMindProgressMonitor? = null
    var rounds = 0
        private set

    @JvmField
    protected var mProgressDescription: ProgressDescription? = null
    private val mGlass: JPanel
    private val mOldGlassPane: Component

    protected class ProgressDescription(
        var mProgressString: String,
        var mProgressParameters: Array<Any>
    )

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
                    progressDescription = ProgressDescription("FreeMindTask.Default", arrayOf(rounds))
                }
                val canceled = mProgressMonitor!!.showProgress(
                    rounds,
                    amountOfSteps, progressDescription!!.mProgressString,
                    progressDescription.mProgressParameters
                )
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
