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

import freemind.controller.actions.generated.instance.EdgeStyleFormatAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.EdgeAdapter
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 26.03.2014
 */
class EdgeStyleActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun getDoActionClass(): Class<EdgeStyleFormatAction> {
        return EdgeStyleFormatAction::class.java
    }

    /**
     * @param node
     * @param style use null to remove the style
     */
    fun setEdgeStyle(node: MindMapNode, style: String?) {
        if (Tools.safeEquals(style, getStyle(node))) {
            return
        }
        if (style != null) {
            var found = false
            // check style:
            for (i in EdgeAdapter.EDGESTYLES.indices) {
                val possibleStyle = EdgeAdapter.EDGESTYLES[i]
                if (Tools.safeEquals(style, possibleStyle)) {
                    found = true
                    break
                }
            }
            require(found) {
                (
                    "Style " + style +
                        " is not known"
                    )
            }
        }
        execute(getActionPair(node, style))
    }

    fun getActionPair(selected: MindMapNode, style: String?): ActionPair {
        val styleAction = createNodeStyleFormatAction(
            selected, style
        )
        val undoStyleAction = createNodeStyleFormatAction(
            selected, getStyle(selected)
        )
        return ActionPair(styleAction, undoStyleAction)
    }

    fun getStyle(selected: MindMapNode): String? {
        var oldStyle = selected.edge.style
        if (!selected.edge.hasStyle()) {
            oldStyle = null
        }
        return oldStyle
    }

    private fun createNodeStyleFormatAction(
        selected: MindMapNode,
        style: String?
    ): EdgeStyleFormatAction {
        val edgeStyleAction = EdgeStyleFormatAction()
        edgeStyleAction.node = getNodeID(selected)
        edgeStyleAction.style = style
        return edgeStyleAction
    }

    override fun act(action: XmlAction) {
        if (action is EdgeStyleFormatAction) {
            val edgeStyleAction = action
            val node = getNodeFromID(edgeStyleAction.node)
            val newStyle = edgeStyleAction.style
            val edge = node?.edge
            if (!Tools.safeEquals(
                    if (edge != null && edge.hasStyle()) edge.style else null,
                    newStyle
                )
            ) {
                (edge as EdgeAdapter).style = newStyle
                exMapFeedback?.nodeChanged(node)
            }
        }
    }
}
