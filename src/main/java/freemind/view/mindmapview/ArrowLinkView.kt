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
/*$Id: ArrowLinkView.java,v 1.8.14.4.4.6 2008/06/08 14:00:32 dpolivaev Exp $*/
package freemind.view.mindmapview

import freemind.modes.MindMapArrowLink
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon
import java.awt.Rectangle
import java.awt.Stroke
import java.awt.geom.CubicCurve2D
import java.awt.geom.FlatteningPathIterator
import java.awt.geom.PathIterator
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

// end Convex Hull
/**
 * This class represents a ArrowLink around a node.
 */
class ArrowLinkView     /* Note, that source and target are nodeviews and not nodemodels!. */(
    /**
     * fc: This getter is public, because the view gets the model by click on
     * the curve.
     */
    var model: MindMapArrowLink,
    protected var source: NodeView?,
    protected var target: NodeView?
) {
    protected var iterativeLevel = 0
    @JvmField
	var arrowLinkCurve: CubicCurve2D? = null
    val bounds: Rectangle
        get() = if (arrowLinkCurve == null) Rectangle() else arrowLinkCurve!!.bounds

    /**
     * \param iterativeLevel describes the n-th nested arrowLink that is to be
     * painted.
     */
    fun paint(graphics: Graphics) {
        if (!isSourceVisible && !isTargetVisible) return
        var p1: Point? = null
        var p2: Point? = null
        var p3: Point? = null
        var p4: Point? = null
        var targetIsLeft = false
        var sourceIsLeft = false
        val g = graphics.create() as Graphics2D
        g.color = color
        /* set stroke. */g.stroke = stroke
        // if one of the nodes is not present then draw a dashed line:
        if (!isSourceVisible || !isTargetVisible) g.stroke = BasicStroke(
            width.toFloat(), BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND, 0F, floatArrayOf(0f, 3f, 0f, 3f), 0F
        )

        // determine, whether destination exists:
        if (isSourceVisible) {
            p1 = source!!.getLinkPoint(model.startInclination)
            sourceIsLeft = source!!.isLeft
        }
        if (isTargetVisible) {
            p2 = target!!.getLinkPoint(model.endInclination)
            targetIsLeft = target!!.isLeft
        }
        // determine point 2 and 3:
        if (model.endInclination == null
            || model.startInclination == null
        ) {
            val dellength: Double? = if (isSourceVisible && isTargetVisible) p1
                ?.distance(p2)?.div(zoom) else 30.0
            if (isSourceVisible
                && model.startInclination == null
            ) {
                val incl = calcInclination(dellength)
                model.startInclination = incl
                p1 = source!!.getLinkPoint(model.startInclination)
            }
            if (isTargetVisible && model.endInclination == null) {
                val incl = calcInclination(dellength)
                incl.y = -incl.y
                model.endInclination = incl
                p2 = target!!.getLinkPoint(model.endInclination)
            }
        }
        arrowLinkCurve = CubicCurve2D.Double()
        if (p1 != null) {
            p3 = Point(p1)
            p3.translate(
                (if (sourceIsLeft) -1 else 1)
                        * map.getZoomed(
                    model.startInclination.x
                ),
                map.getZoomed(model.startInclination.y)
            )
            if (p2 == null) {
                arrowLinkCurve?.setCurve(p1, p3, p1, p3)
            }
        }
        if (p2 != null) {
            p4 = Point(p2)
            p4.translate(
                (if (targetIsLeft) -1 else 1)
                        * map.getZoomed(
                    model.endInclination.x
                ),
                map.getZoomed(model.endInclination.y)
            )
            if (p1 == null) {
                arrowLinkCurve?.setCurve(p2, p4, p2, p4)
            }
        }
        if (p1 != null && p2 != null) {
            arrowLinkCurve?.setCurve(p1, p3, p4, p2)
            g.draw(arrowLinkCurve)
            // arrow source:
        }
        if (isSourceVisible && model.startArrow != "None") {
            paintArrow(p1, p3, g)
        }
        // arrow target:
        if (isTargetVisible && model.endArrow != "None") {
            paintArrow(p2, p4, g)
        }
        // Control Points
        if (model.showControlPointsFlag || !isSourceVisible
            || !isTargetVisible
        ) {
            g.stroke = BasicStroke(
                width.toFloat(), BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 0F, floatArrayOf(0f, 3f, 0f, 3f), 0F
            )
            if (p1 != null) {
                g.drawLine(p1.x, p1.y, p3!!.x, p3.y)
            }
            if (p2 != null) {
                g.drawLine(p2.x, p2.y, p4!!.x, p4.y)
            }
        }
    }

    /**
     */
    private val isTargetVisible: Boolean
        get() = target != null && target!!.isContentVisible

    /**
     */
    private val isSourceVisible: Boolean
        get() = source != null && source!!.isContentVisible

    /**
     */
    private fun calcInclination(dellength: Double?): Point {
        /*
		 * int w = node.getWidth(); int h = node.getHeight(); double r =
		 * Math.sqrt(w*w+h*h); double wr = dellength * w / r; double hr =
		 * dellength * h / r; return new Point((int)wr, (int)hr);
		 */
        return Point(dellength?.toInt() ?: 0, 0)
    }

    /**
     * @param p1
     * is the start point
     * @param p3
     * is the another point indicating the direction of the arrow.
     */
    private fun paintArrow(p1: Point?, p3: Point?, g: Graphics2D) {
        val dx: Double
        val dy: Double
        val dxn: Double
        val dyn: Double
        dx = (p3!!.x - p1!!.x).toDouble() /* direction of p1 -> p3 */
        dy = (p3.y - p1.y).toDouble()
        val length = Math.sqrt(dx * dx + dy * dy) / (zoom * 10 /*
																	 * =zoom
																	 * factor
																	 * for
																	 * arrows
																	 */)
        dxn = dx / length /* normalized direction of p1 -> p3 */
        dyn = dy / length
        // suggestion of daniel to have arrows that are not so wide open. fc,
        // 7.12.2003.
        val width = .5
        val p = Polygon()
        p.addPoint(p1.x, p1.y)
        p.addPoint(
            (p1.x + dxn + width * dyn).toInt(), (p1.y + dyn - width
                    * dxn).toInt()
        )
        p.addPoint(
            (p1.x + dxn - width * dyn).toInt(), (p1.y + dyn + (width
                    * dxn)).toInt()
        )
        p.addPoint(p1.x, p1.y)
        g.fillPolygon(p)
    }

    /** MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION describes itself.  */
    private val MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION = 16

    /**
     * Determines, whether or not a given point p is in an epsilon-neighbourhood
     * for the cubic curve.
     */
    fun detectCollision(p: Point): Boolean {
        if (arrowLinkCurve == null) return false
        val rec = getControlPoint(p)
        // flatten the curve and test for intersection (bug fix, fc, 16.1.2004).
        val pi = FlatteningPathIterator(
            arrowLinkCurve!!.getPathIterator(null),
            (MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION / 4).toDouble(), 10 /*
																	 * =maximal
																	 * 2^10=1024
																	 * points.
																	 */
        )
        var oldCoordinateX = 0.0
        var oldCoordinateY = 0.0
        while (pi.isDone == false) {
            val coordinates = DoubleArray(6)
            val type = pi.currentSegment(coordinates)
            when (type) {
                PathIterator.SEG_LINETO -> {
                    if (rec.intersectsLine(
                            oldCoordinateX, oldCoordinateY,
                            coordinates[0], coordinates[1]
                        )
                    ) return true
                    oldCoordinateX = coordinates[0]
                    oldCoordinateY = coordinates[1]
                }
                PathIterator.SEG_MOVETO -> {
                    oldCoordinateX = coordinates[0]
                    oldCoordinateY = coordinates[1]
                }
                PathIterator.SEG_QUADTO, PathIterator.SEG_CUBICTO, PathIterator.SEG_CLOSE -> {}
                else -> {}
            }
            pi.next()
        }
        return false
    }

    protected fun getControlPoint(p: Point2D): Rectangle2D {
        // Create a small square around the given point.
        val side = MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION
        return Rectangle2D.Double(
            p.x - side / 2, p.y - side / 2,
            side.toDouble(), side.toDouble()
        )
    }

    /* new Color(240,240,240) */ /* selectedColor */
    val color: Color
        get() = model.color /* new Color(240,240,240) */ /* selectedColor */
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
        get() = model.width

    /**
     * Get the width in pixels rather than in width constant (like -1)
     */
    val realWidth: Int
        get() {
            val width = width
            return if (width < 1) 1 else width
        }
    protected val map: MapView
        get() = if (source == null) target!!.map else source!!.map
    protected val zoom: Double
        get() = map.zoom.toDouble()

    /**
     */
//    fun changeInclination(originX: Int, originY: Int, newX: Int, newY: Int) {
//        // TODO Auto-generated method stub
//    }

    companion object {
        val DEF_STROKE: Stroke = BasicStroke(1F)
    }
}