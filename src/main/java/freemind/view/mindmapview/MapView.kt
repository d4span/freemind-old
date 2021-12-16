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
import freemind.view.mindmapview.MapView.CheckLaterForCenterNodeTask
import javax.swing.JComponent
import kotlin.jvm.JvmOverloads
import javax.swing.JViewport
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DropTargetListener
import freemind.modes.MindMapNode
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
import freemind.controller.*
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
import java.lang.Exception
import java.util.*
import java.util.logging.Logger

/**
 * This class represents the view of a whole MindMap (in analogy to class
 * JTree).
 */
open class MapView(//
        // get/set methods
        //
        val model: MindMap,
        /**
         * @return the belonging instance of a ViewFeedback (in fact, a ModeController)
         */
        val viewFeedback: ViewFeedback) : JPanel(), ViewAbstraction, Printable, Autoscroll {
    /**
     * Currently, this listener does nothing. But it should move the map
     * according to the resize event, such that the current map's center stays
     * at the same location (seen relative).
     */
    private inner class ResizeListener internal constructor() : ComponentAdapter() {
        var mSize: Dimension

        init {
            mSize = size
        }

        override fun componentResized(pE: ComponentEvent) {
            logger!!.fine("Component resized " + pE + " old size " + mSize
                    + " new size " + size)
            // int deltaWidth = mSize.width - getWidth();
            // int deltaHeight = mSize.height - getHeight();
            // Point viewPosition = getViewPosition();
            // viewPosition.x += deltaWidth/2;
            // viewPosition.y += deltaHeight/2;
            // mapViewport.setViewPosition(viewPosition);
            mSize = size
        }
    }

    class ScrollPane : JScrollPane() {
        override fun processKeyBinding(pKs: KeyStroke, pE: KeyEvent,
                                       pCondition: Int, pPressed: Boolean): Boolean {
            /*
			 * the scroll pane eats control page up and down. Moreover, the page
			 * up and down itself is not very useful, as the map hops away too
			 * far.
			 */
            return if (pE.keyCode == KeyEvent.VK_PAGE_DOWN
                    || pE.keyCode == KeyEvent.VK_PAGE_UP) false else super.processKeyBinding(pKs, pE, pCondition, pPressed)
        }

        override fun validateTree() {
            val view = getViewport().view
            view?.validate()
            super.validateTree()
        }
    }

    private inner class Selected {
        private val mySelected = Vector<NodeView?>()
        fun clear() {
            if (size() > 0) {
                removeFocusForHooks(get(0))
            }
            mySelected.clear()
            val selectedCopy = Vector(mySelected)
            for (view in selectedCopy) {
                changeSelection(view, false)
            }
            logger!!.finest("Cleared selected.")
        }

        /**
         * @param pNode
         */
        private fun changeSelection(pNode: NodeView?, pIsSelected: Boolean) {
            if (pNode.getModel() == null) return
            viewFeedback.changeSelection(pNode, pIsSelected)
        }

        fun size(): Int {
            return mySelected.size
        }

        fun remove(node: NodeView?) {
            if (mySelected.indexOf(node) == 0) {
                removeFocusForHooks(node)
            }
            mySelected.remove(node)
            changeSelection(node, false)
            logger!!.finest("Removed focused $node")
        }

        fun add(node: NodeView?) {
            if (size() > 0) {
                removeFocusForHooks(get(0))
            }
            mySelected.add(0, node)
            addFocusForHooks(node)
            changeSelection(node, true)
            logger!!.finest("Added focused $node\nAll=$mySelected")
        }

        private fun removeFocusForHooks(node: NodeView?) {
            if (node.getModel() == null) return
            viewFeedback.onLostFocusNode(node)
        }

        private fun addFocusForHooks(node: NodeView?) {
            viewFeedback.onFocusNode(node)
        }

        operator fun get(i: Int): NodeView? {
            return mySelected[i]
        }

        operator fun contains(node: NodeView?): Boolean {
            return mySelected.contains(node)
        }

        /**
         */
        fun moveToFirst(newSelected: NodeView?) {
            if (contains(newSelected)) {
                val pos = mySelected.indexOf(newSelected)
                if (pos > 0) { // move
                    if (size() > 0) {
                        removeFocusForHooks(get(0))
                    }
                    mySelected.remove(newSelected)
                    mySelected.add(0, newSelected)
                }
            } else {
                add(newSelected)
            }
            addFocusForHooks(newSelected)
            logger!!.finest("""
    MovedToFront selected $newSelected
    All=$mySelected
    """.trimIndent())
        }
    }

    var root: NodeView? = null
        private set
    private override val selected = Selected()
    private var zoom = 1f
    private var disableMoveCursor = true

    // this property is used when the user navigates up/down using cursor keys
    // (PN)
    // it will keep the level of nodes that are understand as "siblings"
    var siblingMaxLevel = 0

    /**
     * For nodes, they can ask, whether or not the width must be bigger to
     * prevent the "..." at the output. (Bug of java).
     */
    var isCurrentlyPrinting = false // use for remove selection from print
        private set
    private var shiftSelectionOrigin: NodeView? = null
    var maxNodeWidth = 0
        get() {
            if (field == 0) {
                field = try {
                    viewFeedback
                            .getProperty("max_node_width").toInt()
                } catch (e: NumberFormatException) {
                    Resources.getInstance().logException(e)
                    viewFeedback
                            .getProperty("el__max_default_window_width").toInt()
                }
            }
            return field
        }
        private set
    private var background: Color? = null
    private var boundingRectangle: Rectangle? = null
    private var fitToPage = true
    var mPaintingTime = 0
    var mPaintingAmount = 0

    /** Used to identify a right click onto a link curve.  */
    private var mArrowLinkViews: Vector<ArrowLinkView>? = Vector()
    private var rootContentLocation: Point? = null
    private var nodeToBeVisible: NodeView? = null
    private var extraWidth = 0
    private var selectedsValid = true
    private fun createPropertyChangeListener() {
        propertyChangeListener = object : FreemindPropertyListener {
            override fun propertyChanged(propertyName: String?, newValue: String?,
                                         oldValue: String?) {
                if (propertyName == FreeMind.RESOURCES_NODE_TEXT_COLOR) {
                    standardNodeTextColor = xmlToColor(newValue)
                    this@MapView.root!!.updateAll()
                } else if (propertyName
                        == FreeMind.RESOURCES_BACKGROUND_COLOR) {
                    standardMapBackgroundColor = xmlToColor(newValue)
                    setBackground(standardMapBackgroundColor)
                } else if (propertyName
                        == FreeMind.RESOURCES_SELECTED_NODE_COLOR) {
                    standardSelectColor = xmlToColor(newValue)
                    repaintSelecteds()
                } else if (propertyName
                        == FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR) {
                    standardSelectRectangleColor = xmlToColor(newValue)
                    repaintSelecteds()
                } else if (propertyName
                        == FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION) {
                    standardDrawRectangleForSelection = xmlToBoolean(newValue!!)
                    repaintSelecteds()
                } else if (propertyName
                        == FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND) {
                    printOnWhiteBackground = xmlToBoolean(newValue!!)
                } else if (propertyName == FreeMindCommon.RESOURCE_ANTIALIAS) {
                    if ("antialias_none" == newValue) {
                        antialiasEdges = false
                        antialiasAll = false
                    }
                    if ("antialias_edges" == newValue) {
                        antialiasEdges = true
                        antialiasAll = false
                    }
                    if ("antialias_all" == newValue) {
                        antialiasEdges = true
                        antialiasAll = true
                    }
                }
            }
        }
        Controller.addPropertyChangeListener(propertyChangeListener)
    }

    fun setEdgesRenderingHint(g: Graphics2D): Any {
        val renderingHint = g
                .getRenderingHint(RenderingHints.KEY_ANTIALIASING)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                if (antialiasEdges) RenderingHints.VALUE_ANTIALIAS_ON else RenderingHints.VALUE_ANTIALIAS_OFF)
        return renderingHint
    }

    fun setTextRenderingHint(g: Graphics2D) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                if (antialiasAll) RenderingHints.VALUE_TEXT_ANTIALIAS_ON else RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                if (antialiasAll) RenderingHints.VALUE_ANTIALIAS_ON else RenderingHints.VALUE_ANTIALIAS_OFF)
    }

    fun initRoot() {
        rootContentLocation = Point()
        this.root = NodeViewFactory.Companion.getInstance().newNodeView(
                model.rootNode, 0, this, this)
        root!!.insert()
        revalidate()
    }

    //
    // Navigation
    //
    internal inner class CheckLaterForCenterNodeTask(var mNode: NodeView?) : TimerTask() {
        override fun run() {
            centerNode(mNode)
        }
    }

    /**
     * Problem: Before scrollRectToVisible is called, the node has the location
     * (0,0), ie. the location first gets calculated after the scrollpane is
     * actually scrolled. Thus, as a workaround, I simply call
     * scrollRectToVisible twice, the first time the location of the node is
     * calculated, the second time the scrollPane is actually scrolled.
     */
    fun centerNode(node: NodeView?) {
        // FIXME: Correct the resize map behaviour.
        waitForEventQueue()
        if (!isValid) {
            mCenterNodeTimer.schedule(CheckLaterForCenterNodeTask(node),
                    100)
            return
        }
        val d = viewportSize
        val content = node.getContent()
        val rect = Rectangle(content!!.width / 2 - d!!.width / 2,
                content!!.height / 2 - d.height / 2, d.width, d.height)
        logger!!.fine("Scroll to " + rect + ", " + this.preferredSize)

        // One call of scrollRectToVisible suffices
        // after patching the FreeMind.java
        // and the FreeMindApplet
        content!!.scrollRectToVisible(rect)
    }

    // scroll with extension (PN)
    // e.g. the input field is bigger than the node view => scroll in order to
    // fit the input field into the screen
    // scroll with extension (PN 6.2)
    @JvmOverloads
    fun scrollNodeToVisible(node: NodeView?, extraWidth: Int = 0) {
        // see centerNode()
        if (!isValid) {
            nodeToBeVisible = node
            this.extraWidth = extraWidth
            return
        }
        val HORIZ_SPACE = 10
        val HORIZ_SPACE2 = 20
        val VERT_SPACE = 5
        val VERT_SPACE2 = 10

        // get left/right dimension
        val nodeContent = node.getContent()
        var width = nodeContent!!.width
        if (extraWidth < 0) { // extra left width
            width -= extraWidth
            nodeContent!!.scrollRectToVisible(Rectangle(-HORIZ_SPACE
                    + extraWidth, -VERT_SPACE, width + HORIZ_SPACE2,
                    nodeContent!!.height + VERT_SPACE2))
        } else { // extra right width
            width += extraWidth
            nodeContent!!.scrollRectToVisible(Rectangle(-HORIZ_SPACE,
                    -VERT_SPACE, width + HORIZ_SPACE2, nodeContent!!.height
                    + VERT_SPACE2))
        }
    }

    /**
     * Scroll the viewport of the map to the south-west, i.e. scroll the map
     * itself to the north-east.
     */
    fun scrollBy(x: Int, y: Int) {
        val currentPoint = viewPosition!!
        currentPoint.translate(x, y) // Add the difference to it
        setViewLocation(currentPoint.x, currentPoint.y)
    }

    fun setViewLocation(x: Int, y: Int) {
        val currentPoint = Point(x, y)
        // Watch for the boundaries
        // Low boundaries
        if (currentPoint.getX() < 0) {
            currentPoint.setLocation(0.0, currentPoint.getY())
        }
        if (currentPoint.getY() < 0) {
            currentPoint.setLocation(currentPoint.getX(), 0.0)
        }
        // High boundaries
        val viewportSize = viewportSize ?: return
        val size = size
        // getView() gets viewed area - JPanel
        val maxX = size.getWidth() - viewportSize.getWidth()
        val maxY = size.getHeight() - viewportSize.getHeight()
        if (currentPoint.getX() > maxX) {
            currentPoint.setLocation(maxX, currentPoint.getY())
        }
        if (currentPoint.getY() > maxY) {
            currentPoint.setLocation(currentPoint.getX(), maxY)
        }
        viewPosition = currentPoint
    }

    //
    // Node Navigation
    //
    private fun getVisibleLeft(oldSelected: NodeView?): NodeView? {
        var newSelected = oldSelected
        if (oldSelected.getModel().isRoot) {
            newSelected = oldSelected!!.getPreferredVisibleChild(true)
        } else if (!oldSelected!!.isLeft) {
            newSelected = oldSelected.visibleParentView
        } else {
            // If folded in the direction, unfold
            if (oldSelected.getModel().isFolded) {
                viewFeedback.setFolded(oldSelected.getModel(),
                        false)
                return oldSelected
            }
            newSelected = oldSelected.getPreferredVisibleChild(true)
            while (newSelected != null && !newSelected.isContentVisible) {
                newSelected = newSelected.getPreferredVisibleChild(true)
            }
        }
        return newSelected
    }

    private fun getVisibleRight(oldSelected: NodeView?): NodeView? {
        var newSelected = oldSelected
        if (oldSelected.getModel().isRoot) {
            newSelected = oldSelected!!.getPreferredVisibleChild(false)
        } else if (oldSelected!!.isLeft) {
            newSelected = oldSelected.visibleParentView
        } else {
            // If folded in the direction, unfold
            if (oldSelected.getModel().isFolded) {
                viewFeedback.setFolded(oldSelected.getModel(),
                        false)
                return oldSelected
            }
            newSelected = oldSelected.getPreferredVisibleChild(false)
            while (newSelected != null && !newSelected.isContentVisible) {
                newSelected = newSelected.getPreferredVisibleChild(false)
            }
        }
        return newSelected
    }

    private fun getVisibleNeighbour(directionCode: Int): NodeView? {
        val oldSelected = getSelected()
        logger!!.fine("Old selected: $oldSelected")
        var newSelected: NodeView? = null
        when (directionCode) {
            KeyEvent.VK_LEFT -> {
                newSelected = getVisibleLeft(oldSelected)
                if (newSelected != null) {
                    siblingMaxLevel = newSelected.getModel().nodeLevel
                }
                return newSelected
            }
            KeyEvent.VK_RIGHT -> {
                newSelected = getVisibleRight(oldSelected)
                if (newSelected != null) {
                    siblingMaxLevel = newSelected.getModel().nodeLevel
                }
                return newSelected
            }
            KeyEvent.VK_UP -> newSelected = oldSelected.getPreviousVisibleSibling()
            KeyEvent.VK_DOWN -> newSelected = oldSelected.getNextVisibleSibling()
            KeyEvent.VK_PAGE_UP -> newSelected = oldSelected.getPreviousPage()
            KeyEvent.VK_PAGE_DOWN -> newSelected = oldSelected.getNextPage()
        }
        return if (newSelected !== oldSelected) newSelected else null
    }

    fun move(e: KeyEvent) {
        val newSelected = getVisibleNeighbour(e.keyCode)
        logger!!.fine("New selected: $newSelected")
        if (newSelected != null) {
            if (!(newSelected === getSelected())) {
                extendSelectionWithKeyMove(newSelected, e)
                scrollNodeToVisible(newSelected)
            }
            e.consume()
        }
    }

    fun resetShiftSelectionOrigin() {
        shiftSelectionOrigin = null
    }

    private fun extendSelectionWithKeyMove(newlySelectedNodeView: NodeView,
                                           e: KeyEvent) {
        if (e.isShiftDown) {
            // left or right
            if (e.keyCode == KeyEvent.VK_LEFT
                    || e.keyCode == KeyEvent.VK_RIGHT) {
                shiftSelectionOrigin = null
                val toBeNewSelected = if (newlySelectedNodeView
                                .isParentOf(getSelected())) newlySelectedNodeView else getSelected()!!
                selectBranch(toBeNewSelected, false)
                makeTheSelected(toBeNewSelected)
                return
            }
            if (shiftSelectionOrigin == null) {
                shiftSelectionOrigin = getSelected()
            }
            val newY = getMainViewY(newlySelectedNodeView)
            val selectionOriginY = getMainViewY(shiftSelectionOrigin)
            val deltaY = newY - selectionOriginY
            var currentSelected = getSelected()

            // page up and page down
            if (e.keyCode == KeyEvent.VK_PAGE_UP) {
                while (true) {
                    val currentSelectedY = getMainViewY(currentSelected)
                    if (currentSelectedY > selectionOriginY) deselect(currentSelected) else makeTheSelected(currentSelected)
                    if (currentSelectedY <= newY) break
                    currentSelected = currentSelected
                            .getPreviousVisibleSibling()
                }
                return
            }
            if (e.keyCode == KeyEvent.VK_PAGE_DOWN) {
                while (true) {
                    val currentSelectedY = getMainViewY(currentSelected)
                    if (currentSelectedY < selectionOriginY) deselect(currentSelected) else makeTheSelected(currentSelected)
                    if (currentSelectedY >= newY) break
                    currentSelected = currentSelected.getNextVisibleSibling()
                }
                return
            }
            val enlargingMove = deltaY > 0
                    && e.keyCode == KeyEvent.VK_DOWN || deltaY < 0
                    && e.keyCode == KeyEvent.VK_UP
            if (enlargingMove) {
                toggleSelected(newlySelectedNodeView)
            } else {
                toggleSelected(getSelected())
                makeTheSelected(newlySelectedNodeView)
            }
        } else {
            shiftSelectionOrigin = null
            selectAsTheOnlyOneSelected(newlySelectedNodeView)
        }
    }

    private fun getMainViewY(node: NodeView?): Int {
        val newSelectedLocation = Point()
        convertPointToAncestor(node!!.mainView!!, newSelectedLocation,
                this)
        return newSelectedLocation.y
    }

    fun moveToRoot() {
        selectAsTheOnlyOneSelected(root)
        centerNode(root)
    }

    override fun select(node: NodeView?) {
        if (node == null) {
            logger!!.warning("Select with null NodeView called!")
            return
        }
        scrollNodeToVisible(node)
        selectAsTheOnlyOneSelected(node)
        // this level is default
        siblingMaxLevel = node.getModel().nodeLevel
    }

    /**
     * Select the node, resulting in only that one being selected.
     */
    open fun selectAsTheOnlyOneSelected(newSelected: NodeView?) {
        logger!!.finest("selectAsTheOnlyOneSelected")
        val oldSelecteds = selecteds
        // select new node
        selected.clear()
        selected.add(newSelected)

        // set last focused as preferred (PN)
        if (newSelected.getModel().parentNode != null) {
            (newSelected!!.parent as NodeView).setPreferredChild(newSelected)
        }
        scrollNodeToVisible(newSelected)
        newSelected!!.repaintSelected()
        for (oldSelected in oldSelecteds) {
            oldSelected?.repaintSelected()
        }
    }

    /**
     * Add the node to the selection if it is not yet there, remove it
     * otherwise.
     */
    fun toggleSelected(newSelected: NodeView?) {
        logger!!.finest("toggleSelected")
        var oldSelected = getSelected()
        if (isSelected(newSelected)) {
            if (selected.size() > 1) {
                selected.remove(newSelected)
                oldSelected = newSelected
            }
        } else {
            selected.add(newSelected)
        }
        getSelected()!!.repaintSelected()
        oldSelected?.repaintSelected()
    }

    /**
     * Add the node to the selection if it is not yet there, making it the
     * focused selected node.
     */
    fun makeTheSelected(newSelected: NodeView?) {
        logger!!.finest("makeTheSelected")
        if (isSelected(newSelected)) {
            selected.moveToFirst(newSelected)
        } else {
            selected.add(newSelected)
        }
        getSelected()!!.repaintSelected()
    }

    override fun deselect(newSelected: NodeView?) {
        if (isSelected(newSelected)) {
            selected.remove(newSelected)
            newSelected!!.repaintSelected()
        }
    }

    /**
     * Select the node and his descendants. On extend = false clear up the
     * previous selection. if extend is false, the past selection will be empty.
     * if yes, the selection will extended with this node and its children
     */
    fun selectBranch(newlySelectedNodeView: NodeView?, extend: Boolean) {
        // if (!extend || !isSelected(newlySelectedNodeView))
        // toggleSelected(newlySelectedNodeView);
        if (!extend) {
            selectAsTheOnlyOneSelected(newlySelectedNodeView)
        } else if (!isSelected(newlySelectedNodeView)
                && newlySelectedNodeView.isContentVisible()) {
            toggleSelected(newlySelectedNodeView)
        }
        // select(newSelected,extend);
        for (target in newlySelectedNodeView.getChildrenViews()) {
            selectBranch(target, true)
        }
    }

    fun selectContinuous(newSelected: NodeView): Boolean {
        /* fc, 25.1.2004: corrected due to completely inconsistent behaviour. */
        var oldSelected: NodeView? = null
        // search for the last already selected item among the siblings:
        val selList = selecteds
        val j: ListIterator<NodeView?> = selList.listIterator()
        while (j.hasNext()) {
            val selectedNode = j.next()
            if (selectedNode !== newSelected && newSelected.isSiblingOf(selectedNode)) {
                oldSelected = selectedNode
                break
            }
        }
        // no such sibling found. select the new one, and good bye.
        if (oldSelected == null) {
            if (!isSelected(newSelected) && newSelected.isContentVisible) {
                toggleSelected(newSelected)
                return true
            }
            return false
        }
        // fc, bug fix: only select the nodes on the same side:
        val oldPositionLeft = oldSelected.isLeft
        val newPositionLeft = newSelected.isLeft
        /* find old starting point. */
        var i: ListIterator<NodeView?> = newSelected.siblingViews.listIterator()
        while (i.hasNext()) {
            val nodeView = i.next()
            if (nodeView === oldSelected) {
                break
            }
        }
        /*
		 * Remove all selections for the siblings in the connected component
		 * between old and new.
		 */
        val i_backup = i
        while (i.hasNext()) {
            val nodeView = i.next()
            if (nodeView!!.isLeft == oldPositionLeft || nodeView.isLeft == newPositionLeft) {
                if (isSelected(nodeView)) deselect(nodeView) else break
            }
        }
        /* other direction. */i = i_backup
        if (i.hasPrevious()) {
            i.previous() /* this is old selected! */
            while (i.hasPrevious()) {
                val nodeView = i.previous()
                if (nodeView!!.isLeft == oldPositionLeft
                        || nodeView.isLeft == newPositionLeft) {
                    if (isSelected(nodeView)) deselect(nodeView) else break
                }
            }
        }
        /* reset iterator */i = newSelected.siblingViews.listIterator()
        /* find starting point. */i = newSelected.siblingViews.listIterator()
        while (i.hasNext()) {
            val nodeView = i.next()
            if (nodeView === newSelected || nodeView === oldSelected) {
                if (!isSelected(nodeView) && nodeView.isContentVisible) toggleSelected(nodeView)
                break
            }
        }
        /* select all up to the end point. */while (i.hasNext()) {
            val nodeView = i.next()
            if ((nodeView!!.isLeft == oldPositionLeft || nodeView.isLeft == newPositionLeft)
                    && !isSelected(nodeView) && nodeView.isContentVisible) toggleSelected(nodeView)
            if (nodeView === newSelected || nodeView === oldSelected) {
                break
            }
        }
        // now, make oldSelected the last of the list in order to make this
        // repeatable:
        toggleSelected(oldSelected)
        toggleSelected(oldSelected)
        return true
    }

    // e.g. for dragging cursor (PN)
    fun setMoveCursor(isHand: Boolean) {
        val requiredCursor = if (isHand && !disableMoveCursor) Cursor.MOVE_CURSOR else Cursor.DEFAULT_CURSOR
        if (cursor.type != requiredCursor) {
            cursor = if (requiredCursor != Cursor.DEFAULT_CURSOR) Cursor(
                    requiredCursor) else null
        }
    }

    val nodeMouseMotionListener: NodeMouseMotionListener?
        get() = viewFeedback.nodeMouseMotionListener
    val nodeMotionListener: NodeMotionListener?
        get() = viewFeedback.nodeMotionListener
    val nodeKeyListener: NodeKeyListener?
        get() = viewFeedback.nodeKeyListener
    val nodeDragListener: DragGestureListener?
        get() = viewFeedback.nodeDragListener
    val nodeDropListener: DropTargetListener?
        get() = viewFeedback.nodeDropListener

    override fun getSelected(): NodeView? {
        return if (selected.size() > 0) selected[0] else null
    }

    private fun getSelected(i: Int): NodeView? {
        return selected[i]
    }

    // return an ArrayList of NodeViews.
    override val selecteds: LinkedList<NodeView?>
        get() {
            // return an ArrayList of NodeViews.
            val result = LinkedList<NodeView?>()
            for (i in 0 until selected.size()) {
                result.add(getSelected(i))
            }
            return result
        }// do the sorting:

    // logger.fine("Cutting #" + selectedNodes.size());
    // for (Iterator it = selectedNodes.iterator(); it.hasNext();) {
    // MindMapNode node = (MindMapNode) it.next();
    // logger.fine("Cutting " + node);
    // }
    /**
     * @return an ArrayList of MindMapNode objects. If both ancestor and
     * descendant node are selected, only the ancestor is returned
     */
    override val selectedNodesSortedByY: ArrayList<MindMapNode>
        get() {
            val selectedNodesSet = HashSet<MindMapNode?>()
            for (i in 0 until selected.size()) {
                selectedNodesSet.add(getSelected(i).getModel())
            }
            val pointNodePairs = LinkedList<Tools.Pair>()
            val point = Point()
            iteration@ for (i in 0 until selected.size()) {
                val view = getSelected(i)
                val node = view.getModel()
                run {
                    var parent = node!!.parentNode
                    while (parent != null) {
                        if (selectedNodesSet.contains(parent)) {
                            continue@iteration
                        }
                        parent = parent
                                .parentNode
                    }
                }
                view.getContent().getLocation(point)
                convertPointToAncestor(view!!, point, this)
                pointNodePairs.add(Tools.Pair(point.y, node!!))
            }
            // do the sorting:
            Collections.sort(pointNodePairs) { pair0, pair1 ->
                val int0 = pair0.first as Int
                val int1 = pair1.first as Int
                int0.compareTo(int1)
            }
            val selectedNodes = ArrayList<MindMapNode>()
            val it: Iterator<Tools.Pair> = pointNodePairs.iterator()
            while (it.hasNext()) {
                selectedNodes.add(it.next().second as MindMapNode)
            }

            // logger.fine("Cutting #" + selectedNodes.size());
            // for (Iterator it = selectedNodes.iterator(); it.hasNext();) {
            // MindMapNode node = (MindMapNode) it.next();
            // logger.fine("Cutting " + node);
            // }
            return selectedNodes
        }

    /**
     * @return an ArrayList of MindMapNode objects. If both ancestor and
     * descandant node are selected, only the ancestor ist returned
     */
    val singleSelectedNodes: ArrayList<MindMapNode?>
        get() {
            val selectedNodes = ArrayList<MindMapNode?>(selected.size())
            for (i in selected.size() - 1 downTo 0) {
                selectedNodes.add(getSelected(i).getModel().shallowCopy())
            }
            return selectedNodes
        }

    override fun isSelected(n: NodeView?): Boolean {
        return if (isCurrentlyPrinting) false else selected.contains(n)
    }

    fun getZoom(): Float {
        return zoom
    }

    fun getZoomed(number: Int): Int {
        return (number * zoom).toInt()
    }

    fun setZoom(zoom: Float) {
        this.zoom = zoom
        root!!.updateAll()
        revalidate()
        nodeToBeVisible = getSelected()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#validateTree()
	 */
    override fun validateTree() {
        validateSelecteds()
        super.validateTree()
        setViewPositionAfterValidate()
    }

    private fun setViewPositionAfterValidate() {
        var viewPosition = viewPosition!!
        val oldRootContentLocation = rootContentLocation
        val root = root
        val newRootContentLocation = root.getContent().location
        convertPointToAncestor(root!!, newRootContentLocation,
                parent)
        val deltaX = newRootContentLocation.x - oldRootContentLocation!!.x
        val deltaY = newRootContentLocation.y - oldRootContentLocation.y
        if (deltaX != 0 || deltaY != 0) {
            viewPosition.x += deltaX
            viewPosition.y += deltaY
            var scrollMode = scrollMode
            // avoid immediate scrolling here:
            scrollMode = JViewport.SIMPLE_SCROLL_MODE
            viewPosition = viewPosition
            scrollMode = scrollMode
        } else {
            // FIXME: fc, 7.9.2011: Here, a viewport->repaint was previously.
            // Test if really needed.
            repaint()
        }
        if (nodeToBeVisible != null) {
            var scrollMode = scrollMode
            scrollMode = JViewport.SIMPLE_SCROLL_MODE
            scrollNodeToVisible(nodeToBeVisible, extraWidth)
            scrollMode = scrollMode
            nodeToBeVisible = null
        }
    }

    /*****************************************************************
     * P A I N T I N G **
     */
    // private static Image image = null;
    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
    override fun paint(g: Graphics) {
        val startMilli = System.currentTimeMillis()
        if (isValid) {
            root.getContent().getLocation(rootContentLocation)
            convertPointToAncestor(root!!, rootContentLocation!!,
                    parent)
        }
        val g2 = g as Graphics2D
        val renderingHint = g2
                .getRenderingHint(RenderingHints.KEY_ANTIALIASING)
        val renderingTextHint = g2
                .getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING)
        setTextRenderingHint(g2)
        val oldRenderingHintFM = g2
                .getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS)
        val newRenderingHintFM = if (getZoom() != 1f) RenderingHints.VALUE_FRACTIONALMETRICS_ON else RenderingHints.VALUE_FRACTIONALMETRICS_OFF
        if (oldRenderingHintFM !== newRenderingHintFM) {
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    newRenderingHintFM)
        }
        super.paint(g)
        if (oldRenderingHintFM !== newRenderingHintFM
                && RenderingHints.KEY_FRACTIONALMETRICS
                        .isCompatibleValue(oldRenderingHintFM)) {
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    oldRenderingHintFM)
        }
        if (RenderingHints.KEY_ANTIALIASING.isCompatibleValue(renderingHint)) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, renderingHint)
        }
        if (RenderingHints.KEY_TEXT_ANTIALIASING
                        .isCompatibleValue(renderingTextHint)) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    renderingTextHint)
        }

        // final Rectangle rect = getInnerBounds();
        // g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        val localTime = System.currentTimeMillis() - startMilli
        mPaintingAmount++
        mPaintingTime += localTime.toInt()
        logger!!.fine("End paint of " + model.restorable + " in "
                + localTime + ". Mean time:"
                + mPaintingTime / mPaintingAmount)
    }

    public override fun paintChildren(graphics: Graphics) {
        val labels = HashMap<String, NodeView?>()
        mArrowLinkViews = Vector()
        collectLabels(this.root, labels)
        super.paintChildren(graphics)
        val graphics2d = graphics as Graphics2D
        val renderingHint = setEdgesRenderingHint(graphics2d)
        paintLinks(this.root, graphics2d, labels, null)
        restoreAntialiasing(graphics2d, renderingHint)
        paintSelecteds(graphics2d)
    }

    private fun paintSelecteds(g: Graphics2D) {
        if (!standardDrawRectangleForSelection || isCurrentlyPrinting) {
            return
        }
        val c = g.color
        val s = g.stroke
        g.color = standardSelectRectangleColor
        if (standardSelectionStroke == null) {
            standardSelectionStroke = BasicStroke(2.0f)
        }
        g.stroke = standardSelectionStroke
        val renderingHint = setEdgesRenderingHint(g)
        val i: Iterator<NodeView?> = selecteds.iterator()
        while (i.hasNext()) {
            paintSelected(g, i.next())
        }
        restoreAntialiasing(g, renderingHint)
        g.color = c
        g.stroke = s
    }

    private fun paintSelected(g: Graphics2D, selected: NodeView?) {
        val arcWidth = 4
        val content = selected.getContent()
        val contentLocation = Point()
        convertPointToAncestor(content!!, contentLocation, this)
        g.drawRoundRect(contentLocation.x - arcWidth, contentLocation.y
                - arcWidth, content!!.width + 2 * arcWidth,
                content!!.height + 2 * arcWidth, 15, 15)
    }

    /** collect all existing labels in the current map.  */
    protected fun collectLabels(source: NodeView?, labels: HashMap<String, NodeView?>) {
        // check for existing registry:
        if (model.linkRegistry == null) return
        // apply own label:
        val label = model.linkRegistry!!.getLabel(source.getModel())
        if (label != null) labels[label] = source
        for (target in source.getChildrenViews()) {
            collectLabels(target, labels)
        }
    }

    protected fun paintLinks(source: NodeView?, graphics: Graphics2D,
                             labels: HashMap<String, NodeView?>?, LinkAlreadyVisited: HashSet<MindMapLink?>?) {
        // check for existing registry:
        var LinkAlreadyVisited = LinkAlreadyVisited
        if (model.linkRegistry == null) return
        if (LinkAlreadyVisited == null) LinkAlreadyVisited = HashSet()
        // references first
        // logger.fine("Searching for links of " +
        // source.getModel().toString());
        // paint own labels:
        val vec = model.linkRegistry!!
                .getAllLinks(source.getModel())
        for (i in vec.indices) {
            val ref = vec[i]
            if (LinkAlreadyVisited.add(ref)) {
                // determine type of link
                if (ref is MindMapArrowLink) {
                    val arrowLink = ArrowLinkView(
                            ref,
                            getNodeView(ref.source),
                            getNodeView(ref.target))
                    arrowLink.paint(graphics)
                    mArrowLinkViews!!.add(arrowLink)
                    // resize map?
                    // adjust container size
                    // Rectangle rec = arrowLink.getBounds();
                    // the following does not work correctly. fc, 23.10.2003:
                    // if (rec.x < 0) {
                    // getMindMapLayout().resizeMap(rec.x);
                    // } else if (rec.x+rec.width > getSize().width) {
                    // getMindMapLayout().resizeMap(rec.x+rec.width);
                    // }
                }
            }
        }
        for (target in source.getChildrenViews()) {
            paintLinks(target, graphics, labels, LinkAlreadyVisited)
        }
    }

    fun detectCollision(p: Point): MindMapArrowLink? {
        if (mArrowLinkViews == null) return null
        for (i in mArrowLinkViews!!.indices) {
            val arrowView = mArrowLinkViews!![i] as ArrowLinkView
            if (arrowView.detectCollision(p)) return arrowView.model
        }
        return null
    }

    /**
     * Call preparePrinting() before printing and endPrinting() after printing
     * to minimize calculation efforts
     */
    fun preparePrinting() {
        if (!isCurrentlyPrinting) {
            isCurrentlyPrinting = true
            /* repaint for printing: */if (NEED_PREF_SIZE_BUG_FIX) {
                root!!.updateAll()
                validate()
            } else {
                repaintSelecteds()
            }
            if (printOnWhiteBackground) {
                background = getBackground()
                setBackground(Color.WHITE)
            }
            boundingRectangle = innerBounds
            fitToPage = Resources.getInstance().getBoolProperty("fit_to_page")
        } else {
            logger!!.warning("Called preparePrinting although isPrinting is true.")
        }
    }

    private fun repaintSelecteds() {
        val iterator: Iterator<NodeView?> = selecteds.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next!!.repaintSelected()
        }
        // repaint();
    }

    /**
     * Call preparePrinting() before printing and endPrinting() after printing
     * to minimize calculation efforts
     */
    fun endPrinting() {
        if (isCurrentlyPrinting) {
            isCurrentlyPrinting = false
            if (printOnWhiteBackground) {
                setBackground(background)
            }
            /* repaint for end printing: */if (NEED_PREF_SIZE_BUG_FIX) {
                root!!.updateAll()
                validate()
            } else {
                repaintSelecteds()
            }
        } else {
            logger!!.warning("Called endPrinting although isPrinting is false.")
        }
    }

    override fun print(graphics: Graphics, pageFormat: PageFormat, pageIndex: Int): Int {
        // TODO:
        // ask user for :
        // - center in page (in page format ?)
        // - print zoom or maximize (in page format ?)
        // - print selection only
        // remember those parameters from one session to another
        // (as orientation & margin from pf)

        // User parameters
        var userZoomFactor = 1.0
        try {
            userZoomFactor = viewFeedback
                    .getProperty("user_zoom").toDouble()
        } catch (e: Exception) {
            // freemind.main.Resources.getInstance().logException(e);
        }
        userZoomFactor = Math.max(0.0, userZoomFactor)
        userZoomFactor = Math.min(2.0, userZoomFactor)

        // TODO: read user parameters from properties, make sure the multiple
        // page
        // printing really works, have look at Book class.
        if (fitToPage && pageIndex > 0) {
            return Printable.NO_SUCH_PAGE
        }
        val graphics2D = graphics as Graphics2D
        try {
            preparePrinting()
            var zoomFactor = 1.0
            if (fitToPage) {
                val zoomFactorX = (pageFormat.imageableWidth
                        / boundingRectangle!!.getWidth())
                val zoomFactorY = (pageFormat.imageableHeight
                        / boundingRectangle!!.getHeight())
                zoomFactor = Math.min(zoomFactorX, zoomFactorY)
            } else {
                zoomFactor = userZoomFactor
                val nrPagesInWidth = Math.ceil(zoomFactor
                        * boundingRectangle!!.getWidth()
                        / pageFormat.imageableWidth).toInt()
                val nrPagesInHeight = Math.ceil(zoomFactor
                        * boundingRectangle!!.getHeight()
                        / pageFormat.imageableHeight).toInt()
                if (pageIndex >= nrPagesInWidth * nrPagesInHeight) {
                    return Printable.NO_SUCH_PAGE
                }
                val yPageCoord = Math.floor((pageIndex / nrPagesInWidth).toDouble()).toInt()
                val xPageCoord = pageIndex - yPageCoord * nrPagesInWidth
                graphics2D.translate(-pageFormat.imageableWidth
                        * xPageCoord, -pageFormat.imageableHeight
                        * yPageCoord)
            }
            graphics2D.translate(pageFormat.imageableX,
                    pageFormat.imageableY)
            graphics2D.scale(zoomFactor, zoomFactor)
            graphics2D.translate(-boundingRectangle!!.getX(),
                    -boundingRectangle!!.getY())
            print(graphics2D)
        } finally {
            endPrinting()
        }
        return Printable.PAGE_EXISTS
    }
    // public void print(Graphics g) {
    // try{
    // preparePrinting();
    // super.print(g);
    // }
    // finally{
    // endPrinting();
    // }
    // }
    /**
     * Return the bounding box of all the descendants of the source view, that
     * without BORDER. Should that be implemented in LayoutManager as minimum
     * size?
     */
    val innerBounds: Rectangle
        get() {
            val innerBounds = root.getInnerBounds()
            innerBounds!!.x += root!!.x
            innerBounds!!.y += root!!.y
            val maxBounds = Rectangle(0, 0, width, height)
            for (i in mArrowLinkViews!!.indices) {
                val arrowView = mArrowLinkViews!![i] as ArrowLinkView
                val arrowLinkCurve = arrowView.arrowLinkCurve ?: continue
                val arrowViewBigBounds = arrowLinkCurve.bounds
                if (!innerBounds!!.contains(arrowViewBigBounds)) {
                    val arrowViewBounds = PathBBox.getBBox(arrowLinkCurve)
                            .bounds
                    innerBounds!!.add(arrowViewBounds)
                }
            }
            return innerBounds!!.intersection(maxBounds)
        }
    private val mCenterNodeTimer: Timer

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.Autoscroll#getAutoscrollInsets()
	 */
    override fun getAutoscrollInsets(): Insets {
        val outer = bounds
        val inner = parent.bounds
        return Insets(inner.y - outer.y + margin, inner.x - outer.x
                + margin, outer.height - inner.height - inner.y + outer.y
                + margin, outer.width - inner.width - inner.x + outer.x
                + margin)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.dnd.Autoscroll#autoscroll(java.awt.Point)
	 */
    override fun autoscroll(cursorLocn: Point) {
        val r = Rectangle(cursorLocn.getX().toInt() - margin,
                cursorLocn.getY().toInt() - margin, 1 + 2 * margin,
                1 + 2 * margin)
        scrollRectToVisible(r)
    }

    override fun getNodeView(node: MindMapNode?): NodeView? {
        if (node == null) {
            return null
        }
        val viewers: Collection<NodeView> = getViewers(node)
        val iterator = viewers.iterator()
        while (iterator.hasNext()) {
            val candidateView = iterator.next()
            if (candidateView.map === this) {
                return candidateView
            }
        }
        return null
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
    override fun getPreferredSize(): Dimension {
        return if (!parent.isValid) {
            layout
                    .preferredLayoutSize(this)
        } else super.getPreferredSize()
    }

    fun revalidateSelecteds() {
        selectedsValid = false
    }

    private fun validateSelecteds() {
        if (selectedsValid) {
            return
        }
        selectedsValid = true
        // Keep selected nodes
        logger!!.finest("validateSelecteds")
        val selectedNodes = ArrayList<NodeView>()
        for (nodeView in selecteds) {
            if (nodeView != null) {
                selectedNodes.add(nodeView)
            }
        }
        // Warning, the old views still exist, because JVM has not deleted them.
        // But don't use them!
        selected.clear()
        for (oldNodeView in selectedNodes) {
            if (oldNodeView.isContentVisible) {
                val newNodeView = getNodeView(oldNodeView.getModel())
                // test, whether or not the node is still visible:
                if (newNodeView != null) {
                    selected.add(newNodeView)
                }
            }
        }
    }

    fun getNodeContentLocation(nodeView: NodeView): Point {
        val contentXY = Point(0, 0)
        convertPointToAncestor(nodeView.content, contentXY, this)
        return contentXY
    }

    /**
     * Returns the size of the visible part of the view in view coordinates.
     */
    val viewportSize: Dimension?
        get() {
            if (parent is JViewport) {
                val mapViewport = parent as JViewport
                return mapViewport?.size
            }
            return null
        }

    /**
     * @return the position of the view or null, if not present.
     */
    var viewPosition: Point?
        get() {
            var viewPosition = Point(0, 0)
            if (parent is JViewport) {
                val mapViewport = parent as JViewport
                viewPosition = mapViewport.viewPosition
            }
            return viewPosition
        }
        protected set(currentPoint) {
            if (parent is JViewport) {
                val mapViewport = parent as JViewport
                mapViewport.viewPosition = currentPoint
            }
        }
    /**
     * @return
     */
    /**
     * @param pSimpleScrollMode
     */
    private var scrollMode: Int
        private get() {
            if (parent is JViewport) {
                val mapViewport = parent as JViewport
                return mapViewport.scrollMode
            }
            return 0
        }
        private set(pSimpleScrollMode) {
            if (parent is JViewport) {
                val mapViewport = parent as JViewport
                mapViewport.scrollMode = pSimpleScrollMode
            }
        }
    private var views: HashMap<MindMapNode?, Vector<NodeView>>? = null

    init {
        if (logger == null) logger = Resources.getInstance().getLogger(this.javaClass.name)
        mCenterNodeTimer = Timer()
        // initialize the standard colors.
        if (standardNodeTextColor == null) {
            try {
                val stdcolor = viewFeedback.getProperty(
                        FreeMind.RESOURCES_BACKGROUND_COLOR)
                standardMapBackgroundColor = xmlToColor(stdcolor)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                standardMapBackgroundColor = Color.WHITE
            }
            try {
                val stdcolor = viewFeedback.getProperty(
                        FreeMind.RESOURCES_NODE_TEXT_COLOR)
                standardNodeTextColor = xmlToColor(stdcolor)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                standardSelectColor = Color.WHITE
            }
            // initialize the selectedColor:
            try {
                val stdcolor = viewFeedback.getProperty(
                        FreeMind.RESOURCES_SELECTED_NODE_COLOR)
                standardSelectColor = xmlToColor(stdcolor)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                standardSelectColor = Color.BLUE.darker()
            }

            // initialize the selectedTextColor:
            try {
                val stdtextcolor = viewFeedback.getProperty(
                        FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR)
                standardSelectRectangleColor = xmlToColor(stdtextcolor)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                standardSelectRectangleColor = Color.WHITE
            }
            try {
                val drawCircle = viewFeedback.getProperty(
                        FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION)
                standardDrawRectangleForSelection = xmlToBoolean(drawCircle!!)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                standardDrawRectangleForSelection = false
            }
            try {
                val printOnWhite = viewFeedback.getProperty(
                        FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND)
                printOnWhiteBackground = xmlToBoolean(printOnWhite!!)
            } catch (ex: Exception) {
                Resources.getInstance().logException(ex)
                printOnWhiteBackground = true
            }
            // only created once:
            createPropertyChangeListener()
            // initialize antializing:
            propertyChangeListener!!.propertyChanged(FreeMindCommon.RESOURCE_ANTIALIAS, viewFeedback.getProperty(FreeMindCommon.RESOURCE_ANTIALIAS), null)
        }
        autoscrolls = true
        this.layout = MindMapLayout()
        initRoot()
        setBackground(standardMapBackgroundColor)
        addMouseListener(viewFeedback.mapMouseMotionListener)
        addMouseMotionListener(viewFeedback.mapMouseMotionListener)
        addMouseWheelListener(viewFeedback.mapMouseWheelListener)
        addKeyListener(nodeKeyListener)

        // fc, 20.6.2004: to enable tab for insert.
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, emptySet())
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, emptySet())
        setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, emptySet())
        // end change.

        // fc, 31.3.2013: set policy to achive that after note window close, the
        // current node is selected.
        focusTraversalPolicy = object : FocusTraversalPolicy() {
            override fun getLastComponent(pAContainer: Container): Component {
                return getDefaultComponent(pAContainer)
            }

            override fun getFirstComponent(pAContainer: Container): Component {
                return getDefaultComponent(pAContainer)
            }

            override fun getDefaultComponent(pAContainer: Container): Component {
                val defaultComponent: Component = getSelected()!!
                logger!!.fine("Focus traversal to: $defaultComponent")
                return defaultComponent
            }

            override fun getComponentBefore(pAContainer: Container,
                                            pAComponent: Component): Component {
                return getDefaultComponent(pAContainer)
            }

            override fun getComponentAfter(pAContainer: Container,
                                           pAComponent: Component): Component {
                return getDefaultComponent(pAContainer)
            }
        }
        this.isFocusTraversalPolicyProvider = true
        // like in excel - write a letter means edit (PN)
        // on the other hand it doesn't allow key navigation (sdfe)
        disableMoveCursor = Resources.getInstance().getBoolProperty(
                "disable_cursor_move_paper")
        addComponentListener(ResizeListener())
    }

    fun getViewers(pNode: MindMapNode?): MutableCollection<NodeView> {
        if (views == null) {
            views = HashMap()
        }
        if (!views!!.containsKey(pNode)) {
            views!![pNode] = Vector()
        }
        return views!![pNode]!!
    }

    fun addViewer(pNode: MindMapNode?, viewer: NodeView) {
        getViewers(pNode).add(viewer)
        pNode!!.addTreeModelListener(viewer)
    }

    fun removeViewer(pNode: MindMapNode?, viewer: NodeView) {
        val viewers = getViewers(pNode)
        viewers.remove(viewer)
        if (viewers.isEmpty()) {
            views!!.remove(pNode)
        }
        pNode!!.removeTreeModelListener(viewer)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.MindMapNode#acceptViewVisitor(freemind.view.mindmapview
	 * .NodeViewVisitor)
	 */
    fun acceptViewVisitor(pNode: MindMapNode?, visitor: NodeViewVisitor) {
        val iterator: Iterator<NodeView> = getViewers(pNode).iterator()
        while (iterator.hasNext()) {
            visitor.visit(iterator.next())
        }
    }

    companion object {
        // Logging:
        private var logger: Logger? = null
        var printOnWhiteBackground = false
        var standardMapBackgroundColor: Color? = null
        var standardSelectColor: Color? = null
        var standardSelectRectangleColor: Color? = null
        var standardNodeTextColor: Color? = null
        var standardDrawRectangleForSelection = false
        private var standardSelectionStroke: Stroke? = null
        private var propertyChangeListener: FreemindPropertyListener? = null

        //
        // Constructors
        //
        var NEED_PREF_SIZE_BUG_FIX = Controller.JAVA_VERSION
                .compareTo("1.5.0") < 0
        private var antialiasEdges = false
        private var antialiasAll = false
        private const val margin = 20
    }
}