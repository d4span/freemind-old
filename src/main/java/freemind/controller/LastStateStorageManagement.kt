/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.main.Resources
import freemind.main.Tools
import java.util.Collections
import java.util.TreeMap
import java.util.Vector
import java.util.logging.Logger

/**
 * @author foltin
 */
class LastStateStorageManagement(pXml: String?) {
    var lastFocussedTab: Int = 0
    private var mLastStatesMap: MindmapLastStateMapStorage? = null

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        try {
            val action = Tools.unMarshall(pXml)
            if (action != null) {
                if (action is MindmapLastStateMapStorage) {
                    mLastStatesMap = action
                }
            }
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
        if (mLastStatesMap == null) {
            logger!!.warning("Creating a new last state map storage as there was no old one or it was corrupt.")
            mLastStatesMap = MindmapLastStateMapStorage()
        }
    }

    val xml: String
        get() = Tools.marshall(mLastStatesMap)

    fun clearTabIndices() {
        val it = mLastStatesMap!!.listMindmapLastStateStorageList.iterator()
        while (it.hasNext()) {
            val store = it.next()
            store?.tabIndex = -1
        }
    }

    fun changeOrAdd(pStore: MindmapLastStateStorage) {
        var found = false
        val it = mLastStatesMap!!.listMindmapLastStateStorageList.iterator()
        while (it.hasNext()) {
            val store = it.next()
            if (Tools.safeEquals(
                    pStore.restorableName,
                    store?.restorableName
                )
            ) {
                // deep copy
                store?.lastZoom = pStore.lastZoom
                store?.lastSelected = pStore.lastSelected
                store?.x = pStore.x
                store?.y = pStore.y
                val listCopy = Vector(pStore.listNodeListMemberList)
                store?.clearNodeListMemberList()
                for (member in listCopy) {
                    store?.addNodeListMember(member)
                }
                found = true
                setLastChanged(store)
                break
            }
        }
        if (!found) {
            setLastChanged(pStore)
            mLastStatesMap!!.addMindmapLastStateStorage(pStore)
        }
        // size limit
        if (mLastStatesMap!!.sizeMindmapLastStateStorageList() > LIST_AMOUNT_LIMIT) {
            // make map from date to object:
            val dateToStoreMap = TreeMap<Long, MindmapLastStateStorage?>()
            val iterator = mLastStatesMap!!.listMindmapLastStateStorageList.iterator()
            while (iterator.hasNext()) {
                val store = iterator.next()
                dateToStoreMap[java.lang.Long.valueOf(-(store?.lastChanged ?: 0))] = store
            }
            // clear list
            mLastStatesMap!!.clearMindmapLastStateStorageList()
            // rebuild
            var counter = 0
            for ((_, value) in dateToStoreMap) {
                mLastStatesMap!!.addMindmapLastStateStorage(value)
                counter++
                if (counter >= LIST_AMOUNT_LIMIT) {
                    // drop the rest of the elements.
                    break
                }
            }
        }
    }

    private fun setLastChanged(pStore: MindmapLastStateStorage?) {
        pStore?.lastChanged = System.currentTimeMillis()
    }

    fun getStorage(pRestorableName: String?): MindmapLastStateStorage? {
        val it = mLastStatesMap!!.listMindmapLastStateStorageList.iterator()
        while (it.hasNext()) {
            val store = it.next()
            if (Tools.safeEquals(pRestorableName, store?.restorableName)) {
                setLastChanged(store)
                return store
            }
        }
        return null
    }

    val lastOpenList: List<MindmapLastStateStorage>
        get() {
            val ret = Vector<MindmapLastStateStorage>()
            val it = mLastStatesMap!!.listMindmapLastStateStorageList.iterator()
            while (it.hasNext()) {
                val store = it.next()
                if ((store?.tabIndex ?: -1) >= 0) {
                    ret.add(store)
                }
            }
            Collections.sort(ret) { store0, store1 -> store0.tabIndex - store1.tabIndex }
            return ret
        }

    companion object {
        const val LIST_AMOUNT_LIMIT = 50
        protected var logger: Logger? = null
    }
}