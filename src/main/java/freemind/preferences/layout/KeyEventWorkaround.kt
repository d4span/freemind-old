/*
 * KeyEventWorkaround.java - Works around bugs in Java event handling
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 2000, 2004 Slava Pestov
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

//{{{ Imports
/**
 * Various hacks to get keyboard event handling to behave in a consistent manner
 * across Java implementations.
 *
 * @author Slava Pestov
 * @version $Id: KeyEventWorkaround.java,v 1.1.2.1 2005/05/10 20:55:31
 * christianfoltin Exp $
 */
object KeyEventWorkaround {
    const val ALT_KEY_PRESSED_DISABLED = false
    const val ALTERNATIVE_DISPATCHER = false

    // {{{ processKeyEvent() method
    fun processKeyEvent(evt: KeyEvent): KeyEvent? {
        val keyCode = evt.keyCode
        val ch = evt.keyChar
        return when (evt.id) {
            KeyEvent.KEY_PRESSED -> {
                lastKeyTime = evt.getWhen()
                when (keyCode) {
                    KeyEvent.VK_DEAD_GRAVE, KeyEvent.VK_DEAD_ACUTE, KeyEvent.VK_DEAD_CIRCUMFLEX, KeyEvent.VK_DEAD_TILDE, KeyEvent.VK_DEAD_MACRON, KeyEvent.VK_DEAD_BREVE, KeyEvent.VK_DEAD_ABOVEDOT, KeyEvent.VK_DEAD_DIAERESIS, KeyEvent.VK_DEAD_ABOVERING, KeyEvent.VK_DEAD_DOUBLEACUTE, KeyEvent.VK_DEAD_CARON, KeyEvent.VK_DEAD_CEDILLA, KeyEvent.VK_DEAD_OGONEK, KeyEvent.VK_DEAD_IOTA, KeyEvent.VK_DEAD_VOICED_SOUND, KeyEvent.VK_DEAD_SEMIVOICED_SOUND, '\u0000' -> null
                    KeyEvent.VK_ALT -> {
                        modifiers = modifiers or InputEvent.ALT_MASK
                        null
                    }
                    KeyEvent.VK_ALT_GRAPH -> {
                        modifiers = modifiers or InputEvent.ALT_GRAPH_MASK
                        null
                    }
                    KeyEvent.VK_CONTROL -> {
                        modifiers = modifiers or InputEvent.CTRL_MASK
                        null
                    }
                    KeyEvent.VK_SHIFT -> {
                        modifiers = modifiers or InputEvent.SHIFT_MASK
                        null
                    }
                    KeyEvent.VK_META -> {
                        modifiers = modifiers or InputEvent.META_MASK
                        null
                    }
                    else -> {
                        if (!evt.isMetaDown) {
                            if (evt.isControlDown && evt.isAltDown) {
                                lastKeyTime = 0L
                            } else if (!evt.isControlDown && !evt.isAltDown) {
                                lastKeyTime = 0L
                                if (keyCode >= KeyEvent.VK_0
                                        && keyCode <= KeyEvent.VK_9) {
                                    return null
                                }
                                if (keyCode >= KeyEvent.VK_A
                                        && keyCode <= KeyEvent.VK_Z) {
                                    return null
                                }
                            }
                        }
                        if (ALT_KEY_PRESSED_DISABLED) {
                            /* we don't handle key pressed A+ */
                            /* they're too troublesome */
                            if (modifiers and InputEvent.ALT_MASK != 0) return null
                        }
                        when (keyCode) {
                            KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_MULTIPLY, KeyEvent.VK_ADD, KeyEvent.VK_SUBTRACT, KeyEvent.VK_DECIMAL, KeyEvent.VK_DIVIDE -> last = LAST_NUMKEYPAD
                            else -> last = LAST_NOTHING
                        }
                        evt
                    }
                }
            }
            KeyEvent.KEY_TYPED -> {
                // need to let \b through so that backspace will work
                // in HistoryTextFields
                if ((ch.code < 0x20 || ch.code == 0x7f || ch.code == 0xff) && ch != '\b' && ch != '\t' && ch != '\n') {
                    return null
                }
                if (evt.getWhen() - lastKeyTime < 750) {
                    if (!ALTERNATIVE_DISPATCHER) {
                        if (modifiers and InputEvent.CTRL_MASK != 0 xor (modifiers and InputEvent.ALT_MASK) != 0
                                || modifiers and InputEvent.META_MASK != 0) {
                            return null
                        }
                    }

                    // if the last key was a numeric keypad key
                    // and NumLock is off, filter it out
                    if (last == LAST_NUMKEYPAD) {
                        last = LAST_NOTHING
                        if (ch >= '0' && ch <= '9' || ch == '.' || ch == '/' || ch == '*' || ch == '-' || ch == '+') {
                            return null
                        }
                    } else if (last == LAST_ALT) {
                        last = LAST_NOTHING
                        when (ch) {
                            'B', 'M', 'X', 'c', '!', ',', '?' -> return null
                        }
                    }
                } else {
                    if (modifiers and InputEvent.SHIFT_MASK != 0) {
                        when (ch) {
                            '\n', '\t' -> return null
                        }
                    }
                    modifiers = 0
                }
                evt
            }
            KeyEvent.KEY_RELEASED -> {
                when (keyCode) {
                    KeyEvent.VK_ALT -> {
                        modifiers = modifiers and InputEvent.ALT_MASK.inv()
                        lastKeyTime = evt.getWhen()
                        // we consume this to work around the bug
                        // where A+TAB window switching activates
                        // the menu bar on Windows.
                        evt.consume()
                        return null
                    }
                    KeyEvent.VK_ALT_GRAPH -> {
                        modifiers = modifiers and InputEvent.ALT_GRAPH_MASK.inv()
                        return null
                    }
                    KeyEvent.VK_CONTROL -> {
                        modifiers = modifiers and InputEvent.CTRL_MASK.inv()
                        return null
                    }
                    KeyEvent.VK_SHIFT -> {
                        modifiers = modifiers and InputEvent.SHIFT_MASK.inv()
                        return null
                    }
                    KeyEvent.VK_META -> {
                        modifiers = modifiers and InputEvent.META_MASK.inv()
                        return null
                    }
                    KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_END, KeyEvent.VK_HOME ->                /*
				 * workaround for A+keys producing garbage on Windows
				 */if (modifiers == InputEvent.ALT_MASK) last = LAST_ALT
                }
                evt
            }
            else -> evt
        }
    } // }}}
    // {{{ numericKeypadKey() method
    /**
     * A workaround for non-working NumLock status in some Java versions.
     *
     * @since jEdit 4.0pre8
     */
    fun numericKeypadKey() {
        last = LAST_NOTHING
    } // }}}

    // {{{ Package-private members
    var lastKeyTime: Long = 0
    var modifiers = 0

    // }}}
    // {{{ Private members
    private var last = 0
    private const val LAST_NOTHING = 0
    private const val LAST_NUMKEYPAD = 1
    private const val LAST_ALT = 2 // }}}
}