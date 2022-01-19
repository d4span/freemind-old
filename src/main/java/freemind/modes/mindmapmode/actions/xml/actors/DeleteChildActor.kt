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

import freemind.controller.actions.generated.instance.DeleteNodeAction
import freemind.controller.actions.generated.instance.PasteNodeAction
import freemind.controller.actions.generated.instance.UndoPasteNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.extensions.PermanentNodeHook
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate
import freemind.view.mindmapview.NodeView

/**
 * @author foltin
 * @date 18.03.2014
 */
class DeleteChildActor
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
        val deleteNodeAction = action as DeleteNodeAction
        val selectedNode = getNodeFromID(deleteNodeAction.node)
        deleteWithoutUndo(selectedNode)
    }

    /**
     */
    fun deleteWithoutUndo(selectedNode: MindMapNode?) {
        if (selectedNode != null) {
            require(!selectedNode.isRoot) { "Root node can't be deleted" }
            // remove hooks:
            removeHooks(selectedNode)
            val parent = selectedNode.parentNode
            exMapFeedback?.fireNodePreDeleteEvent(selectedNode)
            // deregister node:
            val map = exMapFeedback?.map
            map?.linkRegistry
                ?.deregisterLinkTarget(selectedNode)
            // deselect
            val view = exMapFeedback?.viewAbstraction
            if (view != null) {
                val nodeView = view.getNodeView(selectedNode)
                view.deselect(nodeView)
                if (view.selecteds.size == 0) {
                    val newSelectedView: NodeView
                    val childIndex = parent.getChildPosition(selectedNode)
                    newSelectedView = if (parent.childCount > childIndex + 1) {
                        // the next node
                        view.getNodeView(parent.getChildAt(childIndex + 1) as MindMapNode)
                    } else if (childIndex > 0) {
                        // the node before:
                        view.getNodeView(parent.getChildAt(childIndex - 1) as MindMapNode)
                    } else {
                        // no other node on same level. take the parent.
                        view.getNodeView(parent)
                    }
                    view.select(newSelectedView)
                }
            }
            exMapFeedback?.removeNodeFromParent(selectedNode)
            // post event
            exMapFeedback?.fireNodePostDeleteEvent(selectedNode, parent)
        }
    }

    private fun removeHooks(selectedNode: MindMapNode) {
        val it: Iterator<MindMapNode> = selectedNode.childrenUnfolded()
        while (it.hasNext()) {
            val child = it.next()
            removeHooks(child)
        }
        var currentRun: Long = 0
        // determine timeout:
        val timeout = (selectedNode.activatedHooks.size * 2 + 2).toLong()
        while (selectedNode.activatedHooks.size > 0) {
            val hook = selectedNode
                .activatedHooks.iterator().next() as PermanentNodeHook
            selectedNode.removeHook(hook)
            check(currentRun++ <= timeout) { "Timeout reached shutting down the hooks." }
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<DeleteNodeAction> {
        return DeleteNodeAction::class.java
    }

    fun deleteNode(selectedNode: MindMapNode) {
        require(!selectedNode.isRoot) { "Root node can't be deleted" }
        val newId = getNodeID(selectedNode)
        val copy = exMapFeedback?.copy(selectedNode, true)
        val coord = NodeCoordinate(
            selectedNode,
            selectedNode.isLeft
        )
        // Undo-action
        var pasteNodeAction: PasteNodeAction?
        pasteNodeAction = exMapFeedback?.actorFactory?.pasteActor?.getPasteNodeAction(
            copy,
            coord, null as UndoPasteNodeAction?
        )
        val deleteAction = getDeleteNodeAction(newId)
        exMapFeedback?.doTransaction(
            doActionClass.name,
            ActionPair(deleteAction, pasteNodeAction)
        )
    }

    fun getDeleteNodeAction(newId: String?): DeleteNodeAction {
        val deleteAction = DeleteNodeAction()
        deleteAction.node = newId
        return deleteAction
    }
}
