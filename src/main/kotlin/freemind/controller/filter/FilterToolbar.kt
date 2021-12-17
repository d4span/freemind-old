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
import freemind.controller.filter.condition.*
import freemind.main.*
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.view.ImageFactory
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.*
import javax.swing.*

class FilterToolbar(c: Controller) : FreeMindToolBar() {
    private val mFilterController: FilterController?

    /**
     */
    var filterDialog: FilterComposerDialog? = null
        private set
    private val activeFilterConditionComboBox: JComboBox<Condition?>
    private val showAncestors: JCheckBox
    private val showDescendants: JCheckBox
    private var activeFilter: Filter?
    private val btnEdit: JButton?
    private val btnUnfoldAncestors: JButton?
    private val c: Controller
    private val pathToFilterFile: String
    private val filterChangeListener: FilterChangeListener

    private inner class FilterChangeListener  /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        : AbstractAction(), ItemListener, PropertyChangeListener {
        override fun actionPerformed(arg0: ActionEvent) {
            resetFilter()
            setMapFilter()
            refreshMap()
            DefaultFilter.Companion.selectVisibleNode(c.view)
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent
		 * )
		 */
        override fun itemStateChanged(e: ItemEvent) {
            if (e.stateChange == ItemEvent.SELECTED) filterChanged()
        }

        private fun filterChanged() {
            resetFilter()
            setMapFilter()
            val map = mFilterController?.map
            if (map != null) {
                activeFilter!!.applyFilter(c)
                refreshMap()
                DefaultFilter.Companion.selectVisibleNode(c.view)
            }
        }

        override fun propertyChange(evt: PropertyChangeEvent) {
            if (evt.propertyName == "model") {
                addStandardConditions()
                filterChanged()
            }
        }
    }

    private inner class EditFilterAction internal constructor() : AbstractAction("", ImageFactory.instance?.createIcon(Resources.instance?.getResource(
            "images/Btn_edit.gif"))) {
        init {
            putValue(SHORT_DESCRIPTION, Resources.instance
                    ?.getResourceString("filter_edit_description"))
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        private fun getFilterDialog(): FilterComposerDialog {
            if (filterDialog == null) {
                filterDialog = FilterComposerDialog(c, this@FilterToolbar)
                filterDialog!!.setLocationRelativeTo(this@FilterToolbar)
            }
            return filterDialog!!
        }

        override fun actionPerformed(arg0: ActionEvent) {
            val selectedItem = filterConditionModel!!.selectedItem
            if (selectedItem != null) {
                getFilterDialog().setSelectedItem(selectedItem)
            }
            getFilterDialog().show()
        }
    }

    private inner class UnfoldAncestorsAction
    /**
     *
     */
    internal constructor() : AbstractAction("", ImageFactory.instance?.createIcon(Resources.instance?.getResource(
            "images/unfold.png"))) {
        private fun unfoldAncestors(parent: MindMapNode?) {
            val i = parent?.childrenUnfolded()
            while (i?.hasNext() ?: false) {
                val node = i?.next()
                if (showDescendants.isSelected
                        || node?.filterInfo.isAncestor) {
                    setFolded(node, false)
                    unfoldAncestors(node)
                }
            }
        }

        private fun setFolded(node: MindMapNode?, state: Boolean) {
            if (node?.hasChildren() ?: false && node?.isFolded != state) {
                c.modeController?.setFolded(node, state)
            }
        }

        override fun actionPerformed(e: ActionEvent) {
            if (selectedCondition != null) {
                unfoldAncestors(c.model?.rootNode)
            }
        }
    }

    init {
        mFilterController = c.filterController
        this.c = c
        isVisible = false
        isFocusable = false
        isRollover = true
        filterChangeListener = FilterChangeListener()
        add(JLabel(Resources.instance?.getResourceString(
                "filter_toolbar")
                + " "))
        activeFilter = null
        activeFilterConditionComboBox = object : JComboBox<Condition?>() {
            override fun getMaximumSize(): Dimension {
                return preferredSize
            }
        }
        activeFilterConditionComboBox.isFocusable = false
        pathToFilterFile = (c.frame.freemindDirectory + File.separator
                + "auto."
                + FilterController.Companion.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT)
        btnEdit = add(EditFilterAction())
        add(btnEdit)
        btnUnfoldAncestors = add(UnfoldAncestorsAction())
        btnUnfoldAncestors.setToolTipText(Resources.instance?.getResourceString("filter_unfold_ancestors"))
        add(btnUnfoldAncestors)
        showAncestors = JCheckBox(Resources.instance
                ?.getResourceString("filter_show_ancestors"), true)
        add(showAncestors)
        showAncestors.model.addActionListener(filterChangeListener)
        showDescendants = JCheckBox(Resources.instance
                ?.getResourceString("filter_show_descendants"), false)
        add(showDescendants)
        showDescendants.model.addActionListener(filterChangeListener)
    }

    fun addStandardConditions() {
        val filterConditionModel = mFilterController!!.getFilterConditionModel()
        val noFiltering = NoFilteringCondition.Companion.createCondition()
        filterConditionModel!!.insertElementAt(noFiltering, 0)
        filterConditionModel.insertElementAt(
                SelectedViewCondition.Companion.CreateCondition(), 1)
        if (filterConditionModel.selectedItem == null) {
            filterConditionModel.selectedItem = noFiltering
        }
    }

    fun initConditions() {
        try {
            mFilterController!!.loadConditions(mFilterController.getFilterConditionModel(), pathToFilterFile)
        } catch (e: Exception) {
        }
        addStandardConditions()
        activeFilterConditionComboBox.selectedIndex = 0
        activeFilterConditionComboBox.setRenderer(mFilterController?.conditionRenderer)
        add(activeFilterConditionComboBox)
        add(Box.createHorizontalGlue())
        activeFilterConditionComboBox.addItemListener(filterChangeListener)
        activeFilterConditionComboBox
                .addPropertyChangeListener(filterChangeListener)
    }

    /**
     *
     */
    fun resetFilter() {
        activeFilter = null
    }

    private val selectedCondition: Condition?
        private get() = activeFilterConditionComboBox.selectedItem as Condition

    fun setMapFilter() {
        if (activeFilter == null) activeFilter = DefaultFilter(selectedCondition,
                showAncestors.model.isSelected, showDescendants
                .model.isSelected)
        val map = mFilterController?.map
        if (map != null) {
            map.filter = activeFilter
        }
    }

    /**
     */
    fun mapChanged(newMap: MindMap?) {
        if (!isVisible) return
        val filter: Filter?
        if (newMap != null) {
            filter = newMap.filter
            if (filter !== activeFilter) {
                activeFilter = filter
                activeFilterConditionComboBox.selectedItem = filter
                        .condition
                showAncestors.isSelected = filter.areAncestorsShown()
                showDescendants.isSelected = filter.areDescendantsShown()
            }
        } else {
            filter = null
            activeFilterConditionComboBox.setSelectedIndex(0)
        }
    }

    private fun refreshMap() {
        mFilterController!!.refreshMap()
    }

    fun saveConditions() {
        try {
            mFilterController!!.saveConditions(mFilterController.getFilterConditionModel(), pathToFilterFile)
        } catch (e: Exception) {
        }
    }

    var filterConditionModel: ComboBoxModel<Condition?>?
        get() = activeFilterConditionComboBox.model
        set(filterConditionModel) {
            activeFilterConditionComboBox.model = filterConditionModel
        }
}