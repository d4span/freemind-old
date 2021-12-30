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

import freemind.main.Resources
import freemind.main.Tools
import freemind.view.mindmapview.MainView
import java.awt.Cursor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DragGestureEvent
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DragSource
import java.awt.dnd.DragSourceDragEvent
import java.awt.dnd.DragSourceDropEvent
import java.awt.dnd.DragSourceEvent
import java.awt.dnd.DragSourceListener
import java.awt.event.InputEvent

/**
 * The NodeDragListener which belongs to every NodeView
 */
class NodeDragListener(private val c: Controller) : DragGestureListener {
    fun getCursorByAction(dragAction: Int): Cursor {
        return when (dragAction) {
            DnDConstants.ACTION_COPY -> DragSource.DefaultCopyDrop
            DnDConstants.ACTION_LINK -> DragSource.DefaultLinkDrop
            else -> DragSource.DefaultMoveDrop
        }
    }

    override fun dragGestureRecognized(e: DragGestureEvent) {
        if (!Resources.getInstance().getBoolProperty("draganddrop")) return
        val node = (e.component as MainView).nodeView
            .model
        if (node.isRoot) return

        // Transferable t; // = new StringSelection("");
        var dragAction = "MOVE"
        var cursor: Cursor? = getCursorByAction(e.dragAction)
        val modifiersEx = e.triggerEvent.modifiersEx
        val macLinkAction = (Tools.isMacOsX()
                && modifiersEx and InputEvent.BUTTON1_DOWN_MASK != 0
                && e.triggerEvent.isMetaDown)
        val otherOsLinkAction = modifiersEx and InputEvent.BUTTON3_DOWN_MASK != 0
        if (macLinkAction || otherOsLinkAction) {
            // Change drag action
            cursor = DragSource.DefaultLinkDrop
            dragAction = "LINK"
        }
        if (modifiersEx and InputEvent.BUTTON2_DOWN_MASK != 0) {
            // Change drag action
            cursor = DragSource.DefaultCopyDrop
            dragAction = "COPY"
        }
        val t = c.modeController!!.copy()
        // new MindMapNodesSelection("Ahoj","Ahoj","Ahoj", dragAction);
        (t as MindMapNodesSelection).setDropAction(dragAction)
        // public void setDropAction(String dropActionContent) {

        // starts the dragging
        // DragSource dragSource = DragSource.getDefaultDragSource();
        e.startDrag(cursor, t, object : DragSourceListener {
            override fun dragDropEnd(dsde: DragSourceDropEvent) {}
            override fun dragEnter(e: DragSourceDragEvent) {}
            override fun dragExit(dse: DragSourceEvent) {}
            override fun dragOver(dsde: DragSourceDragEvent) {}
            override fun dropActionChanged(dsde: DragSourceDragEvent) {
                dsde.dragSourceContext.cursor = getCursorByAction(dsde.userAction)
            }
        })
    }
}