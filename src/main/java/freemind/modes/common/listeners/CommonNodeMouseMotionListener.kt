/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
package freemind.modes.common.listeners

import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.main.Tools
import freemind.main.Tools.BooleanHolder
import freemind.main.Tools.IntHolder
import freemind.modes.ModeController
import freemind.view.mindmapview.MainView
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import java.util.Timer
import java.util.TimerTask
import java.util.logging.Logger
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

/**
 * The MouseMotionListener which belongs to every NodeView.
 * Handles delayed selection.
 */
class CommonNodeMouseMotionListener(private val c: ModeController) : NodeMouseMotionObserver {
    /**
     * And a static method to reread this holder. This is used when the
     * selection method is changed via the option menu.
     */
    override fun updateSelectionMethod() {
        if (timeForDelayedSelection == null) {
            timeForDelayedSelection = IntHolder()
        }
        delayedSelectionEnabled = BooleanHolder()
        delayedSelectionEnabled!!.value = if (c.frame
            .getProperty("selection_method")
            == "selection_method_direct"
        ) false else true
        /*
		 * set time for delay to infinity, if selection_method equals
		 * selection_method_by_click.
		 */if (c.frame.getProperty("selection_method")
            == "selection_method_by_click"
        ) {
            timeForDelayedSelection!!.value = Int.MAX_VALUE
        } else {
            timeForDelayedSelection!!.value = c.frame
                .getProperty("time_for_delayed_selection").toInt()
        }
    }

    private var timerForDelayedSelection: Timer? = null

    /**
     * The mouse has to stay in this region to enable the selection after a
     * given time.
     */
    private var controlRegionForDelayedSelection: Rectangle? = null
    private var mMousePressedEvent: MouseEvent? = null

    init {
        if (logger == null) logger = c.frame.getLogger(this.javaClass.name)
        if (delayedSelectionEnabled == null) updateSelectionMethod()
    }

    override fun mouseMoved(e: MouseEvent) {
        logger!!.finest("Event: mouseMoved")
        // Invoked when the mouse button has been moved on a component (with no
        // buttons down).
        val node = e.component as MainView
        val isLink = node.updateCursor(e.x.toDouble())
        // links are displayed in the status bar:
        if (isLink) {
            c.frame.out(c.getLinkShortText(node.nodeView.model))
        }
        // test if still in selection region:
        if (controlRegionForDelayedSelection != null &&
            delayedSelectionEnabled!!.value
        ) {
            if (!controlRegionForDelayedSelection!!.contains(e.point)) {
                // point is not in the region. start timer again and adjust
                // region to the current point:
                createTimer(e)
            }
        }
    }

    /** Invoked when a mouse button is pressed on a component and then dragged.  */
    override fun mouseDragged(e: MouseEvent) {
        logger!!.fine("Event: mouseDragged")
        // first stop the timer and select the node:
        stopTimerForDelayedSelection()
        val nodeV = (e.component as MainView).nodeView

        // if dragged for the first time, select the node:
        if (!c.view.isSelected(nodeV)) c.extendSelection(e)
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {
        logger!!.finest("Event: mouseEntered")
        if (!JOptionPane.getFrameForComponent(e.component).isFocused) return
        createTimer(e)
        // c.select(e);
    }

    override fun mousePressed(e: MouseEvent) {
        logger!!.fine("Event: mousePressed")
        // for Linux/Mac
        mMousePressedEvent = e
    }

    override fun mouseExited(e: MouseEvent) {
        logger!!.finest("Event: mouseExited")
        stopTimerForDelayedSelection()
    }

    @Suppress("DEPRECATION")
    override fun mouseReleased(e: MouseEvent) {
        // handling click in mouseReleased rather than in mouseClicked
        // provides better interaction. If mouse was slightly moved
        // between pressed and released events, the event clicked
        // is not triggered.
        // The behavior is not tested on Linux.
        logger!!.fine("Event: mouseReleased")
        var ev: MouseEvent? = e
        /*
		 * For Mac see
		 * https://developer.apple.com/library/mac/#documentation/Java/Conceptual/Java14Development/07-NativePlatformIntegration/NativePlatformIntegration.html
		 * */if (Tools.isLinux() || Tools.isMacOsX()) {
            ev = mMousePressedEvent
        }
        handlePopupMenu(ev)
        if (ev!!.isConsumed) {
            return
        }
        if (e.modifiers == MouseEvent.BUTTON1_MASK) {
            // FIXME Dimitry: Double Click comes after Plain Click combining
            // (un)folding with editing
            // if (e.getClickCount() % 2 == 0) {
            // c.doubleClick(e);
            // } else {
            c.plainClick(e)
            // }
            e.consume()
        }
    }

    protected fun handlePopupMenu(e: MouseEvent?) {
        // first stop the timer and select the node:
        stopTimerForDelayedSelection()
        logger!!.fine("Extending selection for $e")
        c.extendSelection(e)
        // Right mouse <i>press</i> is <i>not</i> a popup trigger for Windows.
        // Only Right mouse release is a popup trigger!
        // OK, but Right mouse <i>press</i> <i>is</i> a popup trigger on Linux.
        logger!!.fine("Looking for popup for $e")
        c.showPopupMenu(e)
    }

    protected fun getControlRegion(p: Point2D): Rectangle {
        // Create a small square around the given point.
        val side = 8
        return Rectangle((p.x - side / 2).toInt(), (p.y - side / 2).toInt(), side, side)
    }

    fun createTimer(e: MouseEvent) {
        // stop old timer if present.*/
        stopTimerForDelayedSelection()
        /* Region to check for in the sequel. */controlRegionForDelayedSelection = getControlRegion(e.point)
        timerForDelayedSelection = Timer()
        timerForDelayedSelection?.schedule(
            timeDelayedSelection(c, e), /*
				 * if the new selection method is not enabled we put 0 to get
				 * direct selection.
				 */
            (if (delayedSelectionEnabled!!.value) timeForDelayedSelection?.getValue()?.toLong() else 0.toLong()) ?: 0L
        )
    }

    protected fun stopTimerForDelayedSelection() {
        // stop timer.
        if (timerForDelayedSelection != null) timerForDelayedSelection!!.cancel()
        timerForDelayedSelection = null
        controlRegionForDelayedSelection = null
    }

    @Suppress("DEPRECATION")
    protected inner class timeDelayedSelection internal constructor(
        private val c: ModeController,
        private val e: MouseEvent
    ) : TimerTask() {
        /** TimerTask method to enable the selection after a given time.  */
        override fun run() {
            /*
			 * formerly in ControllerAdapter. To guarantee, that point-to-select
			 * does not change selection if any meta key is pressed.
			 */
            SwingUtilities.invokeLater {
                if (e.modifiers == 0 && !c.isBlocked &&
                    c.view.selecteds.size <= 1
                ) {
                    c.extendSelection(e)
                }
            }
        }
    }

    companion object {
        // Logging:
        private var logger: Logger? = null

        /** time in ms, overwritten by property time_for_delayed_selection  */
        private var timeForDelayedSelection: IntHolder? = null

        /** overwritten by property delayed_selection_enabled  */
        private var delayedSelectionEnabled: BooleanHolder? = null
    }
}
