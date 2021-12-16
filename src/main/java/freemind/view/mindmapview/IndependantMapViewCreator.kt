/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
import java.awt.geom.CubicCurve2D
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
import java.awt.geom.AffineTransform
import java.awt.GradientPaint
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
import java.awt.Shape
import java.awt.geom.Rectangle2D
import java.awt.geom.QuadCurve2D
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
import java.net.URISyntaxException
import freemind.main.Tools.FileReaderCreator
import freemind.modes.MapAdapter
import freemind.view.mindmapview.IndependantMapViewCreator
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHookSubstituteUnknown
import freemind.main.*
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import javax.imageio.ImageIO
import java.text.MessageFormat
import freemind.view.MapModule
import freemind.view.ScalableImageIcon
import java.awt.image.ImageObserver
import java.io.*

/**
 * @author foltin
 * @date 28.09.2011
 */
class IndependantMapViewCreator : MapFeedbackAdapter() {
    private var mMap: MindMapMapModel? = null
    @Throws(FileNotFoundException::class, IOException::class, URISyntaxException::class)
    fun createMapViewForFile(inputFileName: String?, parent: JPanel,
                             pFreeMindMain: FreeMindMain?): MapView {
        mMap = MindMapMapModel(this)
        val readerCreator = FileReaderCreator(File(inputFileName))
        val node = mMap!!.loadTree(readerCreator, MapAdapter.sDontAskInstance)
        mMap!!.root = node
        val mapView = MapView(mMap!!, this)
        parent.add(mapView, BorderLayout.CENTER)
        mapView.bounds = parent.bounds
        waitForEventQueue()
        mapView.addNotify()
        return mapView
    }

    @Throws(FileNotFoundException::class, IOException::class, URISyntaxException::class)
    fun exportFileToPng(inputFileName: String?, outputFileName: String?,
                        pFreeMindMain: FreeMindMain?) {
        val parent = JPanel()
        val bounds = Rectangle(0, 0, 400, 600)
        parent.bounds = bounds
        val mapView = createMapViewForFile(inputFileName, parent,
                pFreeMindMain)
        // layout components:
        mapView.root.mainView.doLayout()
        parent.isOpaque = true
        parent.isDoubleBuffered = false // for better performance
        parent.doLayout()
        parent.validate() // this might not be necessary
        mapView.preparePrinting()
        parent.bounds = mapView.bounds
        printToFile(mapView, outputFileName, false, 0)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#getMap()
	 */
    override val map: MindMap?
        get() = mMap

    /* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#createNodeHook(java.lang.String, freemind.modes.MindMapNode)
	 */
    override fun createNodeHook(pLoadName: String?, pNode: MindMapNode): NodeHook? {
        return PermanentNodeHookSubstituteUnknown(pLoadName!!)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("java.awt.headless", "true")
            if (args.size != 2) {
                println("Export map to png.\nUsage:\n java -jar lib/freemind.jar freemind.view.mindmapview.IndependantMapViewCreator <map_path>.mm <picture_path>.png")
                System.exit(0)
            }
            val freeMindMain = FreeMindMainMock()
            val creator = IndependantMapViewCreator()
            try {
                val outputFileName = args[1]
                creator.exportFileToPng(args[0], outputFileName, freeMindMain)
                println("Export to $outputFileName done.")
                System.exit(0)
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            } catch (e: URISyntaxException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            }
            System.err.println("Error.")
            System.exit(1)
        }

        /**
         * @param parent
         * @param mapView
         * @param outputFileName
         * @param scale
         * @throws FileNotFoundException
         * @throws IOException
         */
        @Throws(FileNotFoundException::class, IOException::class)
        fun printToFile(mapView: MapView,
                        outputFileName: String?, scale: Boolean, destSize: Int) {
            val parent = mapView.parent
            val dimI = mapView.innerBounds
            val dim = mapView.bounds
            // do print
            var backBuffer = BufferedImage(dimI!!.width, dimI.height,
                    BufferedImage.TYPE_INT_ARGB)
            val g: Graphics = backBuffer.createGraphics()
            val newX = -dim.x - dimI.x
            val newY = -dim.y - dimI.y
            g.translate(newX, newY)
            g.clipRect(-newX, -newY, dimI.width, dimI.height)
            parent.print(g)
            g.dispose()
            if (scale) {
                val maxDim = Math.max(dimI.getHeight(), dimI.getWidth())
                val newWidth = (dimI.getWidth() * destSize / maxDim).toInt()
                val newHeight = (dimI.getHeight() * destSize / maxDim).toInt()
                val resized = BufferedImage(newWidth, newHeight,
                        backBuffer.type)
                val g2 = resized.createGraphics()
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR)
                g2.drawImage(backBuffer, 0, 0, newWidth, newHeight, 0, 0,
                        backBuffer.width, backBuffer.height, null)
                g2.dispose()
                backBuffer = resized
            }
            val out1 = FileOutputStream(outputFileName)
            ImageIO.write(backBuffer, "png", out1)
            out1.close()
        }
    }
}