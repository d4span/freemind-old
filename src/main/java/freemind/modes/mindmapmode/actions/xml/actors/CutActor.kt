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

import freemind.controller.actions.generated.instance.CompoundAction
import freemind.controller.actions.generated.instance.CutNodeAction
import freemind.controller.actions.generated.instance.UndoPasteNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate
import java.awt.datatransfer.Transferable

/**
 * @author foltin
 * @date 11.04.2014
 */
class CutActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun getCutNodeAction(node: MindMapNode?): CutNodeAction {
        val cutAction = CutNodeAction()
        cutAction.node = getNodeID(node)
        return cutAction
    }

    fun cut(nodeList: List<MindMapNode>): Transferable? {
        exMapFeedback?.sortNodesByDepth(nodeList)
        val totalCopy = exMapFeedback?.copy(nodeList, true)
        // Do-action
        val doAction = CompoundAction()
        // Undo-action
        val undo = CompoundAction()
        // sort selectedNodes list by depth, in order to guarantee that sons are
        // deleted first:
        val i = nodeList.iterator()
        while (i.hasNext()) {
            val node = i.next()
            if (node.parentNode == null) {
                continue
            }
            val cutNodeAction = getCutNodeAction(node)
            doAction.addChoice(cutNodeAction)
            val coord = NodeCoordinate(node, node.isLeft)
            val copy = exMapFeedback?.copy(node, true)
            val pasteNodeAction = xmlActorFactory?.pasteActor
                ?.getPasteNodeAction(copy, coord, null as UndoPasteNodeAction?)
            // The paste actions are reversed because of the strange
            // coordinates.
            undo.addAtChoice(0, pasteNodeAction)
        }
        if (doAction.sizeChoiceList() > 0) {
            execute(ActionPair(doAction, undo))
        }
        return totalCopy
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction) {
        val cutAction = action as CutNodeAction
        val selectedNode = getNodeFromID(
            cutAction
                .node
        )
        xmlActorFactory?.deleteChildActor?.deleteWithoutUndo(selectedNode)
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<CutNodeAction> {
        return CutNodeAction::class.java
    }
}
