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

import freemind.controller.actions.generated.instance.NodeStyleFormatAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 27.03.2014
 */
class NodeStyleActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun getDoActionClass(): Class<NodeStyleFormatAction> {
        return NodeStyleFormatAction::class.java
    }

    fun setStyle(node: MindMapNode, style: String?) {
        if (style == null) {
            execute(getActionPair(node, null))
            return
        }
        for (i in MindMapNode.NODE_STYLES.indices) {
            val dstyle = MindMapNode.NODE_STYLES[i]
            if (Tools.safeEquals(style, dstyle)) {
                execute(getActionPair(node, style))
                return
            }
        }
        throw IllegalArgumentException("Unknown style $style")
    }

    fun getActionPair(targetNode: MindMapNode, style: String?): ActionPair {
        val styleAction = createNodeStyleFormatAction(
            targetNode, style
        )
        val undoStyleAction = createNodeStyleFormatAction(
            targetNode, targetNode.style
        )
        return ActionPair(styleAction, undoStyleAction)
    }

    private fun createNodeStyleFormatAction(
        selected: MindMapNode,
        style: String?
    ): NodeStyleFormatAction {
        val nodeStyleAction = NodeStyleFormatAction()
        nodeStyleAction.node = getNodeID(selected)
        nodeStyleAction.style = style
        return nodeStyleAction
    }

    override fun act(action: XmlAction) {
        if (action is NodeStyleFormatAction) {
            val nodeStyleAction = action
            val node = getNodeFromID(nodeStyleAction.node)
            val style = nodeStyleAction.style
            if (!Tools.safeEquals(
                    if (node?.hasStyle() ?: false) node?.bareStyle else null,
                    style
                )
            ) {
                // logger.info("Setting style of " + node + " to "+ style +
                // " and was " + node.getStyle());
                node?.style = style
                exMapFeedback?.nodeStyleChanged(node)
            }
        }
    }
}
