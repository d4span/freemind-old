/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 08.08.2004
 */
/*$Id: MapModuleManager.java,v 1.1.4.4.2.14 2008/05/31 10:55:04 dpolivaev Exp $*/
package freemind.controller

import freemind.main.Tools
import freemind.modes.MindMap
import freemind.modes.Mode
import freemind.modes.ModeController
import freemind.view.MapModule
import freemind.view.mindmapview.MapView
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.Collections
import java.util.LinkedList
import java.util.Vector

/**
 * Manages the list of MapModules. As this task is very complex, I exported it
 * from Controller to this class to keep Controller simple.
 *
 * The information exchange between controller and this class is managed by
 * oberser pattern (the controller observes changes to the map modules here).
 *
 * TODO: Use an vector with the map modules ordered by the screen order.
 */
class MapModuleManager internal constructor() {
    interface MapModuleChangeObserver {
        /**
         * The params may be null to indicate the there was no previous map, or
         * that the last map is closed now.
         */
        fun isMapModuleChangeAllowed(
            oldMapModule: MapModule?, oldMode: Mode?,
            newMapModule: MapModule?, newMode: Mode?
        ): Boolean

        fun beforeMapModuleChange(
            oldMapModule: MapModule?, oldMode: Mode?,
            newMapModule: MapModule?, newMode: Mode?
        )

        fun afterMapClose(oldMapModule: MapModule?, oldMode: Mode?)
        fun afterMapModuleChange(
            oldMapModule: MapModule?, oldMode: Mode?,
            newMapModule: MapModule?, newMode: Mode?
        )

        /**
         * To enable/disable the previous/next map actions.
         *
         * @param pIndex
         * TODO
         */
        fun numberOfOpenMapInformation(number: Int, pIndex: Int)
    }

    class MapModuleChangeObserverCompound : MapModuleChangeObserver {
        private val listeners = HashSet<MapModuleChangeObserver>()
        fun addListener(listener: MapModuleChangeObserver) {
            listeners.add(listener)
        }

        fun removeListener(listener: MapModuleChangeObserver) {
            listeners.remove(listener)
        }

        override fun isMapModuleChangeAllowed(
            oldMapModule: MapModule?,
            oldMode: Mode?, newMapModule: MapModule?, newMode: Mode?
        ): Boolean {
            var returnValue = true
            for (observer in Vector(listeners)) {
                returnValue = observer.isMapModuleChangeAllowed(
                    oldMapModule,
                    oldMode, newMapModule, newMode
                )
                if (!returnValue) {
                    break
                }
            }
            return returnValue
        }

        override fun beforeMapModuleChange(
            oldMapModule: MapModule?, oldMode: Mode?,
            newMapModule: MapModule?, newMode: Mode?
        ) {
            for (observer in Vector(listeners)) {
                observer.beforeMapModuleChange(
                    oldMapModule, oldMode,
                    newMapModule, newMode
                )
            }
        }

        override fun afterMapModuleChange(
            oldMapModule: MapModule?, oldMode: Mode?,
            newMapModule: MapModule?, newMode: Mode?
        ) {
            for (observer in Vector(listeners)) {
                observer.afterMapModuleChange(
                    oldMapModule, oldMode,
                    newMapModule, newMode
                )
            }
        }

        override fun numberOfOpenMapInformation(number: Int, pIndex: Int) {
            for (observer in Vector(listeners)) {
                observer.numberOfOpenMapInformation(number, pIndex)
            }
        }

        override fun afterMapClose(oldMapModule: MapModule?, oldMode: Mode?) {
            for (observer in Vector(listeners)) {
                observer.afterMapClose(oldMapModule, oldMode)
            }
        }
    }

    /**
     * You can register yourself to this listener at the main controller.
     */
    interface MapTitleChangeListener {
        fun setMapTitle(
            pNewMapTitle: String?, pMapModule: MapModule?,
            pModel: MindMap?
        )
    }

    var listener = MapModuleChangeObserverCompound()
    fun addListener(pListener: MapModuleChangeObserver) {
        listener.addListener(pListener)
    }

    fun removeListener(pListener: MapModuleChangeObserver) {
        listener.removeListener(pListener)
    }

    /**
     * You can register yourself as a contributor to the title at the main
     * controller.
     */
    interface MapTitleContributor {
        /**
         * @param pOldTitle
         * The current title
         * @param pMapModule
         * @param pModel
         * @return The current title can be changed or something can be added,
         * but it must be returned as a whole.
         */
        fun getMapTitle(
            pOldTitle: String?, pMapModule: MapModule?,
            pModel: MindMap?
        ): String?
    }
    // /** Contains pairs String (key+extension) => MapModule instances.
    // * The instances of mode, ie. the Model/View pairs. Normally, the
    // * order should be the order of insertion, but such a Map is not
    // * available. */
    // private Map mapModules = new HashMap();
    /**
     * A vector of MapModule instances. They are ordered according to their
     * screen order.
     */
    private val mapModuleVector = Vector<MapModule?>()

    /** reference to the current mapmodule; null is allowed, too.  */
    var mapModule: MapModule? = null
        private set

    /**
     * Reference to the current mode as the mapModule may be null.
     */
    private var mCurrentMode: Mode? = null

    /**
     * @return a map of String to MapModule elements.
     */
    @get:Deprecated(
        """use getMapModuleVector instead (and get the displayname as
	              MapModule.getDisplayName()."""
    )
    val mapModules: Map<String, MapModule?>
        get() {
            val returnValue = HashMap<String, MapModule?>()
            for (module in mapModuleVector) {
                returnValue[module!!.displayName] = module
            }
            return Collections.unmodifiableMap(returnValue)
        }

    fun getMapModuleVector(): List<MapModule?> {
        return Collections.unmodifiableList(mapModuleVector)
    }

    /** @return an unmodifiable set of all display names of current opened maps.
     */
    val mapKeys: List<String>
        get() {
            val returnValue = LinkedList<String>()
            for (module in mapModuleVector) {
                returnValue.add(module!!.displayName)
            }
            return Collections.unmodifiableList(returnValue)
        }

    fun newMapModule(map: MindMap?, modeController: ModeController) {
        val mapModule = MapModule(
            map, MapView(map, modeController),
            modeController.mode, modeController
        )
        addToOrChangeInMapModules(mapModule.toString(), mapModule)
        setMapModule(mapModule, modeController.mode)
    }

    fun getModuleGivenModeController(pModeController: ModeController): MapModule? {
        var mapModule: MapModule? = null
        for (value in getMapModuleVector()) {
            mapModule = value
            if (pModeController == mapModule!!.modeController) {
                break
            }
            mapModule = null
        }
        return mapModule
    }

    fun updateMapModuleName() {
        // removeFromViews() doesn't work because MapModuleChanged()
        // must not be called at this state
        mapModule!!.rename()
        addToOrChangeInMapModules(mapModule.toString(), mapModule)
        setMapModule(mapModule, mapModule!!.mode)
    }

    fun nextMapModule() {
        val index: Int
        val size = mapModuleVector.size
        index = if (mapModule != null) mapModuleVector.indexOf(mapModule) else size - 1
        if (index + 1 < size && index >= 0) {
            changeToMapModule(mapModuleVector[index + 1])
        } else if (size > 0) {
            // Change to the first in the list
            changeToMapModule(mapModuleVector[0])
        }
    }

    fun previousMapModule() {
        val index: Int
        val size = mapModuleVector.size
        index = if (mapModule != null) mapModuleVector.indexOf(mapModule) else 0
        if (index > 0) {
            changeToMapModule(mapModuleVector[index - 1])
        } else {
            if (size > 0) {
                changeToMapModule(mapModuleVector[size - 1])
            }
        }
    }
    // Change MapModules
    /**
     * This is the question whether the map is already opened. If this is the
     * case, the map is automatically opened + returns true. Otherwise does
     * nothing + returns false.
     */
    fun tryToChangeToMapModule(mapModule: String?): Boolean {
        return if (mapModule != null && mapKeys.contains(mapModule)) {
            changeToMapModule(mapModule)
            true
        } else {
            false
        }
    }

    /**
     *
     * Checks, whether or not a given url is already opened. Unlike
     * tryToChangeToMapModule, it does not consider the map+extension
     * identifiers nor switches to the module.
     *
     * @return null, if not found, the map+extension identifier otherwise.
     */
    @Throws(MalformedURLException::class)
    fun checkIfFileIsAlreadyOpened(urlToCheck: URL): String? {
        for (module in mapModuleVector) {
            if (module!!.model != null) {
                val moduleUrl = module.model.url
                if (sameFile(urlToCheck, moduleUrl)) return module.displayName
            }
        }
        return null
    }

    private fun sameFile(urlToCheck: URL, moduleUrl: URL?): Boolean {
        if (moduleUrl == null) {
            return false
        }
        return if (urlToCheck.protocol == "file" && moduleUrl.protocol == "file") {
            File(urlToCheck.file) == File(
                moduleUrl
                    .file
            )
        } else urlToCheck.sameFile(moduleUrl)
    }

    fun changeToMapModule(mapModuleDisplayName: String): Boolean {
        var mapModuleCandidate: MapModule? = null
        for (mapMod in mapModuleVector) {
            if (Tools.safeEquals(mapModuleDisplayName, mapMod!!.displayName)) {
                mapModuleCandidate = mapMod
                break
            }
        }
        requireNotNull(mapModuleCandidate) {
            ("Map module "
                    + mapModuleDisplayName + " not found.")
        }
        return changeToMapModule(mapModuleCandidate)
    }

    fun changeToMapModule(mapModuleCandidate: MapModule?): Boolean {
        return setMapModule(mapModuleCandidate, mapModuleCandidate!!.mode)
    }

    fun changeToMapOfMode(mode: Mode) {
        for (mapMod in mapModuleVector) {
            if (mapMod!!.mode === mode) {
                changeToMapModule(mapMod)
                return
            }
        }
        // there is no map with the given mode open. We have to create an empty
        // one?
        setMapModule(null, mode)
        // FIXME: Is getting here an error? fc, 25.11.2005.
    }

    /**
     * is null if the old mode should be closed.
     *
     * @return true if the set command was successful.
     */
    fun setMapModule(newMapModule: MapModule?, newMode: Mode?): Boolean {
        // allowed?
        val oldMapModule = mapModule
        val oldMode = mCurrentMode
        if (!listener.isMapModuleChangeAllowed(
                oldMapModule, oldMode,
                newMapModule, newMode
            )
        ) {
            return false
        }
        listener.beforeMapModuleChange(
            oldMapModule, oldMode, newMapModule,
            newMode
        )
        mapModule = newMapModule
        mCurrentMode = newMode
        listener.afterMapModuleChange(
            oldMapModule, oldMode, newMapModule,
            newMode
        )
        fireNumberOfOpenMapInformation()
        return true
    }

    private fun fireNumberOfOpenMapInformation() {
        listener.numberOfOpenMapInformation(
            mapModuleVector.size,
            mapModuleVector.indexOf(mapModule)
        )
    }

    // private
    private fun addToOrChangeInMapModules(
        key: String,
        newOrChangedMapModule: MapModule?
    ) {
        // begin bug fix, 20.12.2003, fc.
        // check, if already present:
        var extension = ""
        var count = 1
        val mapKeys = mapKeys
        while (mapKeys.contains(key + extension)) {
            extension = "<" + ++count + ">"
        }
        // rename map:
        newOrChangedMapModule!!.name = key + extension
        newOrChangedMapModule.displayName = key + extension
        if (!mapModuleVector.contains(newOrChangedMapModule)) {
            mapModuleVector.add(newOrChangedMapModule)
        }
        // end bug fix, 20.12.2003, fc.
    }

    /**
     * Close the currently active map, return false if closing canceled.
     *
     * @param force
     * forces the closing without any save actions.
     * @param pRestorable
     * is a buffer, if the name of the restorable is needed after
     * saving.
     * @return false if saving was canceled.
     */
    fun close(force: Boolean, pRestorable: StringBuffer?): Boolean {
        // (DP) The mode controller does not close the map
        val module = mapModule
        // FIXME: This is not correct, as this class should not ask somebody.
        // This class is only a list!
        val closingNotCancelled = module!!.modeController.close(
            force,
            this
        )
        if (!closingNotCancelled) {
            return false
        }
        pRestorable?.append(module.model.restorable)
        var index = mapModuleVector.indexOf(module)
        mapModuleVector.remove(module)
        if (mapModuleVector.isEmpty()) {
            /* Keep the current running mode */
            setMapModule(null, module.mode)
        } else {
            if (index >= mapModuleVector.size || index < 0) {
                index = mapModuleVector.size - 1
            }
            changeToMapModule(mapModuleVector[index])
        }
        listener.afterMapClose(module, module.mode)
        return true
    }

    // }}
    fun swapModules(src: Int, dst: Int) {
        Tools.swapVectorPositions(mapModuleVector, src, dst)
        fireNumberOfOpenMapInformation()
    }
}