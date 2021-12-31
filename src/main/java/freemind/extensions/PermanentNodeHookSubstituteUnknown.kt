/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: PermanentNodeHookSubstituteUnknown.java,v 1.1.2.1 2005/02/13 22:39:56 christianfoltin Exp $*/
package freemind.extensions

import freemind.main.XMLElement

/**
 * If a hook can't be find at map loading, this substition is used.
 * The class saves xml data such that it is preserved until the hook is back.
 */
class PermanentNodeHookSubstituteUnknown(override var name: String?) : PermanentNodeHookAdapter() {
    private var child: XMLElement? = null
    override fun loadFrom(child: XMLElement?) {
        this.child = child
        super.loadFrom(child)
    }

    override fun save(hookElement: XMLElement?) {
        super.save(hookElement)
        val i: Iterator<XMLElement> = child!!.children.iterator()
        while (i.hasNext()) {
            val childchild = i.next()
            hookElement!!.addChild(childchild)
        }
    }
}
