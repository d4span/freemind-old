/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 02.05.2004
 */
/*$Id: EditNodeTextField.java,v 1.1.4.3.10.25 2010/02/22 21:18:53 christianfoltin Exp $*/
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
 * @author foltin
 */
open class EditNodeTextField @JvmOverloads constructor(node: NodeView, text: String,
                                                       private val firstEvent: KeyEvent, controller: ModeController,
                                                       editControl: EditControl, protected var mParent: JComponent? = node.map,
                                                       private val mFocusListener: JComponent = node) : EditNodeBase(node, text, controller, editControl) {
    val EDIT = 1
    val CANCEL = 2
    var cursorWidth = 1
    var xOffset = 0
    var yOffset = -1 // Optimized for Windows style; basically ad hoc
    var widthAddition = 2 * 0 + cursorWidth + 2
    var heightAddition = 2

    // minimal width for input field of leaf or folded node (PN)
    val MINIMAL_LEAF_WIDTH = 150
    val MINIMAL_WIDTH = 50
    val MINIMAL_HEIGHT = 20
    protected var textfield: JTextField? = null
    private var mEventSource: Tools.IntHolder? = null
    private var mUndoManager: UndoManager? = null

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
    }

    fun show() {
        // Make fields for short texts editable
        textfield = if (getText().length < 8) JTextField(getText(), 8) else JTextField(getText())

        // Set textFields's properties
        val nodeView = getNode()
        val model = nodeView.getModel()
        var xSize = nodeView!!.mainView.textWidth + widthAddition
        xOffset += nodeView!!.mainView.textX
        var xExtraWidth = 0
        if (MINIMAL_LEAF_WIDTH > xSize
                && (model!!.isFolded || !model!!.hasChildren())) {
            // leaf or folded node with small size
            xExtraWidth = MINIMAL_LEAF_WIDTH - xSize
            xSize = MINIMAL_LEAF_WIDTH // increase minimum size
            if (nodeView!!.isLeft) { // left leaf
                xExtraWidth = -xExtraWidth
                textfield!!.horizontalAlignment = JTextField.RIGHT
            }
        } else if (MINIMAL_WIDTH > xSize) {
            // opened node with small size
            xExtraWidth = MINIMAL_WIDTH - xSize
            xSize = MINIMAL_WIDTH // increase minimum size
            if (nodeView!!.isLeft) { // left node
                xExtraWidth = -xExtraWidth
                textfield!!.horizontalAlignment = JTextField.RIGHT
            }
        }
        var ySize = nodeView!!.mainView!!.height + heightAddition
        if (ySize < MINIMAL_HEIGHT) {
            ySize = MINIMAL_HEIGHT
        }
        textfield!!.setSize(xSize, ySize)
        var font = nodeView.textFont
        val mapView = nodeView.map
        val zoom = mapView!!.zoom
        if (zoom != 1f) {
            font = font!!.deriveFont(font!!.size * zoom
                    * MainView.Companion.ZOOM_CORRECTION_FACTOR)
        }
        textfield!!.font = font
        val nodeTextColor = nodeView.textColor
        textfield!!.foreground = nodeTextColor
        val nodeTextBackground = nodeView.textBackground
        textfield!!.background = nodeTextBackground
        textfield!!.caretColor = nodeTextColor

        // textField.selectAll(); // no selection on edit (PN)
        mEventSource = Tools.IntHolder()
        mEventSource!!.value = EDIT

        // create the listener
        val textFieldListener = TextFieldListener()

        // Add listeners
        this.textFieldListener = textFieldListener
        textfield!!.addKeyListener(textFieldListener)
        textfield!!.addMouseListener(textFieldListener)
        mUndoManager = UndoManager()
        textfield!!.document.addUndoableEditListener(mUndoManager)
        // Create an undo action and add it to the text component
        textfield!!.actionMap.put("Undo", object : AbstractAction("Undo") {
            override fun actionPerformed(evt: ActionEvent) {
                try {
                    if (mUndoManager!!.canUndo()) {
                        mUndoManager!!.undo()
                    }
                } catch (e: CannotUndoException) {
                }
            }
        })

        // Bind the undo action to ctl-Z (or command-Z on mac)
        textfield!!.inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit
                        .getDefaultToolkit().menuShortcutKeyMask), "Undo")

        // Create a redo action and add it to the text component
        textfield!!.actionMap.put("Redo", object : AbstractAction("Redo") {
            override fun actionPerformed(evt: ActionEvent) {
                try {
                    if (mUndoManager!!.canRedo()) {
                        mUndoManager!!.redo()
                    }
                } catch (e: CannotRedoException) {
                }
            }
        })

        // Bind the redo action to ctl-Y (or command-Y on mac)
        textfield!!.inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit
                        .getDefaultToolkit().menuShortcutKeyMask), "Redo")

        // screen positionining ---------------------------------------------

        // SCROLL if necessary
        view.scrollNodeToVisible(nodeView, xExtraWidth)
        var mPoint: Point? = null
        if (mPoint == null) {
            // NOTE: this must be calculated after scroll because the pane
            // location
            // changes
            mPoint = Point()
            convertPointToAncestor(nodeView!!.mainView!!, mPoint,
                    mapView!!)
            if (xExtraWidth < 0) {
                mPoint.x += xExtraWidth
            }
            mPoint.x += xOffset
            mPoint.y += yOffset
        }
        setTextfieldLoaction(mPoint)
        addTextfield()
        textfield!!.repaint()
        redispatchKeyEvents(textfield!!, firstEvent)
        if (EditNodeBase.Companion.checkSpelling) {
            SpellChecker.register(textfield, false, true, true, true)
        }
        EventQueue.invokeLater {
            textfield!!.requestFocus()
            // Add listener now, as there are focus changes before.
            textfield!!.addFocusListener(textFieldListener)
            mFocusListener.addComponentListener(textFieldListener)
        }
    }

    // listener class
    internal inner class TextFieldListener : KeyListener, FocusListener, MouseListener, ComponentListener {
        private val checkSpelling: Boolean = Resources.getInstance()
                .getBoolProperty(FreeMindCommon.CHECK_SPELLING)

        override fun focusGained(e: FocusEvent) {} // focus gained
        override fun focusLost(e: FocusEvent) {
            // %%% open problems:
            // - adding of a child to the rightmost node
            // - scrolling while in editing mode (it can behave just like
            // other viewers)
            // - block selected events while in editing mode
            if (!textfield!!.isVisible || mEventSource!!.value == CANCEL) {
                if (checkSpelling) {
                    mEventSource!!.value = EDIT // allow focus lost again
                }
                return
            }
            if (e == null) { // can be when called explicitly
                hideMe()
                editControl.ok(textfield!!.text)
                mEventSource!!.value = CANCEL // disallow real focus lost
            } else {
                // always confirm the text if not yet
                hideMe()
                editControl.ok(textfield!!.text)
            }
        }

        override fun keyPressed(e: KeyEvent) {
            // add to check meta keydown by koh 2004.04.16
            // logger.info("Key " + e);
            if (e.isAltDown || e.isControlDown || e.isMetaDown
                    || mEventSource!!.value == CANCEL) {
                return
            }
            var commit = true
            when (e.keyCode) {
                KeyEvent.VK_ESCAPE -> {
                    commit = false
                    e.consume()
                    mEventSource!!.value = CANCEL
                    hideMe()
                    // do not process loose of focus
                    if (commit) {
                        editControl.ok(textfield!!.text)
                    } else {
                        editControl.cancel()
                    }
                    e.consume()
                }
                KeyEvent.VK_ENTER -> {
                    e.consume()
                    mEventSource!!.value = CANCEL
                    hideMe()
                    if (commit) {
                        editControl.ok(textfield!!.text)
                    } else {
                        editControl.cancel()
                    }
                    e.consume()
                }
                KeyEvent.VK_SPACE -> e.consume()
            }
        }

        override fun keyTyped(e: KeyEvent) {}
        override fun keyReleased(e: KeyEvent) {}
        override fun mouseClicked(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseExited(e: MouseEvent) {}
        override fun mousePressed(e: MouseEvent) {
            conditionallyShowPopup(e)
        }

        override fun mouseReleased(e: MouseEvent) {
            conditionallyShowPopup(e)
        }

        private fun conditionallyShowPopup(e: MouseEvent) {
            if (e.isPopupTrigger) {
                val popupMenu: JPopupMenu = EditPopupMenu(textfield)
                if (checkSpelling) {
                    popupMenu.add(SpellChecker.createCheckerMenu())
                    popupMenu.add(SpellChecker.createLanguagesMenu())
                    mEventSource!!.value = CANCEL // disallow real focus lost
                }
                popupMenu.show(e.component, e.x, e.y)
                e.consume()
            }
        }

        override fun componentHidden(e: ComponentEvent) {
            focusLost(null)
        }

        override fun componentMoved(e: ComponentEvent) {
            focusLost(null)
        }

        override fun componentResized(e: ComponentEvent) {
            focusLost(null)
        }

        override fun componentShown(e: ComponentEvent) {
            focusLost(null)
        }
    }

    protected open fun addTextfield() {
        mParent!!.add(textfield, 0)
    }

    protected open fun setTextfieldLoaction(mPoint: Point?) {
        textfield!!.location = mPoint
    }

    private fun hideMe() {
        val parent = textfield!!.parent as JComponent
        val bounds = textfield!!.bounds
        textfield!!.removeFocusListener(textFieldListener)
        textfield!!.removeKeyListener(textFieldListener as KeyListener)
        textfield!!.removeMouseListener(textFieldListener as MouseListener)
        mFocusListener
                .removeComponentListener(textFieldListener as ComponentListener)
        // workaround for java bug
        // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7075600, see
        // FreeMindStarter
        parent.remove(textfield)
        parent.revalidate()
        parent.repaint(bounds)
        textFieldListener = null
    }

    companion object {
        protected var logger: Logger? = null
    }
}