/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Christian Foltin, Dimitry Polivaev and others.
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
 * Created on 07.08.2007
 */
/*$Id: RemindValueProperty.java,v 1.1.2.2 2007/12/16 22:05:13 dpolivaev Exp $*/
package freemind.common

/**
 * @author foltin
 */
class RemindValueProperty(
    pDescription: String?,
    pLabel: String?,
    private val mTextTranslator: TextTranslator
) : ThreeCheckBoxProperty(pDescription, pLabel) {
    init {
        mDontTouchValue = ""
    }

    /**
     *
     */
    override fun setState(newState: Int) {
        setState(newState)
        val strings: Array<String?>
        strings = arrayOfNulls(3) // {MINUS_IMAGE, PLUS_IMAGE, NO_IMAGE};
        strings[TRUE_VALUE_INT] = mTextTranslator.getText(
            "OptionalDontShowMeAgainDialog.ok"
        )!!.replaceFirst("&".toRegex(), "")
        strings[FALSE_VALUE_INT] = mTextTranslator.getText(
            "OptionalDontShowMeAgainDialog.cancel"
        )!!.replaceFirst("&".toRegex(), "")
        strings[DON_T_TOUCH_VALUE_INT] = mTextTranslator.getText(
            "OptionPanel.ask"
        )!!.replaceFirst("&".toRegex(), "")
        mButton.text = strings[getState()]
    }
}
