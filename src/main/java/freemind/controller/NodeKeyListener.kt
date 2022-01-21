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
/*$Id: NodeKeyListener.java,v 1.16.18.2 2006/01/12 23:10:12 christianfoltin Exp $*/
package freemind.controller

import java.awt.event.KeyEvent
import java.awt.event.KeyListener

/**
 * The KeyListener which belongs to the node and cares for Events like C-D
 * (Delete Node). It forwards the requests to NodeController.
 */
class NodeKeyListener : KeyListener {
    private var mListener: KeyListener? = null
    fun register(listener: KeyListener?) {
        mListener = listener
    }

    fun deregister() {
        mListener = null
    }

    override fun keyPressed(e: KeyEvent) {
        if (mListener != null) mListener!!.keyPressed(e)
    }

    override fun keyReleased(e: KeyEvent) {
        if (mListener != null) mListener!!.keyReleased(e)
    }

    override fun keyTyped(e: KeyEvent) {
        if (mListener != null) mListener!!.keyTyped(e)
    }
}