/**
 * Created on 22.02.2004
 * FreeMind - A Program for creating and viewing Mindmaps
 * Copyright (C) 2000-2001  Joerg Mueller <joergmueller></joergmueller>@bigfoot.com>
 * See COPYING for Details
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * @author [Lars Berning](mailto:labe@users.sourceforge.net)
 */
package freemind.modes.common.dialogs

import freemind.main.FreeMindMain
import freemind.modes.IconInformation
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.Vector
import javax.swing.BorderFactory
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.BevelBorder

@Suppress("DEPRECATION")
class IconSelectionPopupDialog(
    caller: JFrame?,
    icons: Vector<IconInformation>,
    freeMindMain: FreeMindMain
) : JDialog(caller, freeMindMain.getResourceString("select_icon")), KeyListener, MouseListener {
    private val icons: Vector<IconInformation>
    var result = 0
        private set
    private val iconPanel = JPanel()
    private val iconLabels: Array<JLabel?>
    private val descriptionLabel: JLabel
    private val numOfIcons: Int
    private val xDimension: Int
    private var yDimension = 0
    private var selected = Position(0, 0)
    private val freeMindMain: FreeMindMain
    private var mModifiers = 0

    init {
        contentPane.layout = BorderLayout()
        this.freeMindMain = freeMindMain
        this.icons = icons
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                close()
            }
        })

        // we will build a button-matrix which is closest to quadratical
        numOfIcons = icons.size
        xDimension = Math.ceil(Math.sqrt(numOfIcons.toDouble())).toInt()
        yDimension = if (numOfIcons <= xDimension * (xDimension - 1)) xDimension - 1 else xDimension
        val gridlayout = GridLayout(0, xDimension)
        gridlayout.hgap = 3
        gridlayout.vgap = 3
        iconPanel.layout = gridlayout
        iconLabels = arrayOfNulls(numOfIcons)
        for (i in 0 until numOfIcons) {
            val icon = icons[i] as IconInformation
            iconPanel.add(JLabel(icon.icon).also { iconLabels[i] = it })
            iconLabels[i]!!.border = BorderFactory
                .createBevelBorder(BevelBorder.RAISED)
            iconLabels[i]!!.addMouseListener(this)
        }
        var perIconSize = 27
        if (icons.size > 0) {
            // assume, that all icons are of the same size
            perIconSize = (icons[0].icon.iconWidth * 1.7f).toInt()
        }
        iconPanel.preferredSize = Dimension(
            xDimension * perIconSize,
            yDimension * perIconSize
        )
        iconPanel.minimumSize = Dimension(
            xDimension * perIconSize,
            yDimension * perIconSize
        )
        iconPanel.maximumSize = Dimension(
            xDimension * perIconSize,
            yDimension * perIconSize
        )
        iconPanel.size = Dimension(
            xDimension * perIconSize,
            yDimension
                * perIconSize
        )
        contentPane.add(iconPanel, BorderLayout.CENTER)
        descriptionLabel = JLabel(" ")
        // descriptionLabel.setEnabled(false);
        contentPane.add(descriptionLabel, BorderLayout.SOUTH)
        val selected = lastPosition
        select(selected)

        this.selected = selected
        addKeyListener(this)
        pack()
    }

    private fun canSelect(position: Position): Boolean {
        return (
            position.x >= 0 && position.x < xDimension &&
                position.y >= 0 && position.y < yDimension && calculateIndex(position) < numOfIcons
            )
    }

    private fun calculateIndex(position: Position): Int {
        return position.y * xDimension + position.x
    }

    private fun getPosition(caller: JLabel): Position {
        var index: Int
        index = 0
        while (index < iconLabels.size) {
            if (caller === iconLabels[index]) break
            index++
        }
        return getPositionFromIndex(index)
    }

    private fun getPositionFromIndex(index: Int): Position {
        return Position(index % xDimension, index / xDimension)
    }

    private var selectedPosition: Position
        get() = selected
        private set(position) {
            selected = position
            lastPosition = position
        }

    private fun select(position: Position) {
        unhighlight(selectedPosition)
        selectedPosition = position
        highlight(position)
        val index = calculateIndex(position)
        val iconInformation = icons[index] as IconInformation
        val keyStroke = freeMindMain
            .getAdjustableProperty(
                iconInformation
                    .keystrokeResourceName
            )
        if (keyStroke != null) {
            descriptionLabel.text = (
                iconInformation.description + ", " +
                    keyStroke
                )
        } else {
            descriptionLabel.text = iconInformation.description
        }
    }

    private fun unhighlight(position: Position) {
        iconLabels[calculateIndex(position)]!!.border = BorderFactory
            .createBevelBorder(BevelBorder.RAISED)
    }

    private fun highlight(position: Position) {
        iconLabels[calculateIndex(position)]!!.border = BorderFactory
            .createBevelBorder(BevelBorder.LOWERED)
    }

    private fun cursorLeft() {
        val newPosition = Position(
            selectedPosition.x - 1,
            selectedPosition.y
        )
        if (canSelect(newPosition)) select(newPosition)
    }

    private fun cursorRight() {
        val newPosition = Position(
            selectedPosition.x + 1,
            selectedPosition.y
        )
        if (canSelect(newPosition)) select(newPosition)
    }

    private fun cursorUp() {
        val newPosition = Position(
            selectedPosition.x,
            selectedPosition.y - 1
        )
        if (canSelect(newPosition)) select(newPosition)
    }

    private fun cursorDown() {
        val newPosition = Position(
            selectedPosition.x,
            selectedPosition.y + 1
        )
        if (canSelect(newPosition)) select(newPosition)
    }

    private fun addIcon(pModifiers: Int) {
        result = calculateIndex(selectedPosition)
        mModifiers = pModifiers
        dispose()
    }

    /**
     * Transfer shift masks from InputEvent to ActionEvent. But, why don't they
     * use the same constants???? Java miracle.
     */
    val modifiers: Int
        get() {
            var m = mModifiers
            if (mModifiers and (ActionEvent.SHIFT_MASK or InputEvent.SHIFT_DOWN_MASK) != 0) m =
                m or ActionEvent.SHIFT_MASK
            if (mModifiers and (ActionEvent.CTRL_MASK or InputEvent.CTRL_DOWN_MASK) != 0) m = m or ActionEvent.CTRL_MASK
            if (mModifiers and (ActionEvent.ALT_MASK or InputEvent.ALT_DOWN_MASK) != 0) m = m or ActionEvent.ALT_MASK
            return m
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
    override fun keyPressed(keyEvent: KeyEvent) {
        when (keyEvent.keyCode) {
            KeyEvent.VK_RIGHT, KeyEvent.VK_KP_RIGHT -> {
                cursorRight()
                return
            }
            KeyEvent.VK_LEFT, KeyEvent.VK_KP_LEFT -> {
                cursorLeft()
                return
            }
            KeyEvent.VK_DOWN, KeyEvent.VK_KP_DOWN -> {
                cursorDown()
                return
            }
            KeyEvent.VK_UP, KeyEvent.VK_KP_UP -> {
                cursorUp()
                return
            }
            KeyEvent.VK_ESCAPE -> {
                keyEvent.consume()
                close()
                return
            }
            KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> {
                keyEvent.consume()
                addIcon(keyEvent.modifiers)
                return
            }
        }
        val index = findIndexByKeyEvent(keyEvent)
        if (index != -1) {
            result = index
            lastPosition = getPositionFromIndex(index)
            mModifiers = keyEvent.modifiers
            keyEvent.consume()
            dispose()
        }
    }

    private fun findIndexByKeyEvent(keyEvent: KeyEvent): Int {
        for (i in icons.indices) {
            val info = icons[i] as IconInformation
            val iconKeyStroke = info.keyStroke
            if (iconKeyStroke != null && (
                keyEvent.keyCode == iconKeyStroke.keyCode && keyEvent.keyCode != 0 && iconKeyStroke.modifiers and KeyEvent.SHIFT_MASK == keyEvent
                    .modifiers and KeyEvent.SHIFT_MASK || keyEvent
                    .keyChar == iconKeyStroke.keyChar
                ) &&
                keyEvent.keyChar.code != 0 && keyEvent.keyChar != KeyEvent.CHAR_UNDEFINED
            ) {
                return i
            }
        }
        return -1
    }

    private fun close() {
        result = -1
        mModifiers = 0
        dispose()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
    override fun keyReleased(arg0: KeyEvent) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
    override fun keyTyped(arg0: KeyEvent) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
    override fun mouseClicked(mouseEvent: MouseEvent) {
        addIcon(mouseEvent.modifiers)
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
    override fun mouseEntered(arg0: MouseEvent) {
        select(getPosition(arg0.source as JLabel))
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
    override fun mouseExited(arg0: MouseEvent) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    override fun mousePressed(arg0: MouseEvent) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    override fun mouseReleased(arg0: MouseEvent) {}
    internal class Position(
        /**
         * @return Returns the x.
         */
        val x: Int,
        /**
         * @return Returns the y.
         */
        val y: Int
    ) {

        override fun toString(): String {
            return "($x,$y)"
        }
    }

    companion object {
        private var lastPosition = Position(0, 0)
    }
}
