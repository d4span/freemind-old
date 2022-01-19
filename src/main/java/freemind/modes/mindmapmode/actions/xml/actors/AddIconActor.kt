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

import freemind.controller.actions.generated.instance.AddIconAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindIcon
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 25.03.2014
 */
class AddIconActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun addIcon(node: MindMapNode, icon: MindIcon) {
        execute(getAddLastIconActionPair(node, icon))
    }

    override fun act(action: XmlAction) {
        if (action is AddIconAction) {
            val iconAction = action
            val node = getNodeFromID(
                iconAction
                    .node
            )
            val iconName = iconAction.iconName
            val position = iconAction.iconPosition
            val icon = MindIcon.factory(iconName)
            node?.addIcon(icon, position)
            exMapFeedback?.nodeChanged(node)
        }
    }

    override fun getDoActionClass(): Class<AddIconAction> {
        return AddIconAction::class.java
    }

    fun createAddIconAction(
        node: MindMapNode?,
        icon: MindIcon,
        iconPosition: Int
    ): AddIconAction {
        val action = AddIconAction()
        action.node = getNodeID(node)
        action.iconName = icon.name
        action.iconPosition = iconPosition
        return action
    }

    /**
     */
    private fun getAddLastIconActionPair(node: MindMapNode, icon: MindIcon): ActionPair {
        val iconIndex = MindIcon.LAST
        return getAddIconActionPair(node, icon, iconIndex)
    }

    private fun getAddIconActionPair(
        node: MindMapNode,
        icon: MindIcon,
        iconIndex: Int
    ): ActionPair {
        val doAction = createAddIconAction(node, icon, iconIndex)
        val undoAction = xmlActorFactory?.removeIconActor?.createRemoveIconXmlAction(
            node, iconIndex
        )
        return ActionPair(doAction, undoAction)
    }

    /**
     */
    private fun getToggleIconActionPair(node: MindMapNode, icon: MindIcon): ActionPair {
        val iconIndex = Tools.iconFirstIndex(
            node,
            icon.name
        )
        return if (iconIndex == -1) {
            getAddLastIconActionPair(node, icon)
        } else {
            getRemoveIconActionPair(node, icon, iconIndex)
        }
    }

    /**
     * @param removeFirst
     */
    private fun getRemoveIconActionPair(
        node: MindMapNode,
        icon: MindIcon,
        removeFirst: Boolean
    ): ActionPair? {
        val iconIndex = if (removeFirst) Tools.iconFirstIndex(
            node, icon.name
        ) else Tools.iconLastIndex(
            node, icon.name
        )
        return if (iconIndex >= 0) getRemoveIconActionPair(node, icon, iconIndex) else null
    }

    private fun getRemoveIconActionPair(
        node: MindMapNode,
        icon: MindIcon,
        iconIndex: Int
    ): ActionPair {
        val doAction = xmlActorFactory?.removeIconActor?.createRemoveIconXmlAction(
            node, iconIndex
        )
        val undoAction: XmlAction = createAddIconAction(node, icon, iconIndex)
        return ActionPair(doAction, undoAction)
    }

    fun toggleIcon(node: MindMapNode, icon: MindIcon) {
        exMapFeedback?.doTransaction(
            this.javaClass.name + "/toggle", getToggleIconActionPair(node, icon)
        )
    }

    fun removeIcon(node: MindMapNode, icon: MindIcon, removeFirst: Boolean) {
        val removeIconActionPair = getRemoveIconActionPair(
            node,
            icon, removeFirst
        ) ?: return
        exMapFeedback?.doTransaction(
            this.javaClass.name + "/remove", removeIconActionPair
        )
    }
}
