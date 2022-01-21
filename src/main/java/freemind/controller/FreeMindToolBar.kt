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
 * Created on 28.03.2004
 *
 */
package freemind.controller

import freemind.main.Tools
import java.awt.Insets
import javax.swing.Action
import javax.swing.JButton
import javax.swing.JToolBar

/**
 * @author Stefan Zechmeister
 */
open class FreeMindToolBar @JvmOverloads constructor(arg0: String? = "", arg1: Int = HORIZONTAL) :
    JToolBar(arg0, arg1) {
    /**
     */
    constructor(arg0: Int) : this("", arg0) {}
    /**
     */
    /**
     *
     */
    /**
     */
    init {
        margin = nullInsets
        isFloatable = false
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JToolBar#add(javax.swing.Action)
	 */
    override fun add(arg0: Action): JButton {
        val actionName = arg0.getValue(Action.NAME)
        arg0.putValue(
            Action.SHORT_DESCRIPTION,
            Tools.removeMnemonic(actionName.toString())
        )
        val returnValue = super.add(arg0)
        returnValue.name = actionName.toString()
        returnValue.text = ""
        returnValue.margin = nullInsets
        returnValue.isFocusable = false

        // fc, 20.6.2004: try to make the toolbar looking good under Mac OS X.
        if (Tools.isMacOsX()) {
            returnValue.isBorderPainted = false
        }
        returnValue.isContentAreaFilled = false
        return returnValue
    }

    companion object {
        private val nullInsets = Insets(0, 0, 0, 0)
    }
}