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
/*$Id: IconProperty.java,v 1.1.2.1.2.4 2007/08/05 22:15:21 dpolivaev Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.main.FreeMindMain
import freemind.modes.IconInformation
import freemind.modes.MindIcon
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*
import javax.swing.JButton

class IconProperty(
    override var description: String, override var label: String, private val mFreeMindMain: FreeMindMain,
    /**
     * Of IconInformation s.
     */
    private val mIcons: Vector<MindIcon>
) : PropertyBean(), PropertyControl, ActionListener {
    var mButton: JButton
    private var mActualIcon: MindIcon? = null

    init {
        mButton = JButton()
        mButton.addActionListener(this)
    }

    private fun setIcon(actualIcon: MindIcon?) {
        mButton.icon = actualIcon!!.icon
        mButton.toolTipText = actualIcon.description
    }

    override var value: String?
        get() = mActualIcon!!.name
        set(value) {
            for (icon in mIcons) {
                if (icon.name == value) {
                    mActualIcon = icon
                    setIcon(mActualIcon)
                }
            }
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(pTranslator!!.getText(label), mButton)
        label.toolTipText = pTranslator.getText(description)
    }

    override fun actionPerformed(arg0: ActionEvent) {
        val icons = Vector<IconInformation>()
        val descriptions = Vector<String>()
        for (icon in mIcons) {
            icons.add(icon)
            descriptions.add(icon.description)
        }
        val dialog = IconSelectionPopupDialog(
            mFreeMindMain.jFrame, icons, mFreeMindMain
        )
        dialog.setLocationRelativeTo(mFreeMindMain.jFrame)
        dialog.isModal = true
        dialog.isVisible = true
        val result = dialog.result
        if (result >= 0) {
            val icon = mIcons[result] as MindIcon
            value = icon.name
            firePropertyChangeEvent()
        }
    }

    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }
}