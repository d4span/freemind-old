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
 * Created on 11.11.2005
 */
package freemind.modes.common.listeners

import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.modes.ModeController
import freemind.view.mindmapview.MapView
import java.awt.Rectangle
import java.awt.event.MouseEvent

/**
 * @author foltin
 */
class CommonMouseMotionManager
/**
 *
 */(private val mController: ModeController) : MapMouseMotionReceiver {
    var originX = -1
    var originY = -1

    // |= oldX >=0 iff we are in the drag
    override fun mouseDragged(e: MouseEvent?) {
        val r = Rectangle(e!!.x, e.y, 1, 1)
        val mapView = e.component as MapView
        val isEventPointVisible = mapView.visibleRect.contains(r)
        if (!isEventPointVisible) {
            mapView.scrollRectToVisible(r)
        }
        // Always try to get mouse to the original position in the Map.
        if (originX >= 0 && isEventPointVisible) {
            (e.component as MapView).scrollBy(
                originX - e.x,
                originY -
                    e.y
            )
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        if (!mController.isBlocked && e!!.button == MouseEvent.BUTTON1) {
            mController.view.setMoveCursor(true)
            originX = e.x
            originY = e.y
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        originX = -1
        originY = -1
    }
}
