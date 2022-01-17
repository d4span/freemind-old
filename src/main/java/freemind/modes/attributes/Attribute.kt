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
 * Created on 18.06.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.attributes

/**
 * @author Dimitri Polivaev 18.06.2005
 */
class Attribute {
    var name: String?
    var value: String?

    /**
     */
    constructor(name: String?) {
        this.name = name
        value = ""
    }

    constructor(name: String?, value: String?) {
        this.name = name
        this.value = value
    }

    /**
     * @param pAttribute
     * deep copy.
     */
    constructor(pAttribute: Attribute) {
        name = pAttribute.name
        value = pAttribute.value
    }

    override fun toString(): String {
        return "[$name, $value]"
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (name == null) 0 else name.hashCode()
        result = prime * result + if (value == null) 0 else value.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val attribute = other as Attribute
        if (name == null) {
            if (attribute.name != null) return false
        } else if (name != attribute.name) return false
        if (value == null) {
            if (attribute.value != null) return false
        } else if (value != attribute.value) return false
        return true
    }
}
