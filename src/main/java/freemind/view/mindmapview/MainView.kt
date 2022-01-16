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

import freemind.main.HtmlTools
import freemind.main.Resources
import freemind.main.Tools
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.GradientPaint
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.util.logging.Logger
import javax.swing.JLabel
import javax.swing.SwingUtilities

/**
 * Base class for all node views.
 */
abstract class MainView internal constructor() : JLabel() {
    val zoomedFoldingSymbolHalfWidth: Int
        get() = nodeView.zoomedFoldingSymbolHalfWidth

    override fun getMinimumSize(): Dimension {
        return Companion.minimumSize
    }

    override fun getMaximumSize(): Dimension {
        return Companion.maximumSize
    }

    private var isPainting: Boolean
    val nodeView: NodeView
        get() = SwingUtilities.getAncestorOfClass(
            NodeView::class.java,
            this
        ) as NodeView

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
    override fun getPreferredSize(): Dimension {
        val text = text
        val isEmpty = (
            text.length == 0 ||
                HtmlTools.isHtmlNode(text) && text.indexOf("<img") < 0 && HtmlTools.htmlToPlain(text).length == 0
            )
        if (isEmpty) {
            setText("!")
        }
        val prefSize = super.getPreferredSize()
        val zoom = nodeView.map.getZoom()
        if (zoom != 1f) {
            // TODO: Why 0.99? fc, 23.4.2011
            prefSize.width = (0.99 + prefSize.width * zoom).toInt()
            prefSize.height = (0.99 + prefSize.height * zoom).toInt()
        }
        if (isCurrentlyPrinting && MapView.NEED_PREF_SIZE_BUG_FIX) {
            prefSize.width += nodeView.map.getZoomed(10)
        }
        prefSize.width = Math.max(
            nodeView.map.getZoomed(MIN_HOR_NODE_SIZE),
            prefSize.width
        )
        if (isEmpty) {
            setText("")
        }
        prefSize.width += nodeView.map.getZoomed(12)
        prefSize.height += nodeView.map.getZoomed(4)
        // /*@@@@@@@@@@@@@@*/
        // prefSize.width = 150;
        // prefSize.height = 20;
        return prefSize
    }

    override fun paint(g: Graphics) {
        var zoom = zoom
        if (zoom != 1f) {
            // Dimitry: Workaround because Swing do not use fractional metrics
            // for laying JLabels out
            val g2 = g as Graphics2D
            zoom *= ZOOM_CORRECTION_FACTOR
            val transform = g2.transform
            g2.scale(zoom.toDouble(), zoom.toDouble())
            isPainting = true
            super.paint(g)
            isPainting = false
            g2.transform = transform
        } else {
            super.paint(g)
        }
    }

    protected val isCurrentlyPrinting: Boolean
        get() = nodeView.map.isCurrentlyPrinting
    private val zoom: Float
        get() = nodeView.map.getZoom()

    override fun printComponent(g: Graphics) {
        super.paintComponent(g)
    }

    open fun paintSelected(graphics: Graphics2D) {
        if (nodeView.useSelectionColors()) {
            paintBackground(graphics, NodeView.selectedColor)
        } else {
            val backgroundColor = nodeView.model
                .backgroundColor
            backgroundColor?.let { paintBackground(graphics, it) }
        }
    }

    protected open fun paintBackground(graphics: Graphics2D, color: Color?) {
        graphics.color = color
        graphics.fillRect(0, 0, width - 1, height - 1)
    }

    open fun paintDragOver(graphics: Graphics2D) {
        if (draggedOver == NodeView.DRAGGED_OVER_SON) {
            if (nodeView.isLeft) {
                graphics.paint = GradientPaint(
                    (width * 3 / 4).toFloat(), 0F,
                    nodeView.map.background, (width / 4).toFloat(),
                    0F, NodeView.dragColor
                )
                graphics.fillRect(0, 0, width * 3 / 4, height - 1)
            } else {
                graphics.paint = GradientPaint(
                    (width / 4).toFloat(), 0F,
                    nodeView.map.background,
                    (width * 3 / 4).toFloat(), 0F, NodeView.dragColor
                )
                graphics.fillRect(
                    width / 4, 0, width - 1,
                    height - 1
                )
            }
        }
        if (draggedOver == NodeView.DRAGGED_OVER_SIBLING) {
            graphics.paint = GradientPaint(
                0F, (height * 3 / 5).toFloat(),
                nodeView.map.background, 0F, (height / 5).toFloat(),
                NodeView.dragColor
            )
            graphics.fillRect(0, 0, width - 1, height - 1)
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#getHeight()
	 */
    override fun getHeight(): Int {
        if (isPainting) {
            val zoom = zoom
            if (zoom != 1f) {
                return (super.getHeight() / zoom).toInt()
            }
        }
        return super.getHeight()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#getWidth()
	 */
    override fun getWidth(): Int {
        if (isPainting) {
            val zoom = zoom
            if (zoom != 1f) {
                return (0.99f + super.getWidth() / zoom).toInt()
            }
        }
        return super.getWidth()
    }

    protected abstract val centerPoint: Point?
    abstract val leftPoint: Point?
    abstract val rightPoint: Point?

    /** get x coordinate including folding symbol  */
    open val deltaX: Int
        get() = 0

    /** get y coordinate including folding symbol  */
    val deltaY: Int
        get() = 0

    /** get height including folding symbol  */
    open val mainViewHeightWithFoldingMark: Int
        get() = height

    /** get width including folding symbol  */
    open val mainViewWidthWithFoldingMark: Int
        get() = width

    protected fun convertPointToMap(p: Point?) {
        Tools.convertPointToAncestor(this, p, nodeView.map)
    }

    protected fun convertPointFromMap(p: Point?) {
        Tools.convertPointFromAncestor(nodeView.map, p, this)
    }

    // not very
    // understandable,
    // was: 0.97F;
    var draggedOver = NodeView.DRAGGED_OVER_NO

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        isPainting = false
        alignmentX = CENTER_ALIGNMENT
        horizontalAlignment = CENTER
        verticalAlignment = CENTER
    }

    open fun setDraggedOver(p: Point) {
        draggedOver = if (dropAsSibling(p.getX())) NodeView.DRAGGED_OVER_SIBLING else NodeView.DRAGGED_OVER_SON
    }

    open fun dropAsSibling(xCoord: Double): Boolean {
        return isInVerticalRegion(xCoord, 1.0 / 3)
    }

    /** @return true if should be on the left, false otherwise.
     */
    open fun dropPosition(xCoord: Double): Boolean {
        /* here it is the same as me. */
        return nodeView.isLeft
    }

    /**
     * Determines whether or not the xCoord is in the part p of the node: if
     * node is on the left: part [1-p,1] if node is on the right: part[ 0,p] of
     * the total width.
     */
    fun isInVerticalRegion(xCoord: Double, p: Double): Boolean {
        return if (nodeView.isLeft) xCoord > size.width * (1.0 - p) else xCoord < size.width * p
    }

    abstract val style: String?
    abstract val alignment: Int
    open val textWidth: Int
        get() = width - iconWidth
    open val textX: Int
        get() {
            var gap = (width - preferredSize.width) / 2
            val isLeft = nodeView.isLeft
            if (isLeft) {
                gap = -gap
            }
            return gap + if (isLeft && !nodeView.isRoot) 0 else iconWidth
        }
    val iconWidth: Int
        get() {
            val icon = icon ?: return 0
            return nodeView.map.getZoomed(icon.iconWidth)
        }

    fun isInFollowLinkRegion(xCoord: Double): Boolean {
        val model = nodeView.model
        return (
            model.link != null &&
                (
                    model.isRoot || !model.hasChildren() || isInVerticalRegion(
                        xCoord, 1.0 / 2
                    )
                    )
            )
    }

    /**
     * @return true if a link is to be displayed and the cursor is the hand now.
     */
    fun updateCursor(xCoord: Double): Boolean {
        val followLink = isInFollowLinkRegion(xCoord)
        val requiredCursor = if (followLink) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR
        if (cursor.type != requiredCursor) {
            cursor = if (requiredCursor != Cursor.DEFAULT_CURSOR) Cursor(
                requiredCursor
            ) else null
        }
        return followLink
    }

    companion object {
        var minimumSize = Dimension(0, 0)
        var maximumSize = Dimension(Int.MAX_VALUE, Int.MAX_VALUE)
        private var logger: Logger? = null
        private const val MIN_HOR_NODE_SIZE = 10
        const val ZOOM_CORRECTION_FACTOR = 1.0f // former value, but
    }
}
