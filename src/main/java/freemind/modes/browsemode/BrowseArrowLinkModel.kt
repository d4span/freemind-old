/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: BrowseArrowLinkModel.java,v 1.1.18.1.16.1 2007/04/21 15:11:21 dpolivaev Exp $*/
package freemind.modes.browsemode

import freemind.main.XMLElement
import freemind.modes.ArrowLinkAdapter
import freemind.modes.MapFeedback
import freemind.modes.MindMapNode
import freemind.view.mindmapview.MapView

class BrowseArrowLinkModel(
    source: MindMapNode?,
    target: MindMapNode?,
    pMapFeedback: MapFeedback?
) : ArrowLinkAdapter(source, target, pMapFeedback) {
    /* maybe this method is wrong here, but ... */
    override fun clone(): Any {
        return super.clone()
    }

    override fun save(): XMLElement? {
        return null
    }

    override fun toString(): String {
        return "Source=" + source + ", target=" + getTarget()
    }

    /**
     * @see freemind.modes.MindMapArrowLink.changeInclination
     */
    override fun changeInclination(
        map: MapView,
        oldX: Int,
        oldY: Int,
        deltaX: Int,
        deltaY: Int
    ) {
    }
}
