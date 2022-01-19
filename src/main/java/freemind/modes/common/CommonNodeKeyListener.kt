/*FreeMind - A Program for creating and viewing Mindmaps
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
package freemind.modes.common

import freemind.main.Resources
import freemind.modes.ModeController
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.logging.Logger
import javax.swing.KeyStroke

/**
 * The KeyListener which belongs to the node and cares for Events like C-D
 * (Delete Node). It forwards the requests to NodeController.
 */
class CommonNodeKeyListener(
    private val c: ModeController,
    private val editHandler: EditHandler
) : KeyListener {
    interface EditHandler {
        fun edit(e: KeyEvent?, addNew: Boolean, editLong: Boolean)
    }

    private val up: String
    private val down: String
    private val left: String
    private val right: String
    private var disabledKeyType = true
    private var keyTypeAddsNew = false
    private val keyStrokeUp: KeyStroke?
    private val keyStrokeDown: KeyStroke?
    private val keyStrokeLeft: KeyStroke?
    private val keyStrokeRight: KeyStroke?

    init {
        if (logger == null) {
            logger = c.frame.getLogger(this.javaClass.name)
        }
        up = c.frame.getAdjustableProperty("keystroke_move_up")
        down = c.frame.getAdjustableProperty("keystroke_move_down")
        left = c.frame.getAdjustableProperty("keystroke_move_left")
        right = c.frame.getAdjustableProperty("keystroke_move_right")

        // like in excel - write a letter means edit (PN)
        // on the other hand it doesn't allow key navigation (sdfe)
        disabledKeyType = Resources.getInstance().getBoolProperty(
            "disable_key_type"
        )
        keyTypeAddsNew = Resources.getInstance().getBoolProperty(
            "key_type_adds_new"
        )
        keyStrokeUp = KeyStroke.getKeyStroke(up)
        keyStrokeDown = KeyStroke.getKeyStroke(down)
        keyStrokeLeft = KeyStroke.getKeyStroke(left)
        keyStrokeRight = KeyStroke.getKeyStroke(right)
    }

    //
    // Interface KeyListener
    //
    override fun keyTyped(e: KeyEvent) {}
    override fun keyPressed(e: KeyEvent) {
        logger!!.finest("Key pressend " + e.keyChar + " alias " + e.keyCode)
        // add to check meta keydown by koh 2004.04.16
        if (e.isAltDown || e.isControlDown || e.isMetaDown) {
            return
        }
        when (e.keyCode) {
            KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE, KeyEvent.VK_SHIFT, KeyEvent.VK_DELETE, KeyEvent.VK_SPACE, KeyEvent.VK_INSERT, KeyEvent.VK_TAB -> // end change.
                return // processed by Adapters ActionListener
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN -> {
                c.view.move(e)
                return
            }
            KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_BACK_SPACE -> {
                editHandler.edit(e, false, false)
                return
            }
        }

        // printable key creates new node in edit mode (PN)
        if (!disabledKeyType) {
            if (!e.isActionKey && e.keyChar != KeyEvent.CHAR_UNDEFINED) {
                logger!!.finest("Starting edit mode with: " + e.keyChar)
                editHandler.edit(e, keyTypeAddsNew, false)
                return // do not process the (sdfe) navigation
            }
        }

        // printable key used for navigation
        var doMove = false // unified call of the move method (PN)
        if (keyStrokeUp != null && e.keyCode == keyStrokeUp.keyCode) {
            e.keyCode = KeyEvent.VK_UP
            doMove = true
        } else if (keyStrokeDown != null &&
            e.keyCode == keyStrokeDown.keyCode
        ) {
            e.keyCode = KeyEvent.VK_DOWN
            doMove = true
        } else if (keyStrokeLeft != null &&
            e.keyCode == keyStrokeLeft.keyCode
        ) {
            e.keyCode = KeyEvent.VK_LEFT
            doMove = true
        } else if (keyStrokeRight != null &&
            e.keyCode == keyStrokeRight.keyCode
        ) {
            e.keyCode = KeyEvent.VK_RIGHT
            doMove = true
        }
        if (doMove) {
            c.view.move(e)
            e.consume()
            return
        }
    }

    override fun keyReleased(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_SHIFT) {
            c.view.resetShiftSelectionOrigin()
        }
    }

    companion object {
        private var logger: Logger? = null
    }
}
