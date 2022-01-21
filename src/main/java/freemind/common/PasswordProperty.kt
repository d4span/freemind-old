/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2012 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.common

import freemind.main.Tools
import javax.swing.JPasswordField

/**
 * @author foltin
 * @date 06.08.2012
 */
class PasswordProperty
/**
 * @param pDescription
 * @param pLabel
 */
    (pDescription: String?, pLabel: String?) : StringProperty(pDescription, pLabel) {
    override fun initializeTextfield() {
        mTextField = JPasswordField()
    }

    override var value: String?
        get() = Tools.compress(mTextField?.text)
        set(value) {
            val pwInPlain = Tools.decompress(value)
            super.value = pwInPlain
        }
}