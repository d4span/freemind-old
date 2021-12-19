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
/*$Id: ComboProperty.java,v 1.1.2.5.2.2 2006/07/25 20:28:19 christianfoltin Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import java.util.Arrays
import java.util.Vector
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

open class ComboProperty : PropertyBean, PropertyControl {
    override var description: String
    override var label: String
    protected var mComboBox = JComboBox<String?>()
    @JvmField
    protected var possibleValues: Vector<String>? = null

    /**
     * @param pTranslator
     * TODO
     */
    constructor(
        description: String,
        label: String,
        possibles: Array<String>,
        pTranslator: TextTranslator
    ) : super() {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        val possibleTranslations = Vector<String?>()
        for (key in possibleValues!!) {
            possibleTranslations.add(pTranslator.getText(key))
        }
        mComboBox.model = DefaultComboBoxModel(possibleTranslations)
        addActionListener()
    }

    protected fun addActionListener() {
        mComboBox.addActionListener { firePropertyChangeEvent() }
    }

    constructor(
        description: String,
        label: String,
        possibles: Array<String>,
        possibleTranslations: List<String?>?
    ) {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        mComboBox.model = DefaultComboBoxModel(
            Vector(
                possibleTranslations
            )
        )
        addActionListener()
    }

    constructor(
        description: String,
        label: String,
        possibles: List<String>,
        possibleTranslations: List<String?>?
    ) {
        this.description = description
        this.label = label
        fillPossibleValues(possibles)
        mComboBox.model = DefaultComboBoxModel(
            Vector(
                possibleTranslations
            )
        )
    }

    /**
     */
    private fun fillPossibleValues(possibles: Array<String>) {
        fillPossibleValues(Arrays.asList(*possibles))
    }

    /**
     */
    private fun fillPossibleValues(possibles: List<String>) {
        possibleValues = Vector()
        possibleValues!!.addAll(possibles)
    }

    /**
     * If your combo base changes, call this method to update the values. The
     * old selected value is not selected, but the first in the list. Thus, you
     * should call this method only shortly before setting the value with
     * setValue.
     */
    fun updateComboBoxEntries(possibles: List<String>, possibleTranslations: List<String?>?) {
        mComboBox.model = DefaultComboBoxModel(
            Vector(
                possibleTranslations
            )
        )
        fillPossibleValues(possibles)
        if (possibles.size > 0) {
            mComboBox.selectedIndex = 0
        }
    }

    override var value: String?
        get() = possibleValues!![mComboBox.selectedIndex] as String
        set(value) {
            if (possibleValues!!.contains(value)) {
                mComboBox.setSelectedIndex(possibleValues!!.indexOf(value))
            } else {
                System.err.println(
                    "Can't set the value:" + value +
                        " into the combo box " + label + "/" +
                        description
                )
                if (mComboBox.model.size > 0) {
                    mComboBox.selectedIndex = 0
                }
            }
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(
            pTranslator!!.getText(label),
            mComboBox
        )
        label.toolTipText = pTranslator.getText(description)
    }

    override fun setEnabled(pEnabled: Boolean) {
        mComboBox.isEnabled = pEnabled
    }
}
