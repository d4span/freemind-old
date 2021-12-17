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
import freemind.main.Tools
import freemind.main.XMLElement

abstract class CompareConditionAdapter(private val conditionValue: String?, private val ignoreCase: Boolean) : NodeCondition() {
    @Throws(NumberFormatException::class)
    protected operator fun compareTo(nodeValue: String?): Int {
        try {
            val i2 = conditionValue?.toInt() ?: Int.MIN_VALUE
            val i1 = nodeValue?.toInt() ?: Int.MIN_VALUE
            return if (i1 < i2) -1 else if (i1 == i2) 0 else 1
        } catch (fne: NumberFormatException) {
        }
        val d2: Double
        return try {
            d2 = conditionValue?.toDouble() ?: Double.MIN_VALUE
            val d1 = nodeValue?.toDouble() ?: Double.MIN_VALUE
            java.lang.Double.compare(d1, d2)
        } catch (fne: NumberFormatException) {
            if (nodeValue != null) {
                if (ignoreCase) nodeValue.compareTo(conditionValue ?: "", ignoreCase = true) else nodeValue.compareTo(
                    conditionValue ?: ""
                )
            } else {
                -1
            }
        }
    }

    override fun saveAttributes(child: XMLElement) {
        super.saveAttributes(child)
        child.setAttribute(VALUE, conditionValue)
        child.setAttribute(IGNORE_CASE, Tools.BooleanToXml(ignoreCase))
    }

    fun createDescription(attribute: String?, comparationResult: Int?,
                          succeed: Boolean): String {
        val simpleCondition: NamedObject?
        simpleCondition = when (comparationResult) {
            -1 -> if (succeed) ConditionFactory.Companion.FILTER_LT else ConditionFactory.Companion.FILTER_GE
            0 -> if (succeed) ConditionFactory.Companion.FILTER_IS_EQUAL_TO else ConditionFactory.Companion.FILTER_IS_NOT_EQUAL_TO
            1 -> if (succeed) ConditionFactory.Companion.FILTER_GT else ConditionFactory.Companion.FILTER_LE
            else -> throw IllegalArgumentException()
        }
        return ConditionFactory.Companion.createDescription(attribute, simpleCondition?.name,
                conditionValue, ignoreCase)
    }

    companion object {
        const val IGNORE_CASE = "ignore_case"
        const val VALUE = "value"
    }
}