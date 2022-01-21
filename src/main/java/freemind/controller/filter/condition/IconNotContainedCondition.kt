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
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition

import freemind.controller.Controller
import freemind.main.Resources
import freemind.main.XMLElement
import freemind.modes.MindIcon
import freemind.modes.MindMapNode
import javax.swing.JComponent
import javax.swing.JLabel

class IconNotContainedCondition(private val iconName: String) : Condition {

    override fun checkNode(c: Controller?, node: MindMapNode?): Boolean {
        return iconFirstIndex(node, iconName) == -1 && !isStateIconContained(node, iconName)
    }

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    override val listCellRendererComponent: JComponent
        get() {
            val component = JCondition()
            val text = (Resources.getInstance().getResourceString("filter_icon")
                    + ' '
                    + Resources.getInstance().getResourceString("filter_not_contains")
                    + ' ')
            component.add(JLabel(text))
            component.add(MindIcon.factory(iconName).rendererComponent)
            return component
        }

    override fun save(element: XMLElement?) {
        val child = XMLElement()
        child.name = NAME
        child.setAttribute(ICON, iconName)
        element!!.addChild(child)
    }

    companion object {
        const val ICON = "icon"
        const val NAME = "icon_not_contained_condition"
        fun iconFirstIndex(node: MindMapNode?, iconName: String): Int {
            val icons = node!!.icons
            val i: ListIterator<MindIcon> = icons.listIterator()
            while (i.hasNext()) {
                val nextIcon = i.next()
                if (iconName == nextIcon.name) return i.previousIndex()
            }
            return -1
        }

        fun iconLastIndex(node: MindMapNode, iconName: String): Int {
            val icons = node.icons
            val i: ListIterator<MindIcon> = icons.listIterator(icons.size)
            while (i.hasPrevious()) {
                if (iconName == i.previous().name) return i.nextIndex()
            }
            return -1
        }

        private fun isStateIconContained(node: MindMapNode?, iconName: String): Boolean {
            val stateIcons: Set<String> = node!!.stateIcons.keys
            for (nextIcon in stateIcons) {
                if (iconName == nextIcon) return true
            }
            return false
        }

        fun load(element: XMLElement): Condition {
            return IconNotContainedCondition(
                element.getStringAttribute(ICON)
            )
        }
    }
}