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

import freemind.main.FreeMind
import freemind.main.Resources
import freemind.main.Tools
import freemind.view.mindmapview.NodeView.Companion.selectedColor
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.GradientPaint
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point

internal class RootMainView : MainView() {
    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.view.mindmapview.NodeView.MainView#getPreferredSize()
	 */
    override fun getPreferredSize(): Dimension {
        val prefSize = super.getPreferredSize()
        prefSize.width *= 1.1.toInt()
        prefSize.height *= 2
        return prefSize
    }

    override fun paint(g: Graphics) {
        val graphics2D = g as Graphics2D
        val renderingHint = nodeView.map.setEdgesRenderingHint(graphics2D)
        paintSelected(graphics2D)
        paintDragOver(graphics2D)

        // Draw a root node
        graphics2D.color = Color.gray
        graphics2D.stroke = BasicStroke(1.0f)
        graphics2D.drawOval(0, 0, width - 1, height - 1)
        Tools.restoreAntialiasing(graphics2D, renderingHint)
        super.paint(graphics2D)
    }

    override fun paintDragOver(graphics: Graphics2D) {
        val draggedOver = draggedOver
        if (draggedOver == NodeView.DRAGGED_OVER_SON) {
            graphics.paint = GradientPaint(
                (width / 4).toFloat(), 0F,
                nodeView.map.background, (width * 3 / 4).toFloat(),
                0F, NodeView.dragColor
            )
            graphics.fillRect(
                width / 4, 0, width - 1,
                height - 1
            )
        } else if (draggedOver == NodeView.DRAGGED_OVER_SON_LEFT) {
            graphics.paint = GradientPaint(
                (width * 3 / 4).toFloat(), 0F,
                nodeView.map.background, (width / 4).toFloat(), 0F,
                NodeView.dragColor
            )
            graphics.fillRect(0, 0, width * 3 / 4, height - 1)
        }
    }

    override fun paintSelected(graphics: Graphics2D) {
        if (nodeView.useSelectionColors()) {
            paintBackground(graphics, selectedColor)
        } else {
            paintBackground(graphics, nodeView.textBackground)
        }
    }

    override fun paintBackground(graphics: Graphics2D, color: Color?) {
        graphics.color = color
        graphics.fillOval(1, 1, width - 2, height - 2)
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

    override fun setDraggedOver(p: Point) {
        draggedOver = if (dropPosition(p.getX())) NodeView.DRAGGED_OVER_SON_LEFT else NodeView.DRAGGED_OVER_SON
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.view.mindmapview.NodeView#getStyle()
	 */
    override val style: String
        get() = Resources.getInstance().getProperty(
            FreeMind.RESOURCES_ROOT_NODE_STYLE
        )
    override val alignment: Int
        get() = TODO("Not yet implemented")

    override val textWidth: Int
        get() = super.textWidth - width / 10

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.view.mindmapview.NodeView#getTextX()
	 */
    override val textX: Int
        get() = iconWidth + width / 20

    override fun dropAsSibling(xCoord: Double): Boolean {
        return false
    }

    /** @return true if should be on the left, false otherwise.
     */
    override fun dropPosition(xCoord: Double): Boolean {
        return xCoord < size.width * 1 / 2
    }
}
