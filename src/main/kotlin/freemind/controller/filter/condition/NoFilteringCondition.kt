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
 * Created on 18.04.2006
 * Created by Dimitri Polivaev
 */
package freemind.controller.filter.condition

import freemind.controller.Controller
import freemind.main.Resources
import freemind.main.XMLElement
import freemind.modes.MindMapNode
import javax.swing.JComponent
import javax.swing.JLabel

class NoFilteringCondition private constructor() : Condition {
    override fun toString(): String {
        if (description == null) {
            description = Resources.instance?.getResourceString(
                    "filter_no_filtering")
        }
        return description!!
    }

    override fun checkNode(c: Controller?, node: MindMapNode): Boolean {
        return true
    }

    override val listCellRendererComponent: JComponent?
        get() {
            if (renderer == null) {
                renderer = JLabel(description)
            }
            return renderer
        }

    override fun save(element: XMLElement) {}

    companion object {
        private var description: String? = null
        private var renderer: JComponent? = null
        private var condition: NoFilteringCondition? = null
        fun createCondition(): Condition? {
            if (condition == null) {
                condition = NoFilteringCondition()
            }
            return condition
        }
    }
}