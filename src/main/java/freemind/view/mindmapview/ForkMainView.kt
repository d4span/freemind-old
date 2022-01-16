/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.view.mindmapview

import freemind.main.Tools
import freemind.modes.EdgeAdapter
import freemind.modes.MindMapNode
import java.awt.BasicStroke
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point

internal class ForkMainView : MainView() {
    override fun paint(g: Graphics) {
        val graphics2D = g as Graphics2D
        val nodeView = nodeView
        val model = nodeView.model
        val renderingHint = nodeView.map.setEdgesRenderingHint(graphics2D)
        paintSelected(graphics2D)
        paintDragOver(graphics2D)
        val edgeWidth = edgeWidth
        val oldColor = graphics2D.color
        graphics2D.stroke = BasicStroke(edgeWidth.toFloat())
        // Draw a standard node
        graphics2D.color = model.edge.color
        graphics2D.drawLine(
            0, height - edgeWidth / 2 - 1, width,
            height -
                edgeWidth / 2 - 1
        )
        graphics2D.color = oldColor
        Tools.restoreAntialiasing(graphics2D, renderingHint)
        super.paint(graphics2D)
    }

    override val mainViewWidthWithFoldingMark: Int
        get() {
            var width = width
            if (nodeView.model.isFolded) {
                width += (
                    zoomedFoldingSymbolHalfWidth * 2 +
                        zoomedFoldingSymbolHalfWidth
                    )
            }
            return width
        }
    override val mainViewHeightWithFoldingMark: Int
        get() {
            var height = height
            if (nodeView.model.isFolded) {
                height += zoomedFoldingSymbolHalfWidth
            }
            return height
        }
    override val deltaX: Int
        get() = if (nodeView.model.isFolded && nodeView.isLeft) {
            super.deltaX + zoomedFoldingSymbolHalfWidth * 3
        } else super.deltaX

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.view.mindmapview.NodeView#getStyle()
	 */
    override val style: String
        get() = MindMapNode.STYLE_FORK

    /**
     * Returns the relative position of the Edge
     */
    override val alignment: Int
        get() = NodeView.ALIGN_BOTTOM
    override val leftPoint: Point
        get() {
            val edgeWidth = edgeWidth
            return Point(0, height - edgeWidth / 2 - 1)
        }

    // here, we take the maximum of width of children:
    protected val edgeWidth: Int
        get() {
            val nodeModel = nodeView.model
            val edge = nodeModel.edge
            var edgeWidth = edge.width
            if (edgeWidth == 0) {
                edgeWidth = 1
            }
            when (edge.styleAsInt) {
                EdgeAdapter.INT_EDGESTYLE_SHARP_BEZIER, EdgeAdapter.INT_EDGESTYLE_SHARP_LINEAR -> {
                    // here, we take the maximum of width of children:
                    edgeWidth = 1
                    val it: Iterator<MindMapNode> = nodeModel.childrenUnfolded()
                    while (it.hasNext()) {
                        val child = it.next()
                        edgeWidth = Math.max(edgeWidth, child.edge.width)
                    }
                }
            }
            return edgeWidth
        }
    override val centerPoint: Point
        get() = Point(width / 2, height / 2)
    override val rightPoint: Point
        get() {
            val edgeWidth = edgeWidth
            return Point(width - 1, height - edgeWidth / 2 - 1)
        }
}
