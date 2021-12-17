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
package freemind.controller

import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import javax.swing.Icon

internal class ColorSwatch : Icon {
    var color = Color.white

    constructor() {}
    constructor(color: Color) {
        this.color = color
    }

    override fun getIconWidth(): Int {
        return 11
    }

    override fun getIconHeight(): Int {
        return 11
    }

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        g.color = Color.black
        g.fillRect(x, y, iconWidth, iconHeight)
        g.color = color
        g.fillRect(x + 2, y + 2, iconWidth - 4, iconHeight - 4)
    }
}