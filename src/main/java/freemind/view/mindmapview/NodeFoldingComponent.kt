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

import freemind.main.Tools.xmlToColor
import freemind.main.Tools.xmlToBoolean
import freemind.preferences.FreemindPropertyListener.propertyChanged
import freemind.modes.MindMap.rootNode
import freemind.main.Tools.waitForEventQueue
import freemind.modes.MindMapNode.isRoot
import freemind.modes.MindMapNode.isFolded
import freemind.modes.MindMapNode.nodeLevel
import freemind.main.Tools.convertPointToAncestor
import freemind.modes.MindMapNode.parentNode
import freemind.main.Tools.Pair.first
import freemind.main.Tools.Pair.second
import freemind.modes.MindMapNode.shallowCopy
import freemind.modes.MindMap.restorable
import freemind.main.Tools.restoreAntialiasing
import freemind.modes.MindMap.linkRegistry
import freemind.modes.MindMapLinkRegistry.getLabel
import freemind.modes.MindMapLinkRegistry.getAllLinks
import freemind.modes.MindMapLink.source
import freemind.modes.MindMapLink.target
import freemind.modes.MindMapNode.addTreeModelListener
import freemind.modes.MindMapNode.removeTreeModelListener
import freemind.modes.MindMapLine.width
import freemind.modes.MindMapNode.edge
import freemind.modes.MindMapNode.backgroundColor
import freemind.main.Tools.convertPointFromAncestor
import freemind.modes.MindMapNode.link
import freemind.modes.MindMapNode.hasChildren
import freemind.main.Tools.safeEquals
import freemind.modes.MindMapNode.hasVisibleChilds
import freemind.modes.MindMapNode.cloud
import freemind.modes.MindMapNode.isLeft
import freemind.modes.MindMapNode.isVisible
import freemind.modes.MindMapNode.childrenFolded
import freemind.modes.MindMapNode.toString
import freemind.modes.MindMap.uRL
import freemind.modes.MindMapNode.font
import freemind.modes.MindMapNode.stateIcons
import freemind.modes.MindMapNode.attributeTableLength
import freemind.modes.MindMapNode.icons
import freemind.modes.MindIcon.unscaledIcon
import freemind.modes.NodeAdapter.link
import freemind.main.Tools.executableByExtension
import freemind.modes.MindMapNode.color
import freemind.modes.MindMapNode.style
import freemind.modes.MindMapNode.toolTip
import freemind.modes.MindMapNode.calcShiftY
import freemind.modes.MindMapNode.vGap
import freemind.modes.MindMapNode.hGap
import freemind.modes.MindMapLine.color
import freemind.modes.MindMapCloud.iterativeLevel
import freemind.modes.MindMapCloud.exteriorColor
import freemind.modes.ModeController.view
import freemind.modes.ModeController.controller
import freemind.modes.ModeController.getText
import freemind.modes.ModeController.frame
import freemind.main.Tools.clipboard
import freemind.modes.MindMapEdge.styleAsInt
import freemind.modes.MindMapNode.childrenUnfolded
import freemind.modes.MindMapArrowLink.startInclination
import freemind.modes.MindMapArrowLink.endInclination
import freemind.modes.MindMapArrowLink.startArrow
import freemind.modes.MindMapArrowLink.endArrow
import freemind.modes.MindMapArrowLink.showControlPointsFlag
import freemind.main.FreeMindMain.getProperty
import freemind.main.Tools.setLabelAndMnemonic
import freemind.main.Tools.BooleanHolder.value
import freemind.main.Tools.updateFontSize
import freemind.main.Tools.setDialogLocationRelativeTo
import freemind.main.Tools.addEscapeActionToDialog
import freemind.main.Tools.addKeyActionToDialog
import freemind.controller.Controller.frame
import freemind.main.FreeMindMain.openDocument
import freemind.modes.MindMapNode.isItalic
import freemind.modes.MindMapNode.isBold
import freemind.main.Tools.colorToXml
import freemind.main.Tools.IntHolder.value
import freemind.modes.MapAdapter.loadTree
import freemind.modes.MapAdapter.root
import freemind.main.Tools.getFile
import freemind.modes.Mode.controller
import freemind.main.Tools.scalingFactorPlain
import freemind.main.Tools.scalingFactor
import freemind.modes.MindMap
import freemind.view.mindmapview.ViewFeedback
import freemind.modes.ViewAbstraction
import java.awt.print.Printable
import java.awt.dnd.Autoscroll
import freemind.view.mindmapview.MapView
import freemind.view.mindmapview.MapView.Selected
import freemind.view.mindmapview.ArrowLinkView
import freemind.preferences.FreemindPropertyListener
import freemind.view.mindmapview.NodeViewFactory
import java.lang.NumberFormatException
import java.util.TimerTask
import freemind.view.mindmapview.MapView.CheckLaterForCenterNodeTask
import kotlin.jvm.JvmOverloads
import java.util.LinkedList
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DropTargetListener
import freemind.modes.MindMapNode
import java.util.Collections
import freemind.modes.MindMapLink
import freemind.modes.MindMapArrowLink
import java.awt.print.PageFormat
import java.awt.geom.CubicCurve2D
import freemind.view.mindmapview.PathBBox
import freemind.view.mindmapview.MindMapLayout
import freemind.view.mindmapview.NodeViewVisitor
import freemind.modes.EdgeAdapter
import freemind.view.mindmapview.EdgeView
import freemind.modes.MindMapEdge
import freemind.view.mindmapview.MainView
import java.awt.geom.AffineTransform
import javax.swing.event.TreeModelListener
import freemind.view.mindmapview.NodeMotionListenerView
import freemind.view.mindmapview.NodeFoldingComponent
import java.awt.dnd.DragSource
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import freemind.modes.MindMapCloud
import freemind.view.mindmapview.CloudView
import freemind.view.mindmapview.NodeViewLayout
import java.net.MalformedURLException
import freemind.view.mindmapview.MultipleImage
import freemind.view.ImageFactory
import freemind.modes.MindIcon
import freemind.modes.NodeAdapter
import java.util.TreeMap
import java.lang.StringBuffer
import javax.swing.event.TreeModelEvent
import freemind.view.mindmapview.BubbleMainView
import java.awt.geom.Rectangle2D
import java.awt.geom.QuadCurve2D
import freemind.view.mindmapview.ConvexHull
import freemind.view.mindmapview.ConvexHull.thetaComparator
import freemind.modes.ModeController
import freemind.view.mindmapview.EditNodeBase.EditControl
import freemind.view.mindmapview.EditNodeBase
import freemind.view.mindmapview.EditNodeBase.EditDialog.DialogWindowListener
import javax.swing.text.JTextComponent
import java.awt.datatransfer.StringSelection
import freemind.view.mindmapview.EditNodeBase.EditCopyAction
import java.awt.datatransfer.Clipboard
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListener
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import java.awt.geom.FlatteningPathIterator
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import freemind.view.mindmapview.VerticalRootNodeViewLayout
import freemind.view.mindmapview.BezierEdgeView
import freemind.view.mindmapview.EditNodeBase.EditDialog
import freemind.view.mindmapview.EditNodeDialog
import freemind.main.Tools.BooleanHolder
import java.lang.Runnable
import freemind.view.mindmapview.EditNodeBase.EditPopupMenu
import com.inet.jortho.SpellChecker
import freemind.view.mindmapview.EditNodeDialog.LongNodeDialog
import com.lightdev.app.shtm.SHTMLPanel
import freemind.view.mindmapview.EditNodeBase.EditDialog.SubmitAction
import kotlin.Throws
import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import freemind.view.mindmapview.EditNodeWYSIWYG
import freemind.view.mindmapview.EditNodeWYSIWYG.HTMLDialog
import freemind.view.mindmapview.NodeViewFactory.ContentPane
import freemind.view.mindmapview.NodeViewFactory.ContentPaneLayout
import freemind.view.mindmapview.SharpBezierEdgeView
import freemind.view.mindmapview.SharpLinearEdgeView
import freemind.view.mindmapview.LinearEdgeView
import freemind.view.mindmapview.RootMainView
import freemind.view.mindmapview.LeftNodeViewLayout
import freemind.view.mindmapview.RightNodeViewLayout
import freemind.view.mindmapview.ForkMainView
import javax.swing.undo.UndoManager
import freemind.view.mindmapview.EditNodeTextField
import freemind.view.mindmapview.EditNodeTextField.TextFieldListener
import javax.swing.undo.CannotUndoException
import javax.swing.undo.CannotRedoException
import freemind.view.mindmapview.NodeViewLayoutAdapter
import java.awt.geom.GeneralPath
import freemind.view.mindmapview.NodeFoldingComponent.RoundImageButtonUI
import javax.swing.plaf.basic.BasicButtonUI
import javax.swing.plaf.basic.BasicButtonListener
import java.awt.geom.Ellipse2D
import freemind.modes.MapFeedbackAdapter
import freemind.modes.mindmapmode.MindMapMapModel
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URISyntaxException
import freemind.main.Tools.FileReaderCreator
import freemind.modes.MapAdapter
import freemind.view.mindmapview.IndependantMapViewCreator
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHookSubstituteUnknown
import freemind.main.*
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import java.io.FileOutputStream
import javax.imageio.ImageIO
import java.io.FileWriter
import java.text.MessageFormat
import freemind.view.MapModule
import freemind.view.ScalableImageIcon
import java.awt.*
import java.awt.event.*
import java.awt.image.ImageObserver
import java.util.logging.Logger
import javax.swing.*

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
                    this.javaClass.name)
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
                FreeMind.RESOURCES_DISPLAY_FOLDING_BUTTONS)
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
        private get() = nodeView.zoomedFoldingSymbolHalfWidth

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
                    val b = e.source as AbstractButton
                    initShape(b)
                    if (shape!!.contains(e.x.toDouble(), e.y.toDouble())) {
                        super.mousePressed(e)
                    }
                }

                override fun mouseEntered(e: MouseEvent) {
                    val b = e.source as AbstractButton
                    initShape(b)
                    if (shape!!.contains(e.x.toDouble(), e.y.toDouble())) {
                        super.mouseEntered(e)
                    }
                }

                override fun mouseMoved(e: MouseEvent) {
                    val b = e.source as AbstractButton
                    initShape(b)
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
            g2.stroke = BubbleMainView.Companion.DEF_STROKE
            val b = c as NodeFoldingComponent
            val bounds = shape!!.bounds
            val col = colorForCounter
            val lineColor: Color = nodeView.getModel().edge.color
            if (b.mIsEntered) {
                val oldColor = g2.color
                g2.color = nodeView.map.background
                g2.fillOval(bounds.x, bounds.y, bounds.width, bounds.height)
                g2.color = lineColor
                val xmiddle = bounds.x + bounds.width / 2
                val ymiddle = bounds.y + bounds.height / 2
                g2.drawLine(bounds.x, ymiddle, bounds.x + bounds.width, ymiddle)
                if (isFolded) {
                    g2.drawLine(xmiddle, bounds.y, xmiddle, bounds.y
                            + bounds.height)
                }
                g2.draw(shape)
                g2.color = oldColor
            } else {
                val xmiddle = bounds.x + bounds.width / 2
                val ymiddle = bounds.y + bounds.height / 2
                val foldingCircleDiameter = (bounds.width
                        / SIZE_FACTOR_ON_MOUSE_OVER)
                val oldColor = g2.color
                if (mColorCounter != 0) {
                    var diameter = (bounds.width * mColorCounter
                            / COLOR_COUNTER_MAX)
                    if (isFolded) {
                        diameter = Math.max(diameter, foldingCircleDiameter)
                    }
                    val radius = diameter / 2
                    g2.color = nodeView.map.background
                    g2.fillOval(xmiddle - radius, ymiddle - radius, diameter,
                            diameter)
                    g2.color = col
                    if (isFolded) {
                        g2.drawLine(xmiddle, ymiddle - radius, xmiddle, ymiddle
                                + radius)
                    }
                    g2.drawLine(xmiddle - radius, ymiddle, xmiddle + radius,
                            ymiddle)
                    g2.color = lineColor
                    g2.drawOval(xmiddle - radius, ymiddle - radius, diameter,
                            diameter)
                    g2.color = oldColor
                } else {
                    if (isFolded) {
                        val radius = foldingCircleDiameter / 2
                        g2.color = nodeView.map.background
                        g2.fillOval(xmiddle - radius, ymiddle - radius,
                                foldingCircleDiameter, foldingCircleDiameter)
                        g2.color = lineColor
                        g2.drawOval(xmiddle - radius, ymiddle - radius,
                                foldingCircleDiameter, foldingCircleDiameter)
                        g2.color = oldColor
                    }
                }
            }
            restoreAntialiasing(g2, oldRenderingHint)
        }

        /**
         * @return
         */
        private val colorForCounter: Color
            private get() {
                val color: Color = nodeView.getModel().edge.color
                val col = 16 * mColorCounter
                return Color(color.red, color.green,
                        color.blue, col)
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
                shape = Ellipse2D.Float(0, 0, (s.width - 1).toFloat(), (s.height - 1).toFloat())
            }
        }
    }

    fun setCorrectedLocation(p: Point?) {
        val zoomedCircleRadius = zoomedCircleRadius
        val left = nodeView.getModel().isLeft
        val xCorrection = (zoomedCircleRadius * (SIZE_FACTOR_ON_MOUSE_OVER + if (left) +1f else -1f)).toInt()
        setLocation(p!!.x - xCorrection, (p.y - zoomedCircleRadius
                * SIZE_FACTOR_ON_MOUSE_OVER))
    }

    fun dispose() {
        if (mTimer != null) {
            stopTimer()
            mTimer = null
        }
    }

    protected val isFolded: Boolean
        protected get() {
            val model = nodeView.getModel()
            return model!!.isFolded && model.isVisible
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