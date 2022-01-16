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

import freemind.main.Resources
import freemind.main.Tools
import java.awt.Dimension
import java.awt.Point

/**
 * Root layout.
 * @author dimitri 05.06.2005
 */
class VerticalRootNodeViewLayout : NodeViewLayoutAdapter() {
    override fun layout() {
        val rightContentHeight = getChildContentHeight(false)
        var rightChildVerticalShift = getChildVerticalShift(false)
        val leftContentHeight = getChildContentHeight(true)
        var leftChildVerticalShift = getChildVerticalShift(true)
        val childHorizontalShift = childHorizontalShift
        val contentHeight = Math.max(
            rightContentHeight,
            leftContentHeight
        )
        val x = Math.max(spaceAround, -childHorizontalShift)
        if (view!!.isContentVisible) {
            content!!.isVisible = true
            val contentPreferredSize = content?.getPreferredSize() ?: Dimension(0, 0)
            rightChildVerticalShift += (contentPreferredSize.height - rightContentHeight) / 2
            leftChildVerticalShift += (contentPreferredSize.height - leftContentHeight) / 2
            val childVerticalShift = Math.min(
                rightChildVerticalShift,
                leftChildVerticalShift
            )
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(
                x, y, contentPreferredSize.width,
                contentPreferredSize.height
            )
        } else {
            content!!.isVisible = false
            val childVerticalShift = Math.min(
                rightChildVerticalShift,
                leftChildVerticalShift
            )
            val y = Math.max(spaceAround, -childVerticalShift)
            content!!.setBounds(x, y, 0, contentHeight)
        }
        placeLeftChildren(leftChildVerticalShift)
        val width1 = view!!.width
        val height1 = view!!.height
        placeRightChildren(rightChildVerticalShift)
        val width2 = view!!.width
        val height2 = view!!.height
        view!!.setSize(Math.max(width1, width2), Math.max(height1, height2))
    }

    override fun layoutNodeMotionListenerView(view: NodeMotionListenerView?) {
        // there is no move handle at root.
    }

    override fun getMainViewOutPoint(
        view: NodeView?,
        targetView: NodeView?,
        destinationPoint: Point?
    ): Point? {
        val mainView = view!!.getMainView()
        if (USE_COMMON_OUT_POINT_FOR_ROOT_NODE) {
            return if (targetView!!.isLeft) {
                mainView!!.leftPoint
            } else {
                mainView!!.rightPoint
            }
        }
        val p = Point(destinationPoint)
        Tools.convertPointFromAncestor(view, p, mainView)
        val nWidth = (mainView!!.width / 2f).toDouble()
        val nHeight = (mainView.height / 2f).toDouble()
        val centerPoint = Point(nWidth.toInt(), nHeight.toInt())
        // assume, that destinationPoint is on the right:
        var angle = Math.atan(
            (
                (p.y - centerPoint.y + 0f) /
                    (p.x - centerPoint.x + 0f)
                ).toDouble()
        )
        if (p.x < centerPoint.x) {
            angle += Math.PI
        }
        // now determine point on ellipsis corresponding to that angle:
        return Point(
            centerPoint.x +
                (Math.cos(angle) * nWidth).toInt(),
            centerPoint.y +
                (Math.sin(angle) * nHeight).toInt()
        )
    }

    override fun getMainViewInPoint(view: NodeView?): Point? {
        return view!!.getMainView()!!.centerPoint
    }

    companion object {
        private const val USE_COMMON_OUT_POINT_FOR_ROOT_NODE_STRING = "use_common_out_point_for_root_node"
        var USE_COMMON_OUT_POINT_FOR_ROOT_NODE = Resources.getInstance()
            .getBoolProperty(USE_COMMON_OUT_POINT_FOR_ROOT_NODE_STRING)
        var instance: VerticalRootNodeViewLayout? = null
            get() {
                if (field == null) field = VerticalRootNodeViewLayout()
                return field
            }
            private set
    }
}
