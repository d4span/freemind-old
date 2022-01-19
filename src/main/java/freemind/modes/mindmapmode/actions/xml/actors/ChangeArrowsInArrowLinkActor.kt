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

import freemind.controller.actions.generated.instance.ArrowLinkArrowXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ArrowLinkAdapter
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapArrowLink
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 01.04.2014
 */
class ChangeArrowsInArrowLinkActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun changeArrowsOfArrowLink(
        arrowLink: MindMapArrowLink,
        hasStartArrow: Boolean,
        hasEndArrow: Boolean
    ) {
        execute(getActionPair(arrowLink, hasStartArrow, hasEndArrow))
    }

    /**
     */
    private fun getActionPair(
        arrowLink2: MindMapArrowLink,
        hasStartArrow2: Boolean,
        hasEndArrow2: Boolean
    ): ActionPair {
        return ActionPair(
            createArrowLinkArrowXmlAction(
                arrowLink2,
                hasStartArrow2, hasEndArrow2
            ),
            createArrowLinkArrowXmlAction(
                arrowLink2, arrowLink2.startArrow,
                arrowLink2.endArrow
            )
        )
    }

    override fun act(action: XmlAction) {
        if (action is ArrowLinkArrowXmlAction) {
            val arrowAction = action
            val link = linkRegistry!!.getLinkForId(
                arrowAction.id
            )
            (link as ArrowLinkAdapter).startArrow = arrowAction.startArrow
            link.endArrow = arrowAction.endArrow
            exMapFeedback?.nodeChanged(link.getSource())
            exMapFeedback?.nodeChanged(link.getTarget())
        }
    }

    override fun getDoActionClass(): Class<ArrowLinkArrowXmlAction> {
        return ArrowLinkArrowXmlAction::class.java
    }

    private fun createArrowLinkArrowXmlAction(
        arrowLink: MindMapArrowLink,
        hasStartArrow: Boolean,
        hasEndArrow: Boolean
    ): ArrowLinkArrowXmlAction {
        return createArrowLinkArrowXmlAction(
            arrowLink,
            if (hasStartArrow) "Default" else "None", if (hasEndArrow) "Default" else "None"
        )
    }

    private fun createArrowLinkArrowXmlAction(
        arrowLink: MindMapArrowLink,
        hasStartArrow: String,
        hasEndArrow: String
    ): ArrowLinkArrowXmlAction {
        val action = ArrowLinkArrowXmlAction()
        action.startArrow = hasStartArrow
        action.endArrow = hasEndArrow
        action.id = arrowLink.uniqueId
        return action
    }
}
