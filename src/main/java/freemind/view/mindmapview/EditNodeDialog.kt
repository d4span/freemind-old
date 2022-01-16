/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
/*$Id: EditNodeDialog.java,v 1.1.4.1.16.20 2009/06/24 20:40:19 christianfoltin Exp $*/
package freemind.view.mindmapview

import com.inet.jortho.SpellChecker
import freemind.main.Tools
import freemind.main.Tools.BooleanHolder
import freemind.modes.ModeController
import java.awt.Component
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTextArea

/**
 * @author foltin
 */
@Suppress("DEPRECATION")
class EditNodeDialog(
    node: NodeView?,
    text: String?,
    private val firstEvent: KeyEvent,
    controller: ModeController?,
    editControl: EditControl?
) : EditNodeBase((node)!!, (text)!!, (controller)!!, (editControl)!!) {
    internal open inner class LongNodeDialog() : EditDialog(this@EditNodeDialog) {
        private val textArea: JTextArea

        init {
            textArea = JTextArea(text)
            textArea.lineWrap = true
            textArea.wrapStyleWord = true
            // wish from
            // https://sourceforge.net/forum/message.php?msg_id=5923410
            // textArea.setTabSize(4);
            // wrap around words rather than characters
            val editorScrollPane = JScrollPane(textArea)
            editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

            // int preferredHeight = new
            // Integer(getFrame().getProperty("el__default_window_height")).intValue();
            var preferredHeight = node.height
            preferredHeight = Math.max(
                preferredHeight,
                frame.getProperty(
                    "el__min_default_window_height"
                ).toInt()
            )
            preferredHeight = Math.min(
                preferredHeight,
                frame.getProperty(
                    "el__max_default_window_height"
                ).toInt()
            )
            var preferredWidth = node.width
            preferredWidth = Math.max(
                preferredWidth,
                frame.getProperty(
                    "el__min_default_window_width"
                ).toInt()
            )
            preferredWidth = Math.min(
                preferredWidth,
                frame.getProperty(
                    "el__max_default_window_width"
                ).toInt()
            )
            editorScrollPane.preferredSize = Dimension(
                preferredWidth,
                preferredHeight
            )
            // textArea.setPreferredSize(new Dimension(500, 160));
            val panel = JPanel()

            // String performedAction;
            val okButton = JButton()
            val cancelButton = JButton()
            val splitButton = JButton()
            val enterConfirms = JCheckBox(
                "",
                binOptionIsTrue("el__enter_confirms_by_default")
            )
            Tools.setLabelAndMnemonic(okButton, getText("ok"))
            Tools.setLabelAndMnemonic(cancelButton, getText("cancel"))
            Tools.setLabelAndMnemonic(splitButton, getText("split"))
            Tools.setLabelAndMnemonic(enterConfirms, getText("enter_confirms"))
            if (booleanHolderForConfirmState == null) {
                booleanHolderForConfirmState = BooleanHolder()
                booleanHolderForConfirmState!!.value = enterConfirms
                    .isSelected
            } else {
                enterConfirms.isSelected = booleanHolderForConfirmState!!
                    .value
            }
            okButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) {
                    // next try to avoid bug 1159: focus jumps to file-menu after closing html-editing-window
                    EventQueue.invokeLater(Runnable { submit() })
                }
            })
            cancelButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) {
                    cancel()
                }
            })
            splitButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) {
                    split()
                }
            })
            enterConfirms.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) {
                    textArea.requestFocus()
                    booleanHolderForConfirmState!!.value = enterConfirms
                        .isSelected
                }
            })

            // On Enter act as if OK button was pressed
            textArea.addKeyListener(object : KeyListener {
                override fun keyPressed(e: KeyEvent) {
                    // escape key in long text editor (PN)
                    if (e.keyCode == KeyEvent.VK_ESCAPE) {
                        e.consume()
                        confirmedCancel()
                    } else if (e.keyCode == KeyEvent.VK_ENTER) {
                        if ((
                            enterConfirms.isSelected &&
                                (e.modifiers and KeyEvent.SHIFT_MASK) != 0
                            )
                        ) {
                            e.consume()
                            textArea.insert("\n", textArea.caretPosition)
                        } else if ((
                            enterConfirms.isSelected ||
                                ((e.modifiers and KeyEvent.ALT_MASK) != 0)
                            )
                        ) {
                            e.consume()
                            submit()
                        } else {
                            e.consume()
                            textArea.insert("\n", textArea.caretPosition)
                        }
                    }
                }

                override fun keyTyped(e: KeyEvent) {}
                override fun keyReleased(e: KeyEvent) {}
            })
            textArea.addMouseListener(object : MouseListener {
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
                        val popupMenu: JPopupMenu = EditPopupMenu(textArea)
                        if (checkSpelling) {
                            popupMenu.add(SpellChecker.createCheckerMenu())
                            popupMenu.add(SpellChecker.createLanguagesMenu())
                        }
                        popupMenu.show(e.component, e.x, e.y)
                        e.consume()
                    }
                }
            })
            var font = node.textFont
            font = Tools.updateFontSize(
                font, view.getZoom(),
                font.size
            )
            textArea.font = font
            val nodeTextColor = node.textColor
            textArea.foreground = nodeTextColor
            val nodeTextBackground = node.textBackground
            textArea.background = nodeTextBackground
            textArea.caretColor = nodeTextColor

            // panel.setPreferredSize(new Dimension(500, 160));
            // editorScrollPane.setPreferredSize(new Dimension(500, 160));
            val buttonPane = JPanel()
            buttonPane.add(enterConfirms)
            buttonPane.add(okButton)
            buttonPane.add(cancelButton)
            buttonPane.add(splitButton)
            buttonPane.maximumSize = Dimension(1000, 20)
            if ((frame.getProperty("el__buttons_position") == "above")) {
                panel.add(buttonPane)
                panel.add(editorScrollPane)
            } else {
                panel.add(editorScrollPane)
                panel.add(buttonPane)
            }
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            contentPane = panel
            if (true) {
                redispatchKeyEvents(textArea, firstEvent)
            } // 1st key event defined
            else {
                textArea.caretPosition = text.length
            }
            if (checkSpelling) {
                SpellChecker.register(textArea, false, true, true, true)
            }
        }

        override fun show() {
            textArea.requestFocus()
            super.setVisible(true)
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#cancel()
		 */
        override fun cancel() {
            editControl.cancel()
            super.cancel()
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#split()
		 */
        override fun split() {
            editControl.split(
                textArea.text,
                textArea.caretPosition
            )
            super.split()
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#submit()
		 */
        override fun submit() {
            editControl.ok(textArea.text)
            super.submit()
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#isChanged()
		 */
        override val isChanged: Boolean
            get() = !(text == textArea.text)

        override fun getMostRecentFocusOwner(): Component {
            return if (isFocused) {
                focusOwner
            } else {
                textArea
            }
        }
    }

    fun show() {
        val dialog: EditDialog = LongNodeDialog()
        dialog.pack() // calculate the size

        // set position
        view.scrollNodeToVisible(node, 0)
        Tools.setDialogLocationRelativeTo(dialog, node)
        dialog.isVisible = true
    }

    companion object {
        /** Private variable to hold the last value of the "Enter confirms" state.  */
        private var booleanHolderForConfirmState: BooleanHolder? = null
    }
}
