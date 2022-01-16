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
/*$Id: EdgeView.java,v 1.13.14.2.4.9 2008/06/09 21:01:15 dpolivaev Exp $*/
package freemind.view.mindmapview

import freemind.main.Tools
import freemind.modes.EdgeAdapter
import freemind.modes.MindMapEdge
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Stroke

/**
 * This class represents a single Edge of a MindMap.
 */
abstract class EdgeView {
    /**
     * @return Returns the target.
     */
    protected var target: NodeView? = null
        private set

    /**
     * @return Returns the source.
     */
    @JvmField
    protected var source: NodeView? = null
    @JvmField
    protected var start: Point? = null
    @JvmField
    protected var end: Point? = null

    /**
     * This should be a task of MindMapLayout start,end must be initialized...
     *
     * @param target
     * TODO
     */
    fun paint(target: NodeView, g: Graphics2D?) {
        source = target.visibleParentView
        this.target = target
        createEnd()
        createStart()
        paint(g)
        source = null
        this.target = null
    }

    protected fun createEnd() {
        end = target!!.mainViewInPoint
        Tools.convertPointToAncestor(target!!.getMainView(), end, source)
    }

    protected open fun createStart() {
        start = source!!.getMainViewOutPoint(target, end)
        Tools.convertPointToAncestor(source!!.getMainView(), start, source)
    }

    protected abstract fun paint(g: Graphics2D?)
    protected fun reset() {
        source = null
        target = null
    }

    abstract val color: Color?
    val stroke: Stroke
        get() {
            val width = width
            return if (width == EdgeAdapter.WIDTH_THIN) {
                DEF_STROKE
            } else BasicStroke(
                width * map.getZoom(),
                BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER
            )
        }
    val width: Int
        get() = model.width
    protected val model: MindMapEdge
        get() = target!!.model.edge
    protected val map: MapView
        get() = target!!.map
    protected val isTargetEclipsed: Boolean
        get() = target!!.isParentHidden

    companion object {
        @JvmField
        val DEF_STROKE = BasicStroke()
        var ECLIPSED_STROKE: Stroke? = null
        @JvmStatic
        protected val eclipsedStroke: Stroke?
            get() {
                if (ECLIPSED_STROKE == null) {
                    val dash = floatArrayOf(3.0f, 9.0f)
                    ECLIPSED_STROKE = BasicStroke(
                        3.0f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 12.0f, dash, 0.0f
                    )
                }
                return ECLIPSED_STROKE
            }
    }
}
