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
// ConvexHull.java (c) fc
// 
// Adapted from Sedgewick, Algorithms
//
package freemind.view.mindmapview

import java.awt.Point
import java.util.Collections
import java.util.LinkedList
import java.util.Vector

class ConvexHull {
    protected fun ccw(p0: Point, p1: Point, p2: Point): Int {
        val dx1: Int
        val dx2: Int
        val dy1: Int
        val dy2: Int
        dx1 = p1.x - p0.x
        dy1 = p1.y - p0.y
        dx2 = p2.x - p0.x
        dy2 = p2.y - p0.y
        val comp = dx1 * dy2 - dy1 * dx2
        if (comp > 0) return 1
        if (comp < 0) return -1
        if (dx1 * dx2 < 0 || dy1 * dy2 < 0) return -1
        return if (dx1 * dx1 + dy1 * dy1 >= dx2 * dx2 + dy2 * dy2) 0 else 1
    }

    protected inner class thetaComparator(p0: Point?) : Comparator<Point> {
        var p0: Point

        init {
            this.p0 = Point(p0)
        }

        /* the < relation. */
        override fun compare(p1: Point, p2: Point): Int {
            val comp = theta(p0, p1) - theta(p0, p2)
            if (p1 == p2) return 0
            if (comp > 0) return 1
            if (comp < 0) return -1
            // here, the points are collinear with p0 (i.e. p0,p1,p2 are on one
            // line). So we reverse the compare relation to get that nearer
            // points are greater.
            // we take the point that is nearer to p0:
            val dx1: Int
            val dx2: Int
            val dy1: Int
            val dy2: Int
            dx1 = p1.x - p0.x
            dy1 = p1.y - p0.y
            dx2 = p2.x - p0.x
            dy2 = p2.y - p0.y
            val comp2 = dx1 * dx1 + dy1 * dy1 - (dx2 * dx2 + dy2 * dy2)
            if (comp2 > 0) return -1
            return if (comp2 < 0) 1 else 0
        }

        fun theta(p1: Point, p2: Point): Double {
            val dx: Int
            val dy: Int
            val ax: Int
            val ay: Int
            var t: Double
            dx = p2.x - p1.x
            ax = Math.abs(dx)
            dy = p2.y - p1.y
            ay = Math.abs(dy)
            t = if (dx == 0 && dy == 0) 0.0 else dy.toDouble() / (ax + ay).toDouble()
            if (dx < 0) t = 2f - t else {
                if (dy < 0) t = 4f + t
            }
            return t * 90f
        }
    }

    fun doGraham(p: Vector<Point>): Vector<Point> {
        var i: Int
        var min: Int
        var M: Int
        var t: Point
        min = 0
        // search for minimum:
        i = 1
        while (i < p.size) {
            if ((p[i] as Point).y < (p[min] as Point).y) min = i
            ++i
        }
        // continue along the values with same y component
        i = 0
        while (i < p.size) {
            if ((p[i] as Point).y == (p[min] as Point).y
                && (p[i] as Point).x > (p[min] as Point).x
            ) {
                min = i
            }
            ++i
        }
        // swap:
        t = p[0] as Point
        p[0] = p[min]
        p[min] = t
        val comp = thetaComparator(p[0] as Point)
        Collections.sort(p, comp)
        p.add(0, Point(p[p.size - 1] as Point)) // the first is the
        // last.
        M = 3
        i = 4
        while (i < p.size) {
            while (ccw(p[M] as Point, p[M - 1] as Point, p[i] as Point) >= 0) {
                M--
            }
            M++
            // swap:
            t = p[M] as Point
            p[M] = p[i]
            p[i] = t
            ++i
        }
        p.removeAt(0)
        p.setSize(M)
        return p
    }

    fun calculateHull(coordinates: LinkedList<Point>): Vector<Point> {
        val q = Vector<Point>()
        val coordinates_it: ListIterator<Point> = coordinates.listIterator()
        while (coordinates_it.hasNext()) {
            q.add(coordinates_it.next())
        }
        return doGraham(q)
    }
}