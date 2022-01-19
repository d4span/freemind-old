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

import freemind.controller.actions.generated.instance.EditNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 01.04.2014
 */
class EditActor
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
        val editAction = action as EditNodeAction
        val node = getNodeFromID(
            editAction
                .node
        )
        if (node.toString() != editAction.text) {
            node?.setUserObject(editAction.text)
            exMapFeedback?.nodeChanged(node)
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<EditNodeAction> {
        return EditNodeAction::class.java
    }

    fun setNodeText(selected: MindMapNode, newText: String?) {
        val oldText = selected.toString()
        val EditAction = EditNodeAction()
        val nodeID = getNodeID(selected)
        EditAction.node = nodeID
        EditAction.text = newText
        val undoEditAction = EditNodeAction()
        undoEditAction.node = nodeID
        undoEditAction.text = oldText
        execute(ActionPair(EditAction, undoEditAction))
    }
}
