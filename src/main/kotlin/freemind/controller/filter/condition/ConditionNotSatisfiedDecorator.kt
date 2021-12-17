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
class ConditionNotSatisfiedDecorator
/**
 *
 */(private val originalCondition: Condition?) : Condition {
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#checkNode(freemind.modes
	 * .MindMapNode)
	 */
    override fun checkNode(c: Controller?, node: MindMapNode): Boolean {
        return !originalCondition!!.checkNode(null, node)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#getListCellRendererComponent
	 * ()
	 */
    override val listCellRendererComponent: JComponent?
        get() {
            val component = JCondition()
            val not = Tools.removeMnemonic(Resources.instance
                    ?.getResourceString("filter_not"))
            val text = "$not "
            component.add(JLabel(text))
            val renderer = originalCondition?.listCellRendererComponent
            renderer?.isOpaque = false
            component.add(renderer)
            return component
        }

    override fun save(element: XMLElement) {
        val child = XMLElement()
        child.name = NAME
        originalCondition!!.save(child)
        element.addChild(child)
    }

    companion object {
        const val NAME = "negate_condition"
        fun load(element: XMLElement?): Condition {
            val children = element?.getChildren()
            val cond = FilterController.Companion.conditionFactory?.loadCondition(
                    children?.get(0) as XMLElement)
            return ConditionNotSatisfiedDecorator(cond)
        }
    }
}