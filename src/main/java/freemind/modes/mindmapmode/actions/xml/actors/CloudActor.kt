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

import freemind.controller.actions.generated.instance.AddCloudXmlAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Tools
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.MindMapCloudModel
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.Color

/**
 * @author foltin
 * @date 26.03.2014
 */
class CloudActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : NodeXmlActorAdapter(pMapFeedback) {
    override fun getDoActionClass(): Class<AddCloudXmlAction> {
        return AddCloudXmlAction::class.java
    }

    override fun apply(model: MindMap, selected: MindMapNode): ActionPair {
        return getActionPair(selected, selected.cloud == null)
    }

    fun setCloud(node: MindMapNode?, enable: Boolean) {
        execute(getActionPair(node, enable))
    }

    private fun getActionPair(selected: MindMapNode?, enable: Boolean): ActionPair {
        val cloudAction = createAddCloudXmlAction(
            selected,
            enable, null
        )
        var undocloudAction: AddCloudXmlAction?
        undocloudAction = if (selected?.cloud != null) {
            createAddCloudXmlAction(
                selected, true,
                selected
                    .cloud?.color
            )
        } else {
            createAddCloudXmlAction(selected, false, null)
        }
        return ActionPair(cloudAction, undocloudAction)
    }

    private fun createAddCloudXmlAction(
        selected: MindMapNode?,
        enable: Boolean,
        color: Color?
    ): AddCloudXmlAction {
        val nodecloudAction = AddCloudXmlAction()
        nodecloudAction.node = getNodeID(selected)
        nodecloudAction.enabled = enable
        nodecloudAction.color = Tools.colorToXml(color)
        return nodecloudAction
    }

    override fun act(action: XmlAction) {
        if (action is AddCloudXmlAction) {
            val nodecloudAction = action
            val node = getNodeFromID(nodecloudAction.node)
            if (node?.cloud == null == nodecloudAction.enabled) {
                if (nodecloudAction.enabled) {
                    if (node?.isRoot ?: false) {
                        return
                    }
                    val cloudModel = MindMapCloudModel(
                        node,
                        exMapFeedback
                    )
                    node?.cloud = cloudModel
                    if (nodecloudAction.color != null) {
                        val color = Tools.xmlToColor(
                            nodecloudAction
                                .color
                        )
                        cloudModel.color = color
                    }
                } else {
                    node?.cloud = null
                }
                exMapFeedback?.nodeChanged(node)
            }
        }
    }
}
