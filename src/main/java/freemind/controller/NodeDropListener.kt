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
package freemind.controller

import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetListener

// import ublic class MindMapNodesSelection implements Transferable,
// ClipboardOwner {
// public static DataFlavor fileListFlavor = null;
class NodeDropListener : DropTargetListener {
    private var mListener: DropTargetListener? = null
    fun register(listener: DropTargetListener?) {
        mListener = listener
    }

    fun deregister() {
        mListener = null
    }

    override fun dragEnter(dtde: DropTargetDragEvent) {
        if (mListener != null) mListener!!.dragEnter(dtde)
    }

    override fun dragExit(dte: DropTargetEvent) {
        if (mListener != null) mListener!!.dragExit(dte)
    }

    override fun dragOver(dtde: DropTargetDragEvent) {
        if (mListener != null) mListener!!.dragOver(dtde)
    }

    override fun drop(dtde: DropTargetDropEvent) {
        if (mListener != null) mListener!!.drop(dtde)
    }

    override fun dropActionChanged(dtde: DropTargetDragEvent) {
        if (mListener != null) mListener!!.dropActionChanged(dtde)
    }
}