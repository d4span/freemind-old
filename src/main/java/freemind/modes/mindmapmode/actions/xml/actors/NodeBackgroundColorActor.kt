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

import freemind.controller.actions.generated.instance.NodeBackgroundColorFormatAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Color

/**
 * @author foltin
 * @date 01.04.2014
 */
class NodeBackgroundColorActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun setNodeBackgroundColor(node: MindMapNode, color: Color?) {
        val doAction = createNodeBackgroundColorFormatAction(
            node, color
        )
        val undoAction = createNodeBackgroundColorFormatAction(
            node, node.backgroundColor
        )
        execute(ActionPair(doAction, undoAction))
    }

    fun createNodeBackgroundColorFormatAction(
        node: MindMapNode?,
        color: Color?
    ): NodeBackgroundColorFormatAction {
        val nodeAction = NodeBackgroundColorFormatAction()
        nodeAction.node = getNodeID(node)
        nodeAction.color = Tools.colorToXml(color)
        return nodeAction
    }

    override fun act(action: XmlAction) {
        if (action is NodeBackgroundColorFormatAction) {
            val nodeColorAction = action
            val color = Tools.xmlToColor(nodeColorAction.color)
            val node = getNodeFromID(
                nodeColorAction
                    .node
            )
            val oldColor = node?.backgroundColor
            if (!Tools.safeEquals(color, oldColor)) {
                node?.backgroundColor = color // null
                exMapFeedback?.nodeChanged(node)
            }
        }
    }

    override fun getDoActionClass(): Class<NodeBackgroundColorFormatAction> {
        return NodeBackgroundColorFormatAction::class.java
    }
}
