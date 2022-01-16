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
/*$Id: SharpBezierEdgeView.java,v 1.5.34.6 2007/10/25 15:32:59 dpolivaev Exp $*/
package freemind.view.mindmapview

import freemind.main.Tools
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.CubicCurve2D
import java.awt.geom.GeneralPath
import java.awt.geom.Point2D

/**
 * This class represents a sharp Edge of a MindMap.
 */
class SharpBezierEdgeView : // controlpoint
    EdgeView() {
    var line1 = CubicCurve2D.Float()
    var line2 = CubicCurve2D.Float()
    var graph = GeneralPath()
    var one: Point2D.Float? = null
    var two: Point2D.Float? = null
    private var deltaX = 0
    private var deltaY = 0
    private fun update() {
        val zoom = map.getZoom()
        val xctrlRelative = XCTRL * zoom
        // YCTRL could be implemented but then we had to check whether target is
        // above or below source.
        if (target!!.isLeft) {
            one = Point2D.Float(start!!.x - xctrlRelative, start!!.y.toFloat())
            two = Point2D.Float(end!!.x + xctrlRelative, end!!.y.toFloat())
        } else {
            one = Point2D.Float(start!!.x + xctrlRelative, start!!.y.toFloat())
            two = Point2D.Float(end!!.x - xctrlRelative, end!!.y.toFloat())
        }
        val w = (width / 2f + 1) * zoom
        val w2 = w / 2
        line1.setCurve(
            (start!!.x - deltaX).toFloat(), (start!!.y - deltaY).toFloat(), one!!.x - deltaX,
            one!!.y - deltaY, two!!.x, two!!.y - w2, end!!.x.toFloat(), end!!.y.toFloat()
        )
        line2.setCurve(
            end!!.x.toFloat(), end!!.y.toFloat(), two!!.x, two!!.y + w2, one!!.x + deltaX,
            one!!.y +
                deltaY,
            (start!!.x + deltaX).toFloat(), (start!!.y + deltaY).toFloat()
        )
        graph.reset()
        graph.append(line1, true)
        graph.append(line2, true)
        graph.closePath()
    }

    override fun paint(g: Graphics2D?) {
        update()
        g!!.color = color
        g.paint = color
        g.stroke = DEF_STROKE
        g.fill(graph)
        g.draw(graph)
    }

    override val color: Color
        get() = model.color

    override fun createStart() {
        if (source!!.isRoot) {
            start = source!!.getMainViewOutPoint(target, end)
            val mainView = source!!.getMainView()
            val w = (mainView!!.width / 2).toDouble()
            val x0 = start!!.x - w
            val w2 = w * w
            val x02 = x0 * x0
            if (w2 == x02) {
                val delta = map.getZoomed(width / 2 + 1)
                deltaX = 0
                deltaY = delta
            } else {
                val delta = (map.getZoom() * (width / 2 + 1)).toDouble()
                val h = mainView.height / 2
                val y0 = start!!.y - h
                val k = h / w * x0 / Math.sqrt(w2 - x02)
                val dx = delta / Math.sqrt(1 + k * k)
                deltaX = dx.toInt()
                deltaY = (k * dx).toInt()
                if (y0 > 0) {
                    deltaY = -deltaY
                }
            }
            Tools.convertPointToAncestor(mainView, start, source)
        } else {
            val delta = map.getZoomed(width / 2 + 1)
            super.createStart()
            deltaX = 0
            deltaY = delta
        }
    }

    companion object {
        private const val XCTRL = 12f // the distance between endpoint and
    }
}
