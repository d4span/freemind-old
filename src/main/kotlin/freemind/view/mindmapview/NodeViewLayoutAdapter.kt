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
 * @author foltin
 * @date 11.07.2013
 */
abstract class NodeViewLayoutAdapter : NodeViewLayout {
    protected val LISTENER_VIEW_WIDTH = 10
    protected var location = Point()

    /**
     * @return Returns the view.
     */
    protected var view: NodeView? = null
        private set

    /**
     * @return Returns the model.
     */
    protected var model: MindMapNode? = null
        private set

    /**
     * @return Returns the childCount.
     */
    protected var childCount = 0
        private set

    /**
     * @return Returns the content.
     */
    protected var content: JComponent? = null
        private set

    /**
     * @return Returns the vGap.
     */
    var vGap = 0
        private set

    /**
     * @return Returns the spaceAround.
     */
    var spaceAround = 0
        private set

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
    }

    override fun addLayoutComponent(arg0: String, arg1: Component) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
    override fun removeLayoutComponent(arg0: Component) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
    override fun minimumLayoutSize(arg0: Container): Dimension {
        if (minDimension == null) minDimension = Dimension(0, 0)
        return minDimension!!
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
    override fun preferredLayoutSize(c: Container): Dimension {
        if (!c.isValid) {
            c.validate()
        }
        return c.size
    }

    override fun layoutContainer(c: Container) {
        setUp(c)
        layout()
        val location2 = view!!.location
        convertPointToAncestor(view, location2, view.getMap())
        //		logger.info("Layouting node '" + view.getModel() + "' to " + location2);
        layoutOtherItems()
        shutDown()
    }

    /**
     *
     */
    private fun layoutOtherItems() {
        val componentCount = view!!.componentCount
        for (i in 0 until componentCount) {
            val component = view!!.getComponent(i)
            if (component is NodeMotionListenerView) {
                layoutNodeMotionListenerView(component)
            } else if (component is NodeFoldingComponent) {
                layoutNodeFoldingComponent(component)
            }
        }
    }

    protected abstract fun layout()
    private fun setUp(c: Container) {
        val localView = c as NodeView
        val localChildCount = localView.componentCount - 1
        for (i in 0 until localChildCount) {
            localView.getComponent(i).validate()
        }
        view = localView
        model = localView.getModel()
        childCount = localChildCount
        content = localView.content
        if (model.isVisible) {
            vGap = view.getVGap()
        } else {
            vGap = view.getVisibleParentView().vGap
        }
        spaceAround = view.getMap().getZoomed(NodeView.Companion.SPACE_AROUND)
    }

    private fun shutDown() {
        view = null
        model = null
        content = null
        childCount = 0
        vGap = 0
        spaceAround = 0
    }

    protected fun getChildContentHeight(isLeft: Boolean): Int {
        val childCount = childCount
        if (childCount == 0) {
            return 0
        }
        var height = 0
        var count = 0
        for (i in 0 until childCount) {
            val component = view!!.getComponent(i)
            if (component is NodeView) {
                val child = component
                if (child.isLeft == isLeft) {
                    val additionalCloudHeigth = child
                            .additionalCloudHeigth
                    val contentHeight = child.content.height
                    height += contentHeight + additionalCloudHeigth
                    count++
                }
            }
        }
        return height + vGap * (count - 1)
    }

    /**
     * @param isLeft
     * @return a shift, which is less than or equal zero
     */
    protected fun getChildVerticalShift(isLeft: Boolean): Int {
        var shift = 0
        var isFirstNodeView = false
        var foundNodeView = false
        for (i in 0 until childCount) {
            val component = view!!.getComponent(i)
            if (component is NodeView) {
                val child = component
                if (child.isLeft == isLeft) {
                    val childShift = child.shift
                    if (childShift < 0 || isFirstNodeView) {
                        shift += childShift
                        isFirstNodeView = false
                    }
                    shift -= child.content.y - spaceAround
                    foundNodeView = true
                }
            }
        }
        if (foundNodeView) {
            shift -= spaceAround
        }
        return shift
    }

    protected val childHorizontalShift: Int
        protected get() {
            if (childCount == 0) return 0
            var shift = 0
            for (i in 0 until childCount) {
                val component = view!!.getComponent(i)
                if (component is NodeView) {
                    val child = component
                    var shiftCandidate: Int
                    if (child.isLeft) {
                        shiftCandidate = (-child.content.x
                                - child.content.width)
                        if (child.isContentVisible) {
                            shiftCandidate -= (child.hGap
                                    + child.additionalCloudHeigth / 2)
                        }
                    } else {
                        shiftCandidate = -child.content.x
                        if (child.isContentVisible) {
                            shiftCandidate += child.hGap
                        }
                    }
                    shift = Math.min(shift, shiftCandidate)
                }
            }
            return shift
        }

    /**
     * Implemented in the base class, as the root layout needs it, too.
     * @param childVerticalShift
     */
    protected fun placeRightChildren(childVerticalShift: Int) {
        val baseX = content!!.x + content!!.width
        var y = content!!.y + childVerticalShift
        var right = baseX + spaceAround
        var child: NodeView? = null
        for (i in 0 until childCount) {
            val componentC = view!!.getComponent(i)
            if (componentC is NodeView) {
                val component = componentC
                if (component.isLeft) {
                    continue
                }
                child = component
                val additionalCloudHeigth = child.additionalCloudHeigth / 2
                y += additionalCloudHeigth
                val shiftY = child.shift
                val childHGap = if (child.content.isVisible) child
                        .hGap else 0
                val x = baseX + childHGap - child.content.x
                if (shiftY < 0) {
                    child.setLocation(x, y)
                    y -= shiftY
                } else {
                    y += shiftY
                    child.setLocation(x, y)
                }
                //				logger.info("Place of child " + component.getModel().getText() + ": " + child.getLocation());
                y += (child.height - 2 * spaceAround + vGap
                        + additionalCloudHeigth)
                right = Math.max(right, x + child.width
                        + additionalCloudHeigth)
            }
        }
        val bottom = (content!!.y + content!!.height
                + spaceAround)
        if (child != null) {
            view!!.setSize(
                    right,
                    Math.max(
                            bottom,
                            child.y + child.height
                                    + child.additionalCloudHeigth / 2))
        } else {
            view!!.setSize(right, bottom)
        }
    }

    /**
     * Implemented in the base class, as the root layout needs it, too.
     * @param childVerticalShift
     */
    protected fun placeLeftChildren(childVerticalShift: Int) {
        val baseX = content!!.x
        var y = content!!.y + childVerticalShift
        var right = baseX + content!!.width + spaceAround
        var child: NodeView? = null
        for (i in 0 until childCount) {
            val componentC = view!!.getComponent(i)
            if (componentC is NodeView) {
                val component = componentC
                if (!component.isLeft) {
                    continue
                }
                child = component
                val additionalCloudHeigth = child.additionalCloudHeigth / 2
                y += additionalCloudHeigth
                val shiftY = child.shift
                val childHGap = if (child.content.isVisible) child
                        .hGap else 0
                val x = (baseX - childHGap - child.content.x
                        - child.content.width)
                if (shiftY < 0) {
                    child.setLocation(x, y)
                    y -= shiftY
                } else {
                    y += shiftY
                    child.setLocation(x, y)
                }
                y += (child.height - 2 * spaceAround + vGap
                        + additionalCloudHeigth)
                right = Math.max(right, x + child.width)
            }
        }
        val bottom = (content!!.y + content!!.height
                + spaceAround)
        if (child != null) {
            view!!.setSize(
                    right,
                    Math.max(
                            bottom,
                            child.y + child.height
                                    + child.additionalCloudHeigth / 2))
        } else {
            view!!.setSize(right, bottom)
        }
    }

    /* (non-Javadoc)
	 * @see freemind.view.mindmapview.NodeViewLayout#layoutNodeFoldingComponent(freemind.view.mindmapview.NodeFoldingListenerView)
	 */
    override fun layoutNodeFoldingComponent(
            pFoldingComponent: NodeFoldingComponent) {
        val movedView = pFoldingComponent.nodeView
        val location = movedView.foldingMarkPosition
        val content = movedView.content
        convertPointToAncestor(content!!, location!!, pFoldingComponent.parent)
        pFoldingComponent.setCorrectedLocation(location)
        val preferredSize = pFoldingComponent.preferredSize
        pFoldingComponent.setSize(preferredSize!!.width, preferredSize.height)
    }

    companion object {
        protected var logger: Logger? = null
        private var minDimension: Dimension? = null
    }
}