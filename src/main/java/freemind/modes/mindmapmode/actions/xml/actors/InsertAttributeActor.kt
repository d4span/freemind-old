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

import freemind.controller.actions.generated.instance.InsertAttributeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.attributes.Attribute
import freemind.modes.mindmapmode.actions.xml.ActionPair

class InsertAttributeActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun act(action: XmlAction) {
        if (action is InsertAttributeAction) {
            val setAttributeAction = action
            val node = getNodeFromID(setAttributeAction.node)
            val newAttribute = Attribute(
                setAttributeAction.name, setAttributeAction.value
            )
            val position = setAttributeAction.position
            node?.checkAttributePosition(position)
            node?.insertAttribute(position, newAttribute)
            exMapFeedback?.nodeChanged(node)
        }
    }

    override fun getDoActionClass(): Class<InsertAttributeAction> {
        return InsertAttributeAction::class.java
    }

    fun getActionPair(
        selected: MindMapNode?,
        pPosition: Int,
        pAttribute: Attribute
    ): ActionPair {
        val insertAttributeAction = getInsertAttributeAction(
            selected,
            pPosition, pAttribute
        )
        val undoInsertAttributeAction =
            xmlActorFactory?.removeAttributeActor?.getRemoveAttributeAction(selected, pPosition)
        return ActionPair(insertAttributeAction, undoInsertAttributeAction)
    }

    /**
     * @param pSelected
     * @param pPosition
     * @param pAttribute
     * @return
     */
    fun getInsertAttributeAction(
        pSelected: MindMapNode?,
        pPosition: Int,
        pAttribute: Attribute
    ): InsertAttributeAction {
        val insertAttributeAction = InsertAttributeAction()
        insertAttributeAction.node = getNodeID(pSelected)
        insertAttributeAction.name = pAttribute.name
        insertAttributeAction.value = pAttribute.value
        insertAttributeAction.position = pPosition
        return insertAttributeAction
    }

    fun insertAttribute(pNode: MindMapNode?, pPosition: Int, pAttribute: Attribute) {
        val actionPair = getActionPair(pNode, pPosition, pAttribute)
        execute(actionPair)
    }
}
