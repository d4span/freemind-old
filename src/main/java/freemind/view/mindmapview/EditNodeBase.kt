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
/*$Id: EditNodeBase.java,v 1.1.4.2.12.14 2008/12/16 21:57:01 christianfoltin Exp $*/
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
import java.awt.event.ComponentAdapter
import java.awt.Dimension
import java.awt.event.ComponentEvent
import freemind.view.mindmapview.MapView
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
import kotlin.jvm.JvmOverloads
import java.util.LinkedList
import java.awt.Cursor
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
import freemind.view.mindmapview.MainView
import java.awt.geom.AffineTransform
import java.awt.GradientPaint
import javax.swing.event.TreeModelListener
import freemind.view.mindmapview.NodeMotionListenerView
import freemind.view.mindmapview.NodeFoldingComponent
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
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.BorderLayout
import freemind.view.mindmapview.EditNodeBase.EditDialog.DialogWindowListener
import javax.swing.text.JTextComponent
import java.awt.datatransfer.StringSelection
import freemind.view.mindmapview.EditNodeBase.EditCopyAction
import java.awt.datatransfer.Clipboard
import java.awt.KeyEventDispatcher
import java.awt.event.FocusEvent
import java.awt.event.MouseWheelEvent
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
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import freemind.view.mindmapview.EditNodeBase.EditPopupMenu
import com.inet.jortho.SpellChecker
import freemind.view.mindmapview.EditNodeDialog.LongNodeDialog
import com.lightdev.app.shtm.SHTMLPanel
import freemind.view.mindmapview.EditNodeBase.EditDialog.SubmitAction
import kotlin.Throws
import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import freemind.controller.*
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
import java.awt.event.ComponentListener
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
import java.awt.image.ImageObserver
import javax.swing.*

/**
 * @author foltin
 */
open class EditNodeBase internal constructor(var node: NodeView, protected var text: String,
                                             protected val modeController: ModeController, val editControl: EditControl) {
    // this enables from outside close the edit mode
    var textFieldListener: FocusListener? = null

    internal abstract class EditDialog(base: EditNodeBase) : JDialog(base.frame as JFrame, base.getText("edit_long_node"),  /*modal = */
            true) {
        /**
         * @return Returns the base.
         */
        /**
         * @param base
         * The base to set.
         */
        var base: EditNodeBase

        internal inner class DialogWindowListener : WindowAdapter() {
            /*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent
			 * )
			 */
            override fun windowClosing(e: WindowEvent) {
                if (isVisible) {
                    confirmedSubmit()
                }
            }
        }

        internal inner class SubmitAction : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                submit()
            }

            companion object {
                private const val serialVersionUID = -859458051986869388L
            }
        }

        internal inner class SplitAction : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                split()
            }

            companion object {
                private const val serialVersionUID = 6876147686811246433L
            }
        }

        internal inner class CancelAction : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                confirmedCancel()
            }

            companion object {
                private const val serialVersionUID = -6277471363654329607L
            }
        }

        init {
            contentPane.layout = BorderLayout()
            defaultCloseOperation = DO_NOTHING_ON_CLOSE
            val dfl = DialogWindowListener()
            addWindowListener(dfl)
            this.base = base
        }

        protected fun confirmedSubmit() {
            if (isChanged) {
                val action = JOptionPane.showConfirmDialog(this,
                        base.getText("long_node_changed_submit"), "",
                        JOptionPane.YES_NO_CANCEL_OPTION)
                if (action == JOptionPane.CANCEL_OPTION) return
                if (action == JOptionPane.YES_OPTION) {
                    submit()
                    return
                }
            }
            cancel()
        }

        protected fun confirmedCancel() {
            if (isChanged) {
                val action = JOptionPane.showConfirmDialog(this,
                        base.getText("long_node_changed_cancel"), "",
                        JOptionPane.OK_CANCEL_OPTION)
                if (action == JOptionPane.CANCEL_OPTION) return
            }
            cancel()
        }

        protected open fun submit() {
            isVisible = false
        }

        protected open fun cancel() {
            isVisible = false
        }

        protected open fun split() {
            isVisible = false
        }

        protected abstract val isChanged: Boolean

        companion object {
            private const val serialVersionUID = 6064679828160694117L
        }
    }

    interface EditControl {
        fun cancel()
        fun ok(newText: String?)
        fun split(newText: String?, position: Int)
    }

    protected val view: MapView?
        protected get() = modeController.view

    fun getController(): Controller? {
        return modeController.controller
    }

    fun getText(string: String?): String {
        return modeController.getText(string)
    }

    protected val frame: FreeMindMain
        protected get() = modeController.frame

    protected fun binOptionIsTrue(option: String?): Boolean {
        return Resources.getInstance().getBoolProperty(option)
    }

    protected inner class EditCopyAction(private val textComponent: JTextComponent?) : AbstractAction(getText("copy")) {
        override fun actionPerformed(e: ActionEvent) {
            val selection = textComponent!!.selectedText
            if (selection != null) {
                clipboard.setContents(StringSelection(selection), null)
            }
        }

        companion object {
            private const val serialVersionUID = 5104219263806454592L
        }
    }

    protected inner class EditPopupMenu(textComponent: JTextComponent?) : JPopupMenu() {
        init {
            val editCopyAction = EditCopyAction(textComponent)
            val selectedText = textComponent!!.selectedText
            if (selectedText == null || selectedText == "") {
                editCopyAction.isEnabled = false
            }
            this.add(editCopyAction)
        }

        companion object {
            private const val serialVersionUID = -6667980271052571216L
        }
    }

    fun closeEdit() {
        if (textFieldListener != null) {
            textFieldListener!!.focusLost(null) // hack to close the edit
        }
    }

    val clipboard: Clipboard
        get() = Tools.clipboard

    protected fun redispatchKeyEvents(textComponent: JTextComponent,
                                      firstKeyEvent: KeyEvent?) {
        if (textComponent.hasFocus()) {
            return
        }
        val currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager()

        class KeyEventQueue : KeyEventDispatcher, FocusListener {
            var events = LinkedList<KeyEvent>()
            override fun dispatchKeyEvent(e: KeyEvent): Boolean {
                events.add(e)
                return true
            }

            override fun focusGained(e: FocusEvent) {
                e.component.removeFocusListener(this)
                currentKeyboardFocusManager.removeKeyEventDispatcher(this)
                val iterator: Iterator<KeyEvent> = events.iterator()
                while (iterator.hasNext()) {
                    val ke = iterator.next()
                    ke.source = textComponent
                    textComponent.dispatchEvent(ke)
                }
            }

            override fun focusLost(e: FocusEvent) {}
        }

        val keyEventDispatcher = KeyEventQueue()
        currentKeyboardFocusManager.addKeyEventDispatcher(keyEventDispatcher)
        textComponent.addFocusListener(keyEventDispatcher)
        if (firstKeyEvent == null) {
            return
        }
        if (firstKeyEvent.keyChar == KeyEvent.CHAR_UNDEFINED) {
            when (firstKeyEvent.keyCode) {
                KeyEvent.VK_HOME -> textComponent.caretPosition = 0
                KeyEvent.VK_END -> textComponent.caretPosition = textComponent.document
                        .length
            }
        } else {
            textComponent.selectAll() // to enable overwrite
            // redispath all key events
            textComponent.dispatchEvent(firstKeyEvent)
        }
    }

    companion object {
        protected var checkSpelling: Boolean = Resources.getInstance().getBoolProperty(FreeMindCommon.CHECK_SPELLING)
        protected const val BUTTON_OK = 0
        protected const val BUTTON_CANCEL = 1
        protected const val BUTTON_SPLIT = 2
    }
}