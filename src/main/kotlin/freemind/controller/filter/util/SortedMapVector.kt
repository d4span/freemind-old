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
package freemind.controller.filter.util

import java.util.*

/**
 * @author Dimitri Polivaev 18.06.2005
 */
class SortedMapVector {
    private class MapElement(val key: Comparable<Any?>, val value: Any)

    private val elements: Vector<MapElement>

    init {
        elements = Vector(0, CAPACITY_INCREMENT)
    }

    fun add(key: Comparable<Any?>, value: Any): Int {
        var index = findElement(key)
        if (index and ELEMENT_NOT_FOUND_FLAG != 0) {
            index = index and ELEMENT_NOT_FOUND_FLAG.inv()
            elements.add(index, MapElement(key, value))
        }
        return index
    }

    fun capacity(): Int {
        return elements.capacity()
    }

    fun clear() {
        elements.clear()
    }

    fun getValue(index: Int): Any {
        return (elements[index] as MapElement).value
    }

    fun getValue(key: Comparable<Any?>): Any {
        val index = findElement(key)
        if (index and ELEMENT_NOT_FOUND_FLAG == 0) return (elements[index] as MapElement).value
        throw NoSuchElementException()
    }

    fun getKey(index: Int): Comparable<*> {
        return (elements[index] as MapElement).key
    }

    fun containsKey(key: Comparable<Any?>): Boolean {
        val index = findElement(key)
        return index and ELEMENT_NOT_FOUND_FLAG == 0
    }

    fun indexOf(key: Comparable<Any?>): Int {
        val index = findElement(key)
        return if (index and ELEMENT_NOT_FOUND_FLAG == 0) index else -1
    }

    private fun findElement(key: Comparable<Any?>, first: Int = 0, size: Int = size()): Int {
        if (size == 0) return first or ELEMENT_NOT_FOUND_FLAG
        val halfSize = size / 2
        val middle = first + halfSize
        val middleElement = elements[middle]
        var comparationResult = key.compareTo(middleElement.key)
        val last = first + size - 1
        return if (comparationResult < 0) {
            if (halfSize <= 1) {
                if (middle != first) comparationResult = key.compareTo(elements[first].key)
                if (comparationResult < 0) return first or ELEMENT_NOT_FOUND_FLAG
                return if (comparationResult == 0) first else middle or ELEMENT_NOT_FOUND_FLAG
            }
            findElement(key, first, halfSize)
        } else if (comparationResult == 0) {
            middle
        } else {
            if (halfSize <= 1) {
                if (middle != last) comparationResult = key.compareTo(elements[last].key)
                if (comparationResult < 0) return last or ELEMENT_NOT_FOUND_FLAG
                return if (comparationResult == 0) last else last + 1 or ELEMENT_NOT_FOUND_FLAG
            }
            findElement(key, middle, size - halfSize)
        }
    }

    fun remove(key: Comparable<Any?>): Boolean {
        val index = findElement(key)
        if (index and ELEMENT_NOT_FOUND_FLAG == 0) {
            elements.removeAt(index)
            return true
        }
        return false
    }

    fun remove(index: Int) {
        elements.removeElementAt(index)
    }

    fun size(): Int {
        return elements.size
    }

    companion object {
        private const val ELEMENT_NOT_FOUND_FLAG = 1 shl 31
        private const val CAPACITY_INCREMENT = 10
    }
}