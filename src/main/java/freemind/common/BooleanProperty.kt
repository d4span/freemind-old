/*
 * FreeMind - A Program for creating and viewing Mindmaps Copyright (C)
 * 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Created on 25.02.2006
 */
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import java.util.*
import javax.swing.JCheckBox

open class BooleanProperty(override var description: String, override var label: String) : PropertyBean(),
    PropertyControl {
    @JvmField
	protected var mFalseValue = FALSE_VALUE
    @JvmField
	protected var mTrueValue = TRUE_VALUE
    var mCheckBox = JCheckBox()

    /**
     */
    init {
        mCheckBox.addItemListener { firePropertyChangeEvent() }
    }

    override var value: String?
        get() = if (mCheckBox.isSelected) mTrueValue else mFalseValue
        set(value) {
            require(
                !(value == null
                        || !(value.lowercase(Locale.getDefault()) == mTrueValue || value
                    .lowercase(Locale.getDefault()) == mFalseValue))
            ) {
                ("Cannot set a boolean to '"
                        + value + "', allowed are " + mTrueValue + " and "
                        + mFalseValue + ".")
            }
            mCheckBox.isSelected = value.lowercase(Locale.getDefault()) == mTrueValue
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(
            pTranslator!!.getText(label),
            mCheckBox
        )
        label.toolTipText = pTranslator.getText(description)
    }

    override fun setEnabled(pEnabled: Boolean) {
        mCheckBox.isEnabled = pEnabled
    }

    companion object {
        const val FALSE_VALUE = "false"
        const val TRUE_VALUE = "true"
    }
}