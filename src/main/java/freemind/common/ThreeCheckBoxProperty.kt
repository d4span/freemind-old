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
import freemind.controller.BlindIcon
import freemind.main.Resources
import freemind.view.ImageFactory
import java.util.Locale
import javax.swing.Icon
import javax.swing.JButton

open class ThreeCheckBoxProperty(override var description: String?, override var label: String?) :
    PropertyBean(),
    PropertyControl {
    protected var mFalseValue = "false"
    protected var mTrueValue = "true"
    protected var mDontTouchValue = "don_t_touch"
    private var state = 0
    var mButton = JButton()

    /**
     */
    init {
        // setState(0);
        mButton.addActionListener {
            setState((getState() + 1) % 3)
            firePropertyChangeEvent()
        }
    }

    fun getState(): Int {
        return state
    }

    private fun transformString(string: String?): Int {
        if (string == null) {
            return DON_T_TOUCH_VALUE_INT
        }
        if (string.lowercase(Locale.getDefault()) == mTrueValue) {
            return TRUE_VALUE_INT
        }
        return if (string.lowercase(Locale.getDefault()) == mFalseValue) {
            FALSE_VALUE_INT
        } else DON_T_TOUCH_VALUE_INT
    }

    override var value: String?
        get() {
            when (state) {
                TRUE_VALUE_INT -> return mTrueValue
                FALSE_VALUE_INT -> return mFalseValue
                DON_T_TOUCH_VALUE_INT -> return mDontTouchValue
            }
            return null
        }
        set(value) {
            require(
                !(
                    value == null ||
                        !(
                            value.lowercase(Locale.getDefault()) == mTrueValue || value.lowercase(Locale.getDefault()) == mFalseValue || value
                                .lowercase(Locale.getDefault()) == mDontTouchValue
                            )
                    )
            ) {
                (
                    "Cannot set a boolean to " +
                        value
                    )
            }
            setState(transformString(value))
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(pTranslator!!.getText(label), mButton)
        val tooltiptext = pTranslator.getText(description)
        label.toolTipText = tooltiptext
        mButton.toolTipText = tooltiptext
    }

    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }

    /**
     *
     */
    protected open fun setState(newState: Int) {
        state = newState
        val icons: Array<Icon?>
        icons = arrayOfNulls(3) // {MINUS_IMAGE, PLUS_IMAGE, NO_IMAGE};
        icons[TRUE_VALUE_INT] = PLUS_IMAGE
        icons[FALSE_VALUE_INT] = MINUS_IMAGE
        icons[DON_T_TOUCH_VALUE_INT] = NO_IMAGE
        // mButton.setText(DISPLAY_VALUES[state]);
        mButton.icon = icons[state]
    }

    companion object {
        const val FALSE_VALUE = "false"
        const val TRUE_VALUE = "true"
        const val DON_T_TOUCH_VALUE = "don_t_touch"
        const val DON_T_TOUCH_VALUE_INT = 2
        const val TRUE_VALUE_INT = 0
        const val FALSE_VALUE_INT = 1
        private val PLUS_IMAGE = ImageFactory.getInstance().createIcon(
            Resources
                .getInstance().getResource("images/edit_add.png")
        )
        private val MINUS_IMAGE = ImageFactory.getInstance().createIcon(
            Resources
                .getInstance().getResource("images/edit_remove.png")
        )
        private val NO_IMAGE: Icon = BlindIcon(15)
    }
}
