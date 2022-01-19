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

import freemind.controller.actions.generated.instance.ArrowLinkPointXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapArrowLink
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Point

/**
 * @author foltin
 * @date 01.04.2014
 */
class ChangeArrowLinkEndPointsActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    fun setArrowLinkEndPoints(
        link: MindMapArrowLink,
        startPoint: Point,
        endPoint: Point
    ) {
        execute(getActionPair(link, startPoint, endPoint))
    }

    /**
     */
    private fun getActionPair(
        link: MindMapArrowLink,
        startPoint: Point,
        endPoint: Point
    ): ActionPair {
        return ActionPair(
            createArrowLinkPointXmlAction(
                link, startPoint,
                endPoint
            ),
            createArrowLinkPointXmlAction(
                link,
                link.startInclination, link.endInclination
            )
        )
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction) {
        if (action is ArrowLinkPointXmlAction) {
            val pointAction = action
            val link = linkRegistry?.getLinkForId(pointAction.id) as MindMapArrowLink
            link.startInclination = Tools.xmlToPoint(
                pointAction
                    .startPoint
            )
            link.endInclination = Tools.xmlToPoint(pointAction.endPoint)
            exMapFeedback?.nodeChanged(link.source)
            exMapFeedback?.nodeChanged(link.target)
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<ArrowLinkPointXmlAction> {
        return ArrowLinkPointXmlAction::class.java
    }

    private fun createArrowLinkPointXmlAction(
        arrowLink: MindMapArrowLink,
        startPoint: Point,
        endPoint: Point
    ): ArrowLinkPointXmlAction {
        val action = ArrowLinkPointXmlAction()
        action.startPoint = Tools.PointToXml(startPoint)
        action.endPoint = Tools.PointToXml(endPoint)
        action.id = arrowLink.uniqueId
        return action
    }
}
