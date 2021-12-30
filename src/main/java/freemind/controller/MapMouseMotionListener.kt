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
/*$Id: MapMouseMotionListener.java,v 1.7.16.5.2.1 2008/01/04 22:52:30 christianfoltin Exp $*/
package freemind.controller

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JPopupMenu

/**
 * The MouseListener which belongs to MapView
 */
class MapMouseMotionListener(private val c: Controller) : MouseMotionListener, MouseListener {
    interface MapMouseMotionReceiver {
        fun mouseDragged(e: MouseEvent?)
        fun mousePressed(e: MouseEvent?)
        fun mouseReleased(e: MouseEvent?)
    }

    private var mReceiver: MapMouseMotionReceiver? = null
    fun register(receiver: MapMouseMotionReceiver?) {
        mReceiver = receiver
    }

    fun deregister() {
        mReceiver = null
    }

    private fun handlePopup(e: MouseEvent) {
        if (e.isPopupTrigger) {
            var popup: JPopupMenu? = null
            // detect collision with an element placed on the root pane of the
            // window.
            val obj: Any? = c.view!!.detectCollision(e.point)
            if (obj != null) {
                // there is a collision with object obj.
                // call the modecontroller to give a popup menu for this object
                popup = c.modeController!!.getPopupForModel(obj)
            }
            if (popup == null) { // no context popup found:
                // normal popup:
                popup = c.frame.freeMindMenuBar.mapsPopupMenu
            }
            popup!!.show(e.component, e.x, e.y)
            popup.isVisible = true
        }
    }

    override fun mouseMoved(e: MouseEvent) {}
    override fun mouseDragged(e: MouseEvent) {
        if (mReceiver != null) {
            mReceiver!!.mouseDragged(e)
        }
    }

    override fun mouseClicked(e: MouseEvent) {
        // to loose the focus in edit
        c.view!!.selectAsTheOnlyOneSelected(c.view!!.selected)
        c.obtainFocusForSelected()
    }

    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {
        if (e.isPopupTrigger) { // start the move, when the user press the
            // mouse (PN)
            handlePopup(e)
        } else if (mReceiver != null) mReceiver!!.mousePressed(e)
        e.consume()
    }

    override fun mouseReleased(e: MouseEvent) {
        if (mReceiver != null) {
            mReceiver!!.mouseReleased(e)
        }
        handlePopup(e)
        e.consume()
        c.view!!.setMoveCursor(false) // release the cursor to default
        // (PN)
    }
}
