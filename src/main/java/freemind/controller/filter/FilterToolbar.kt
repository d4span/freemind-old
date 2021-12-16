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

import freemind.common.NamedObject.equals
import freemind.common.NamedObject.name
import freemind.common.JOptionalSplitPane.lastDividerPosition
import freemind.common.JOptionalSplitPane.setComponent
import freemind.common.JOptionalSplitPane.removeComponent
import freemind.common.JOptionalSplitPane.amountOfComponents
import freemind.common.JOptionalSplitPane.dividerPosition
import java.awt.Color
import freemind.controller.color.ColorPair
import java.awt.image.BufferedImage
import freemind.controller.color.JColorCombo.ColorIcon
import java.awt.Graphics
import java.awt.Dimension
import freemind.controller.color.JColorCombo
import freemind.controller.color.JColorCombo.ComboBoxRenderer
import kotlin.jvm.JvmStatic
import freemind.controller.filter.util.SortedMapVector.MapElement
import freemind.controller.filter.util.SortedMapVector
import java.util.NoSuchElementException
import freemind.controller.filter.util.SortedListModel
import java.util.SortedSet
import java.util.TreeSet
import java.util.Arrays
import freemind.controller.filter.util.SortedMapListModel
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import freemind.controller.filter.util.ExtendedComboBoxModel.ExtensionDataListener
import freemind.controller.filter.util.ExtendedComboBoxModel
import freemind.modes.MindMapNode
import freemind.modes.MindIcon
import freemind.controller.filter.FilterController
import java.lang.NumberFormatException
import kotlin.Throws
import java.util.Locale
import freemind.modes.MindMap
import freemind.view.mindmapview.MapView
import java.util.LinkedList
import freemind.controller.filter.FilterComposerDialog
import freemind.controller.filter.FilterToolbar.FilterChangeListener
import java.awt.event.ItemListener
import java.beans.PropertyChangeListener
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import java.beans.PropertyChangeEvent
import freemind.controller.filter.FilterToolbar.EditFilterAction
import freemind.controller.filter.FilterToolbar.UnfoldAncestorsAction
import freemind.controller.MapModuleManager.MapModuleChangeObserver
import freemind.controller.filter.FilterToolbar
import freemind.modes.common.plugins.NodeNoteBase
import freemind.view.MapModule
import java.lang.NullPointerException
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.MouseAdapter
import java.lang.Runnable
import java.awt.event.ActionListener
import freemind.controller.filter.FilterComposerDialog.MindMapFilterFileFilter
import freemind.modes.FreeMindFileDialog
import freemind.controller.filter.util.SortedComboBoxModel
import freemind.controller.filter.FilterComposerDialog.ConditionListSelectionListener
import java.awt.BorderLayout
import freemind.controller.filter.FilterComposerDialog.SelectedAttributeChangeListener
import freemind.controller.filter.FilterComposerDialog.SimpleConditionChangeListener
import freemind.controller.filter.FilterComposerDialog.AddConditionAction
import freemind.controller.filter.FilterComposerDialog.CreateNotSatisfiedConditionAction
import freemind.controller.filter.FilterComposerDialog.CreateConjunctConditionAction
import freemind.controller.filter.FilterComposerDialog.CreateDisjunctConditionAction
import freemind.controller.filter.FilterComposerDialog.DeleteConditionAction
import freemind.controller.filter.FilterComposerDialog.LoadAction
import freemind.controller.filter.FilterComposerDialog.ConditionListMouseListener
import freemind.controller.actions.generated.instance.XmlAction
import freemind.controller.actions.generated.instance.ResultBase
import freemind.controller.actions.generated.instance.PatternNodeBackgroundColor
import freemind.controller.actions.generated.instance.PatternNodeColor
import freemind.controller.actions.generated.instance.PatternNodeStyle
import freemind.controller.actions.generated.instance.PatternNodeText
import freemind.controller.actions.generated.instance.PatternNodeFontName
import freemind.controller.actions.generated.instance.PatternNodeFontBold
import freemind.controller.actions.generated.instance.PatternNodeFontStrikethrough
import freemind.controller.actions.generated.instance.PatternNodeFontItalic
import freemind.controller.actions.generated.instance.PatternNodeFontSize
import freemind.controller.actions.generated.instance.PatternIcon
import freemind.controller.actions.generated.instance.PatternEdgeColor
import freemind.controller.actions.generated.instance.PatternEdgeStyle
import freemind.controller.actions.generated.instance.PatternEdgeWidth
import freemind.controller.actions.generated.instance.PatternChild
import freemind.controller.actions.generated.instance.PatternScript
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.NodeAction
import freemind.controller.actions.generated.instance.MenuActionBase
import freemind.controller.actions.generated.instance.MenuCategoryBase
import freemind.controller.actions.generated.instance.PatternPropertyBase
import freemind.controller.actions.generated.instance.PluginString
import freemind.controller.actions.generated.instance.Place
import freemind.controller.actions.generated.instance.FormatNodeAction
import freemind.controller.actions.generated.instance.TextNodeAction
import freemind.controller.actions.generated.instance.NodeChildParameter
import freemind.controller.actions.generated.instance.TransferableContent
import freemind.controller.actions.generated.instance.CalendarMarking
import freemind.controller.actions.generated.instance.CollaborationActionBase
import freemind.controller.actions.generated.instance.PluginMode
import freemind.controller.actions.generated.instance.CollaborationMapOffer
import freemind.controller.actions.generated.instance.TransferableFile
import freemind.controller.actions.generated.instance.TableColumnSetting
import freemind.controller.actions.generated.instance.TableColumnOrder
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.controller.actions.generated.instance.MapLocationStorage
import freemind.controller.actions.generated.instance.TimeWindowColumnSetting
import freemind.controller.MenuBar.MapsMenuActionListener
import freemind.modes.ModeController
import freemind.controller.MenuBar.ModesMenuActionListener
import freemind.controller.MenuBar.LastOpenedActionListener
import java.awt.event.KeyEvent
import java.awt.print.PageFormat
import java.awt.Graphics2D
import java.awt.print.Printable
import freemind.controller.printpreview.BrowseAction
import java.awt.FlowLayout
import freemind.controller.MapModuleManager.MapTitleChangeListener
import freemind.controller.MapModuleManager.MapTitleContributor
import freemind.modes.ModesCreator
import java.awt.print.PrinterJob
import freemind.controller.Controller.OptionAntialiasAction
import freemind.controller.Controller.PropertyAction
import freemind.controller.Controller.OpenURLAction
import freemind.controller.Controller.PrintPreviewAction
import freemind.controller.Controller.QuitAction
import freemind.controller.Controller.KeyDocumentationAction
import freemind.controller.Controller.DocumentationAction
import freemind.controller.Controller.LicenseAction
import freemind.controller.Controller.NavigationPreviousMapAction
import freemind.controller.Controller.NavigationNextMapAction
import freemind.controller.Controller.NavigationMoveMapLeftAction
import freemind.controller.Controller.NavigationMoveMapRightAction
import freemind.controller.Controller.ShowFilterToolbarAction
import freemind.controller.Controller.ToggleMenubarAction
import freemind.controller.Controller.ToggleToolbarAction
import freemind.controller.Controller.ToggleLeftToolbarAction
import freemind.controller.Controller.OptionHTMLExportFoldingAction
import freemind.controller.Controller.OptionSelectionMechanismAction
import freemind.controller.Controller.ZoomInAction
import freemind.controller.Controller.ZoomOutAction
import freemind.controller.Controller.ShowSelectionAsRectangleAction
import freemind.controller.Controller.MoveToRootAction
import java.awt.KeyboardFocusManager
import freemind.controller.Controller.DefaultLocalLinkConverter
import freemind.preferences.FreemindPropertyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.text.MessageFormat
import java.lang.StringBuffer
import java.lang.SecurityException
import java.awt.print.Paper
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.net.MalformedURLException
import freemind.controller.Controller.LocalLinkConverter
import freemind.modes.browsemode.BrowseMode
import freemind.common.BooleanProperty
import freemind.preferences.layout.OptionPanel
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import java.util.Properties
import java.util.Collections
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import freemind.common.JOptionalSplitPane
import freemind.controller.*
import freemind.controller.Controller.SplitComponentType
import java.awt.HeadlessException
import java.util.StringTokenizer
import java.net.URISyntaxException
import kotlin.jvm.JvmOverloads
import java.awt.event.KeyListener
import freemind.controller.MapModuleManager.MapModuleChangeObserverCompound
import java.awt.dnd.DragGestureListener
import java.awt.Cursor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DragSource
import java.awt.dnd.DragGestureEvent
import freemind.view.mindmapview.MainView
import java.awt.event.InputEvent
import java.awt.datatransfer.Transferable
import java.awt.dnd.DragSourceListener
import java.awt.dnd.DragSourceDropEvent
import java.awt.dnd.DragSourceDragEvent
import java.awt.dnd.DragSourceEvent
import java.awt.dnd.DropTargetListener
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetEvent
import java.awt.dnd.DropTargetDropEvent
import freemind.controller.StructuredMenuHolder.MenuEventSupplier
import javax.swing.event.MenuListener
import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import freemind.controller.NodeMotionListener.NodeMotionAdapter
import freemind.controller.StructuredMenuHolder.MapTokenPair
import freemind.controller.StructuredMenuHolder.SeparatorHolder
import freemind.controller.StructuredMenuHolder.MenuAdder
import java.lang.NoSuchMethodError
import freemind.controller.StructuredMenuHolder.DefaultMenuAdderCreator
import freemind.controller.StructuredMenuHolder.StructuredMenuListener
import freemind.controller.StructuredMenuHolder.MenuAdderCreator
import freemind.controller.StructuredMenuHolder.MenuItemAdder
import freemind.controller.StructuredMenuHolder.PrintMenuAdder
import freemind.controller.StructuredMenuHolder.PrintMenuAdderCreator
import javax.swing.event.MenuEvent
import java.awt.event.MouseWheelListener
import java.awt.event.MouseWheelEvent
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Clipboard
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.controller.filter.condition.*
import freemind.main.*
import freemind.view.ImageFactory
import java.io.*
import java.lang.Exception
import java.util.TreeMap
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
            val map = mFilterController.getMap()
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

    private inner class EditFilterAction internal constructor() : AbstractAction("", ImageFactory.getInstance().createIcon(Resources.getInstance().getResource(
            "images/Btn_edit.gif"))) {
        init {
            putValue(SHORT_DESCRIPTION, Resources.getInstance()
                    .getResourceString("filter_edit_description"))
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
    internal constructor() : AbstractAction("", ImageFactory.getInstance().createIcon(Resources.getInstance().getResource(
            "images/unfold.png"))) {
        private fun unfoldAncestors(parent: MindMapNode) {
            val i: Iterator<MindMapNode> = parent.childrenUnfolded()
            while (i.hasNext()) {
                val node = i.next()
                if (showDescendants.isSelected
                        || node.filterInfo.isAncestor) {
                    setFolded(node, false)
                    unfoldAncestors(node)
                }
            }
        }

        private fun setFolded(node: MindMapNode, state: Boolean) {
            if (node.hasChildren() && node.isFolded != state) {
                c.modeController.setFolded(node, state)
            }
        }

        override fun actionPerformed(e: ActionEvent) {
            if (selectedCondition != null) {
                unfoldAncestors(c.model.rootNode)
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
        add(JLabel(Resources.getInstance().getResourceString(
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
        btnUnfoldAncestors.setToolTipText(Resources.getInstance()
                .getResourceString("filter_unfold_ancestors"))
        add(btnUnfoldAncestors)
        showAncestors = JCheckBox(Resources.getInstance()
                .getResourceString("filter_show_ancestors"), true)
        add(showAncestors)
        showAncestors.model.addActionListener(filterChangeListener)
        showDescendants = JCheckBox(Resources.getInstance()
                .getResourceString("filter_show_descendants"), false)
        add(showDescendants)
        showDescendants.model.addActionListener(filterChangeListener)
    }

    fun addStandardConditions() {
        val filterConditionModel = mFilterController!!.filterConditionModel
        val noFiltering: Condition = NoFilteringCondition.Companion.createCondition()
        filterConditionModel!!.insertElementAt(noFiltering, 0)
        filterConditionModel.insertElementAt(
                SelectedViewCondition.Companion.CreateCondition(), 1)
        if (filterConditionModel.selectedItem == null) {
            filterConditionModel.selectedItem = noFiltering
        }
    }

    fun initConditions() {
        try {
            mFilterController!!.loadConditions(mFilterController.filterConditionModel, pathToFilterFile)
        } catch (e: Exception) {
        }
        addStandardConditions()
        activeFilterConditionComboBox.selectedIndex = 0
        activeFilterConditionComboBox.setRenderer(mFilterController.getConditionRenderer())
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
        val map = mFilterController.getMap()
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
            mFilterController!!.saveConditions(mFilterController.filterConditionModel, pathToFilterFile)
        } catch (e: Exception) {
        }
    }

    var filterConditionModel: ComboBoxModel<Condition?>?
        get() = activeFilterConditionComboBox.model
        set(filterConditionModel) {
            activeFilterConditionComboBox.model = filterConditionModel
        }
}