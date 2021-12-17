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

import freemind.common.NamedObject
import freemind.main.Resources
import freemind.main.XMLElement
import javax.swing.JComponent
import javax.swing.JLabel

/**
 * @author dimitri 17.05.2005
 */
class ConditionFactory
/**
 *
 */
{
    fun loadCondition(element: XMLElement?): Condition? {
        if (element?.name.equals(NodeContainsCondition.Companion.NAME, ignoreCase = true)) return NodeContainsCondition.Companion.load(element)
        if (element?.name.equals(
                        IgnoreCaseNodeContainsCondition.Companion.NAME, ignoreCase = true)) return IgnoreCaseNodeContainsCondition.Companion.load(element)
        if (element?.name.equals(NodeCompareCondition.Companion.NAME, ignoreCase = true)) return NodeCompareCondition.Companion.load(element)
        if (element?.name.equals(IconContainedCondition.Companion.NAME, ignoreCase = true)) return IconContainedCondition.Companion.load(element)
        if (element?.name.equals(IconNotContainedCondition.Companion.NAME, ignoreCase = true)) return IconNotContainedCondition.Companion.load(element)
        if (element?.name.equals(AttributeCompareCondition.Companion.NAME, ignoreCase = true)) return AttributeCompareCondition.Companion.load(element)
        if (element?.name.equals(AttributeExistsCondition.Companion.NAME, ignoreCase = true)) return AttributeExistsCondition.Companion.load(element)
        if (element?.name
                        .equals(AttributeNotExistsCondition.Companion.NAME, ignoreCase = true)) return AttributeNotExistsCondition.Companion.load(element)
        if (element?.name.equals(
                        ConditionNotSatisfiedDecorator.Companion.NAME, ignoreCase = true)) {
            return ConditionNotSatisfiedDecorator.Companion.load(element)
        }
        if (element?.name.equals(ConjunctConditions.Companion.NAME, ignoreCase = true)) {
            return ConjunctConditions.Companion.load(element)
        }
        return if (element?.name.equals(DisjunctConditions.Companion.NAME, ignoreCase = true)) {
            DisjunctConditions.Companion.load(element)
        } else null
    }

    fun createAttributeCondition(attribute: String,
                                 simpleCondition: NamedObject, value: String?, ignoreCase: Boolean): Condition? {
        if (simpleCondition.equals(FILTER_EXIST)) return AttributeExistsCondition(attribute)
        if (simpleCondition.equals(FILTER_DOES_NOT_EXIST)) return AttributeNotExistsCondition(attribute)
        if (ignoreCase) {
            if (simpleCondition.equals(FILTER_IS_EQUAL_TO)) return AttributeCompareCondition(attribute, value, true, 0,
                    true)
            if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO)) return AttributeCompareCondition(attribute, value, true, 0,
                    false)
            if (simpleCondition.equals(FILTER_GT)) return AttributeCompareCondition(attribute, value, true, 1,
                    true)
            if (simpleCondition.equals(FILTER_GE)) return AttributeCompareCondition(attribute, value, true,
                    -1, false)
            if (simpleCondition.equals(FILTER_LT)) return AttributeCompareCondition(attribute, value, true,
                    -1, true)
            if (simpleCondition.equals(FILTER_LE)) return AttributeCompareCondition(attribute, value, true, 1,
                    false)
        } else {
            if (simpleCondition.equals(FILTER_IS_EQUAL_TO)) return AttributeCompareCondition(attribute, value, false,
                    0, true)
            if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO)) return AttributeCompareCondition(attribute, value, false,
                    0, false)
            if (simpleCondition.equals(FILTER_GT)) return AttributeCompareCondition(attribute, value, false,
                    1, true)
            if (simpleCondition.equals(FILTER_GE)) return AttributeCompareCondition(attribute, value, false,
                    -1, false)
            if (simpleCondition.equals(FILTER_LT)) return AttributeCompareCondition(attribute, value, false,
                    -1, true)
            if (simpleCondition.equals(FILTER_LE)) return AttributeCompareCondition(attribute, value, false,
                    1, false)
        }
        return null
    }

    fun createCondition(attribute: NamedObject,
                        simpleCondition: NamedObject, value: String?, ignoreCase: Boolean): Condition? {
        if (attribute.equals(FILTER_ICON)
                && simpleCondition.equals(FILTER_CONTAINS)) return IconContainedCondition(value)
        if (attribute.equals(FILTER_ICON)
                && simpleCondition.equals(FILTER_NOT_CONTAINS)) return IconNotContainedCondition(value)
        return if (attribute.equals(FILTER_NODE)) {
            createNodeCondition(simpleCondition, value, ignoreCase)
        } else null
    }

    val nodeConditionNames: Array<NamedObject?>
        get() = arrayOf(
                FILTER_CONTAINS, FILTER_IS_EQUAL_TO, FILTER_IS_NOT_EQUAL_TO,
                FILTER_GT, FILTER_GE, FILTER_LE, FILTER_LT)
    val iconConditionNames: Array<NamedObject?>
        get() = arrayOf<NamedObject?>(FILTER_CONTAINS, FILTER_NOT_CONTAINS)
    val attributeConditionNames: Array<NamedObject?>
        get() = arrayOf(Resources.instance?.createTranslatedString(FILTER_EXIST),
                Resources.instance?.createTranslatedString(FILTER_DOES_NOT_EXIST), FILTER_IS_EQUAL_TO,
                FILTER_IS_NOT_EQUAL_TO, FILTER_GT, FILTER_GE, FILTER_LE, FILTER_LT)

    protected fun createNodeCondition(simpleCondition: NamedObject,
                                      value: String?, ignoreCase: Boolean): Condition? {
        if (ignoreCase) {
            if (simpleCondition.equals(FILTER_CONTAINS)) {
                return if (value == "") null else IgnoreCaseNodeContainsCondition(value)
            }
            if (simpleCondition.equals(FILTER_IS_EQUAL_TO)) return NodeCompareCondition(value, true, 0, true)
            if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO)) return NodeCompareCondition(value, true, 0, false)
            if (simpleCondition.equals(FILTER_GT)) return NodeCompareCondition(value, true, 1, true)
            if (simpleCondition.equals(FILTER_GE)) return NodeCompareCondition(value, true, -1, false)
            if (simpleCondition.equals(FILTER_LT)) return NodeCompareCondition(value, true, -1, true)
            if (simpleCondition.equals(FILTER_LE)) return NodeCompareCondition(value, true, 1, false)
        } else {
            if (simpleCondition.equals(FILTER_CONTAINS)) {
                return if (value == "") null else NodeContainsCondition(value)
            }
            if (simpleCondition.equals(FILTER_IS_EQUAL_TO)) return NodeCompareCondition(value, false, 0, true)
            if (simpleCondition.equals(FILTER_IS_NOT_EQUAL_TO)) return NodeCompareCondition(value, false, 0, false)
            if (simpleCondition.equals(FILTER_GT)) return NodeCompareCondition(value, false, 1, true)
            if (simpleCondition.equals(FILTER_GE)) return NodeCompareCondition(value, false, -1, false)
            if (simpleCondition.equals(FILTER_LT)) return NodeCompareCondition(value, false, -1, true)
            if (simpleCondition.equals(FILTER_LE)) return NodeCompareCondition(value, false, 1, false)
        }
        return null
    }

    companion object {
        val FILTER_NODE = Resources.instance?.createTranslatedString("filter_node")
        val FILTER_ICON = Resources.instance?.createTranslatedString("filter_icon")
        const val FILTER_DOES_NOT_EXIST = "filter_does_not_exist"
        const val FILTER_EXIST = "filter_exist"
        val FILTER_CONTAINS = Resources.instance?.createTranslatedString("filter_contains")
        val FILTER_NOT_CONTAINS = Resources.instance?.createTranslatedString("filter_not_contains")
        val FILTER_IS_NOT_EQUAL_TO = Resources.instance?.createTranslatedString("filter_is_not_equal_to")
        val FILTER_IS_EQUAL_TO = Resources.instance?.createTranslatedString("filter_is_equal_to")
        val FILTER_LE = NamedObject.literal("<=")
        val FILTER_LT = NamedObject.literal("<")
        val FILTER_GE = NamedObject.literal(">=")
        val FILTER_GT = NamedObject.literal(">")
        const val FILTER_IGNORE_CASE = "filter_ignore_case"
        fun createDescription(attribute: String?, simpleCondition: String?,
                              value: String?, ignoreCase: Boolean): String {
            return (attribute
                    + " "
                    + simpleCondition
                    + (if (value != null) " \"$value\"" else "")
                    + if (ignoreCase && value != null) ", "
                    + Resources.instance?.getResourceString(
                    FILTER_IGNORE_CASE) else "")
        }

        fun createCellRendererComponent(description: String?): JComponent {
            val component = JCondition()
            val label = JLabel(description)
            component.add(label)
            return component
        }
    }
}