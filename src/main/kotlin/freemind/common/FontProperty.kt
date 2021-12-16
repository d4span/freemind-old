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
/*$Id: FontProperty.java,v 1.1.2.4.2.2 2007/06/27 07:03:57 dpolivaev Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import java.awt.Font
import java.awt.GraphicsEnvironment
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox

class FontProperty(override var description: String, override var label: String,
                   pTranslator: TextTranslator?) : PropertyBean(), PropertyControl {
    var font: Font? = null
    var mFontComboBox = JComboBox<String>()
    private val mAvailableFontFamilyNames: Array<String>

    /**
     * TODO TODO
     */
    init {
        mAvailableFontFamilyNames = GraphicsEnvironment
                .getLocalGraphicsEnvironment().availableFontFamilyNames
        mFontComboBox.model = DefaultComboBoxModel(
                mAvailableFontFamilyNames)
        mFontComboBox.addActionListener { firePropertyChangeEvent() }
    }

    override fun layout(builder: DefaultFormBuilder, pTranslator: TextTranslator) {
        val label = builder.append(pTranslator.getText(label),
                mFontComboBox)
        label.toolTipText = pTranslator.getText(description)
    }

    override var value: String?
        get() = mAvailableFontFamilyNames[mFontComboBox.selectedIndex]
        set(pValue) {
            for (i in mAvailableFontFamilyNames.indices) {
                val fontName = mAvailableFontFamilyNames[i]
                if (fontName == pValue) {
                    mFontComboBox.selectedIndex = i
                    return
                }
            }
            System.err.println("Unknown value:$pValue")
            if (mFontComboBox.model.size > 0) {
                mFontComboBox.selectedIndex = 0
            }
        }

    override fun setEnabled(pEnabled: Boolean) {
        mFontComboBox.isEnabled = pEnabled
    }
}