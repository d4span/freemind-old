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
import java.awt.geom.CubicCurve2D
import freemind.view.mindmapview.PathBBox
import freemind.view.mindmapview.MindMapLayout
import freemind.view.mindmapview.NodeViewVisitor
import freemind.modes.EdgeAdapter
import freemind.view.mindmapview.EdgeView
import freemind.modes.MindMapEdge
import javax.swing.JLabel
import freemind.view.mindmapview.MainView
import javax.swing.SwingUtilities
import java.awt.geom.AffineTransform
import javax.swing.SwingConstants
import javax.swing.Icon
import javax.swing.event.TreeModelListener
import freemind.view.mindmapview.NodeMotionListenerView
import freemind.view.mindmapview.NodeFoldingComponent
import javax.swing.ToolTipManager
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
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
import java.awt.geom.Rectangle2D
import java.awt.geom.QuadCurve2D
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
import java.awt.geom.FlatteningPathIterator
import java.awt.geom.Point2D
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
import java.awt.geom.GeneralPath
import javax.swing.DefaultButtonModel
import javax.swing.BorderFactory
import freemind.view.mindmapview.NodeFoldingComponent.RoundImageButtonUI
import javax.swing.plaf.basic.BasicButtonUI
import javax.swing.AbstractButton
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
import java.awt.image.ImageObserver
import java.util.logging.Logger

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
        get() = SwingUtilities.getAncestorOfClass(NodeView::class.java,
                this) as NodeView

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
    override fun getPreferredSize(): Dimension {
        val text = text
        val isEmpty = (text.length == 0
                || HtmlTools.isHtmlNode(text) && text.indexOf("<img") < 0 && HtmlTools
                .htmlToPlain(text).length() === 0)
        if (isEmpty) {
            setText("!")
        }
        val prefSize = super.getPreferredSize()
        val zoom = nodeView.map.zoom
        if (zoom != 1f) {
            // TODO: Why 0.99? fc, 23.4.2011
            prefSize.width = (0.99 + prefSize.width * zoom).toInt()
            prefSize.height = (0.99 + prefSize.height * zoom).toInt()
        }
        if (isCurrentlyPrinting && MapView.Companion.NEED_PREF_SIZE_BUG_FIX) {
            prefSize.width += nodeView.map.getZoomed(10)
        }
        prefSize.width = Math.max(
                nodeView.map.getZoomed(MIN_HOR_NODE_SIZE),
                prefSize.width)
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
        protected get() = nodeView.map.isCurrentlyPrinting
    private val zoom: Float
        private get() = nodeView.map.zoom

    override fun printComponent(g: Graphics) {
        super.paintComponent(g)
    }

    open fun paintSelected(graphics: Graphics2D) {
        if (nodeView.useSelectionColors()) {
            paintBackground(graphics, nodeView.selectedColor)
        } else {
            val backgroundColor: Color? = nodeView.getModel()
                    .backgroundColor
            backgroundColor?.let { paintBackground(graphics, it) }
        }
    }

    protected open fun paintBackground(graphics: Graphics2D, color: Color?) {
        graphics.color = color
        graphics.fillRect(0, 0, width - 1, height - 1)
    }

    open fun paintDragOver(graphics: Graphics2D) {
        if (draggedOver == NodeView.Companion.DRAGGED_OVER_SON) {
            if (nodeView.isLeft) {
                graphics.paint = GradientPaint((width * 3 / 4).toFloat(), 0,
                        nodeView.map.background, (width / 4).toFloat(),
                        0, NodeView.Companion.dragColor)
                graphics.fillRect(0, 0, width * 3 / 4, height - 1)
            } else {
                graphics.paint = GradientPaint((width / 4).toFloat(), 0,
                        nodeView.map.background,
                        (width * 3 / 4).toFloat(), 0, NodeView.Companion.dragColor)
                graphics.fillRect(width / 4, 0, width - 1,
                        height - 1)
            }
        }
        if (draggedOver == NodeView.Companion.DRAGGED_OVER_SIBLING) {
            graphics.paint = GradientPaint(0, (height * 3 / 5).toFloat(),
                    nodeView.map.background, 0, (height / 5).toFloat(),
                    NodeView.Companion.dragColor)
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

    abstract val centerPoint: Point
    abstract val leftPoint: Point
    abstract val rightPoint: Point

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
        convertPointToAncestor(this, p, nodeView.map)
    }

    protected fun convertPointFromMap(p: Point?) {
        convertPointFromAncestor(nodeView.map, p!!, this)
    }

    // not very
    // understandable,
    // was: 0.97F;
    var draggedOver: Int = NodeView.Companion.DRAGGED_OVER_NO

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
        isPainting = false
        alignmentX = CENTER_ALIGNMENT
        horizontalAlignment = CENTER
        verticalAlignment = CENTER
    }

    open fun setDraggedOver(p: Point) {
        draggedOver = if (dropAsSibling(p.getX())) NodeView.Companion.DRAGGED_OVER_SIBLING else NodeView.Companion.DRAGGED_OVER_SON
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

    abstract val style: String
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
        val model = nodeView.getModel()
        return (model!!.link != null
                && (model!!.isRoot || !model!!.hasChildren() || isInVerticalRegion(
                xCoord, 1.0 / 2)))
    }

    /**
     * @return true if a link is to be displayed and the cursor is the hand now.
     */
    fun updateCursor(xCoord: Double): Boolean {
        val followLink = isInFollowLinkRegion(xCoord)
        val requiredCursor = if (followLink) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR
        if (cursor.type != requiredCursor) {
            cursor = if (requiredCursor != Cursor.DEFAULT_CURSOR) Cursor(
                    requiredCursor) else null
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