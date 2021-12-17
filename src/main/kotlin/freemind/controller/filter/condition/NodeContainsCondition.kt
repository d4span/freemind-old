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
 * Created on 17.05.2005
 *
 */
package freemind.controller.filter.condition

import freemind.controller.Controller
import freemind.main.XMLElement
import freemind.modes.MindMapNode

internal class NodeContainsCondition(private val value: String?) : NodeCondition() {
    override fun checkNode(c: Controller?, node: MindMapNode): Boolean {
        return if (value != null) node.text?.indexOf(value) ?: -1 > -1 else false
    }

    override fun save(element: XMLElement) {
        val child = XMLElement()
        child.name = NAME
        super.saveAttributes(child)
        child.setAttribute(VALUE, value)
        element.addChild(child)
    }

    override fun createDesctiption(): String? {
        val nodeCondition: String = ConditionFactory.Companion.FILTER_NODE?.name!!
        val simpleCondition: String = ConditionFactory.Companion.FILTER_CONTAINS?.name!!
        return ConditionFactory.Companion.createDescription(nodeCondition,
                simpleCondition, value, false)
    }

    companion object {
        const val VALUE = "value"
        const val NAME = "node_contains_condition"
        fun load(element: XMLElement?): Condition {
            return NodeContainsCondition(element?.getStringAttribute(VALUE))
        }
    }
}