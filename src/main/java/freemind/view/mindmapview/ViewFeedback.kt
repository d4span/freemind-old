/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.view.mindmapview

import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListener
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.NodeKeyListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeMouseMotionListener
import freemind.modes.MindMapNode
import java.awt.Font
import java.awt.event.MouseWheelEvent

/**
 * ViewFeedback is an interface implemented by the ModeController classes
 * to offer view related methods.
 *
 * @author foltin
 * @date 25.04.2014
 */
interface ViewFeedback {
    /**
     * @param pNode
     * @param pIsSelected
     */
    fun changeSelection(pNode: NodeView?, pIsSelected: Boolean)

    /**
     * @param pNode
     */
    fun onLostFocusNode(pNode: NodeView?)

    /**
     * @param pNode
     */
    fun onFocusNode(pNode: NodeView?)

    /**
     * @param pModel
     * @param pFold true means, that the node should be folded.
     */
    fun setFolded(pModel: MindMapNode?, pFold: Boolean)

    /**
     * @param pNewView
     */
    fun onViewCreatedHook(pNewView: NodeView?)

    /**
     * @param pNodeView
     */
    fun onViewRemovedHook(pNodeView: NodeView?)

    /**
     * @param pResourceId
     * @return the setting of freemind.properties resp. auto.properties.
     */
    fun getProperty(pResourceId: String?): String?

    /**
     * @return
     */
    val defaultFont: Font?

    /**
     * @return
     */
    val nodeMouseMotionListener: NodeMouseMotionListener?

    /**
     * @return
     */
    val nodeMotionListener: NodeMotionListener?

    /**
     * @return
     */
    val nodeKeyListener: NodeKeyListener?

    /**
     * @return
     */
    val nodeDragListener: NodeDragListener?

    /**
     * @return
     */
    val nodeDropListener: NodeDropListener?

    /**
     * @return
     */
    val mapMouseMotionListener: MapMouseMotionListener?

    /**
     * @return
     */
    val mapMouseWheelListener: MapMouseWheelListener?

    interface MouseWheelEventHandler {
        /**
         * @return true if the event was sucessfully processed and false if the
         * event did not apply.
         */
        fun handleMouseWheelEvent(e: MouseWheelEvent?): Boolean
    }

    fun registerMouseWheelEventHandler(handler: MouseWheelEventHandler?)
    fun deRegisterMouseWheelEventHandler(handler: MouseWheelEventHandler?)
}
