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

import freemind.controller.actions.generated.instance.AddAttributeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.attributes.Attribute
import freemind.modes.mindmapmode.actions.xml.ActionPair

class AddAttributeActor(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun act(action: XmlAction) {
        if (action is AddAttributeAction) {
            val addAttributeAction = action
            val node = getNodeFromID(addAttributeAction.node)
            val newAttribute = Attribute(
                addAttributeAction.name, addAttributeAction.value
            )
            node?.addAttribute(newAttribute)
            exMapFeedback?.nodeChanged(node)
        }
    }

    override fun getDoActionClass(): Class<AddAttributeAction> {
        return AddAttributeAction::class.java
    }

    fun getActionPair(selected: MindMapNode, pAttribute: Attribute): ActionPair {
        val setAttributeAction = getAddAttributeAction(
            selected,
            pAttribute
        )
        val undoAddAttributeAction = xmlActorFactory?.removeAttributeActor
            ?.getRemoveAttributeAction(selected, selected.attributeTableLength)
        return ActionPair(setAttributeAction, undoAddAttributeAction)
    }

    /**
     * @param pSelected
     * @param pAttribute
     * @return
     */
    fun getAddAttributeAction(
        pSelected: MindMapNode?,
        pAttribute: Attribute
    ): AddAttributeAction {
        val addAttributeAction = AddAttributeAction()
        addAttributeAction.node = getNodeID(pSelected)
        addAttributeAction.name = pAttribute.name
        addAttributeAction.value = pAttribute.value
        return addAttributeAction
    }

    fun addAttribute(pNode: MindMapNode, pAttribute: Attribute): Int {
        val retValue = pNode.attributeTableLength
        val actionPair = getActionPair(pNode, pAttribute)
        execute(actionPair)
        return retValue
    }
}
