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

import freemind.common.NamedObject
import freemind.controller.*
import freemind.controller.filter.condition.*
import freemind.controller.filter.util.ExtendedComboBoxModel
import freemind.controller.filter.util.SortedComboBoxModel
import freemind.controller.filter.util.SortedListModel
import freemind.main.*
import freemind.modes.FreeMindFileDialog
import freemind.modes.MindIcon
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import java.awt.*
import java.awt.event.*
import java.io.*
import java.util.*
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.filechooser.FileFilter

/**
 * @author dimitri
 */
class FilterComposerDialog(private val mController: Controller, pFilterToolbar: FilterToolbar?) : JDialog(mController.jFrame, mController.getResourceString("filter_dialog")) {
    /**
     * @author dimitri 06.05.2005
     */
    private inner class AddConditionAction internal constructor() : AbstractAction() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        init {
            Tools.setLabelAndMnemonic(this, Resources.instance
                    ?.getResourceString("filter_add"))
        }

        override fun actionPerformed(e: ActionEvent) {
            var newCond: Condition? = null
            val value: String?
            try {
                value = attributeValue
            } catch (ex: NullPointerException) {
                return
            }
            val simpleCond = simpleCondition
                    .selectedItem as NamedObject
            val ignoreCase = caseInsensitive.isSelected
            val selectedItem = attributes.selectedItem
            newCond = if (selectedItem is NamedObject) {
                FilterController.Companion.conditionFactory?.createCondition(selectedItem,
                        simpleCond, value, ignoreCase)
            } else {
                val attribute = selectedItem.toString()
                FilterController.Companion.conditionFactory?.createAttributeCondition(
                        attribute, simpleCond, value, ignoreCase)
            }
            val model = conditionList.model as DefaultComboBoxModel<Condition>
            if (newCond != null) model.addElement(newCond)
            if (values?.isEditable ?: false) {
                val item = values?.selectedItem
                if (item != null && item != "") {
                    values?.removeItem(item)
                    values?.insertItemAt(item, 0)
                    values?.selectedIndex = 0
                    if (values?.itemCount ?: 0 >= 10) values?.removeItemAt(9)
                }
            }
            validate()
        }
    }

    private inner class DeleteConditionAction internal constructor() : AbstractAction() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        init {
            Tools.setLabelAndMnemonic(this, Resources.instance
                    ?.getResourceString("filter_delete"))
        }

        override fun actionPerformed(e: ActionEvent) {
            val model = conditionList
                    .model as DefaultComboBoxModel<Condition>
            val minSelectionIndex = conditionList.minSelectionIndex
            var selectedIndex: Int
            while (0 <= conditionList.selectedIndex.also { selectedIndex = it }) {
                model.removeElementAt(selectedIndex)
            }
            val size = conditionList.model.size
            if (size > 0) {
                conditionList.selectedIndex = if (minSelectionIndex < size) minSelectionIndex else size - 1
            }
            validate()
        }
    }

    private inner class CreateNotSatisfiedConditionAction internal constructor() : AbstractAction() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        init {
            Tools.setLabelAndMnemonic(this, Resources.instance
                    ?.getResourceString("filter_not"))
        }

        override fun actionPerformed(e: ActionEvent) {
            val min = conditionList.minSelectionIndex
            if (min >= 0) {
                val max = conditionList.minSelectionIndex
                if (min == max) {
                    val oldCond = conditionList
                            .selectedValue as Condition
                    val newCond: Condition = ConditionNotSatisfiedDecorator(
                            oldCond)
                    val model = conditionList.model as DefaultComboBoxModel<Condition>
                    model.addElement(newCond)
                    validate()
                }
            }
        }
    }

    private inner class CreateConjunctConditionAction internal constructor() : AbstractAction() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        init {
            Tools.setLabelAndMnemonic(this, Resources.instance
                    ?.getResourceString("filter_and"))
        }

        override fun actionPerformed(e: ActionEvent) {
            val selectedValues = conditionList.selectedValues
            if (selectedValues.size < 2) return
            val newCond: Condition = ConjunctConditions(selectedValues)
            val model = conditionList.model as DefaultComboBoxModel<Condition>
            model.addElement(newCond)
            validate()
        }
    }

    private inner class CreateDisjunctConditionAction internal constructor() : AbstractAction() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        init {
            Tools.setLabelAndMnemonic(this, Resources.instance
                    ?.getResourceString("filter_or"))
        }

        override fun actionPerformed(e: ActionEvent) {
            val selectedValues = conditionList.selectedValues
            if (selectedValues.size < 2) return
            val newCond: Condition = DisjunctConditions(selectedValues)
            val model = conditionList.model as DefaultComboBoxModel<Condition>
            model.addElement(newCond)
            validate()
        }
    }

    private inner class ConditionListSelectionListener : ListSelectionListener, ListDataListener {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.
		 * event.ListSelectionEvent)
		 */
        override fun valueChanged(e: ListSelectionEvent) {
            if (conditionList.minSelectionIndex == -1) {
                btnNot.isEnabled = false
                btnAnd.isEnabled = false
                btnOr.isEnabled = false
                btnDelete.isEnabled = false
                return
            } else if (conditionList.minSelectionIndex == conditionList
                            .maxSelectionIndex) {
                btnNot.isEnabled = true
                btnAnd.isEnabled = false
                btnOr.isEnabled = false
                btnDelete.isEnabled = true
                return
            } else {
                btnNot.isEnabled = false
                btnAnd.isEnabled = true
                btnOr.isEnabled = true
                btnDelete.isEnabled = true
            }
        }

        override fun intervalAdded(e: ListDataEvent) {
            conditionList.selectedIndex = e.index0
        }

        override fun intervalRemoved(e: ListDataEvent) {}
        override fun contentsChanged(e: ListDataEvent) {}
    }

    private inner class ConditionListMouseListener : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.clickCount == 2) {
                EventQueue.invokeLater {
                    if (selectCondition()) {
                        dispose()
                    }
                }
            }
        }
    }

    private inner class CloseAction : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val source = e.source
            if (source === btnOK || source === btnApply) applyChanges()
            if (source === btnOK || source === btnCancel) dispose() else initInternalConditionModel()
        }
    }

    private class MindMapFilterFileFilter : FileFilter() {
        override fun accept(f: File): Boolean {
            if (f.isDirectory) return true
            val extension = Tools.getExtension(f.name)
            return if (extension != null) {
                if (extension
                        == FilterController.Companion.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT) {
                    true
                } else {
                    false
                }
            } else false
        }

        override fun getDescription(): String? {
            return Resources.instance?.getResourceString(
                    "mindmaps_filter_desc")
        }

        companion object {
            var filter: FileFilter = MindMapFilterFileFilter()
        }
    }

    protected val fileChooser: FreeMindFileDialog?
        protected get() = Resources.instance?.getStandardFileChooser(MindMapFilterFileFilter.filter)

    private inner class SaveAction : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val chooser = fileChooser
            chooser?.setDialogTitle(Resources.instance?.getResourceString(
                    "save_as"))
            val returnVal = chooser?.showSaveDialog(this@FilterComposerDialog)
            if (returnVal != JFileChooser.APPROVE_OPTION) { // not ok pressed
                return
            }

            // |= Pressed O.K.
            try {
                val f = chooser.selectedFile
                var canonicalPath = f.canonicalPath
                val suffix = '.'.toString() + FilterController.Companion.FREEMIND_FILTER_EXTENSION_WITHOUT_DOT
                if (!canonicalPath.endsWith(suffix)) {
                    canonicalPath = canonicalPath + suffix
                }
                mFilterController?.saveConditions(internalConditionsModel, canonicalPath)
            } catch (ex: Exception) {
                handleSavingException(ex)
            }
        }

        private fun handleSavingException(ex: Exception) {
            // TODO Auto-generated method stub
        }
    }

    private inner class LoadAction : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val chooser = fileChooser
            val returnVal = chooser?.showOpenDialog(this@FilterComposerDialog)
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    val theFile = chooser.selectedFile
                    mFilterController?.loadConditions(internalConditionsModel,
                            theFile.canonicalPath)
                } catch (ex: Exception) {
                    handleLoadingException(ex)
                }
            }
        }

        private fun handleLoadingException(ex: Exception) {
            // TODO Auto-generated method stub
        }
    }

    private inner class SimpleConditionChangeListener : ItemListener {
        override fun itemStateChanged(e: ItemEvent) {
            if (e.stateChange == ItemEvent.SELECTED) {
                val considerValue = true
                caseInsensitive.isEnabled = considerValue
                values!!.isEnabled = considerValue
            }
        }
    }

    private inner class SelectedAttributeChangeListener : ItemListener {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.
		 * event.ListSelectionEvent)
		 */
        override fun itemStateChanged(e: ItemEvent) {
            if (e.stateChange == ItemEvent.SELECTED) {
                if (attributes.selectedIndex == NODE_POSITION) {
                    simpleCondition.model = simpleNodeConditionComboBoxModel
                    simpleCondition.isEnabled = true
                    values!!.isEditable = true
                    values.isEnabled = true
                    nodes.setExtensionList(null)
                    values.setModel(nodes)
                    caseInsensitive.isEnabled = true
                    return
                }
                if (attributes.selectedIndex == ICON_POSITION) {
                    simpleCondition.setModel(simpleIconConditionComboBoxModel)
                    simpleCondition.isEnabled = true
                    values!!.isEditable = false
                    values.isEnabled = true
                    values.setModel(icons)
                    if (icons.size >= 1) {
                        values.selectedIndex = 0
                    }
                    caseInsensitive.isEnabled = false
                    return
                }
                if (attributes.selectedIndex > NODE_POSITION) {
                    val attributeName = attributes.selectedItem
                            .toString()
                    val attributesInMap = SortedComboBoxModel()
                    addAttributeValuesRecursively(attributeName, mController.map.rootNode, attributesInMap)
                    nodes.setExtensionList(attributesInMap)
                    values!!.setModel(nodes)
                    if (values.selectedItem != null) {
                        if (nodes.size >= 1) {
                            values.setSelectedIndex(0)
                        } else {
                            values.setSelectedItem(null)
                        }
                    }
                    if (simpleCondition.model !== simpleAttributeConditionComboBoxModel) {
                        simpleCondition
                                .setModel(simpleAttributeConditionComboBoxModel)
                        simpleCondition.selectedIndex = 0
                    }
                    if (simpleCondition.selectedIndex == 0) {
                        caseInsensitive.isEnabled = false
                        values.isEnabled = false
                    }
                    values.isEditable = true
                    simpleCondition.isEnabled = true
                    return
                }
            }
        }
    }

    private val mFilterController: FilterController?
    private val conditionList: JList<Condition>
    private val simpleCondition: JComboBox<Condition?>
    private val values: JComboBox<Any?>? = null
    private val attributes: JComboBox<Any?>
    private val btnAdd: JButton
    private val btnNot: JButton
    private val btnAnd: JButton
    private val btnOr: JButton
    private val btnDelete: JButton
    private val caseInsensitive: JCheckBox
    private val icons: ExtendedComboBoxModel
    private val nodes: ExtendedComboBoxModel
    private val simpleNodeConditionComboBoxModel: DefaultComboBoxModel<Condition?>
    private val simpleIconConditionComboBoxModel: DefaultComboBoxModel<Condition?>
    private val filteredAttributeComboBoxModel: ExtendedComboBoxModel
    private var internalConditionsModel: DefaultComboBoxModel<Condition?>? = null
    private var externalConditionsModel: ComboBoxModel<Condition?>? = null
    private val btnOK: JButton
    private val btnApply: JButton
    private val btnCancel: JButton
    private var btnSave: JButton? = null
    private var btnLoad: JButton? = null
    private val conditionListListener: ConditionListSelectionListener
    private val simpleAttributeConditionComboBoxModel: DefaultComboBoxModel<Condition?>

    init {
        mFilterController = mController.filterController
        val simpleConditionBox = Box.createHorizontalBox()
        simpleConditionBox.border = EmptyBorder(5, 0, 5, 0)
        contentPane.add(simpleConditionBox, BorderLayout.NORTH)
        attributes = JComboBox<Any?>()
        filteredAttributeComboBoxModel = ExtendedComboBoxModel(FilterController.Companion.conditionFactory?.attributeConditionNames as Array<Any?>)
        getAttributesFromMap(mController.map)
        attributes.setModel(filteredAttributeComboBoxModel)
        attributes.addItemListener(SelectedAttributeChangeListener())
        simpleConditionBox.add(Box.createHorizontalGlue())
        simpleConditionBox.add(attributes)
        attributes.setRenderer(mFilterController?.conditionRenderer)
        simpleNodeConditionComboBoxModel = DefaultComboBoxModel<Condition?>(emptyArray() /*FilterController.Companion.conditionFactory?.nodeConditionNames*/)
        simpleIconConditionComboBoxModel = DefaultComboBoxModel<Condition?>(emptyArray() /*FilterController.Companion.conditionFactory?.nodeConditionNames*/)
        simpleCondition = JComboBox()
        simpleCondition.setModel(simpleNodeConditionComboBoxModel)
        simpleCondition.addItemListener(SimpleConditionChangeListener())
        simpleConditionBox.add(Box.createHorizontalGlue())
        simpleConditionBox.add(simpleCondition)
        simpleCondition.setRenderer(mFilterController?.conditionRenderer)
        simpleAttributeConditionComboBoxModel = DefaultComboBoxModel<Condition?>(emptyArray() /*FilterController.Companion.conditionFactory?.attributeConditionNames*/)
        nodes = ExtendedComboBoxModel()
        values!!.setModel(nodes)
        simpleConditionBox.add(Box.createHorizontalGlue())
        simpleConditionBox.add(values)
        values.setRenderer(mFilterController?.conditionRenderer)
        values.isEditable = true
        icons = ExtendedComboBoxModel()
        icons.setExtensionList(mController.map.icons)
        caseInsensitive = JCheckBox()
        simpleConditionBox.add(Box.createHorizontalGlue())
        simpleConditionBox.add(caseInsensitive)
        caseInsensitive.text = Resources.instance?.getResourceString(
                "filter_ignore_case")
        val conditionButtonBox = Box.createVerticalBox()
        conditionButtonBox.border = EmptyBorder(0, 10, 0, 10)
        contentPane.add(conditionButtonBox, BorderLayout.EAST)
        btnAdd = JButton(AddConditionAction())
        btnAdd.maximumSize = maxButtonDimension
        conditionButtonBox.add(Box.createVerticalGlue())
        conditionButtonBox.add(btnAdd)
        btnNot = JButton(CreateNotSatisfiedConditionAction())
        conditionButtonBox.add(Box.createVerticalGlue())
        btnNot.maximumSize = maxButtonDimension
        conditionButtonBox.add(btnNot)
        btnNot.isEnabled = false
        btnAnd = JButton(CreateConjunctConditionAction())
        conditionButtonBox.add(Box.createVerticalGlue())
        btnAnd.maximumSize = maxButtonDimension
        conditionButtonBox.add(btnAnd)
        btnAnd.isEnabled = false
        btnOr = JButton(CreateDisjunctConditionAction())
        conditionButtonBox.add(Box.createVerticalGlue())
        btnOr.maximumSize = maxButtonDimension
        conditionButtonBox.add(btnOr)
        btnOr.isEnabled = false
        btnDelete = JButton(DeleteConditionAction())
        btnDelete.isEnabled = false
        conditionButtonBox.add(Box.createVerticalGlue())
        btnDelete.maximumSize = maxButtonDimension
        conditionButtonBox.add(btnDelete)
        conditionButtonBox.add(Box.createVerticalGlue())
        val controllerBox = Box.createHorizontalBox()
        controllerBox.border = EmptyBorder(5, 0, 5, 0)
        contentPane.add(controllerBox, BorderLayout.SOUTH)
        val closeAction: CloseAction = CloseAction()
        btnOK = JButton()
        Tools.setLabelAndMnemonic(btnOK, Resources.instance?.getResourceString("ok"))
        btnOK.addActionListener(closeAction)
        btnOK.maximumSize = maxButtonDimension
        btnApply = JButton()
        Tools.setLabelAndMnemonic(btnApply, Resources.instance?.getResourceString("apply"))
        btnApply.addActionListener(closeAction)
        btnApply.maximumSize = maxButtonDimension
        btnCancel = JButton()
        Tools.setLabelAndMnemonic(btnCancel, Resources.instance?.getResourceString("cancel"))
        btnCancel.addActionListener(closeAction)
        btnCancel.maximumSize = maxButtonDimension
        controllerBox.add(Box.createHorizontalGlue())
        controllerBox.add(btnOK)
        controllerBox.add(Box.createHorizontalGlue())
        controllerBox.add(btnApply)
        controllerBox.add(Box.createHorizontalGlue())
        controllerBox.add(btnCancel)
        controllerBox.add(Box.createHorizontalGlue())
        if (!mController.frame.isApplet) {
            val saveAction: ActionListener = SaveAction()
            btnSave = JButton()
            Tools.setLabelAndMnemonic(btnSave, Resources.instance?.getResourceString("save"))
            btnSave?.addActionListener(saveAction)
            btnSave?.setMaximumSize(maxButtonDimension)
            val loadAction: ActionListener = LoadAction()
            btnLoad = JButton()
            Tools.setLabelAndMnemonic(btnLoad, Resources.instance?.getResourceString("load"))
            btnLoad?.addActionListener(loadAction)
            btnLoad?.setMaximumSize(maxButtonDimension)
            controllerBox.add(btnSave)
            controllerBox.add(Box.createHorizontalGlue())
            controllerBox.add(btnLoad)
            controllerBox.add(Box.createHorizontalGlue())
        }
        conditionList = JList()
        conditionList.selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        conditionList.setCellRenderer(mFilterController?.conditionRenderer)
        conditionList.layoutOrientation = JList.VERTICAL
        conditionList.alignmentX = LEFT_ALIGNMENT
        conditionListListener = ConditionListSelectionListener()
        conditionList.addListSelectionListener(conditionListListener)
        conditionList.addMouseListener(ConditionListMouseListener())
        val conditionScrollPane = JScrollPane(conditionList)
        val conditionColumnHeader = JLabel(Resources.instance?.getResourceString("filter_conditions"))
        conditionColumnHeader.horizontalAlignment = JLabel.CENTER
        conditionScrollPane.setColumnHeaderView(conditionColumnHeader)
        conditionScrollPane.preferredSize = Dimension(500, 200)
        contentPane.add(conditionScrollPane, BorderLayout.CENTER)
        Tools.addEscapeActionToDialog(this)
        pack()
    }

    private fun getAttributesFromMap(map: MindMap?) {
        if (map != null) {
            // gather attributes in the map:
            val attributesInMap: SortedListModel = SortedComboBoxModel()
            addAttributeKeysRecursively(map.rootNode, attributesInMap)
            filteredAttributeComboBoxModel.setExtensionList(attributesInMap)
        } else {
            filteredAttributeComboBoxModel.setExtensionList(null)
        }
    }

    private fun addAttributeKeysRecursively(pNode: MindMapNode, pAttributesInMap: SortedListModel) {
        for (key in pNode.attributeKeyList) {
            pAttributesInMap.add(key)
        }
        val it: Iterator<*> = pNode.children.iterator()
        while (it.hasNext()) {
            val child = it.next() as MindMapNode
            addAttributeKeysRecursively(child, pAttributesInMap)
        }
    }

    private fun addAttributeValuesRecursively(pKey: String, pNode: MindMapNode, pAttributesInMap: SortedListModel) {
        for (attr in pNode.attributes) {
            if (Tools.safeEquals(attr.name, pKey)) {
                pAttributesInMap.add(attr.value)
            }
        }
        val it: Iterator<*> = pNode.children.iterator()
        while (it.hasNext()) {
            val child = it.next() as MindMapNode
            addAttributeValuesRecursively(pKey, child, pAttributesInMap)
        }
    }

    private val attributeValue: String?
        private get() {
            if (attributes.selectedIndex == ICON_POSITION) {
                val mi = values!!.selectedItem as MindIcon
                return mi.name
            }
            val item = values!!.selectedItem
            return item?.toString() ?: ""
        }

    /**
     */
    fun mapChanged(newMap: MindMap?) {
        if (newMap != null) {
            icons.setExtensionList(newMap.icons)
            if (icons.size >= 1 && values!!.model === icons) {
                values!!.setSelectedIndex(0)
            } else {
                values!!.selectedIndex = -1
                if (values.model === icons) {
                    values.selectedItem = null
                }
            }
            if (attributes.selectedIndex > 1) attributes.selectedIndex = 0
            getAttributesFromMap(newMap)
        } else {
            icons.setExtensionList(null)
            values!!.selectedIndex = -1
            attributes.selectedIndex = 0
            filteredAttributeComboBoxModel.setExtensionList(null)
        }
    }

    private fun selectCondition(): Boolean {
        val min = conditionList.minSelectionIndex
        if (min >= 0) {
            val max = conditionList.minSelectionIndex
            if (min == max) {
                applyChanges()
                return true
            }
        }
        return false
    }

    /**
     */
    fun setSelectedItem(selectedItem: Any?) {
        conditionList.setSelectedValue(selectedItem, true)
    }

    override fun show() {
        initInternalConditionModel()
        super.show()
    }

    private fun initInternalConditionModel() {
        externalConditionsModel = mFilterController?.getFilterConditionModel()
        if (internalConditionsModel == null) {
            internalConditionsModel = DefaultComboBoxModel<Condition?>()
            internalConditionsModel?.addListDataListener(conditionListListener)
            conditionList.setModel(internalConditionsModel)
        } else {
            internalConditionsModel!!.removeAllElements()
        }
        var index = -1
        for (i in 2 until externalConditionsModel!!.size) {
            val element = externalConditionsModel!!.getElementAt(i)
            internalConditionsModel!!.addElement(element)
            if (element === externalConditionsModel!!.selectedItem) {
                index = i - 2
            }
        }
        if (index >= 0) {
            conditionList.setSelectedIndex(index)
        } else {
            conditionList.clearSelection()
        }
    }

    private fun applyChanges() {
        internalConditionsModel!!.selectedItem = conditionList
                .selectedValue
        internalConditionsModel!!.removeListDataListener(conditionListListener)
        mFilterController!!.setFilterConditionModel(internalConditionsModel)
        internalConditionsModel = null
    }

    companion object {
        private val maxButtonDimension = Dimension(1000,
                1000)
        private const val NODE_POSITION = 0
        private const val ICON_POSITION = 1
    }
}