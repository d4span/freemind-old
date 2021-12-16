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
 * Created on 08.05.2005
 *
 */
package freemind.common

/**
 * Utility Class for displaying local object names in GUI components.
 *
 * @author Dimitri Polivaev 18.01.2007
 */
class NamedObject {
    var name: String? = null
        private set
    var `object`: Any? = null
        private set

    private constructor() {}
    constructor(`object`: Any?, name: String?) {
        this.`object` = `object`
        this.name = name
    }

    override fun equals(o: Any?): Boolean {
        if (o is NamedObject) {
            return `object` == o.`object`
        }
        return `object` == o
    }

    override fun toString(): String {
        return name!!
    }

    companion object {
        fun literal(literal: String?): NamedObject {
            val result = NamedObject()
            result.`object` = literal
            result.name = literal
            return result
        }
    }
}