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
 * Created on 14.05.2005
 *
 */
package freemind.controller.filter.util

import java.util.*
import javax.swing.AbstractListModel

open class SortedMapListModel : AbstractListModel<Any?>(), SortedListModel {
    var model: SortedSet<Any?>

    init {
        model = TreeSet<Any?>()
    }

    override fun getSize(): Int {
        return model.size
    }

    override fun getElementAt(index: Int): Any? {
        return model.toTypedArray()[index]
    }

    override fun add(element: Any?) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, size)
        }
    }

    fun addAll(elements: Array<Any?>) {
        val c: Collection<*> = Arrays.asList(*elements)
        model.addAll(c)
        fireContentsChanged(this, 0, size)
    }

    fun addAll(pObjects: List<Any?>?) {
        model.addAll(pObjects ?: emptyList())
        fireContentsChanged(this, 0, size)
    }

    override fun clear() {
        val oldSize = size
        if (oldSize > 0) {
            model.clear()
            fireIntervalRemoved(this, 0, oldSize - 1)
        }
    }

    override fun contains(element: Any?): Boolean {
        return model.contains(element)
    }

    fun firstElement(): Any? {
        return model.first()
    }

    operator fun iterator(): Iterator<*> {
        return model.iterator()
    }

    fun lastElement(): Any? {
        return model.last()
    }

    /**
     */
    override fun getIndexOf(o: Any): Int {
        val i = iterator()
        var count = -1
        while (i.hasNext()) {
            count++
            if (i.next() == o) return count
        }
        return -1
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.util.SortedListModel#replace(java.lang.Object,
	 * java.lang.Object)
	 */
    override fun replace(oldO: Any, newO: Any) {
        if (oldO == newO) return
        val removed = model.remove(oldO)
        val added = model.add(newO)
        if (removed || added) {
            fireContentsChanged(this, 0, size)
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.util.SortedListModel#delete(java.lang.Object)
	 */
    override fun remove(element: Any?) {
        if (model.remove(element)) {
            fireContentsChanged(this, 0, size)
        }
    }
}