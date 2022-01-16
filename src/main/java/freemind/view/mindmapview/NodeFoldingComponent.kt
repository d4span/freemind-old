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
/*$Id: NodeMotionListenerView.java,v 1.1.4.4.4.9 2009/03/29 19:37:23 christianfoltin Exp $*/
package freemind.view.mindmapview

import freemind.main.FreeMind
import freemind.main.Resources
import freemind.main.Tools
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Shape
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.Ellipse2D
import java.util.logging.Logger
import javax.swing.AbstractButton
import javax.swing.BorderFactory
import javax.swing.DefaultButtonModel
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.Timer
import javax.swing.plaf.basic.BasicButtonListener
import javax.swing.plaf.basic.BasicButtonUI

/**
 * @author Foltin
 */
class NodeFoldingComponent(view: NodeView) : JButton() {
    private var mIsEntered = false
    private var mColorCounter = 0
    val nodeView: NodeView
    private var mIsEnabled = true
    private var mTimer: Timer? = null

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        nodeView = view
        setModel(DefaultButtonModel())
        init(null, null)
        border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        background = Color.BLACK
        isContentAreaFilled = false
        isFocusPainted = false
        isFocusable = false
        alignmentY = TOP_ALIGNMENT
        setUI(RoundImageButtonUI())
        mIsEnabled = Resources.getInstance().getBoolProperty(
            FreeMind.RESOURCES_DISPLAY_FOLDING_BUTTONS
        )
        if (mIsEnabled) {
            addMouseListener(object : MouseListener {
                override fun mouseReleased(pE: MouseEvent) {}
                override fun mousePressed(pE: MouseEvent) {}
                override fun mouseExited(pE: MouseEvent) {
                    mIsEntered = false
                    mColorCounter = COLOR_COUNTER_MAX
                    repaint()
                }

                override fun mouseEntered(pE: MouseEvent) {
                    mIsEntered = true
                    startTimer()
                    repaint()
                }

                override fun mouseClicked(pE: MouseEvent) {}
            })
            val delay = TIMER_DELAY
            val taskPerformer = ActionListener {
                if (mIsEntered && mColorCounter < COLOR_COUNTER_MAX) {
                    mColorCounter++
                    repaint()
                }
                if (!mIsEntered && mColorCounter > 0) {
                    mColorCounter--
                    if (mColorCounter == 0) {
                        stopTimer()
                    }
                    repaint()
                }
            }
            mTimer = Timer(delay, taskPerformer)
        }
    }

    override fun getPreferredSize(): Dimension {
        return getUI().getPreferredSize(this)
    }

    /**
     * @return
     */
    private val zoomedCircleRadius: Int
        get() = nodeView.zoomedFoldingSymbolHalfWidth

    internal inner class RoundImageButtonUI : BasicButtonUI() {
        protected var shape: Shape? = null
        protected var base: Shape? = null
        override fun installDefaults(b: AbstractButton) {
            super.installDefaults(b)
            clearTextShiftOffset()
            defaultTextShiftOffset = 0
            b.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
            b.isContentAreaFilled = false
            b.isFocusPainted = false
            b.isOpaque = false
            b.background = Color.BLACK
            b.alignmentY = TOP_ALIGNMENT
            initShape(b)
        }

        /* Is called by a button class automatically.*/
        override fun installListeners(b: AbstractButton) {
            val listener: BasicButtonListener = object : BasicButtonListener(b) {
                override fun mousePressed(e: MouseEvent) {
                    val button = e.source as AbstractButton
                    initShape(button)
                    if (shape!!.contains(e.x.toDouble(), e.y.toDouble())) {
                        super.mousePressed(e)
                    }
                }

                override fun mouseEntered(e: MouseEvent) {
                    val button = e.source as AbstractButton
                    initShape(button)
                    if (shape!!.contains(e.x.toDouble(), e.y.toDouble())) {
                        super.mouseEntered(e)
                    }
                }

                override fun mouseMoved(e: MouseEvent) {
                    val button = e.source as AbstractButton
                    initShape(button)
                    if (shape!!.contains(e.x.toDouble(), e.y.toDouble())) {
                        super.mouseEntered(e)
                    } else {
                        super.mouseExited(e)
                    }
                }
            }
            b.addMouseListener(listener)
            b.addMouseMotionListener(listener)
            b.addFocusListener(listener)
            b.addPropertyChangeListener(listener)
            b.addChangeListener(listener)
        }

        override fun paint(g: Graphics, c: JComponent) {
            super.paint(g, c)
            val g2 = g as Graphics2D
            initShape(c)
            // Border
            val oldRenderingHint = nodeView.map
                .setEdgesRenderingHint(g2)
            g2.color = c.background
            g2.stroke = BubbleMainView.DEF_STROKE
            val b = c as NodeFoldingComponent
            val bounds = shape!!.bounds
            val col = colorForCounter
            val lineColor = nodeView.model.edge.color
            if (b.mIsEntered) {
                val oldColor = g2.color
                g2.color = nodeView.map.background
                g2.fillOval(bounds.x, bounds.y, bounds.width, bounds.height)
                g2.color = lineColor
                val xmiddle = bounds.x + bounds.width / 2
                val ymiddle = bounds.y + bounds.height / 2
                g2.drawLine(bounds.x, ymiddle, bounds.x + bounds.width, ymiddle)
                if (isFolded) {
                    g2.drawLine(
                        xmiddle, bounds.y, xmiddle,
                        bounds.y +
                            bounds.height
                    )
                }
                g2.draw(shape)
                g2.color = oldColor
            } else {
                val xmiddle = bounds.x + bounds.width / 2
                val ymiddle = bounds.y + bounds.height / 2
                val foldingCircleDiameter = (
                    bounds.width /
                        SIZE_FACTOR_ON_MOUSE_OVER
                    )
                val oldColor = g2.color
                if (mColorCounter != 0) {
                    var diameter = (
                        bounds.width * mColorCounter /
                            COLOR_COUNTER_MAX
                        )
                    if (isFolded) {
                        diameter = Math.max(diameter, foldingCircleDiameter)
                    }
                    val radius = diameter / 2
                    g2.color = nodeView.map.background
                    g2.fillOval(
                        xmiddle - radius, ymiddle - radius, diameter,
                        diameter
                    )
                    g2.color = col
                    if (isFolded) {
                        g2.drawLine(
                            xmiddle, ymiddle - radius, xmiddle,
                            ymiddle +
                                radius
                        )
                    }
                    g2.drawLine(
                        xmiddle - radius, ymiddle, xmiddle + radius,
                        ymiddle
                    )
                    g2.color = lineColor
                    g2.drawOval(
                        xmiddle - radius, ymiddle - radius, diameter,
                        diameter
                    )
                    g2.color = oldColor
                } else {
                    if (isFolded) {
                        val radius = foldingCircleDiameter / 2
                        g2.color = nodeView.map.background
                        g2.fillOval(
                            xmiddle - radius, ymiddle - radius,
                            foldingCircleDiameter, foldingCircleDiameter
                        )
                        g2.color = lineColor
                        g2.drawOval(
                            xmiddle - radius, ymiddle - radius,
                            foldingCircleDiameter, foldingCircleDiameter
                        )
                        g2.color = oldColor
                    }
                }
            }
            Tools.restoreAntialiasing(g2, oldRenderingHint)
        }

        /**
         * @return
         */
        private val colorForCounter: Color
            get() {
                val color = nodeView.model.edge.color
                val col = 16 * mColorCounter
                return Color(
                    color.red, color.green,
                    color.blue, col
                )
            }

        override fun getPreferredSize(c: JComponent): Dimension {
            val b = c as JButton
            val i = b.insets
            val iw = (zoomedCircleRadius * 2f * SIZE_FACTOR_ON_MOUSE_OVER).toInt()
            return Dimension(iw + i.right + i.left, iw + i.top + i.bottom)
        }

        private fun initShape(c: JComponent) {
            if (c.bounds != base) {
                val s = c.preferredSize
                base = c.bounds
                shape = Ellipse2D.Float(0F, 0F, (s.width - 1).toFloat(), (s.height - 1).toFloat())
            }
        }
    }

    fun setCorrectedLocation(p: Point?) {
        if (p != null) {
            val zoomedCircleRadius = zoomedCircleRadius
            val left = nodeView.model.isLeft
            val xCorrection = (zoomedCircleRadius * (SIZE_FACTOR_ON_MOUSE_OVER + if (left) +1f else -1f)).toInt()
            setLocation(
                p.x - xCorrection,
                (
                    p.y - zoomedCircleRadius
                        * SIZE_FACTOR_ON_MOUSE_OVER
                    )
            )
        }
    }

    fun dispose() {
        if (mTimer != null) {
            stopTimer()
            mTimer = null
        }
    }

    protected val isFolded: Boolean
        get() {
            val model = nodeView.model
            return model.isFolded && model.isVisible
        }

    @Synchronized
    protected fun startTimer() {
        if (!mTimer!!.isRunning) {
            mTimer!!.start()
        }
    }

    @Synchronized
    protected fun stopTimer() {
        if (mTimer!!.isRunning) {
            mTimer!!.stop()
        }
    }

    companion object {
        private const val TIMER_DELAY = 50
        private const val COLOR_COUNTER_MAX = 15
        private const val SIZE_FACTOR_ON_MOUSE_OVER = 4
        protected var logger: Logger? = null
    }
}
