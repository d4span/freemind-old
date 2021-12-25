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
 * Created on 12.07.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition

import freemind.controller.Controller
import freemind.main.Tools
import freemind.main.XMLElement
import freemind.modes.MindMapNode

/**
 * @author Dimitri Polivaev 12.07.2005
 */
class AttributeCompareCondition
/**
 */(
    private val attribute: String, value: String,
    ignoreCase: Boolean, private val comparationResult: Int, private val succeed: Boolean
) : CompareConditionAdapter(value, ignoreCase) {
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#checkNode(freemind.modes
	 * .MindMapNode)
	 */
    override fun checkNode(c: Controller?, node: MindMapNode?): Boolean {
        for (i in 0 until (node?.attributeTableLength ?: 0)) {
            try {
                val attribute2 = node?.getAttribute(i)
                if (attribute2?.name == attribute && succeed == (compareTo(
                        attribute2.value
                            .toString()
                    ) == comparationResult)
                ) return true
            } catch (fne: NumberFormatException) {
            }
        }
        return false
    }

    override fun save(element: XMLElement?) {
        val child = XMLElement()
        child.name = NAME
        super.saveAttributes(child)
        child.setAttribute(ATTRIBUTE, attribute)
        child.setIntAttribute(COMPARATION_RESULT, comparationResult)
        child.setAttribute(SUCCEED, Tools.BooleanToXml(succeed))
        element?.addChild(child)
    }

    override fun createDesctiption(): String {
        return super.createDescription(attribute, comparationResult, succeed)
    }

    companion object {
        const val COMPARATION_RESULT = "comparation_result"
        const val ATTRIBUTE = "attribute"
        const val NAME = "attribute_compare_condition"
        const val SUCCEED = "succeed"
        @JvmStatic
        fun load(element: XMLElement): Condition {
            return AttributeCompareCondition(
                element.getStringAttribute(ATTRIBUTE),
                element.getStringAttribute(VALUE),
                Tools.xmlToBoolean(
                    element
                        .getStringAttribute(IGNORE_CASE)
                ),
                element.getIntAttribute(COMPARATION_RESULT), Tools
                    .xmlToBoolean(element.getStringAttribute(SUCCEED))
            )
        }
    }
}