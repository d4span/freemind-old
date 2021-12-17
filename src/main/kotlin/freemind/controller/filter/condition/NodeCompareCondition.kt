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
import freemind.main.Tools
import freemind.main.XMLElement
import freemind.modes.MindMapNode

internal class NodeCompareCondition(value: String?, ignoreCase: Boolean,
                                    private val comparationResult: Int?, private val succeed: Boolean) : CompareConditionAdapter(value, ignoreCase) {
    override fun checkNode(c: Controller?, node: MindMapNode): Boolean {
        return try {
            succeed == (compareTo(node.text) == comparationResult)
        } catch (fne: NumberFormatException) {
            false
        }
    }

    override fun save(element: XMLElement) {
        val child = XMLElement()
        child.name = NAME
        super.saveAttributes(child)
        child.setIntAttribute(COMPARATION_RESULT, comparationResult)
        child.setAttribute(SUCCEED, Tools.BooleanToXml(succeed))
        element.addChild(child)
    }

    override fun createDesctiption(): String? {
        val nodeCondition: String = ConditionFactory.Companion.FILTER_NODE?.name!!
        return super.createDescription(nodeCondition, comparationResult,
                succeed)
    }

    companion object {
        const val COMPARATION_RESULT = "comparation_result"
        const val VALUE = "value"
        const val NAME = "node_compare_condition"
        const val SUCCEED = "succeed"
        fun load(element: XMLElement?): Condition {
            return NodeCompareCondition(element?.getStringAttribute(VALUE),
                    Tools.xmlToBoolean(element
                            ?.getStringAttribute(CompareConditionAdapter.Companion.IGNORE_CASE)),
                    element?.getIntAttribute(COMPARATION_RESULT),
                    Tools.xmlToBoolean(element?.getStringAttribute(SUCCEED)))
        }
    }
}