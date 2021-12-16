/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
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
/*
 * Created on 16.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
import java.awt.Dimension
import java.awt.event.ComponentEvent
import freemind.view.mindmapview.MapView
import javax.swing.JScrollPane
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import freemind.view.mindmapview.MapView.Selected
import java.awt.Color
import java.awt.Rectangle
import freemind.view.mindmapview.ArrowLinkView
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMind
import freemind.main.Tools
import freemind.main.FreeMindCommon
import java.awt.Graphics2D
import java.awt.RenderingHints
import freemind.view.mindmapview.NodeViewFactory
import java.lang.NumberFormatException
import java.util.TimerTask
import freemind.view.mindmapview.MapView.CheckLaterForCenterNodeTask
import javax.swing.JComponent
import kotlin.jvm.JvmOverloads
import javax.swing.JViewport
import java.util.LinkedList
import java.awt.Cursor
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DropTargetListener
import freemind.modes.MindMapNode
import java.util.Collections
import java.awt.Graphics
import java.awt.Stroke
import java.awt.BasicStroke
import freemind.modes.MindMapLink
import freemind.modes.MindMapArrowLink
import java.awt.print.PageFormat
import freemind.view.mindmapview.PathBBox
import java.awt.Insets
import freemind.view.mindmapview.MindMapLayout
import java.awt.KeyboardFocusManager
import java.awt.AWTKeyStroke
import java.awt.FocusTraversalPolicy
import freemind.view.mindmapview.NodeViewVisitor
import freemind.modes.EdgeAdapter
import freemind.view.mindmapview.EdgeView
import freemind.modes.MindMapEdge
import javax.swing.JLabel
import freemind.view.mindmapview.MainView
import javax.swing.SwingUtilities
import freemind.main.HtmlTools
import java.awt.GradientPaint
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
import java.awt.Shape
import freemind.view.mindmapview.ConvexHull
import java.awt.Polygon
import freemind.view.mindmapview.ConvexHull.thetaComparator
import freemind.modes.ModeController
import freemind.view.mindmapview.EditNodeBase.EditControl
import java.awt.event.FocusListener
import freemind.view.mindmapview.EditNodeBase
import javax.swing.JDialog
import javax.swing.JFrame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.BorderLayout
import javax.swing.WindowConstants
import freemind.view.mindmapview.EditNodeBase.EditDialog.DialogWindowListener
import javax.swing.JOptionPane
import javax.swing.text.JTextComponent
import java.awt.datatransfer.StringSelection
import javax.swing.JPopupMenu
import freemind.view.mindmapview.EditNodeBase.EditCopyAction
import java.awt.datatransfer.Clipboard
import java.awt.KeyEventDispatcher
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
import java.awt.geom.*
import java.awt.image.ImageObserver

internal object PathBBox {
    fun getBBox(s: Shape): Rectangle2D {
        var first = true
        val bounds = DoubleArray(4)
        val coords = DoubleArray(6)
        var curx = 0.0
        var cury = 0.0
        var movx = 0.0
        var movy = 0.0
        var cpx0: Double
        var cpy0: Double
        var cpx1: Double
        var cpy1: Double
        var endx: Double
        var endy: Double
        val pi = s.getPathIterator(null)
        while (!pi.isDone) {
            when (pi.currentSegment(coords)) {
                PathIterator.SEG_MOVETO -> {
                    run {
                        curx = coords[0]
                        movx = curx
                    }
                    run {
                        cury = coords[1]
                        movy = cury
                    }
                    if (first) {
                        bounds[2] = curx
                        bounds[0] = bounds[2]
                        bounds[3] = cury
                        bounds[1] = bounds[3]
                        first = false
                    } else {
                        accum(bounds, curx, cury)
                    }
                }
                PathIterator.SEG_LINETO -> {
                    curx = coords[0]
                    cury = coords[1]
                    accum(bounds, curx, cury)
                }
                PathIterator.SEG_QUADTO -> {
                    cpx0 = coords[0]
                    cpy0 = coords[1]
                    endx = coords[2]
                    endy = coords[3]
                    var t = findQuadZero(curx, cpx0, endx)
                    if (t > 0 && t < 1) {
                        accumQuad(bounds, t, curx, cury, cpx0, cpy0, endx, endy)
                    }
                    t = findQuadZero(cury, cpy0, endy)
                    if (t > 0 && t < 1) {
                        accumQuad(bounds, t, curx, cury, cpx0, cpy0, endx, endy)
                    }
                    curx = endx
                    cury = endy
                    accum(bounds, curx, cury)
                }
                PathIterator.SEG_CUBICTO -> {
                    cpx0 = coords[0]
                    cpy0 = coords[1]
                    cpx1 = coords[2]
                    cpy1 = coords[3]
                    endx = coords[4]
                    endy = coords[5]
                    var num = findCubicZeros(coords, curx, cpx0, cpx1, endx)
                    run {
                        var i = 0
                        while (i < num) {
                            accumCubic(bounds, coords[i], curx, cury, cpx0, cpy0, cpx1,
                                    cpy1, endx, endy)
                            i++
                        }
                    }
                    num = findCubicZeros(coords, cury, cpy0, cpy1, endy)
                    var i = 0
                    while (i < num) {
                        accumCubic(bounds, coords[i], curx, cury, cpx0, cpy0, cpx1,
                                cpy1, endx, endy)
                        i++
                    }
                    curx = endx
                    cury = endy
                    accum(bounds, curx, cury)
                }
                PathIterator.SEG_CLOSE -> {
                    // Original starting point already included
                    curx = movx
                    cury = movy
                }
            }
            pi.next()
        }
        return Rectangle2D.Double(bounds[0], bounds[1], bounds[2]
                - bounds[0], bounds[3] - bounds[1])
    }

    private fun accum(bounds: DoubleArray, x: Double, y: Double) {
        bounds[0] = Math.min(bounds[0], x)
        bounds[1] = Math.min(bounds[1], y)
        bounds[2] = Math.max(bounds[2], x)
        bounds[3] = Math.max(bounds[3], y)
    }

    private fun findQuadZero(cur: Double, cp: Double, end: Double): Double {
        // The polynomial form of the Quadratic is:
        // eqn[0] = cur;
        // eqn[1] = cp + cp - cur - cur;
        // eqn[2] = cur - cp - cp + end;
        // Since we want the derivative, we can calculate it in one step:
        // deriv[0] = cp + cp - cur - cur;
        // deriv[1] = 2 * (cur - cp - cp + end);
        // Since we really want the zero, we can calculate that in one step:
        // zero = -deriv[0] / deriv[1]
        return -(cp + cp - cur - cur) / (2.0 * (cur - cp - cp + end))
    }

    private fun accumQuad(bounds: DoubleArray, t: Double, curx: Double,
                          cury: Double, cpx0: Double, cpy0: Double, endx: Double, endy: Double) {
        val u = 1 - t
        val x = curx * u * u + 2.0 * cpx0 * t * u + endx * t * t
        val y = cury * u * u + 2.0 * cpy0 * t * u + endy * t * t
        accum(bounds, x, y)
    }

    private fun findCubicZeros(zeros: DoubleArray, cur: Double, cp0: Double,
                               cp1: Double, end: Double): Int {
        // The polynomial form of the Cubic is:
        // eqn[0] = cur;
        // eqn[1] = (cp0 - cur) * 3.0;
        // eqn[2] = (cp1 - cp0 - cp0 + cur) * 3.0;
        // eqn[3] = end + (cp0 - cp1) * 3.0 - cur;
        // Since we want the derivative, we can calculate it in one step:
        zeros[0] = (cp0 - cur) * 3.0
        zeros[1] = (cp1 - cp0 - cp0 + cur) * 6.0
        zeros[2] = (end + (cp0 - cp1) * 3.0 - cur) * 3.0
        val num = QuadCurve2D.solveQuadratic(zeros)
        var ret = 0
        for (i in 0 until num) {
            val t = zeros[i]
            if (t > 0 && t < 1) {
                zeros[ret] = t
                ret++
            }
        }
        return ret
    }

    private fun accumCubic(bounds: DoubleArray, t: Double, curx: Double,
                           cury: Double, cpx0: Double, cpy0: Double, cpx1: Double, cpy1: Double,
                           endx: Double, endy: Double) {
        val u = 1 - t
        val x = curx * u * u * u + 3.0 * cpx0 * t * u * u + (3.0 * cpx1 * t
                * t * u) + endx * t * t * t
        val y = cury * u * u * u + 3.0 * cpy0 * t * u * u + (3.0 * cpy1 * t
                * t * u) + endy * t * t * t
        accum(bounds, x, y)
    }
}