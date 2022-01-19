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

import freemind.controller.actions.generated.instance.ArrowLinkColorXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.LineAdapter
import freemind.modes.MindMapLink
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Color

/**
 * @author foltin
 * @date 01.04.2014
 */
class ColorArrowLinkActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun setArrowLinkColor(arrowLink: MindMapLink, color: Color) {
        execute(getActionPair(arrowLink, color))
    }

    /**
     */
    private fun getActionPair(arrowLink: MindMapLink, color: Color): ActionPair {
        return ActionPair(
            createArrowLinkColorXmlAction(arrowLink, color),
            createArrowLinkColorXmlAction(arrowLink, arrowLink.color)
        )
    }

    override fun act(action: XmlAction) {
        if (action is ArrowLinkColorXmlAction) {
            val colorAction = action
            val link = linkRegistry!!.getLinkForId(
                colorAction.id
            )
            (link as LineAdapter).color = Tools.xmlToColor(
                colorAction
                    .color
            )
            exMapFeedback?.nodeChanged(link.source)
        }
    }

    override fun getDoActionClass(): Class<ArrowLinkColorXmlAction> {
        return ArrowLinkColorXmlAction::class.java
    }

    private fun createArrowLinkColorXmlAction(
        arrowLink: MindMapLink,
        color: Color
    ): ArrowLinkColorXmlAction {
        val action = ArrowLinkColorXmlAction()
        action.color = Tools.colorToXml(color)
        action.id = arrowLink.uniqueId
        return action
    }
}
