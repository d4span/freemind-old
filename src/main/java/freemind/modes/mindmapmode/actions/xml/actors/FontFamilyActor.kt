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

import freemind.controller.actions.generated.instance.FontNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.NodeAdapter
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Font

/**
 * @author foltin
 * @date 26.03.2014
 */
class FontFamilyActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    override fun getDoActionClass(): Class<FontNodeAction> {
        return FontNodeAction::class.java
    }

    /**
     */
    fun setFontFamily(node: MindMapNode, fontFamilyValue: String?) {
        execute(getActionPair(node, fontFamilyValue))
    }

    fun getActionPair(node: MindMapNode, fontFamilyValue: String?): ActionPair {
        val fontFamilyAction = createFontNodeAction(
            node,
            fontFamilyValue
        )
        val undoFontFamilyAction = createFontNodeAction(
            node,
            node.fontFamilyName
        )
        return ActionPair(fontFamilyAction, undoFontFamilyAction)
    }

    private fun createFontNodeAction(
        node: MindMapNode,
        fontValue: String?
    ): FontNodeAction {
        val fontFamilyAction = FontNodeAction()
        fontFamilyAction.node = getNodeID(node)
        fontFamilyAction.font = fontValue
        return fontFamilyAction
    }

    /**
     *
     */
    override fun act(action: XmlAction) {
        if (action is FontNodeAction) {
            val fontFamilyAction = action
            val node = getNodeFromID(fontFamilyAction.node)
            val fontFamily = fontFamilyAction.font
            if (!Tools.safeEquals(node?.fontFamilyName, fontFamily)) {
                (node as NodeAdapter).establishOwnFont()
                node.setFont(
                    exMapFeedback?.getFontThroughMap(
                        Font(
                            fontFamily, node.getFont().style,
                            node
                                .getFont().size
                        )
                    )
                )
                exMapFeedback?.nodeChanged(node)
            }
        }
    }
}
