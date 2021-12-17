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
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter

import freemind.controller.*
import freemind.controller.MapModuleManager.MapModuleChangeObserver
import freemind.controller.filter.condition.*
import freemind.main.*
import freemind.modes.*
import freemind.modes.common.plugins.NodeNoteBase
import freemind.view.ImageFactory
import freemind.view.MapModule
import java.io.*
import javax.swing.DefaultComboBoxModel

/**
 * @author dimitri
 */
class FilterController(private val c: Controller) : MapModuleChangeObserver {
    private var filterToolbar: FilterToolbar? = null
    private var filterConditionModel: DefaultComboBoxModel<Condition?>? = null

    /**
     */
    var map: MindMap? = null
        private set

    init {
        c.mapModuleManager?.addListener(this)
    }

    val conditionRenderer: ConditionRenderer?
        get() {
            if (Companion.conditionRenderer == null) Companion.conditionRenderer = ConditionRenderer()
            return Companion.conditionRenderer
        }

    /**
     */
    fun getFilterToolbar(): FilterToolbar {
        if (filterToolbar == null) {
            filterToolbar = FilterToolbar(c)
            filterConditionModel = filterToolbar?.filterConditionModel as DefaultComboBoxModel<Condition?>

            // FIXME state icons should be created on order to make possible
            // their use in the filter component.
            // It should not happen here.
            MindIcon.factory("AttributeExist", ImageFactory.instance?.createIcon(Resources
                    .instance?.getResource("images/showAttributes.gif")))
            MindIcon.factory(NodeNoteBase.NODE_NOTE_ICON, ImageFactory.instance?.createIcon(
                    Resources.instance?.getResource("images/knotes.png")))
            MindIcon.factory("encrypted")
            MindIcon.factory("decrypted")
            filterToolbar!!.initConditions()
        }
        return filterToolbar!!
    }

    /**
     */
    fun showFilterToolbar(show: Boolean) {
        if (show == isVisible) return
        getFilterToolbar().isVisible = show
        val filter = map!!.filter
        if (show) {
            filter.applyFilter(c)
        } else {
            createTransparentFilter()!!.applyFilter(c)
        }
        refreshMap()
    }

    val isVisible: Boolean
        get() = getFilterToolbar().isVisible

    fun refreshMap() {
        c.modeController?.refreshMap()
    }

    override fun isMapModuleChangeAllowed(oldMapModule: MapModule?,
                                          oldMode: Mode?, newMapModule: MapModule?, newMode: Mode?): Boolean {
        return true
    }

    override fun beforeMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                       newMapModule: MapModule?, newMode: Mode?) {
    }

    override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {}
    override fun afterMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                      newMapModule: MapModule?, newMode: Mode) {
        val newMap = newMapModule?.model
        val fd = getFilterToolbar().filterDialog
        fd?.mapChanged(newMap)
        map = newMap
        getFilterToolbar().mapChanged(newMap)
    }

    override fun numberOfOpenMapInformation(number: Int, pIndex: Int) {}
    fun saveConditions() {
        if (filterToolbar != null) {
            filterToolbar!!.saveConditions()
        }
    }

    fun getFilterConditionModel(): DefaultComboBoxModel<Condition?>? {
        return filterConditionModel
    }

    fun setFilterConditionModel(
            filterConditionModel: DefaultComboBoxModel<Condition?>?) {
        this.filterConditionModel = filterConditionModel
        filterToolbar?.filterConditionModel = filterConditionModel
    }

    @Throws(IOException::class)
    fun saveConditions(filterConditionModel: DefaultComboBoxModel<Condition?>?,
                       pathToFilterFile: String?) {
        val saver = XMLElement()
        saver.name = "filter_conditions"
        val writer: Writer = FileWriter(pathToFilterFile)
        for (i in 0 until filterConditionModel!!.size) {
            filterConditionModel.getElementAt(i)!!.save(saver)
        }
        saver.write(writer)
        writer.close()
    }

    @Throws(IOException::class)
    fun loadConditions(filterConditionModel: DefaultComboBoxModel<Condition?>?,
                       pathToFilterFile: String?) {
        filterConditionModel!!.removeAllElements()
        val loader = XMLElement()
        val reader: Reader = FileReader(pathToFilterFile)
        loader.parseFromReader(reader)
        reader.close()
        val conditions = loader.getChildren()
        if (conditions != null) {
            for (i in conditions.indices) {
                filterConditionModel.addElement(conditionFactory!!.loadCondition(conditions[i]))
            }
        }
    }

    companion object {
        private var conditionRenderer: ConditionRenderer? = null
        var conditionFactory: ConditionFactory? = null
            get() {
                if (field == null) field = ConditionFactory()
                return field
            }
            private set
        const val FREEMIND_FILTER_EXTENSION_WITHOUT_DOT = "mmfilter"
        private var inactiveFilter: Filter? = null
        private fun createTransparentFilter(): Filter? {
            if (inactiveFilter == null) inactiveFilter = DefaultFilter(
                    NoFilteringCondition.Companion.createCondition(), true, false)
            return inactiveFilter
        }
    }
}