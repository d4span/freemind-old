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
/*$Id: LinearEdgeView.java,v 1.9.30.5 2008/06/09 21:01:16 dpolivaev Exp $*/
package freemind.view.mindmapview

import java.awt.Color
import java.awt.Graphics2D

/**
 * This class represents a single Edge of a MindMap.
 */
class LinearEdgeView : EdgeView() {
    override fun paint(g: Graphics2D?) {
        val color = color
        g!!.color = color
        val stroke = stroke
        g.stroke = stroke
        val w = width
        if (w <= 1) {
            g.drawLine(start!!.x, start!!.y, end!!.x, end!!.y)
            if (isTargetEclipsed) {
                g.color = g.background
                g.stroke = eclipsedStroke
                g.drawLine(start!!.x, start!!.y, end!!.x, end!!.y)
                g.color = color
                g.stroke = stroke
            }
        } else {
            // a little horizontal part because of line cap
            var dx = w / 3 + 1
            if (target!!.isLeft) dx = -dx
            val xs = intArrayOf(start!!.x, start!!.x + dx, end!!.x - dx, end!!.x)
            val ys = intArrayOf(start!!.y, start!!.y, end!!.y, end!!.y)
            g.drawPolyline(xs, ys, 4)
            if (isTargetEclipsed) {
                g.color = g.background
                g.stroke = eclipsedStroke
                g.drawPolyline(xs, ys, 4)
                g.color = color
                g.stroke = stroke
            }
        }
    }

    override val color: Color
        get() = model.color
}
