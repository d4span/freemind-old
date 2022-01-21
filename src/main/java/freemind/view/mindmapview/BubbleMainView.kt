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
import freemind.modes.MindMapNode
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Stroke

internal class BubbleMainView : MainView() {
    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView.MainView#getPreferredSize()
	 */
    override fun getPreferredSize(): Dimension {
        val prefSize = super.getPreferredSize()
        prefSize.width += nodeView.map.getZoomed(5)
        return prefSize
    }

    override fun paint(g: Graphics) {
        val graphics2D = g as Graphics2D
        val nodeView = nodeView
        val model = nodeView.getModel() ?: return
        val renderingHint = nodeView.map.setEdgesRenderingHint(graphics2D)
        paintSelected(graphics2D)
        paintDragOver(graphics2D)

        // change to bold stroke
        // g.setStroke(BOLD_STROKE); // Changed by Daniel

        // Draw a standard node
        graphics2D.color = model.edge.color
        // g.drawOval(0,0,size.width-1,size.height-1); // Changed by Daniel

        // return to std stroke
        graphics2D.stroke = DEF_STROKE
        graphics2D.drawRoundRect(0, 0, width - 1, height - 1, 10, 10)
        Tools.restoreAntialiasing(graphics2D, renderingHint)
        super.paint(graphics2D)
    }

    override fun paintSelected(graphics: Graphics2D) {
        super.paintSelected(graphics)
        if (nodeView.useSelectionColors()) {
            graphics.color = MapView.standardSelectColor
            graphics.fillRoundRect(
                0, 0, width - 1, height - 1, 10,
                10
            )
        }
    }

    override fun paintBackground(graphics: Graphics2D, color: Color?) {
        graphics.color = color
        graphics.fillRoundRect(0, 0, width - 1, height - 1, 10, 10)
    }

    override val leftPoint: Point
        get() = Point(0, height / 2)
    override val centerPoint: Point
        get() {
            val `in` = leftPoint
            `in`.x = width / 2
            return `in`
        }
    override val rightPoint: Point
        get() {
            val `in` = leftPoint
            `in`.x = width - 1
            return `in`
        }
    override val mainViewWidthWithFoldingMark: Int
        get() {
            var width = width
            val dW = zoomedFoldingSymbolHalfWidth * 2
            if (nodeView.getModel().isFolded) {
                width += dW
            }
            return width + dW
        }
    override val deltaX: Int
        get() = if (nodeView.getModel().isFolded && nodeView.isLeft) {
            super.deltaX + zoomedFoldingSymbolHalfWidth * 2
        } else super.deltaX

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getStyle()
	 */
    override val style: String
        get() = MindMapNode.STYLE_BUBBLE

    /**
     * Returns the relative position of the Edge
     */
    override val alignment: Int
        get() = NodeView.ALIGN_CENTER

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getTextWidth()
	 */
    override val textWidth: Int
        get() = super.textWidth + nodeView.map.getZoomed(5)

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getTextX()
	 */
    override val textX: Int
        get() = super.textX + nodeView.map.getZoomed(2)

    companion object {
        @JvmField
		val DEF_STROKE: Stroke = BasicStroke()
    }
}