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
/*$Id: CloudView.java,v 1.1.16.2.12.4 2008/03/06 20:00:07 dpolivaev Exp $*/
package freemind.view.mindmapview

import freemind.modes.MindMapCloud
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon
import java.awt.Shape
import java.awt.Stroke
import java.awt.geom.QuadCurve2D
import java.util.LinkedList

// end Convex Hull
/**
 * This class represents a Cloud around a node.
 */
class CloudView(protected var model: MindMapCloud?, protected var source: NodeView?) {
    /**
     * getIterativeLevel() describes the n-th nested cloud that is to be
     * painted.
     */
    protected val iterativeLevel: Int
        get() = model!!.iterativeLevel

    fun paint(graphics: Graphics) {
        val g = graphics.create() as Graphics2D
        val gstroke = g.create() as Graphics2D
        g.color = color
        /* set a bigger stroke to prevent not filled areas. */g.stroke = stroke
        /* now bold */gstroke.color = exteriorColor
        gstroke.stroke = stroke
        /*
		 * calculate the distances between two points on the convex hull
		 * depending on the getIterativeLevel().
		 */
        var distanceBetweenPoints = 3 * distanceToConvexHull
        if (iterativeLevel > 4) distanceBetweenPoints = 100 * zoom /* flat */
        val distanceToConvexHull = distanceToConvexHull
        /** get coordinates  */
        val coordinates = LinkedList<Point>()
        val hull = ConvexHull()
        source!!.getCoordinates(coordinates)
        // source.getCoordinates(coordinates, (getIterativeLevel()==0)?(int)(5*
        // getZoom()):0 /* = additionalDistanceForConvexHull */);
        val res = hull.calculateHull(coordinates)
        val p = Polygon()
        for (i in res.indices) {
            val pt = res[i] as Point
            p.addPoint(pt.x, pt.y)
        }
        g.fillPolygon(p)
        g.drawPolygon(p)
        /* ok, now the arcs: */
        val lastPoint = Point(res[0] as Point)
        var x0: Double
        var y0: Double
        x0 = lastPoint.x.toDouble()
        y0 = lastPoint.y.toDouble()
        /* close the path: */res.add(res[0])
        var x2: Double
        var y2: Double /* the drawing start points. */
        x2 = x0
        y2 = y0
        for (i in res.indices.reversed()) {
            val nextPoint = Point(res[i] as Point)
            var x1: Double
            var y1: Double
            var x3: Double
            var y3: Double
            var dx: Double
            var dy: Double
            var dxn: Double
            var dyn: Double
            x1 = nextPoint.x.toDouble()
            y1 = nextPoint.y.toDouble()
            dx = x1 - x0 /* direction of p0 -> p1 */
            dy = y1 - y0
            val length = Math.sqrt(dx * dx + dy * dy)
            dxn = dx / length /* normalized direction of p0 -> p1 */
            dyn = dy / length
            if (length > distanceBetweenPoints) {
                var j = 0
                while (j < length / distanceBetweenPoints - 1) {
                    if ((j + 2) * distanceBetweenPoints < length) {
                        x3 = x0 + (j + 1) * distanceBetweenPoints * dxn /*
																		 * the
																		 * drawing
																		 * end
																		 * point
																		 * .
																		 */
                        y3 = y0 + (j + 1) * distanceBetweenPoints * dyn
                    } else {
                        /* last point */
                        x3 = x1
                        y3 = y1
                    }
                    paintClouds(
                        g, gstroke, x2, y2, x3, y3,
                        distanceToConvexHull
                    )
                    x2 = x3
                    y2 = y3
                    ++j
                }
            } else {
                paintClouds(g, gstroke, x2, y2, x1, y1, distanceToConvexHull)
                x2 = x1
                y2 = y1
            }
            x0 = x1
            y0 = y1
        }
        g.dispose()
    }

    private fun paintClouds(
        g: Graphics2D, gstroke: Graphics2D, x0: Double,
        y0: Double, x1: Double, y1: Double, distanceToConvexHull: Double
    ) {
        // System.out.println("double=" + x0+ ", double=" + y0+ ", double=" +
        // x1+ ", double=" + y1);
        val x2: Double
        val y2: Double
        val dx: Double
        val dy: Double
        dx = x1 - x0
        dy = y1 - y0
        val length = Math.sqrt(dx * dx + dy * dy)
        // nothing to do for length zero.
        if (length == 0.0) return
        val dxn: Double
        val dyn: Double
        dxn = dx / length
        dyn = dy / length
        x2 = x0 + .5f * dx - distanceToConvexHull * dyn
        y2 = y0 + .5f * dy + distanceToConvexHull * dxn
        // System.out.println("Line from " + x0+ ", " +y0+ ", " +x2+ ", " +y2+
        // ", " +x1+ ", " +y1+".");
        val shape: Shape = QuadCurve2D.Double(x0, y0, x2, y2, x1, y1)
        g.fill(shape)
        gstroke.draw(shape)
    }

    /* new Color(240,240,240) */ /* selectedColor */
    val color: Color
        get() = model!!.color /* new Color(240,240,240) */ /* selectedColor */
    val exteriorColor: Color
        get() = model!!.exteriorColor
    val stroke: Stroke
        get() {
            val width = width
            return if (width < 1) {
                DEF_STROKE
            } else BasicStroke(
                width.toFloat(), BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER
            )
        }
    val width: Int
        get() = model!!.width

    /**
     * Get the width in pixels rather than in width constant (like -1)
     */
    val realWidth: Int
        get() {
            val width = width
            return if (width < 1) 1 else width
        }
    private val distanceToConvexHull: Double
        get() = 40 / (iterativeLevel + 1) * zoom
    protected val map: MapView
        get() = source!!.map
    protected val zoom: Double
        get() = map.zoom.toDouble()

    companion object {
        val DEF_STROKE: Stroke = BasicStroke(1F)
        private val heightCalculator = CloudView(null, null)

        /** the layout functions can get the additional height of the clouded node .  */
        @JvmStatic
        fun getAdditionalHeigth(
            cloudModel: MindMapCloud?,
            source: NodeView?
        ): Int {
            heightCalculator.model = cloudModel
            heightCalculator.source = source
            return (1.1 * heightCalculator.distanceToConvexHull).toInt()
        }
    }
}