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
 * Created on 25.02.2006
 */
/*$Id: ColorProperty.java,v 1.1.2.4.2.2 2008/07/24 03:10:36 christianfoltin Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.controller.Controller
import freemind.main.Tools
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class ColorProperty(
    override var description: String,
    override var label: String,
    private val defaultColor: String,
    private val mTranslator: TextTranslator
) : PropertyBean(), PropertyControl, ActionListener {

    /**
     */
    private var colorValue: Color?
    var mButton: JButton
    val menu = JPopupMenu()

    /**
     * @param defaultColor
     * TODO
     * @param pTranslator
     * TODO
     */
    init {
        mButton = JButton()
        mButton.addActionListener(this)
        colorValue = Color.BLACK
    }

    override var value: String?
        get() = Tools.colorToXml(colorValue)
        set(value) {
            setColorValue(Tools.xmlToColor(value))
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(pTranslator!!.getText(label), mButton)
        label.toolTipText = pTranslator.getText(description)
        // add "reset to standard" popup:

        // Create and add a menu item
        val item = JMenuItem(
            mTranslator.getText("ColorProperty.ResetColor")
        )
        item.addActionListener { value = defaultColor }
        menu.add(item)

        // Set the component to show the popup menu
        mButton.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(evt: MouseEvent) {
                if (evt.isPopupTrigger) {
                    menu.show(evt.component, evt.x, evt.y)
                }
            }

            override fun mouseReleased(evt: MouseEvent) {
                if (evt.isPopupTrigger) {
                    menu.show(evt.component, evt.x, evt.y)
                }
            }
        })
    }

    override fun actionPerformed(arg0: ActionEvent) {
        val result = Controller.showCommonJColorChooserDialog(
            mButton.rootPane, mTranslator.getText(label),
            colorValue
        )
        if (result != null) {
            setColorValue(result)
            firePropertyChangeEvent()
        }
    }

    /**
     */
    private fun setColorValue(color: Color) {
        var result: Color? = color
        colorValue = result
        if (result == null) {
            result = Color.WHITE
        }
        mButton.background = result
        mButton.text = Tools.colorToXml(result)
    }

    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }
}
