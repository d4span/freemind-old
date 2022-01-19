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

import freemind.controller.actions.generated.instance.MoveNodeXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.NodeAdapter
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 27.03.2014
 */
class MoveNodeActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : NodeXmlActorAdapter(pMapFeedback) {
    override fun act(action: XmlAction) {
        val moveAction = action as MoveNodeXmlAction
        val node = getNodeFromID(moveAction.node)
        node?.hGap = moveAction.hGap
        node?.shiftY = moveAction.shiftY
        if (node != null && node.isRoot) node.parentNode?.vGap = moveAction.vGap
        exMapFeedback?.nodeChanged(node)
    }

    override fun getDoActionClass(): Class<MoveNodeXmlAction> {
        return MoveNodeXmlAction::class.java
    }

    override fun apply(model: MindMap, selected: MindMapNode): ActionPair? {
        // reset position
        return if (selected.isRoot) null else getActionPair(selected, NodeAdapter.VGAP, NodeAdapter.HGAP, 0)
    }

    private fun getActionPair(
        selected: MindMapNode,
        parentVGap: Int,
        hGap: Int,
        shiftY: Int
    ): ActionPair {
        val moveAction = moveNode(
            selected, parentVGap, hGap,
            shiftY
        )
        val undoAction = moveNode(
            selected,
            selected
                .parentNode.vGap,
            selected.hGap,
            selected.shiftY
        )
        return ActionPair(moveAction, undoAction)
    }

    private fun moveNode(
        selected: MindMapNode,
        parentVGap: Int,
        hGap: Int,
        shiftY: Int
    ): MoveNodeXmlAction {
        val moveNodeAction = MoveNodeXmlAction()
        moveNodeAction.node = getNodeID(selected)
        moveNodeAction.hGap = hGap
        moveNodeAction.vGap = parentVGap
        moveNodeAction.shiftY = shiftY
        return moveNodeAction
    }

    fun moveNodeTo(
        node: MindMapNode,
        parentVGap: Int,
        hGap: Int,
        shiftY: Int
    ) {
        if (parentVGap == node.parentNode.vGap && hGap == node.hGap && shiftY == node.shiftY) {
            return
        }
        execute(getActionPair(node, parentVGap, hGap, shiftY))
    }
}
