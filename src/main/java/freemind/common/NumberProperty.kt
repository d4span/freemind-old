/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 25.02.2006
 */
/*$Id: NumberProperty.java,v 1.1.2.3.2.5 2009/01/14 21:18:36 christianfoltin Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.main.Resources
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class NumberProperty(
    override var description: String?,
    override var label: String,
    private val min: Int,
    private val max: Int,
    private val step: Int
) : PropertyBean(), PropertyControl {

    // JSlider slider;
    private val spinner: JSpinner

    /**
     */
    init {
        // slider = new JSlider(JSlider.HORIZONTAL, 5, 1000, 100);
        spinner = JSpinner(SpinnerNumberModel(min, min, max, step))
        this.description = description
        this.label = label
        spinner.addChangeListener { firePropertyChangeEvent() }
    }

    override var value: String?
        get() = spinner.value.toString()
        set(value) {
            var intValue = min
            try {
                val parsedIntValue = value?.toInt() ?: throw java.lang.NumberFormatException()
                intValue = parsedIntValue
                val stepModul = (intValue - min) % step
                if (intValue < min || intValue > max || stepModul != 0) {
                    System.err.println(
                        "Actual value of property " + label +
                            " is not in the allowed range: " + value
                    )
                    intValue = min
                }
            } catch (e: NumberFormatException) {
                Resources.getInstance().logException(e)
            }
            spinner.value = intValue
        }
    val intValue: Int
        get() = (spinner.value as Int).toInt()

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        // JLabel label = builder
        // .append(pTranslator.getText(getLabel()), slider);
        val label = builder!!.append(pTranslator!!.getText(label), spinner)
        label.toolTipText = pTranslator.getText(description)
    }

    override fun setEnabled(pEnabled: Boolean) {
        spinner.isEnabled = pEnabled
    }
}
