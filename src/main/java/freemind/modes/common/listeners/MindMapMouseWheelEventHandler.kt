/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 09.11.2005
 */
package freemind.modes.common.listeners

import freemind.controller.Controller.Companion.addPropertyChangeListener
import freemind.main.FreeMind
import freemind.main.Resources
import freemind.modes.ControllerAdapter
import freemind.preferences.FreemindPropertyListener
import freemind.view.mindmapview.MapView
import java.awt.event.InputEvent
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.util.logging.Logger

/**
 * @author foltin
 */
class MindMapMouseWheelEventHandler(private val mController: ControllerAdapter) : MouseWheelListener {
    /**
     *
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        addPropertyChangeListener(object : FreemindPropertyListener {
            override fun propertyChanged(
                propertyName: String?,
                newValue: String?,
                oldValue: String?
            ) {
                if (propertyName == FreeMind.RESOURCES_WHEEL_VELOCITY) {
                    SCROLL_SKIPS = newValue!!.toInt()
                }
            }
        })
        SCROLL_SKIPS = mController.frame.getIntProperty(
            FreeMind.RESOURCES_WHEEL_VELOCITY, 8
        )
        logger!!.info("Setting SCROLL_SKIPS to " + SCROLL_SKIPS)
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.modes.ModeController.MouseWheelEventHandler#handleMouseWheelEvent
	 * (java.awt.event.MouseWheelEvent)
	 */
    @Suppress("DEPRECATION")
    override fun mouseWheelMoved(e: MouseWheelEvent) {
        if (mController.isBlocked) {
            return // block the scroll during edit (PN)
        }
        val registeredMouseWheelEventHandler = mController.registeredMouseWheelEventHandler
        for (handler in registeredMouseWheelEventHandler) {
            val result = handler.handleMouseWheelEvent(e)
            if (result) {
                // event was consumed:
                return
            }
        }
        if (e.modifiers and ZOOM_MASK != 0) {
            // fc, 18.11.2003: when control pressed, then the zoom is changed.
            var newZoomFactor = 1f + Math.abs(e.wheelRotation.toFloat()) / 10f
            if (e.wheelRotation < 0) newZoomFactor = 1 / newZoomFactor
            val oldZoom = (e.component as MapView).getZoom()
            var newZoom = oldZoom / newZoomFactor
            // round the value due to possible rounding problems.
            newZoom = Math.rint((newZoom * 1000f).toDouble()).toFloat() / 1000f
            newZoom = Math.max(1f / 32f, newZoom)
            newZoom = Math.min(32f, newZoom)
            if (newZoom != oldZoom) {
                mController.controller.setZoom(newZoom)
            }
            // end zoomchange
        } else if (e.modifiers and HORIZONTAL_SCROLL_MASK != 0) {
            (e.component as MapView).scrollBy(
                SCROLL_SKIPS * e.wheelRotation, 0
            )
        } else {
            (e.component as MapView).scrollBy(
                0,
                SCROLL_SKIPS * e.wheelRotation
            )
        }
    }

    @Suppress("DEPRECATION")
    companion object {
        private var SCROLL_SKIPS = 8
        private const val HORIZONTAL_SCROLL_MASK = (
            InputEvent.SHIFT_MASK
                or InputEvent.BUTTON1_MASK or InputEvent.BUTTON2_MASK
                or InputEvent.BUTTON3_MASK
            )
        private const val ZOOM_MASK = InputEvent.CTRL_MASK

        // |= oldX >=0 iff we are in the drag
        private var logger: Logger? = null
    }
}
