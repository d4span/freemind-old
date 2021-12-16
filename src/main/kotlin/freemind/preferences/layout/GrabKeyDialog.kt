/*
 * GrabKeyDialog.java - Grabs keys from the keyboard
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2001, 2002 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.preferences.layout

import freemind.main.FreeMind.controller
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage.panel
import freemind.common.PropertyBean.label
import freemind.main.FreeMind.getAdjustableProperty
import freemind.common.PropertyBean.value
import freemind.common.PropertyControl.layout
import freemind.main.FreeMind.getResourceString
import freemind.common.PropertyBean.firePropertyChangeEvent
import freemind.common.TextTranslator.getText
import freemind.main.Tools.removeTranslateComment
import freemind.main.FreeMind.getDefaultProperty
import freemind.controller.Controller.modeController
import freemind.modes.IconInformation.keystrokeResourceName
import freemind.modes.IconInformation.description
import freemind.modes.IconInformation.icon
import freemind.main.FreeMindMain.getResourceString
import freemind.main.Tools.isMacOsX
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import freemind.common.TextTranslator
import freemind.common.PropertyControl
import freemind.preferences.layout.OptionPanel.KeyProperty
import freemind.preferences.layout.OptionPanel.ChangeTabAction
import freemind.common.PropertyBean
import freemind.preferences.layout.OptionPanel
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.preferences.layout.VariableSizeCardLayout
import freemind.preferences.layout.OptionPanel.NewTabProperty
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.ButtonBarBuilder
import kotlin.jvm.JvmOverloads
import com.jgoodies.forms.layout.RowSpec
import freemind.preferences.layout.GrabKeyDialog
import java.awt.event.KeyEvent
import freemind.common.SeparatorProperty
import freemind.common.ComboProperty
import freemind.common.BooleanProperty
import freemind.common.NextLineProperty
import freemind.common.PasswordProperty
import freemind.modes.MindMapNode
import javax.swing.UIManager.LookAndFeelInfo
import freemind.controller.StructuredMenuHolder
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.IconInformation
import freemind.common.RemindValueProperty
import freemind.common.DontShowNotificationProperty
import freemind.preferences.FreemindPropertyContributor
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage
import freemind.common.XmlBindingTools
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.main.*
import freemind.preferences.layout.GrabKeyDialog.InputPane
import freemind.preferences.layout.KeyEventWorkaround
import freemind.preferences.layout.KeyEventTranslator
import java.awt.*
import java.lang.StringBuffer
import java.awt.event.InputEvent
import java.lang.Exception
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder

//{{{ Imports
/**
 * A dialog for getting shortcut keys.
 */
class GrabKeyDialog @JvmOverloads constructor(private val fmMain: FreeMindMain?, parent: Dialog?,
                                              binding: KeyBinding, allBindings: Vector<KeyBinding>, debugBuffer: Buffer?,
                                              private val modifierMask: Int = 0) : JDialog(parent, /* FIXME: getText */"grab-key.title", true) {
    class Buffer {// TODO Auto-generated method stub
        /**
         */
        val length: Int
            get() =// TODO Auto-generated method stub
                0

        /**
         */
        fun insert(length: Int, string: String?) {
            // TODO Auto-generated method stub
        }
    }
    // {{{ getShortcut() method
    /**
     * Returns the shortcut, or null if the current shortcut should be removed
     * or the dialog either has been cancelled. Use isOK() to determine if the
     * latter is true.
     */
    fun getShortcut(): String? {
        return if (isOK) shortcut!!.text else null
    } // }}}
    // {{{ isOK() method
    // {{{ isManagingFocus() method
    /**
     * Returns if this component can be traversed by pressing the Tab key. This
     * returns false.
     */ // }}}
    val isManagingFocus: Boolean
        get() = false
    // {{{ getFocusTraversalKeysEnabled() method
    /**
     * Makes the tab key work in Java 1.4.
     *
     * @since jEdit 3.2pre4
     */
    override fun getFocusTraversalKeysEnabled(): Boolean {
        return false
    } // }}}

    // {{{ processKeyEvent() method
    override fun processKeyEvent(evt: KeyEvent) {
        shortcut!!.processKeyEvent(evt)
    } // }}}

    // {{{ Private members
    // {{{ Instance variables
    private var shortcut // this is a bad hack
            : InputPane? = null
    private var assignedTo: JLabel? = null
    private var ok: JButton? = null
    private var remove: JButton? = null
    private var cancel: JButton? = null
    private var clear: JButton? = null

    /**
     * Returns true, if the dialog has not been cancelled.
     *
     * @since jEdit 3.2pre9
     */ // }}}
    var isOK = false
        private set
    private var binding: KeyBinding? = null
    var bindingReset: KeyBinding? = null
    private var allBindings: Vector<KeyBinding>? = null
    private var debugBuffer: Buffer? = null
    // {{{ GrabKeyDialog constructor
    /**
     * Create and show a new modal dialog.
     *
     * @param parent
     * center dialog on this component.
     * @param binding
     * the action/macro that should get a binding.
     * @param allBindings
     * all other key bindings.
     * @param debugBuffer
     * debug info will be dumped to this buffer (may be null)
     * @since jEdit 4.1pre7
     */
    // private GrabKeyDialog(Dialog parent, KeyBinding binding,
    // Vector allBindings, Buffer debugBuffer)
    // {
    // super(parent,(getText(""grab-key.title")),true);
    //
    // init(binding,allBindings,debugBuffer);
    // } //}}}
    // {{{ GrabKeyDialog constructor
    /**
     * Create and show a new modal dialog.
     *
     * @param parent
     * center dialog on this component.
     * @param binding
     * the action/macro that should get a binding.
     * @param allBindings
     * all other key bindings.
     * @param debugBuffer
     * debug info will be dumped to this buffer (may be null)
     * @since jEdit 4.1pre7
     */
    init {
        title = getText("grab-key.title")
        init(binding, allBindings, debugBuffer)
    } // }}}

    // {{{ init() method
    private fun init(binding: KeyBinding, allBindings: Vector<KeyBinding>, debugBuffer: Buffer?) {
        this.binding = binding
        this.allBindings = allBindings
        this.debugBuffer = debugBuffer
        enableEvents(AWTEvent.KEY_EVENT_MASK)

        // create a panel with a BoxLayout. Can't use Box here
        // because Box doesn't have setBorder().
        val content: JPanel = object : JPanel(GridLayout(0, 1, 0, 6)) {
            /**
             * Returns if this component can be traversed by pressing the Tab
             * key. This returns false.
             */
            override fun isManagingFocus(): Boolean {
                return false
            }

            /**
             * Makes the tab key work in Java 1.4.
             *
             * @since jEdit 3.2pre4
             */
            override fun getFocusTraversalKeysEnabled(): Boolean {
                return false
            }
        }
        content.border = EmptyBorder(12, 12, 12, 12)
        contentPane = content
        JLabel(if (debugBuffer == null) getText("grab-key.caption") + " " + binding.label // FIXME: getText("grab-key.caption")+new String[] {
        // binding.label })
        else getText("grab-key.keyboard-test"))
        val input = Box.createHorizontalBox()
        shortcut = InputPane()
        input.add(shortcut)
        input.add(Box.createHorizontalStrut(12))
        clear = JButton(getText("grab-key.clear"))
        clear!!.addActionListener(ActionHandler())
        input.add(clear)
        assignedTo = JLabel()
        if (debugBuffer == null) updateAssignedTo(null)
        val buttons = Box.createHorizontalBox()
        buttons.add(Box.createGlue())
        if (debugBuffer == null) {
            ok = JButton(getText("common.ok"))
            ok!!.addActionListener(ActionHandler())
            buttons.add(ok)
            buttons.add(Box.createHorizontalStrut(12))
            if (binding.isAssigned) {
                // show "remove" button
                remove = JButton(getText("grab-key.remove"))
                remove!!.addActionListener(ActionHandler())
                // FIXME: REACTIVATE buttons.add(remove);
                buttons.add(Box.createHorizontalStrut(12))
            }
        }
        cancel = JButton(getText("common.cancel"))
        cancel!!.addActionListener(ActionHandler())
        buttons.add(cancel)
        buttons.add(Box.createGlue())

        // FIXME: REACTIVATE content.add(label);
        content.add(input)
        // if(debugBuffer == null)
        // FIXME: REACTIVATE content.add(assignedTo);
        content.add(buttons)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        pack()
        setLocationRelativeTo(parent)
        isResizable = false
        isVisible = true
    } // }}}

    // {{{ getSymbolicName() method
    private fun getSymbolicName(keyCode: Int): String? {
        if (keyCode == KeyEvent.VK_UNDEFINED) return null
        /*
		 * else if(keyCode == KeyEvent.VK_OPEN_BRACKET) return "["; else
		 * if(keyCode == KeyEvent.VK_CLOSE_BRACKET) return "]";
		 */if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
            return keyCode.toChar().lowercaseChar().toString()
        }
        try {
            val fields = KeyEvent::class.java.fields
            for (i in fields.indices) {
                val field = fields[i]
                val name = field.name
                if (name.startsWith("VK_") && field.getInt(null) == keyCode) {
                    return name.substring(3)
                }
            }
        } catch (e: Exception) {
            // Log.log(Log.ERROR,this,e);
        }
        return null
    } // }}}

    // {{{ updateAssignedTo() method
    private fun updateAssignedTo(shortcut: String?) {
        var text = getText("grab-key.assigned-to.none")
        val kb = getKeyBinding(shortcut)
        if (kb != null) text = if (kb.isPrefix) getText("grab-key.assigned-to.prefix") + " " + shortcut else kb.label
        if (ok != null) ok!!.isEnabled = kb == null || !kb.isPrefix
        assignedTo!!.text = getText("grab-key.assigned-to") + " " + text
        // FIXME: assignedTo.setText(
        // (getText("grab-key.assigned-to")+
        // new String[] { text }));
    } // }}}

    // {{{ getKeyBinding() method
    private fun getKeyBinding(shortcut: String?): KeyBinding? {
        if (shortcut == null || shortcut.length == 0) return null
        val spacedShortcut = "$shortcut "
        val e = allBindings!!.elements()
        while (e.hasMoreElements()) {
            val kb = e.nextElement() as KeyBinding
            if (!kb.isAssigned) continue
            val spacedKbShortcut = kb.shortcut + " "

            // eg, trying to bind C+n C+p if C+n already bound
            if (spacedShortcut.startsWith(spacedKbShortcut)) return kb

            // eg, trying to bind C+e if C+e is a prefix
            if (spacedKbShortcut.startsWith(spacedShortcut)) {
                // create a temporary (synthetic) prefix
                // KeyBinding, that won't be saved
                return KeyBinding(kb.name, kb.label, shortcut, true)
            }
        }
        return null
    } // }}}
    // }}}
    // {{{ KeyBinding class
    /**
     * A jEdit action or macro with its two possible shortcuts.
     *
     * @since jEdit 3.2pre8
     */
    class KeyBinding(var name: String?, var label: String?, var shortcut: String?,
                     var isPrefix: Boolean) {
        val isAssigned: Boolean
            get() = shortcut != null && shortcut!!.length > 0
    } // }}}

    // {{{ InputPane class
    internal inner class InputPane : JTextField() {
        // {{{ getFocusTraversalKeysEnabled() method
        /**
         * Makes the tab key work in Java 1.4.
         *
         * @since jEdit 3.2pre4
         */
        override fun getFocusTraversalKeysEnabled(): Boolean {
            return false
        } // }}}

        // {{{ processKeyEvent() method
        public override fun processKeyEvent(_evt: KeyEvent) {
            if (modifierMask and _evt.modifiers != 0) {
                val evt = KeyEvent(_evt.component, _evt.id,
                        _evt.getWhen(), modifierMask.inv()
                        and _evt.modifiers, _evt.keyCode,
                        _evt.keyChar, _evt.keyLocation)
                processKeyEvent(evt)
                if (evt.isConsumed) {
                    _evt.consume()
                }
                return
            }
            val evt = KeyEventWorkaround.processKeyEvent(_evt)
            if (debugBuffer != null) {
                debugBuffer!!.insert(debugBuffer.getLength(), "Event "
                        + toString(_evt)
                        + if (evt == null) " filtered\n" else " passed\n")
            }
            if (evt == null) return
            evt.consume()
            val key = KeyEventTranslator.translateKeyEvent(evt) ?: return
            if (debugBuffer != null) {
                debugBuffer!!.insert(debugBuffer.getLength(),
                        "==> Translated to $key\n")
            }
            val keyString = StringBuffer()

            // if(getDocument().getLength() != 0)
            // keyString.append(' ');
            if (key.modifiers != null) keyString.append(key.modifiers).append(' ') // TODO: plus ??
            // .append('+');
            if (key.input == ' ') keyString.append("SPACE") else if (key.input != '\u0000') keyString.append(key.input) else {
                val symbolicName = getSymbolicName(key.key) ?: return
                keyString.append(symbolicName)
            }
            text = keyString.toString()
            if (debugBuffer == null) updateAssignedTo(keyString.toString())
        } // }}}
    } // }}}

    // {{{ ActionHandler class
    internal inner class ActionHandler : ActionListener {
        // {{{ actionPerformed() method
        override fun actionPerformed(evt: ActionEvent) {
            if (evt.source === ok) {
                if (canClose()) dispose()
            } else if (evt.source === remove) {
                shortcut!!.text = null
                isOK = true
                dispose()
            } else if (evt.source === cancel) dispose() else if (evt.source === clear) {
                shortcut!!.text = null
                if (debugBuffer == null) updateAssignedTo(null)
                shortcut!!.requestFocus()
            }
        } // }}}

        // {{{ canClose() method
        private fun canClose(): Boolean {
            val shortcutString = shortcut!!.text
            if (shortcutString.length == 0 && binding!!.isAssigned) {
                // ask whether to remove the old shortcut
                val answer = JOptionPane
                        .showConfirmDialog(this@GrabKeyDialog,
                                getText("grab-key.remove-ask"), null,
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE)
                if (answer == JOptionPane.YES_OPTION) {
                    shortcut!!.text = null
                    isOK = true
                } else return false
            }

            // check whether this shortcut already exists
            bindingReset = getKeyBinding(shortcutString)
            if (bindingReset == null || bindingReset === binding) {
                isOK = true
                return true
            }

            // check whether the other shortcut is the alt. shortcut
            if (bindingReset!!.name === binding!!.name) {
                // we don't need two identical shortcuts
                JOptionPane.showMessageDialog(this@GrabKeyDialog,
                        getText("grab-key.duplicate-alt-shortcut"))
                return false
            }

            // check whether shortcut is a prefix to others
            if (bindingReset!!.isPrefix) {
                // can't override prefix shortcuts
                JOptionPane.showMessageDialog(this@GrabKeyDialog,
                        getText("grab-key.prefix-shortcut"))
                return false
            }

            // ask whether to override that other shortcut
            val answer = JOptionPane.showConfirmDialog(this@GrabKeyDialog,
                    Resources.getInstance().format(
                            "GrabKeyDialog.grab-key.duplicate-shortcut", arrayOf<Any?>(bindingReset!!.name)), null,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
            return if (answer == JOptionPane.YES_OPTION) {
                if (bindingReset!!.shortcut != null
                        && shortcutString.startsWith(bindingReset!!.shortcut!!)) {
                    bindingReset!!.shortcut = null
                }
                isOK = true
                true
            } else false
        } // }}}
    } // }}}

    /**
     */
    private fun getText(resourceString: String): String? {
        return fmMain!!.getResourceString("GrabKeyDialog.$resourceString")
    }

    companion object {
        // {{{ toString() method
        fun toString(evt: KeyEvent): String {
            val id: String
            id = when (evt.id) {
                KeyEvent.KEY_PRESSED -> "KEY_PRESSED"
                KeyEvent.KEY_RELEASED -> "KEY_RELEASED"
                KeyEvent.KEY_TYPED -> "KEY_TYPED"
                else -> "unknown type"
            }
            return (id + ",keyCode=0x" + Integer.toString(evt.keyCode, 16)
                    + ",keyChar=0x" + Integer.toString(evt.keyChar.code, 16)
                    + ",modifiers=0x" + Integer.toString(evt.modifiers, 16))
        } // }}}

        // }}}
        const val MODIFIER_SEPARATOR = " "
    }
}