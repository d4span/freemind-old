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

import freemind.controller.actions.generated.instance.EditNoteToNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.HtmlTools
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 23.04.2014
 */
class ChangeNoteTextActor(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun act(action: XmlAction) {
        if (action is EditNoteToNodeAction) {
            val noteTextAction = action
            val node = getNodeFromID(
                noteTextAction
                    .node
            )
            val newText = noteTextAction.text
            val oldText = node?.noteText
            if (!Tools.safeEquals(newText, oldText)) {
                node?.noteText = newText
                exMapFeedback?.nodeChanged(node)
            }
        }
    }

    override fun getDoActionClass(): Class<EditNoteToNodeAction> {
        return EditNoteToNodeAction::class.java
    }

    fun createEditNoteToNodeAction(
        node: MindMapNode?,
        text: String?
    ): EditNoteToNodeAction {
        val nodeAction = EditNoteToNodeAction()
        nodeAction.node = getNodeID(node)
        if (text != null &&
            (
                HtmlTools.htmlToPlain(text).length != 0 || text
                    .indexOf("<img") >= 0
                )
        ) {
            nodeAction.text = text
        } else {
            nodeAction.text = null
        }
        return nodeAction
    }

    fun setNoteText(node: MindMapNode, text: String) {
        val oldNoteText = node.noteText
        if (Tools.safeEquals(text, oldNoteText)) {
            // they are equal.
            return
        }
        logger!!.fine("Old Note Text:'$oldNoteText, new:'$text'.")
        logger!!.fine(Tools.compareText(oldNoteText, text))
        val doAction = createEditNoteToNodeAction(node, text)
        val undoAction = createEditNoteToNodeAction(
            node,
            oldNoteText
        )
        execute(ActionPair(doAction, undoAction))
    }
}
