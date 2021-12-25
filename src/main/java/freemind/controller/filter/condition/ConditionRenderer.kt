/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 */
/*
 * Created on 06.05.2005
 *
 */
package freemind.controller.filter.condition

import freemind.main.Resources
import freemind.modes.MindIcon
import java.awt.Color
import java.awt.Component
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

/**
 * @author dimitri 06.05.2005
 */
class ConditionRenderer : ListCellRenderer<Any?> {
    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing
	 * .JList, java.lang.Object, int, boolean, boolean)
	 */
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        if (value == null) return JLabel(
            Resources.getInstance().getResourceString(
                "filter_no_filtering"
            )
        )
        val component: JComponent
        component = if (value is MindIcon) {
            JLabel(value.icon)
        } else if (value is Condition) {
            value.listCellRendererComponent!!
        } else {
            JLabel(value.toString())
        }
        component.isOpaque = true
        if (isSelected) {
            component.background = SELECTED_BACKGROUND
        } else {
            component.background = Color.WHITE
        }
        component.alignmentX = Component.LEFT_ALIGNMENT
        return component
    }

    companion object {
        val SELECTED_BACKGROUND = Color(207, 247, 202)
    }
}
