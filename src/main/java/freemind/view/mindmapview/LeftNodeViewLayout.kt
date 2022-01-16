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
class LeftNodeViewLayout : NodeViewLayoutAdapter() {
    override fun layout() {
        val contentHeight = getChildContentHeight(true)
        var childVerticalShift = getChildVerticalShift(true)
        val childHorizontalShift = childHorizontalShift
        val x = Math.max(spaceAround, -childHorizontalShift)
        if (view!!.isContentVisible) {
            content!!.isVisible = true
            val contentPreferredSize = content?.getPreferredSize()
            childVerticalShift += (contentPreferredSize?.height?.minus(contentHeight))?.div(2) ?: 0
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(
                x, y, contentPreferredSize?.width ?: 0,
                contentPreferredSize?.height ?: 0
            )
        } else {
            content!!.isVisible = false
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(x, y, 0, contentHeight)
        }
        placeLeftChildren(childVerticalShift)
    }

    override fun layoutNodeMotionListenerView(view: NodeMotionListenerView?) {
        val nodeView = view!!.movedView
        val content = nodeView.content
        location.x = content?.width ?: 0
        location.y = 0
        Tools.convertPointToAncestor(content, location, view.parent)
        view.location = location
        view.setSize(LISTENER_VIEW_WIDTH, content?.height ?: 0)
    }

    override fun getMainViewOutPoint(
        view: NodeView?,
        targetView: NodeView?,
        destinationPoint: Point?
    ): Point? {
        val mainView = view!!.getMainView()
        return mainView?.leftPoint
    }

    override fun getMainViewInPoint(view: NodeView?): Point? {
        val mainView = view!!.getMainView()
        return mainView?.rightPoint
    }

    companion object {
        @JvmStatic
        var instance: LeftNodeViewLayout? = null
            get() {
                if (field == null) field = LeftNodeViewLayout()
                return field
            }
            private set
    }
}
