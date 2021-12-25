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
 * Created on 08.05.2005
 *
 */
package freemind.controller.filter.condition

import freemind.controller.Controller
import freemind.controller.filter.FilterController
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.XMLElement
import freemind.modes.MindMapNode
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * @author dimitri 08.05.2005
 */
class DisjunctConditions
/**
 *
 */(private val conditions: Array<Any?>) : Condition {
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#checkNode(freemind.modes
	 * .MindMapNode)
	 */
    override fun checkNode(c: Controller?, node: MindMapNode?): Boolean {
        var i: Int
        i = 0
        while (i < conditions.size) {
            val cond = conditions[i] as Condition?
            if (cond!!.checkNode(c, node)) return true
            i++
        }
        return false
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#getListCellRendererComponent
	 * ()
	 */
    override val listCellRendererComponent: JComponent
        get() {
            val component = JCondition()
            component.add(JLabel("("))
            var cond = conditions[0] as Condition?
            var rendererComponent = cond!!.listCellRendererComponent
            rendererComponent!!.isOpaque = false
            component.add(rendererComponent)
            var i: Int
            i = 1
            while (i < conditions.size) {
                val or = Tools.removeMnemonic(
                    Resources.getInstance()
                        .getResourceString("filter_or")
                )
                val text = " $or "
                component.add(JLabel(text))
                cond = conditions[i] as Condition?
                rendererComponent = cond!!.listCellRendererComponent
                rendererComponent!!.isOpaque = false
                component.add(rendererComponent)
                i++
            }
            component.add(JLabel(")"))
            return component
        }

    override fun save(element: XMLElement?) {
        val child = XMLElement()
        child.name = NAME
        for (i in conditions.indices) {
            val cond = conditions[i] as Condition?
            cond!!.save(child)
        }
        element!!.addChild(child)
    }

    companion object {
        const val NAME = "disjunct_condition"
        fun load(element: XMLElement): Condition {
            val children = element.children
            val conditions = arrayOfNulls<Any>(children.size)
            for (i in conditions.indices) {
                val cond = FilterController.getConditionFactory()
                    .loadCondition(children[i])
                conditions[i] = cond
            }
            return DisjunctConditions(conditions)
        }
    }
}