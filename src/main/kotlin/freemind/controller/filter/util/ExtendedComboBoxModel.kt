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
import javax.swing.DefaultComboBoxModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

/**
 * @author dimitri 14.05.2005
 */
class ExtendedComboBoxModel : DefaultComboBoxModel<Any?> {
    private inner class ExtensionDataListener : ListDataListener {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListDataListener#intervalAdded(javax.swing.event
		 * .ListDataEvent)
		 */
        override fun intervalAdded(e: ListDataEvent) {
            val size = ownSize
            fireIntervalAdded(model, size + e.index0,
                    size + e.index1)
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event
		 * .ListDataEvent)
		 */
        override fun intervalRemoved(e: ListDataEvent) {
            val size = ownSize
            fireIntervalRemoved(model, size + e.index0,
                    size + e.index1)
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListDataListener#contentsChanged(javax.swing.event
		 * .ListDataEvent)
		 */
        override fun contentsChanged(e: ListDataEvent) {
            val size = ownSize
            fireContentsChanged(model, size + e.index0,
                    size + e.index1)
        }
    }

    private var extension: SortedListModel? = null
    private val extensionDataListener = ExtensionDataListener()

    /**
     */
    fun setExtensionList(sortedListModel: SortedListModel?) {
        val ownSize = ownSize
        run {
            if (extension != null) {
                extension!!.removeListDataListener(extensionDataListener)
                val extensionSize = extensionSize
                if (extensionSize > 0) {
                    fireIntervalRemoved(this, ownSize, ownSize + extensionSize
                            - 1)
                }
            }
        }
        run {
            extension = sortedListModel
            val extensionSize = extensionSize
            if (extensionSize > 0) {
                fireIntervalAdded(this, ownSize, ownSize + extensionSize - 1)
            }
            if (extension != null) {
                extension!!.addListDataListener(extensionDataListener)
            }
        }
    }

    constructor() : super() {}
    constructor(o: Array<Any?>?) : super(o) {}
    constructor(v: Vector<Any?>?) : super(v) {}

    override fun getElementAt(i: Int): Any? {
        val s = ownSize
        return if (i < s || extension == null) super.getElementAt(i) else extension!!.getElementAt(i - s)
    }

    override fun getSize(): Int {
        return ownSize + extensionSize
    }

    private val extensionSize: Int
        private get() = if (extension != null) extension!!.size else 0

    /**
     */
    private val ownSize: Int
        private get() = super.getSize()
    private val model: ExtendedComboBoxModel
        private get() = this

    override fun insertElementAt(o: Any?, i: Int) {
        super.insertElementAt(o, Math.min(ownSize, i))
    }

    override fun removeAllElements() {
        super.removeAllElements()
        if (extension != null) {
            extension!!.clear()
        }
    }

    override fun removeElement(o: Any) {
        super.removeElement(o)
    }

    override fun removeElementAt(i: Int) {
        if (i < ownSize) super.removeElementAt(i)
    }

    fun addSortedElement(o: Any?) {
        if (extension != null && !extension!!.contains(o)) {
            extension!!.add(o)
        }
    }

    override fun getIndexOf(o: Any): Int {
        val idx = super.getIndexOf(o)
        if (idx > -1 || extension == null) {
            return idx
        }
        val extIdx = extension!!.getIndexOf(o)
        return if (extIdx > -1) extIdx + ownSize else -1
    }
}