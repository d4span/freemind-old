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
/*$Id: MindMapLayout.java,v 1.15.14.5.4.12 2007/04/21 15:11:23 dpolivaev Exp $*/
package freemind.view.mindmapview

import freemind.main.Resources
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager
import java.util.logging.Logger

/**
 * This class will Layout the Nodes and Edges of an MapView.
 */
class MindMapLayout : LayoutManager {
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    override fun addLayoutComponent(name: String, comp: Component) {}
    override fun removeLayoutComponent(comp: Component) {}
    override fun layoutContainer(c: Container) {
        val mapView = c as MapView
        val calcXBorderSize = calcXBorderSize(mapView)
        val calcYBorderSize = calcYBorderSize(mapView)
        getRoot(mapView)!!.validate()
        getRoot(mapView)!!.setLocation(calcXBorderSize, calcYBorderSize)
        mapView.setSize(
            calcXBorderSize * 2 + getRoot(mapView)!!.width,
            calcYBorderSize * 2 + getRoot(mapView)!!.height
        )
        val componentCount = mapView.componentCount
        for (i in 0 until componentCount) {
            val component = mapView.getComponent(i)
            if (!component.isValid) {
                component.validate()
            }
        }
    }

    //
    // Absolute positioning
    //
    //
    // Get Methods
    //
    private fun getRoot(c: Container): NodeView? {
        return (c as MapView).root
    }

    // This is actually never used.
    override fun minimumLayoutSize(parent: Container): Dimension {
        return Dimension(200, 200)
    } // For testing Purposes

    override fun preferredLayoutSize(c: Container): Dimension {
        val mapView = c as MapView
        val preferredSize = mapView.root!!.preferredSize
        return Dimension(
            2 * calcXBorderSize(mapView) + preferredSize.width,
            2 *
                calcYBorderSize(mapView) + preferredSize.height
        )
    }

    /**
     * @param map
     * TODO
     */
    private fun calcYBorderSize(map: MapView): Int {
        val yBorderSize: Int
        val minBorderHeight = map.getZoomed(BORDER)
        val visibleSize = map.viewportSize
        yBorderSize = if (visibleSize != null) {
            Math.max(visibleSize.height, minBorderHeight)
        } else {
            minBorderHeight
        }
        return yBorderSize
    }

    private fun calcXBorderSize(map: MapView): Int {
        val xBorderSize: Int
        val visibleSize = map.viewportSize
        val minBorderWidth = map.getZoomed(
            BORDER +
                MINIMAL_LEAF_WIDTH
        )
        xBorderSize = if (visibleSize != null) {
            Math.max(visibleSize.width, minBorderWidth)
        } else {
            minBorderWidth
        }
        return xBorderSize
    }

    companion object {
        const val BORDER = 30 // width of the border around the map.

        // minimal width for input field of leaf or folded node (PN)
        // the MINIMAL_LEAF_WIDTH is reserved by calculation of the map width
        const val MINIMAL_LEAF_WIDTH = 150
        protected var logger: Logger? = null
    }
} // class MindMapLayout
