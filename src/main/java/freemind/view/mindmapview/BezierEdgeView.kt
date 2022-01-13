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
/*$Id: BezierEdgeView.java,v 1.13.18.1.2.6 2008/06/09 21:01:15 dpolivaev Exp $*/
package freemind.view.mindmapview

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.CubicCurve2D

/**
 * This class represents a single Edge of a MindMap.
 */
class BezierEdgeView : EdgeView() {
    var graph = CubicCurve2D.Float()
    private fun update() {

        // YCTRL could be implemented but then we had to check whether target is
        // above or below source.
        val sign = if (target!!.isLeft) -1 else 1
        var sourceSign = 1
        if (source!!.isRoot
            && !VerticalRootNodeViewLayout.USE_COMMON_OUT_POINT_FOR_ROOT_NODE
        ) {
            sourceSign = 0
        }
        val xctrl = map.getZoomed(sourceSign * sign * XCTRL)
        val childXctrl = map.getZoomed(-1 * sign * CHILD_XCTRL)
        graph.setCurve(
            start!!.x.toFloat(), start!!.y.toFloat(), (start!!.x + xctrl).toFloat(), start!!.y.toFloat(), (end!!.x
                    + childXctrl).toFloat(), end!!.y.toFloat(), end!!.x.toFloat(), end!!.y.toFloat()
        )
    }

    override fun paint(g: Graphics2D?) {
        update()
        val color = color
        g!!.color = color
        val stroke = stroke
        g.stroke = stroke
        g.draw(graph)
        if (isTargetEclipsed) {
            g.color = g.background
            g.stroke = eclipsedStroke
            g.draw(graph)
            g.stroke = stroke
            g.color = color
        }
    }

    override val color: Color
        get() = model.color

    companion object {
        private const val XCTRL = 12 // the distance between endpoint and

        // controlpoint
        private const val CHILD_XCTRL = 20 // -||- at the child's end
    }
}