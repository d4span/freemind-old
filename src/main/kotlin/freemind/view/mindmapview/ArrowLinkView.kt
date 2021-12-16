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
/*$Id: ArrowLinkView.java,v 1.8.14.4.4.6 2008/06/08 14:00:32 dpolivaev Exp $*/
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
import javax.swing.JPanel
import freemind.modes.ViewAbstraction
import java.awt.print.Printable
import java.awt.dnd.Autoscroll
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import freemind.view.mindmapview.MapView
import javax.swing.JScrollPane
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import freemind.view.mindmapview.MapView.Selected
import freemind.view.mindmapview.ArrowLinkView
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMind
import freemind.main.Tools
import freemind.main.FreeMindCommon
import freemind.view.mindmapview.NodeViewFactory
import java.lang.NumberFormatException
import java.util.TimerTask
import freemind.view.mindmapview.MapView.CheckLaterForCenterNodeTask
import javax.swing.JComponent
import kotlin.jvm.JvmOverloads
import javax.swing.JViewport
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
import freemind.view.mindmapview.PathBBox
import freemind.view.mindmapview.MindMapLayout
import freemind.view.mindmapview.NodeViewVisitor
import freemind.modes.EdgeAdapter
import freemind.view.mindmapview.EdgeView
import freemind.modes.MindMapEdge
import javax.swing.JLabel
import freemind.view.mindmapview.MainView
import javax.swing.SwingUtilities
import freemind.main.HtmlTools
import javax.swing.SwingConstants
import javax.swing.Icon
import javax.swing.event.TreeModelListener
import freemind.view.mindmapview.NodeMotionListenerView
import freemind.view.mindmapview.NodeFoldingComponent
import javax.swing.ToolTipManager
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import freemind.main.FreeMindMain
import java.awt.dnd.DragSource
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import freemind.modes.MindMapCloud
import freemind.view.mindmapview.CloudView
import freemind.view.mindmapview.NodeViewLayout
import java.net.MalformedURLException
import freemind.view.mindmapview.MultipleImage
import javax.swing.ImageIcon
import freemind.view.ImageFactory
import freemind.modes.MindIcon
import freemind.modes.NodeAdapter
import java.util.TreeMap
import java.lang.StringBuffer
import javax.swing.event.TreeModelEvent
import freemind.view.mindmapview.BubbleMainView
import freemind.view.mindmapview.ConvexHull
import freemind.view.mindmapview.ConvexHull.thetaComparator
import freemind.modes.ModeController
import freemind.view.mindmapview.EditNodeBase.EditControl
import java.awt.event.FocusListener
import freemind.view.mindmapview.EditNodeBase
import javax.swing.JDialog
import javax.swing.JFrame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.WindowConstants
import freemind.view.mindmapview.EditNodeBase.EditDialog.DialogWindowListener
import javax.swing.JOptionPane
import javax.swing.text.JTextComponent
import java.awt.datatransfer.StringSelection
import javax.swing.JPopupMenu
import freemind.view.mindmapview.EditNodeBase.EditCopyAction
import java.awt.datatransfer.Clipboard
import java.awt.event.FocusEvent
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListener
import java.awt.event.MouseWheelEvent
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import java.awt.image.BufferedImage
import freemind.view.mindmapview.VerticalRootNodeViewLayout
import freemind.view.mindmapview.BezierEdgeView
import freemind.view.mindmapview.EditNodeBase.EditDialog
import javax.swing.JTextArea
import javax.swing.JButton
import javax.swing.JCheckBox
import freemind.view.mindmapview.EditNodeDialog
import freemind.main.Tools.BooleanHolder
import java.lang.Runnable
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import freemind.view.mindmapview.EditNodeBase.EditPopupMenu
import com.inet.jortho.SpellChecker
import javax.swing.BoxLayout
import freemind.view.mindmapview.EditNodeDialog.LongNodeDialog
import com.lightdev.app.shtm.SHTMLPanel
import freemind.view.mindmapview.EditNodeBase.EditDialog.SubmitAction
import kotlin.Throws
import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import freemind.view.mindmapview.EditNodeWYSIWYG
import freemind.view.mindmapview.EditNodeWYSIWYG.HTMLDialog
import javax.swing.JEditorPane
import freemind.view.mindmapview.NodeViewFactory.ContentPane
import freemind.view.mindmapview.NodeViewFactory.ContentPaneLayout
import freemind.view.mindmapview.SharpBezierEdgeView
import freemind.view.mindmapview.SharpLinearEdgeView
import freemind.view.mindmapview.LinearEdgeView
import freemind.view.mindmapview.RootMainView
import freemind.view.mindmapview.LeftNodeViewLayout
import freemind.view.mindmapview.RightNodeViewLayout
import freemind.view.mindmapview.ForkMainView
import javax.swing.JTextField
import javax.swing.undo.UndoManager
import freemind.view.mindmapview.EditNodeTextField
import freemind.view.mindmapview.EditNodeTextField.TextFieldListener
import javax.swing.undo.CannotUndoException
import javax.swing.undo.CannotRedoException
import java.awt.event.ComponentListener
import freemind.view.mindmapview.NodeViewLayoutAdapter
import javax.swing.DefaultButtonModel
import javax.swing.BorderFactory
import freemind.view.mindmapview.NodeFoldingComponent.RoundImageButtonUI
import javax.swing.plaf.basic.BasicButtonUI
import javax.swing.AbstractButton
import javax.swing.plaf.basic.BasicButtonListener
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
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import java.io.FileOutputStream
import javax.imageio.ImageIO
import java.io.FileWriter
import java.text.MessageFormat
import freemind.view.MapModule
import freemind.view.ScalableImageIcon
import java.awt.*
import java.awt.geom.*
import java.awt.image.ImageObserver

// end Convex Hull
/**
 * This class represents a ArrowLink around a node.
 */
class ArrowLinkView     /* Note, that source and target are nodeviews and not nodemodels!. */(
        /**
         * fc: This getter is public, because the view gets the model by click on
         * the curve.
         */
        var model: MindMapArrowLink,
        protected var source: NodeView?,
        protected var target: NodeView?) {
    protected var iterativeLevel = 0
    var arrowLinkCurve: CubicCurve2D? = null
    val bounds: Rectangle
        get() = if (arrowLinkCurve == null) Rectangle() else arrowLinkCurve!!.bounds

    /**
     * \param iterativeLevel describes the n-th nested arrowLink that is to be
     * painted.
     */
    fun paint(graphics: Graphics) {
        if (!isSourceVisible && !isTargetVisible) return
        var p1: Point? = null
        var p2: Point? = null
        var p3: Point? = null
        var p4: Point? = null
        var targetIsLeft = false
        var sourceIsLeft = false
        val g = graphics.create() as Graphics2D
        g.color = color
        /* set stroke. */g.stroke = stroke
        // if one of the nodes is not present then draw a dashed line:
        if (!isSourceVisible || !isTargetVisible) g.stroke = BasicStroke(width.toFloat(), BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 0, floatArrayOf(0f, 3f, 0f, 3f), 0)

        // determine, whether destination exists:
        if (isSourceVisible) {
            p1 = source!!.getLinkPoint(model.startInclination)
            sourceIsLeft = source!!.isLeft
        }
        if (isTargetVisible) {
            p2 = target!!.getLinkPoint(model.endInclination)
            targetIsLeft = target!!.isLeft
        }
        // determine point 2 and 3:
        if (model.endInclination == null
                || model.startInclination == null) {
            val dellength: Double = if (isSourceVisible && isTargetVisible) p1
                    .distance(p2) / zoom else 30
            if (isSourceVisible
                    && model.startInclination == null) {
                val incl = calcInclination(source, dellength)
                model.startInclination = incl
                p1 = source!!.getLinkPoint(model.startInclination)
            }
            if (isTargetVisible && model.endInclination == null) {
                val incl = calcInclination(target, dellength)
                incl.y = -incl.y
                model.endInclination = incl
                p2 = target!!.getLinkPoint(model.endInclination)
            }
        }
        arrowLinkCurve = CubicCurve2D.Double()
        if (p1 != null) {
            p3 = Point(p1)
            p3.translate((if (sourceIsLeft) -1 else 1)
                    * map!!.getZoomed(
                    model.startInclination!!.x),
                    map!!.getZoomed(model.startInclination!!.y))
            if (p2 == null) {
                arrowLinkCurve.setCurve(p1, p3, p1, p3)
            }
        }
        if (p2 != null) {
            p4 = Point(p2)
            p4.translate((if (targetIsLeft) -1 else 1)
                    * map!!.getZoomed(
                    model.endInclination!!.x),
                    map!!.getZoomed(model.endInclination!!.y))
            if (p1 == null) {
                arrowLinkCurve.setCurve(p2, p4, p2, p4)
            }
        }
        if (p1 != null && p2 != null) {
            arrowLinkCurve.setCurve(p1, p3, p4, p2)
            g.draw(arrowLinkCurve)
            // arrow source:
        }
        if (isSourceVisible && model.startArrow != "None") {
            paintArrow(p1, p3, g)
        }
        // arrow target:
        if (isTargetVisible && model.endArrow != "None") {
            paintArrow(p2, p4, g)
        }
        // Control Points
        if (model.showControlPointsFlag || !isSourceVisible
                || !isTargetVisible) {
            g.stroke = BasicStroke(width.toFloat(), BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0, floatArrayOf(0f, 3f, 0f, 3f), 0)
            if (p1 != null) {
                g.drawLine(p1.x, p1.y, p3!!.x, p3.y)
            }
            if (p2 != null) {
                g.drawLine(p2.x, p2.y, p4!!.x, p4.y)
            }
        }
    }

    /**
     */
    private val isTargetVisible: Boolean
        private get() = target != null && target.isContentVisible()

    /**
     */
    private val isSourceVisible: Boolean
        private get() = source != null && source.isContentVisible()

    /**
     */
    private fun calcInclination(node: NodeView?, dellength: Double): Point {
        /*
		 * int w = node.getWidth(); int h = node.getHeight(); double r =
		 * Math.sqrt(w*w+h*h); double wr = dellength * w / r; double hr =
		 * dellength * h / r; return new Point((int)wr, (int)hr);
		 */
        return Point(dellength.toInt(), 0)
    }

    /**
     * @param p1
     * is the start point
     * @param p3
     * is the another point indicating the direction of the arrow.
     */
    private fun paintArrow(p1: Point?, p3: Point?, g: Graphics2D) {
        val dx: Double
        val dy: Double
        val dxn: Double
        val dyn: Double
        dx = (p3!!.x - p1!!.x).toDouble() /* direction of p1 -> p3 */
        dy = (p3.y - p1.y).toDouble()
        val length = Math.sqrt(dx * dx + dy * dy) / (zoom * 10 /*
																	 * =zoom
																	 * factor
																	 * for
																	 * arrows
																	 */)
        dxn = dx / length /* normalized direction of p1 -> p3 */
        dyn = dy / length
        // suggestion of daniel to have arrows that are not so wide open. fc,
        // 7.12.2003.
        val width = .5
        val p = Polygon()
        p.addPoint(p1.x, p1.y)
        p.addPoint((p1.x + dxn + width * dyn).toInt(), (p1.y + dyn - width
                * dxn).toInt())
        p.addPoint((p1.x + dxn - width * dyn).toInt(), (p1.y + dyn + (width
                * dxn)).toInt())
        p.addPoint(p1.x, p1.y)
        g.fillPolygon(p)
    }

    /** MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION describes itself.  */
    private val MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION = 16

    /**
     * Determines, whether or not a given point p is in an epsilon-neighbourhood
     * for the cubic curve.
     */
    fun detectCollision(p: Point): Boolean {
        if (arrowLinkCurve == null) return false
        val rec = getControlPoint(p)
        // flatten the curve and test for intersection (bug fix, fc, 16.1.2004).
        val pi = FlatteningPathIterator(
                arrowLinkCurve!!.getPathIterator(null),
                (MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION / 4).toDouble(), 10 /*
																	 * =maximal
																	 * 2^10=1024
																	 * points.
																	 */)
        var oldCoordinateX = 0.0
        var oldCoordinateY = 0.0
        while (pi.isDone == false) {
            val coordinates = DoubleArray(6)
            val type = pi.currentSegment(coordinates)
            when (type) {
                PathIterator.SEG_LINETO -> {
                    if (rec.intersectsLine(oldCoordinateX, oldCoordinateY,
                                    coordinates[0], coordinates[1])) return true
                    oldCoordinateX = coordinates[0]
                    oldCoordinateY = coordinates[1]
                }
                PathIterator.SEG_MOVETO -> {
                    oldCoordinateX = coordinates[0]
                    oldCoordinateY = coordinates[1]
                }
                PathIterator.SEG_QUADTO, PathIterator.SEG_CUBICTO, PathIterator.SEG_CLOSE -> {}
                else -> {}
            }
            pi.next()
        }
        return false
    }

    protected fun getControlPoint(p: Point2D): Rectangle2D {
        // Create a small square around the given point.
        val side = MAXIMAL_RECTANGLE_SIZE_FOR_COLLISION_DETECTION
        return Rectangle2D.Double(p.x - side / 2, p.y - side / 2,
                side.toDouble(), side.toDouble())
    }

    /* new Color(240,240,240) */ /* selectedColor */
    val color: Color?
        get() = model.color /* new Color(240,240,240) */ /* selectedColor */
    val stroke: Stroke
        get() {
            val width = width
            return if (width < 1) {
                DEF_STROKE
            } else BasicStroke(width.toFloat(), BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER)
        }
    val width: Int
        get() = model.width

    /**
     * Get the width in pixels rather than in width constant (like -1)
     */
    val realWidth: Int
        get() {
            val width = width
            return if (width < 1) 1 else width
        }
    protected val map: MapView?
        protected get() = if (source == null) target.getMap() else source.getMap()
    protected val zoom: Double
        protected get() = map!!.zoom.toDouble()

    /**
     */
    fun changeInclination(originX: Int, originY: Int, newX: Int, newY: Int) {
        // TODO Auto-generated method stub
    }

    companion object {
        val DEF_STROKE: Stroke = BasicStroke(1)
    }
}