/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on 05.06.2005
 *
 */
package freemind.view.mindmapview

import freemind.main.Tools
import java.awt.Point

/**
 * @author dimitri 05.06.2005
 */
class RightNodeViewLayout : NodeViewLayoutAdapter() {
    override fun layout() {
        val contentHeight = getChildContentHeight(false)
        var childVerticalShift = getChildVerticalShift(false)
        val childHorizontalShift = childHorizontalShift
        if (view!!.isContentVisible) {
            content!!.isVisible = true
            val contentPreferredSize = content?.getPreferredSize()
            val x = Math.max(
                spaceAround,
                0.minus(contentPreferredSize?.width?.minus(childHorizontalShift) ?: 0)
            )
            childVerticalShift += (contentPreferredSize?.height?.minus(contentHeight))?.div(2) ?: 0
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(
                x, y, contentPreferredSize?.width ?: 0,
                contentPreferredSize?.height ?: 0
            )
        } else {
            content!!.isVisible = false
            val x = Math.max(spaceAround, -childHorizontalShift)
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(x, y, 0, contentHeight)
        }
        placeRightChildren(childVerticalShift)
    }

    override fun layoutNodeMotionListenerView(view: NodeMotionListenerView?) {
        val nodeView = view!!.movedView
        val content = nodeView.content
        location.x = -LISTENER_VIEW_WIDTH
        location.y = 0
        Tools.convertPointToAncestor(content, location, view.parent)
        view.location = location
        view.setSize(LISTENER_VIEW_WIDTH, content!!.height)
    }

    override fun getMainViewOutPoint(
        view: NodeView?,
        targetView: NodeView?,
        destinationPoint: Point?
    ): Point? {
        val mainView = view!!.getMainView()
        return mainView!!.rightPoint
    }

    override fun getMainViewInPoint(view: NodeView?): Point? {
        val mainView = view!!.getMainView()
        return mainView!!.leftPoint
    }

    companion object {
        var instance: RightNodeViewLayout? = null
            get() {
                if (field == null) field = RightNodeViewLayout()
                return field
            }
            private set
    }
}
