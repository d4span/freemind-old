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

import freemind.controller.actions.generated.instance.FontSizeNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 26.03.2014
 */
class FontSizeActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun getDoActionClass(): Class<FontSizeNodeAction> {
        return FontSizeNodeAction::class.java
    }

    /**
     */
    fun setFontSize(node: MindMapNode, fontSizeValue: String?) {
        if (Tools.safeEquals(fontSizeValue, node.fontSize)) {
            return
        }
        execute(getActionPair(node, fontSizeValue))
    }

    fun getActionPair(node: MindMapNode, fontSizeValue: String?): ActionPair {
        val fontSizeAction = createFontSizeNodeAction(
            node,
            fontSizeValue
        )
        val undoFontSizeAction = createFontSizeNodeAction(
            node,
            node.fontSize
        )
        return ActionPair(fontSizeAction, undoFontSizeAction)
    }

    private fun createFontSizeNodeAction(
        node: MindMapNode,
        fontSizeValue: String?
    ): FontSizeNodeAction {
        val fontSizeAction = FontSizeNodeAction()
        fontSizeAction.node = getNodeID(node)
        fontSizeAction.size = fontSizeValue
        return fontSizeAction
    }

    /**
     *
     */
    override fun act(action: XmlAction) {
        if (action is FontSizeNodeAction) {
            val fontSizeAction = action
            val node = getNodeFromID(fontSizeAction.node)
            try {
                val size = Integer.valueOf(fontSizeAction.size).toInt()
                if (node?.fontSize != fontSizeAction.size) {
                    node?.setFontSize(size)
                    exMapFeedback?.nodeChanged(node)
                }
            } catch (e: NumberFormatException) {
                return
            }
        }
    }
}
