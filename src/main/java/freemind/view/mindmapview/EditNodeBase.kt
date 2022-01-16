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

import freemind.controller.Controller
import freemind.main.FreeMindCommon
import freemind.main.FreeMindMain
import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.ModeController
import java.awt.BorderLayout
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.ActionEvent
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.LinkedList
import javax.swing.AbstractAction
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPopupMenu
import javax.swing.text.JTextComponent

/**
 * @author foltin
 */
open class EditNodeBase internal constructor(
    protected var node: NodeView,
    protected var text: String,
    protected val modeController: ModeController,
    val editControl: EditControl
) {
    // this enables from outside close the edit mode
    @JvmField
    var textFieldListener: FocusListener? = null

    internal abstract class EditDialog(base: EditNodeBase) : JDialog(
        base.frame as JFrame, base.getText("edit_long_node"), /*modal = */
        true
    ) {
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
        }

        internal inner class SplitAction : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                split()
            }
        }

        internal inner class CancelAction : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                confirmedCancel()
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
                val action = JOptionPane.showConfirmDialog(
                    this,
                    base.getText("long_node_changed_submit"), "",
                    JOptionPane.YES_NO_CANCEL_OPTION
                )
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
                val action = JOptionPane.showConfirmDialog(
                    this,
                    base.getText("long_node_changed_cancel"), "",
                    JOptionPane.OK_CANCEL_OPTION
                )
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

    protected val view: MapView
        get() = modeController.view

    fun getController(): Controller {
        return modeController.controller
    }

    fun getText(string: String?): String? {
        return modeController.getText(string)
    }

    protected val frame: FreeMindMain
        get() = modeController.frame

    protected fun binOptionIsTrue(option: String?): Boolean {
        return Resources.getInstance().getBoolProperty(option)
    }

    protected inner class EditCopyAction(private val textComponent: JTextComponent) : AbstractAction(getText("copy")) {
        override fun actionPerformed(e: ActionEvent) {
            val selection = textComponent.selectedText
            if (selection != null) {
                clipboard.setContents(StringSelection(selection), null)
            }
        }
    }

    protected inner class EditPopupMenu(textComponent: JTextComponent) : JPopupMenu() {
        init {
            val editCopyAction = EditCopyAction(textComponent)
            val selectedText = textComponent.selectedText
            if (selectedText == null || selectedText == "") {
                editCopyAction.isEnabled = false
            }
            this.add(editCopyAction)
        }
    }

    fun closeEdit() {
        if (textFieldListener != null) {
            textFieldListener!!.focusLost(null) // hack to close the edit
        }
    }

    val clipboard: Clipboard
        get() = Tools.getClipboard()

    protected fun redispatchKeyEvents(
        textComponent: JTextComponent?,
        firstKeyEvent: KeyEvent?
    ) {
        if (textComponent?.hasFocus() ?: false) {
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
                    textComponent?.dispatchEvent(ke)
                }
            }

            override fun focusLost(e: FocusEvent) {}
        }

        val keyEventDispatcher = KeyEventQueue()
        currentKeyboardFocusManager.addKeyEventDispatcher(keyEventDispatcher)
        textComponent?.addFocusListener(keyEventDispatcher)
        if (firstKeyEvent == null) {
            return
        }
        if (firstKeyEvent.keyChar == KeyEvent.CHAR_UNDEFINED) {
            when (firstKeyEvent.keyCode) {
                KeyEvent.VK_HOME -> textComponent?.caretPosition = 0
                KeyEvent.VK_END -> textComponent?.caretPosition = textComponent?.document?.length ?: 0
            }
        } else {
            textComponent?.selectAll() // to enable overwrite
            // redispath all key events
            textComponent?.dispatchEvent(firstKeyEvent)
        }
    }

    companion object {
        @JvmField
        var checkSpelling = Resources.getInstance().getBoolProperty(FreeMindCommon.CHECK_SPELLING)
        protected const val BUTTON_OK = 0
        protected const val BUTTON_CANCEL = 1
        protected const val BUTTON_SPLIT = 2
    }
}
