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

import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.MindMapArrowLinkModel
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 * @date 27.03.2014
 */
class AddArrowLinkActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback) {
    override fun act(action: XmlAction) {
        if (action is AddArrowLinkXmlAction) {
            val arrowAction = action
            val source = getNodeFromID(arrowAction.node)
            val target = getNodeFromID(arrowAction.destination)
            if (source === target) {
                logger?.warning("Can't create link between itself. ($source).")
                return
            }
            val proposedId = arrowAction.newId
            if (linkRegistry?.getLabel(target) == null) {
                // call registry to give new label
                linkRegistry?.registerLinkTarget(target)
            }
            val linkModel = MindMapArrowLinkModel(
                source,
                target, exMapFeedback
            )
            linkModel.destinationLabel = linkRegistry?.getLabel(target)
            // give label:
            linkModel.uniqueId = linkRegistry?.generateUniqueLinkId(
                proposedId
            )
            // check for other attributes:
            if (arrowAction.color != null) {
                linkModel.color = Tools.xmlToColor(arrowAction.color)
            }
            if (arrowAction.endArrow != null) {
                linkModel.endArrow = arrowAction.endArrow
            }
            if (arrowAction.endInclination != null) {
                linkModel.endInclination = Tools.xmlToPoint(
                    arrowAction
                        .endInclination
                )
            }
            if (arrowAction.startArrow != null) {
                linkModel.startArrow = arrowAction.startArrow
            }
            if (arrowAction.startInclination != null) {
                linkModel.startInclination = Tools.xmlToPoint(
                    arrowAction
                        .startInclination
                )
            }
            // register link.
            linkRegistry?.registerLink(linkModel)
            exMapFeedback?.nodeChanged(target)
            exMapFeedback?.nodeChanged(source)
        }
    }

    override fun getDoActionClass(): Class<AddArrowLinkXmlAction> {
        return AddArrowLinkXmlAction::class.java
    }

    private fun getActionPair(source: MindMapNode, target: MindMapNode): ActionPair {
        val doAction = createAddArrowLinkXmlAction(
            source,
            target, linkRegistry?.generateUniqueLinkId(null)
        )
        // now, the id is clear:
        val undoAction = exMapFeedback
            ?.actorFactory?.removeArrowLinkActor
            ?.createRemoveArrowLinkXmlAction(doAction.newId)
        return ActionPair(doAction, undoAction)
    }

    fun createAddArrowLinkXmlAction(
        source: MindMapNode?,
        target: MindMapNode?,
        proposedID: String?
    ): AddArrowLinkXmlAction {
        val action = AddArrowLinkXmlAction()
        action.node = getNodeID(source)
        action.destination = getNodeID(target)
        action.newId = proposedID
        return action
    }

    /**
     * Source holds the MindMapArrowLinkModel and points to the id placed in
     * target.
     */
    fun addLink(source: MindMapNode, target: MindMapNode) {
        execute(getActionPair(source, target))
    }
}
