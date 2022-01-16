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

import com.inet.jortho.SpellChecker
import freemind.main.FreeMindCommon
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.Tools.IntHolder
import freemind.modes.ModeController
import java.awt.EventQueue
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.util.logging.Logger
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.JPopupMenu
import javax.swing.JTextField
import javax.swing.KeyStroke
import javax.swing.undo.CannotRedoException
import javax.swing.undo.CannotUndoException
import javax.swing.undo.UndoManager

/**
 * @author foltin
 */
@Suppress("DEPRECATION")
open class EditNodeTextField @JvmOverloads constructor(
    node: NodeView,
    text: String?,
    private val firstEvent: KeyEvent,
    controller: ModeController?,
    editControl: EditControl?,
    protected var mParent: JComponent = node.map,
    private val mFocusListener: JComponent = node
) : EditNodeBase(node, (text)!!, (controller)!!, (editControl)!!) {
    val EDIT = 1
    val CANCEL = 2
    var cursorWidth = 1
    var xOffset = 0
    var yOffset = -1 // Optimized for Windows style; basically ad hoc
    var widthAddition = (2 * 0) + cursorWidth + 2
    var heightAddition = 2

    // minimal width for input field of leaf or folded node (PN)
    val MINIMAL_LEAF_WIDTH = 150
    val MINIMAL_WIDTH = 50
    val MINIMAL_HEIGHT = 20
    @JvmField
    protected var textfield: JTextField? = null
    private var mEventSource: IntHolder? = null
    private var mUndoManager: UndoManager? = null

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    fun show() {
        // Make fields for short texts editable
        textfield = if ((text.length < 8)) JTextField(text, 8) else JTextField(text)

        // Set textFields's properties
        val nodeView = node
        val model = nodeView.model
        var xSize = (nodeView.getMainView()?.textWidth ?: 0) + widthAddition
        xOffset += nodeView.getMainView()?.textX ?: 0
        var xExtraWidth = 0
        if ((
            MINIMAL_LEAF_WIDTH > xSize &&
                (model.isFolded || !model.hasChildren())
            )
        ) {
            // leaf or folded node with small size
            xExtraWidth = MINIMAL_LEAF_WIDTH - xSize
            xSize = MINIMAL_LEAF_WIDTH // increase minimum size
            if (nodeView.isLeft) { // left leaf
                xExtraWidth = -xExtraWidth
                textfield!!.horizontalAlignment = JTextField.RIGHT
            }
        } else if (MINIMAL_WIDTH > xSize) {
            // opened node with small size
            xExtraWidth = MINIMAL_WIDTH - xSize
            xSize = MINIMAL_WIDTH // increase minimum size
            if (nodeView.isLeft) { // left node
                xExtraWidth = -xExtraWidth
                textfield!!.horizontalAlignment = JTextField.RIGHT
            }
        }
        var ySize = (nodeView.getMainView()?.height ?: 0) + heightAddition
        if (ySize < MINIMAL_HEIGHT) {
            ySize = MINIMAL_HEIGHT
        }
        textfield!!.setSize(xSize, ySize)
        var font = nodeView.textFont
        val mapView = nodeView.map
        val zoom = mapView.getZoom()
        if (zoom != 1f) {
            font = font.deriveFont(
                (
                    font.size * zoom
                        * MainView.ZOOM_CORRECTION_FACTOR
                    )
            )
        }
        textfield!!.font = font
        val nodeTextColor = nodeView.textColor
        textfield!!.foreground = nodeTextColor
        val nodeTextBackground = nodeView.textBackground
        textfield!!.background = nodeTextBackground
        textfield!!.caretColor = nodeTextColor

        // textField.selectAll(); // no selection on edit (PN)
        mEventSource = IntHolder()
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
        textfield!!.actionMap.put(
            "Undo",
            object : AbstractAction("Undo") {
                override fun actionPerformed(evt: ActionEvent) {
                    try {
                        if (mUndoManager!!.canUndo()) {
                            mUndoManager!!.undo()
                        }
                    } catch (e: CannotUndoException) {
                    }
                }
            }
        )

        // Bind the undo action to ctl-Z (or command-Z on mac)
        textfield!!.inputMap.put(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Z,
                Toolkit
                    .getDefaultToolkit().menuShortcutKeyMask
            ),
            "Undo"
        )

        // Create a redo action and add it to the text component
        textfield!!.actionMap.put(
            "Redo",
            object : AbstractAction("Redo") {
                override fun actionPerformed(evt: ActionEvent) {
                    try {
                        if (mUndoManager!!.canRedo()) {
                            mUndoManager!!.redo()
                        }
                    } catch (e: CannotRedoException) {
                    }
                }
            }
        )

        // Bind the redo action to ctl-Y (or command-Y on mac)
        textfield!!.inputMap.put(
            KeyStroke.getKeyStroke(
                KeyEvent.VK_Y,
                Toolkit
                    .getDefaultToolkit().menuShortcutKeyMask
            ),
            "Redo"
        )

        // screen positionining ---------------------------------------------

        // SCROLL if necessary
        view.scrollNodeToVisible(nodeView, xExtraWidth)
        var mPoint: Point? = null
        if (mPoint == null) {
            // NOTE: this must be calculated after scroll because the pane
            // location
            // changes
            mPoint = Point()
            Tools.convertPointToAncestor(
                nodeView.getMainView(), mPoint,
                mapView
            )
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
        if (checkSpelling) {
            SpellChecker.register(textfield, false, true, true, true)
        }
        EventQueue.invokeLater(
            Runnable {
                textfield!!.requestFocus()
                // Add listener now, as there are focus changes before.
                textfield!!.addFocusListener(textFieldListener)
                mFocusListener.addComponentListener(textFieldListener)
            }
        )
    }

    // listener class
    internal inner class TextFieldListener() : KeyListener, FocusListener, MouseListener, ComponentListener {
        private val checkSpelling = Resources.getInstance()
            .getBoolProperty(FreeMindCommon.CHECK_SPELLING)

        override fun focusGained(e: FocusEvent) {} // focus gained
        override fun focusLost(e: FocusEvent?) {
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
            if ((
                e.isAltDown || e.isControlDown || e.isMetaDown ||
                    (mEventSource!!.value == CANCEL)
                )
            ) {
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
                val popupMenu: JPopupMenu = EditPopupMenu((textfield)!!)
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
        mParent.add(textfield, 0)
    }

    protected open fun setTextfieldLoaction(mPoint: Point?) {
        textfield!!.location = mPoint
    }

    private fun hideMe() {
        val parent = textfield!!.parent as JComponent
        val bounds = textfield!!.bounds
        textfield!!.removeFocusListener(textFieldListener)
        textfield!!.removeKeyListener(textFieldListener as KeyListener?)
        textfield!!.removeMouseListener(textFieldListener as MouseListener?)
        mFocusListener
            .removeComponentListener(textFieldListener as ComponentListener?)
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
