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

import freemind.controller.actions.generated.instance.EdgeColorFormatAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.EdgeAdapter
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.MindMapEdgeModel
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Color

/**
 * @author foltin
 * @date 01.04.2014
 */
class EdgeColorActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun setEdgeColor(node: MindMapNode, color: Color?) {
        val doAction = createEdgeColorFormatAction(
            node,
            color
        )
        val undoAction = createEdgeColorFormatAction(
            node,
            (node.edge as EdgeAdapter).realColor
        )
        execute(ActionPair(doAction, undoAction))
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction) {
        if (action is EdgeColorFormatAction) {
            val edgeAction = action
            val color = Tools.xmlToColor(edgeAction.color)
            val node = getNodeFromID(edgeAction.node)
            val oldColor = (node?.edge as EdgeAdapter).realColor
            if (!Tools.safeEquals(color, oldColor)) {
                (node.edge as MindMapEdgeModel).color = color
                exMapFeedback?.nodeChanged(node)
            }
        }
    }

    fun createEdgeColorFormatAction(
        node: MindMapNode?,
        color: Color?
    ): EdgeColorFormatAction {
        val edgeAction = EdgeColorFormatAction()
        edgeAction.node = getNodeID(node)
        if (color != null) {
            edgeAction.color = Tools.colorToXml(color)
        }
        return edgeAction
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<EdgeColorFormatAction> {
        return EdgeColorFormatAction::class.java
    }
}
