/*
 * KeyEventTranslator.java - Hides some warts of AWT event API
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2003 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import freemind.main.FreeMind
import javax.swing.JDialog
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import freemind.common.TextTranslator
import freemind.common.PropertyControl
import freemind.preferences.layout.OptionPanel.KeyProperty
import javax.swing.JButton
import freemind.preferences.layout.OptionPanel.ChangeTabAction
import java.util.Properties
import freemind.common.PropertyBean
import freemind.preferences.layout.OptionPanel
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import java.awt.CardLayout
import freemind.preferences.layout.VariableSizeCardLayout
import javax.swing.JPanel
import freemind.preferences.layout.OptionPanel.NewTabProperty
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.ButtonBarBuilder
import kotlin.jvm.JvmOverloads
import javax.swing.ImageIcon
import com.jgoodies.forms.layout.RowSpec
import freemind.preferences.layout.GrabKeyDialog
import java.awt.event.KeyEvent
import javax.swing.JLabel
import freemind.common.SeparatorProperty
import freemind.common.ComboProperty
import freemind.main.FreeMindCommon
import freemind.main.Tools
import freemind.common.BooleanProperty
import freemind.common.NextLineProperty
import freemind.common.PasswordProperty
import freemind.modes.MindMapNode
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UIManager
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
import java.awt.Color
import freemind.main.FreeMindMain
import freemind.preferences.layout.GrabKeyDialog.InputPane
import java.awt.AWTEvent
import java.awt.GridLayout
import javax.swing.WindowConstants
import java.util.Enumeration
import javax.swing.JTextField
import freemind.preferences.layout.KeyEventWorkaround
import freemind.preferences.layout.KeyEventTranslator
import java.lang.StringBuffer
import javax.swing.JOptionPane
import java.awt.event.InputEvent
import java.awt.Dimension
import java.awt.Insets
import java.lang.Exception
import java.util.HashMap

//{{{ Imports
/**
 * In conjunction with the `KeyEventWorkaround`, hides some warts in
 * the AWT key event API.
 *
 * @author Slava Pestov
 * @version $Id: KeyEventTranslator.java,v 1.1.2.2 2005/05/12 21:56:57
 * christianfoltin Exp $
 */
object KeyEventTranslator {
    // {{{ addTranslation() method
    /**
     * Adds a keyboard translation.
     *
     * @param key1
     * Translate this key
     * @param key2
     * Into this key
     * @since jEdit 4.2pre3
     */
    fun addTranslation(key1: Key?, key2: Key) {
        transMap[key1] = key2
    } // }}}
    // {{{ translateKeyEvent() method
    /**
     * Pass this an event from
     * [KeyEventWorkaround.processKeyEvent].
     *
     * @since jEdit 4.2pre3
     */
    fun translateKeyEvent(evt: KeyEvent): Key? {
        val modifiers = evt.modifiers
        var returnValue: Key? = null
        when (evt.id) {
            KeyEvent.KEY_PRESSED -> {
                val keyCode = evt.keyCode
                returnValue = if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9
                        || keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
                    if (KeyEventWorkaround.ALTERNATIVE_DISPATCHER) return null else {
                        Key(modifiersToString(modifiers),  // fc, 12.5.2005: changed to upper case as Freemind seems to
                                // need this.
                                '\u0000', keyCode.toChar().uppercaseChar())
                    }
                } else {
                    if (keyCode == KeyEvent.VK_TAB) {
                        evt.consume()
                        Key(modifiersToString(modifiers),
                                keyCode, '\u0000')
                    } else if (keyCode == KeyEvent.VK_SPACE) {
                        // for SPACE or S+SPACE we pass the
                        // key typed since international
                        // keyboards sometimes produce a
                        // KEY_PRESSED SPACE but not a
                        // KEY_TYPED SPACE, eg if you have to
                        // do a "<space> to insert ".
                        if (modifiers and InputEvent.SHIFT_MASK.inv() == 0) null else {
                            Key(modifiersToString(modifiers), 0,
                                    ' ')
                        }
                    } else {
                        Key(modifiersToString(modifiers),
                                keyCode, '\u0000')
                    }
                }
            }
            KeyEvent.KEY_TYPED -> {
                val ch = evt.keyChar
                when (ch) {
                    '\n', '\t', '\b' -> return null
                    ' ' -> if (modifiers and InputEvent.SHIFT_MASK.inv() != 0) return null
                }
                val ignoreMods: Int
                ignoreMods = if (KeyEventWorkaround.ALT_KEY_PRESSED_DISABLED) {
                    /* on MacOS, A+ can be user input */
                    InputEvent.SHIFT_MASK or InputEvent.ALT_GRAPH_MASK or InputEvent.ALT_MASK
                } else {
                    /* on MacOS, A+ can be user input */
                    InputEvent.SHIFT_MASK or InputEvent.ALT_GRAPH_MASK
                }
                returnValue = if (modifiers and InputEvent.ALT_GRAPH_MASK == 0 && evt.getWhen() - KeyEventWorkaround.lastKeyTime < 750 && KeyEventWorkaround.modifiers and ignoreMods.inv() != 0) {
                    if (KeyEventWorkaround.ALTERNATIVE_DISPATCHER) {
                        Key(modifiersToString(modifiers), 0, ch)
                    } else return null
                } else {
                    if (ch == ' ') {
                        Key(modifiersToString(modifiers), 0, ch)
                    } else Key(null, 0, ch)
                }
            }
            else -> return null
        }

        /*
		 * I guess translated events do not have the 'evt' field set so
		 * consuming won't work. I don't think this is a problem as nothing uses
		 * translation anyway
		 */
        val trans = transMap[returnValue]
        return trans ?: returnValue
    } // }}}
    // {{{ parseKey() method
    /**
     * Converts a string to a keystroke. The string should be of the form
     * *modifiers*+*shortcut* where *modifiers* is any
     * combination of A for Alt, C for Control, S for Shift or M for Meta, and
     * *shortcut* is either a single character, or a keycode name from the
     * `KeyEvent` class, without the `VK_` prefix.
     *
     * @param keyStroke
     * A string description of the key stroke
     * @since jEdit 4.2pre3
     */
    fun parseKey(keyStroke: String?): Key? {
        if (keyStroke == null) return null
        val index = keyStroke.indexOf('+')
        var modifiers = 0
        if (index != -1) {
            for (i in 0 until index) {
                when (keyStroke[i].uppercaseChar()) {
                    'A' -> modifiers = modifiers or a
                    'C' -> modifiers = modifiers or c
                    'M' -> modifiers = modifiers or m
                    'S' -> modifiers = modifiers or s
                }
            }
        }
        val key = keyStroke.substring(index + 1)
        return if (key.length == 1) {
            Key(modifiersToString(modifiers), 0, key[0])
        } else if (key.length == 0) {
            // Log.log(Log.ERROR,DefaultInputHandler.class,
            // "Invalid key stroke: " + keyStroke);
            null
        } else if (key == "SPACE") {
            Key(modifiersToString(modifiers), 0, ' ')
        } else {
            val ch: Int
            ch = try {
                KeyEvent::class.java.getField("VK_$key").getInt(null)
            } catch (e: Exception) {
                // Log.log(Log.ERROR,DefaultInputHandler.class,
                // "Invalid key stroke: "
                // + keyStroke);
                return null
            }
            Key(modifiersToString(modifiers), ch, '\u0000')
        }
    } // }}}
    // {{{ setModifierMapping() method
    /**
     * Changes the mapping between symbolic modifier key names (`C`,
     * `A`, `M`, `S`) and Java modifier flags.
     *
     * You can map more than one Java modifier to a symobolic modifier, for
     * example :
     *
     *
     * ``<pre>
     * setModifierMapping(
     * InputEvent.CTRL_MASK,
     * InputEvent.ALT_MASK | InputEvent.META_MASK,
     * 0,
     * InputEvent.SHIFT_MASK);
     * <pre>
     *
     *
     * You cannot map a Java modifer to more than one symbolic modifier.
     *
     * @param c
     * The modifier(s) to map the `C` modifier to
     * @param a
     * The modifier(s) to map the `A` modifier to
     * @param m
     * The modifier(s) to map the `M` modifier to
     * @param s
     * The modifier(s) to map the `S` modifier to
     *
     * @since jEdit 4.2pre3
    </pre></pre> */
    fun setModifierMapping(c: Int, a: Int, m: Int, s: Int) {
        val duplicateMapping = c and a or (c and m) or (c and s) or (a and m) or (a and s) or (m and s)
        require(duplicateMapping and InputEvent.CTRL_MASK == 0) { "CTRL is mapped to more than one modifier" }
        require(duplicateMapping and InputEvent.ALT_MASK == 0) { "ALT is mapped to more than one modifier" }
        require(duplicateMapping and InputEvent.META_MASK == 0) { "META is mapped to more than one modifier" }
        require(duplicateMapping and InputEvent.SHIFT_MASK == 0) { "SHIFT is mapped to more than one modifier" }
        KeyEventTranslator.c = c
        KeyEventTranslator.a = a
        KeyEventTranslator.m = m
        KeyEventTranslator.s = s
    } // }}}
    // {{{ getSymbolicModifierName() method
    /**
     * Returns a the symbolic modifier name for the specified Java modifier
     * flag.
     *
     * @param mod
     * A modifier constant from `InputEvent`
     *
     * @since jEdit 4.2pre3
     */
    fun getSymbolicModifierName(mod: Int): String {
        return if (mod and c != 0) "control" else if (mod and a != 0) "alt" else if (mod and m != 0) "meta" else if (mod and s != 0) "shift" else ""
    } // }}}

    // {{{ modifiersToString() method
    fun modifiersToString(mods: Int): String? {
        var buf: StringBuffer? = null
        if (mods and InputEvent.CTRL_MASK != 0) {
            buf = StringBuffer()
            buf.append(getSymbolicModifierName(InputEvent.CTRL_MASK))
        }
        if (mods and InputEvent.ALT_MASK != 0) {
            if (buf == null) buf = StringBuffer() else buf.append(GrabKeyDialog.Companion.MODIFIER_SEPARATOR)
            buf.append(getSymbolicModifierName(InputEvent.ALT_MASK))
        }
        if (mods and InputEvent.META_MASK != 0) {
            if (buf == null) buf = StringBuffer() else buf.append(GrabKeyDialog.Companion.MODIFIER_SEPARATOR)
            buf.append(getSymbolicModifierName(InputEvent.META_MASK))
        }
        if (mods and InputEvent.SHIFT_MASK != 0) {
            if (buf == null) buf = StringBuffer() else buf.append(GrabKeyDialog.Companion.MODIFIER_SEPARATOR)
            buf.append(getSymbolicModifierName(InputEvent.SHIFT_MASK))
        }
        return buf?.toString()
    } // }}}
    // {{{ getModifierString() method
    /**
     * Returns a string containing symbolic modifier names set in the specified
     * event.
     *
     * @param evt
     * The event
     *
     * @since jEdit 4.2pre3
     */
    fun getModifierString(evt: InputEvent): String? {
        val buf = StringBuffer()
        if (evt.isControlDown) buf.append(getSymbolicModifierName(InputEvent.CTRL_MASK))
        if (evt.isAltDown) buf.append(getSymbolicModifierName(InputEvent.ALT_MASK))
        if (evt.isMetaDown) buf.append(getSymbolicModifierName(InputEvent.META_MASK))
        if (evt.isShiftDown) buf.append(getSymbolicModifierName(InputEvent.SHIFT_MASK))
        return if (buf.length == 0) null else buf.toString()
    } // }}}

    var c = 0
    var a = 0
    var m = 0
    var s = 0

    // {{{ Private members
    private val transMap: MutableMap<Key?, Key> = HashMap()

    init {
        if (isMacOsX) {
            setModifierMapping(InputEvent.META_MASK,  /* == C+ */
                    InputEvent.CTRL_MASK,  /* == A+ */ /* M+ discarded by key event workaround! */
                    InputEvent.ALT_MASK,  /* == M+ */
                    InputEvent.SHIFT_MASK /* == S+ */)
        } else {
            setModifierMapping(InputEvent.CTRL_MASK, InputEvent.ALT_MASK,
                    InputEvent.META_MASK, InputEvent.SHIFT_MASK)
        }
    } // }}}

    // {{{ Key class
    class Key(var modifiers: String?, var key: Int, var input: Char) {
        override fun hashCode(): Int {
            return key + input.code
        }

        override fun equals(o: Any?): Boolean {
            if (o is Key) {
                val k = o
                if (modifiers == k.modifiers && key == k.key && input == k.input) {
                    return true
                }
            }
            return false
        }

        override fun toString(): String {
            return ((if (modifiers == null) "" else modifiers) + "<"
                    + Integer.toString(key, 16) + ","
                    + Integer.toString(input.code, 16) + ">")
        }
    } // }}}
}