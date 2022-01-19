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
package freemind.modes.mindmapmode.actions.xml.actors

import freemind.controller.actions.generated.instance.NewNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapLinkRegistry
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 16.03.2014
 */
class NewChildActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction) {
        val addNodeAction = action as NewNodeAction
        val parent = getNodeFromID(addNodeAction.node)
        val index = addNodeAction.index
        val newNode = exMapFeedback?.newNode("", parent?.map)
        newNode?.isLeft = addNodeAction.position == "left"
        val newId = addNodeAction.newId
        val givenId = linkRegistry?.registerLinkTarget(newNode, newId)
        require(givenId == newId) {
            (
                "Designated id '" + newId +
                    "' was not given to the node. It received '" + givenId +
                    "'."
                )
        }
        exMapFeedback?.insertNodeInto(newNode, parent, index)
        // call hooks:
        for (hook in parent?.activatedHooks ?: emptyList()) {
            hook.onNewChild(newNode)
        }
        // done.
    }

    override val linkRegistry: MindMapLinkRegistry?
        get() = exMapFeedback?.map?.linkRegistry

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<NewNodeAction> {
        return NewNodeAction::class.java
    }

    fun addNewNode(
        parent: MindMapNode,
        index: Int,
        newNodeIsLeft: Boolean
    ): MindMapNode {
        var i = index
        if (i == -1) {
            i = parent.childCount
        }
        // bug fix from Dimitri.
        linkRegistry?.registerLinkTarget(parent)
        val newId = linkRegistry?.generateUniqueID(null)
        val newNodeAction = getAddNodeAction(
            parent, i, newId,
            newNodeIsLeft
        )
        // Undo-action
        val deleteAction = exMapFeedback?.actorFactory?.deleteChildActor
            ?.getDeleteNodeAction(newId)
        exMapFeedback?.doTransaction(
            exMapFeedback?.getResourceString("new_child"),
            ActionPair(newNodeAction, deleteAction)
        )
        return parent.getChildAt(i) as MindMapNode
    }

    fun getAddNodeAction(
        parent: MindMapNode?,
        index: Int,
        newId: String?,
        newNodeIsLeft: Boolean
    ): NewNodeAction {
        val pos = if (newNodeIsLeft) "left" else "right"
        val newNodeAction = NewNodeAction()
        newNodeAction.node = getNodeID(parent)
        newNodeAction.position = pos
        newNodeAction.index = index
        newNodeAction.newId = newId
        return newNodeAction
    }
}
