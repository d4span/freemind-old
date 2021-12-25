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
import freemind.main.Resources
import freemind.main.XMLElement
import freemind.modes.MindMapNode

/**
 * @author Dimitri Polivaev 12.07.2005
 */
class AttributeExistsCondition
/**
 */(private val attribute: String) : NodeCondition() {
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.condition.Condition#checkNode(freemind.modes
	 * .MindMapNode)
	 */
    override fun checkNode(c: Controller?, node: MindMapNode?): Boolean {
        for (i in 0 until node!!.attributeTableLength) {
            val attribute2 = node.getAttribute(i)
            if (attribute2.name == attribute) return true
        }
        return false
    }

    override fun save(element: XMLElement?) {
        val child = XMLElement()
        child.name = NAME
        super.saveAttributes(child)
        child.setAttribute(ATTRIBUTE, attribute)
        element!!.addChild(child)
    }

    override fun createDesctiption(): String? {
        val simpleCondition = Resources.getInstance()
            .getResourceString(ConditionFactory.FILTER_EXIST)
        return ConditionFactory.createDescription(
            attribute, simpleCondition,
            null, false
        )
    }

    companion object {
        const val ATTRIBUTE = "attribute"
        const val NAME = "attribute_exists_condition"
        @JvmStatic
        fun load(element: XMLElement): Condition {
            return AttributeExistsCondition(
                element.getStringAttribute(ATTRIBUTE)
            )
        }
    }
}