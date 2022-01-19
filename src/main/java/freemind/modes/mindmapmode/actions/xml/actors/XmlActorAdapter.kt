/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

import freemind.main.Resources
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapLinkRegistry
import freemind.modes.MindMapNode
import freemind.modes.NodeAdapter
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.modes.mindmapmode.actions.xml.ActorXml
import freemind.view.mindmapview.ViewFeedback
import java.util.logging.Logger

/**
 * @author foltin
 * @date 16.03.2014
 */
abstract class XmlActorAdapter(
    /**
     * @return the mapFeedback
     */
    var exMapFeedback: ExtendedMapFeedback?
) : ActorXml {
    /**
     *
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        addActor(this)
    }

    /**
     * @return
     */
    @get:Deprecated(
        """replaced by {@link XmlActorAdapter#getExMapFeedback()}
	  """
    )
    protected val modeController: MindMapController
        get() = exMapFeedback as MindMapController
    val viewFeedback: ViewFeedback?
        get() = exMapFeedback?.viewFeedback

    /**
     * @param pActionPair
     */
    protected fun execute(pActionPair: ActionPair?) {
        exMapFeedback?.doTransaction(doActionClass.name, pActionPair)
    }

    /**
     * @param pNodeId
     * @return
     */
    protected fun getNodeFromID(pNodeId: String?): NodeAdapter? {
        return exMapFeedback?.getNodeFromID(pNodeId)
    }

    /**
     * @return
     */
    protected val selected: MindMapNode?
        get() = exMapFeedback?.selected

    /**
     * @param pSelected
     * @return
     */
    protected fun getNodeID(pNode: MindMapNode?): String? {
        return exMapFeedback?.getNodeID(pNode)
    }

    protected fun addActor(actor: ActorXml?) {
        if (actor != null) {
            // registration:
            exMapFeedback?.actionRegistry?.registerActor(
                actor,
                actor.doActionClass
            )
        }
    }

    protected val xmlActorFactory: XmlActorFactory?
        get() = exMapFeedback?.actorFactory

    /**
     */
    protected open val linkRegistry: MindMapLinkRegistry?
        get() = exMapFeedback?.map?.linkRegistry

    companion object {
        @JvmField
        var logger: Logger? = null
    }
}
