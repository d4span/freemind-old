/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.modes.mindmapmode.actions.xml.actors

import freemind.controller.actions.generated.instance.EdgeWidthFormatAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.EdgeAdapter
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 26.03.2014
 */
class EdgeWidthActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun getDoActionClass(): Class<EdgeWidthFormatAction> {
        return EdgeWidthFormatAction::class.java
    }

    fun setEdgeWidth(node: MindMapNode, width: Int) {
        if (width == getWidth(node)) {
            return
        }
        execute(getActionPair(node, width))
    }

    fun getActionPair(selected: MindMapNode, width: Int): ActionPair {
        val styleAction = createEdgeWidthFormatAction(
            selected, width
        )
        val undoStyleAction = createEdgeWidthFormatAction(
            selected, getWidth(selected)
        )
        return ActionPair(styleAction, undoStyleAction)
    }

    fun getWidth(selected: MindMapNode): Int {
        return (selected.edge as EdgeAdapter).realWidth
    }

    private fun createEdgeWidthFormatAction(
        selected: MindMapNode,
        width: Int
    ): EdgeWidthFormatAction {
        val edgeWidthAction = EdgeWidthFormatAction()
        edgeWidthAction.node = getNodeID(selected)
        edgeWidthAction.width = width
        return edgeWidthAction
    }

    override fun act(action: XmlAction) {
        if (action is EdgeWidthFormatAction) {
            val edgeWithAction = action
            val node = getNodeFromID(edgeWithAction.node)
            val width = edgeWithAction.width
            val edge = node?.edge as EdgeAdapter
            if (edge.realWidth != width) {
                edge.width = width
                exMapFeedback?.nodeChanged(node)
            }
        }
    }
}
