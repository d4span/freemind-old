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
/*$Id: StringProperty.java,v 1.1.2.4.2.2 2009/02/04 19:31:21 christianfoltin Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JTextField

open class StringProperty(override var description: String, override var label: String) : PropertyBean(), PropertyControl {
    var mTextField: JTextField? = null

    /**
     */
    init {
        initializeTextfield()
        this.description = description
        this.label = label
        // mTextField.addPropertyChangeListener(new PropertyChangeListener() {
        // public void propertyChange(PropertyChangeEvent pEvt)
        // {
        // firePropertyChangeEvent();
        // }
        // });
        mTextField!!.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(pE: KeyEvent) {
                firePropertyChangeEvent()
            }
        })
    }

    /**
     * To be overwritten by PasswordProperty
     */
    protected open fun initializeTextfield() {
        mTextField = JTextField()
    }

    override var value: String?
        get() = mTextField!!.text
        set(value) {
            mTextField!!.text = value
            mTextField!!.selectAll()
        }

    override fun layout(builder: DefaultFormBuilder, pTranslator: TextTranslator) {
        val label = builder.append(pTranslator.getText(label),
                mTextField)
        label.toolTipText = pTranslator.getText(description)
    }

    override fun setEnabled(pEnabled: Boolean) {
        mTextField!!.isEnabled = pEnabled
    }
}