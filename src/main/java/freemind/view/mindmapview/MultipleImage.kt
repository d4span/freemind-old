/*FreeMindget - A Program for creating and viewing Mindmaps
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
/*$Id: MultipleImage.java,v 1.1.18.2 2006/03/14 21:56:28 christianfoltin Exp $*/
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
import freemind.view.mindmapview.MapView.CheckLaterForCenterNodeTask
import javax.swing.JComponent
import kotlin.jvm.JvmOverloads
import javax.swing.JViewport
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
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
import freemind.main.HtmlTools
import java.awt.geom.AffineTransform
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
import java.util.*

class MultipleImage(zoom: Double) : ImageIcon() {
    private val mImages = Vector<ImageIcon?>()
    private var zoomFactor = 1.0
    private var isDirty: Boolean

    init {
        zoomFactor = zoom
        isDirty = true
    }

    val imageCount: Int
        get() = mImages.size

    fun addImage(image: ImageIcon?) {
        mImages.add(image)
        setImage(image!!.image)
        isDirty = true
    }

    override fun getImage(): Image {
        if (!isDirty) return super.getImage()
        val w = iconWidth
        val h = iconHeight
        if (w == 0 || h == 0) {
            return null
        }
        val outImage = BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB)
        val g = outImage.createGraphics()
        for (currentIcon in mImages) {
            // py = /* center: */ ( myHeight -
            // (int)(currentIcon.getIconHeight()* zoomFactor)) /2;
            // int pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
            val pwidth = currentIcon!!.iconWidth * zoomFactor
            val inttrans = AffineTransform.getScaleInstance(
                    zoomFactor, zoomFactor)
            g.drawImage(currentIcon.image, inttrans, null)
            g.translate(pwidth, 0.0)
        }
        g.dispose()
        setImage(outImage)
        isDirty = false
        return super.getImage()
    }

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        if (image != null) {
            super.paintIcon(c, g, x, y)
        }
    }

    // public void paintIcon(Component c,
    // Graphics g,
    // int x,
    // int y)
    // {
    // int myX = x;
    // int myHeight = getIconHeight();
    // for(int i = 0 ; i < mImages.size(); i++) {
    // ImageIcon currentIcon = ((ImageIcon) mImages.get(i));
    // int px,py,pwidth, pheight;
    // px = myX;
    // py = y /* center: */ + ( myHeight - (int)(currentIcon.getIconHeight()*
    // zoomFactor)) /2;
    // pwidth = (int) (currentIcon.getIconWidth() * zoomFactor);
    // pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
    // /* code from ImageIcon.*/
    // if(currentIcon.getImageObserver() == null) {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight, c);
    // } else {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight,
    // currentIcon.getImageObserver());
    // }
    // /* end code*/
    // myX += pwidth;
    // }
    // };
    override fun getIconWidth(): Int {
        var myX = 0
        for (i in mImages.indices) {
            myX += mImages[i]!!.iconWidth
        }
        // System.out.println("width: "+myX);
        return (myX * zoomFactor).toInt()
    }

    override fun getIconHeight(): Int {
        var myY = 0
        for (i in mImages.indices) {
            val otherHeight = mImages[i]!!.iconHeight
            if (otherHeight > myY) myY = otherHeight
        }
        // System.out.println("height: "+myY);
        return (myY * zoomFactor).toInt()
    }
}