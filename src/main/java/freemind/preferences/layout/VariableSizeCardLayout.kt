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
 * Created on 09.04.2006
 * Created by Dimitri Polivaev
 */
package freemind.preferences.layout

import java.awt.CardLayout
import java.awt.Container
import java.awt.Dimension

class VariableSizeCardLayout : CardLayout {
    constructor() : super() {}
    constructor(hgap: Int, vgap: Int) : super(hgap, vgap) {}

    /**
     * Determines the preferred size of the container argument using this card
     * layout.
     *
     * @param parent
     * the parent container in which to do the layout
     * @return the preferred dimensions to lay out the subcomponents of the
     * specified container
     * @see java.awt.Container.getPreferredSize
     *
     * @see java.awt.CardLayout.minimumLayoutSize
     */
    override fun preferredLayoutSize(parent: Container): Dimension {
        synchronized(parent.treeLock) {
            val insets = parent.insets
            val ncomponents = parent.componentCount
            var w = 0
            var h = 0
            for (i in 0 until ncomponents) {
                val comp = parent.getComponent(i)
                if (comp.isVisible) {
                    val d = comp.preferredSize
                    if (d.width > w) {
                        w = d.width
                    }
                    if (d.height > h) {
                        h = d.height
                    }
                }
            }
            return Dimension(
                insets.left + insets.right + w + hgap * 2,
                insets.top +
                    insets.bottom + h + vgap * 2
            )
        }
    }
}
