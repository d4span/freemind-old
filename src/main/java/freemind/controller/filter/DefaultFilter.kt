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
 * Created on 07.05.2005
 *
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
import javax.swing.JComboBox
import freemind.controller.color.ColorPair
import javax.swing.ImageIcon
import java.awt.image.BufferedImage
import freemind.controller.color.JColorCombo.ColorIcon
import java.awt.Graphics
import freemind.main.Tools
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.ListCellRenderer
import javax.swing.SwingConstants
import javax.swing.JList
import freemind.controller.color.JColorCombo
import freemind.controller.color.JColorCombo.ComboBoxRenderer
import kotlin.jvm.JvmStatic
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.ListModel
import freemind.controller.filter.util.SortedMapVector.MapElement
import freemind.controller.filter.util.SortedMapVector
import java.util.NoSuchElementException
import javax.swing.AbstractListModel
import freemind.controller.filter.util.SortedListModel
import java.util.SortedSet
import java.util.TreeSet
import java.util.Arrays
import freemind.controller.filter.util.SortedMapListModel
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import freemind.controller.filter.util.ExtendedComboBoxModel.ExtensionDataListener
import freemind.controller.filter.util.ExtendedComboBoxModel
import freemind.modes.MindMapNode
import javax.swing.JComponent
import freemind.main.XMLElement
import javax.swing.JPanel
import javax.swing.BoxLayout
import freemind.modes.MindIcon
import freemind.controller.filter.FilterController
import java.lang.NumberFormatException
import kotlin.Throws
import java.util.Locale
import freemind.modes.MindMap
import freemind.view.mindmapview.MapView
import java.util.LinkedList
import freemind.controller.filter.FilterComposerDialog
import javax.swing.JCheckBox
import javax.swing.JButton
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
import java.io.IOException
import java.io.FileWriter
import java.io.FileReader
import javax.swing.JDialog
import java.lang.NullPointerException
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.MouseAdapter
import java.lang.Runnable
import java.awt.event.ActionListener
import freemind.controller.filter.FilterComposerDialog.MindMapFilterFileFilter
import freemind.modes.FreeMindFileDialog
import javax.swing.JFileChooser
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
import javax.swing.ListSelectionModel
import freemind.controller.filter.FilterComposerDialog.ConditionListMouseListener
import javax.swing.JScrollPane
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
import javax.swing.JMenuBar
import javax.swing.JPopupMenu
import freemind.controller.MenuBar.MapsMenuActionListener
import freemind.modes.ModeController
import javax.swing.JMenu
import javax.swing.ButtonGroup
import freemind.controller.MenuBar.ModesMenuActionListener
import javax.swing.JRadioButtonMenuItem
import javax.swing.KeyStroke
import javax.swing.JMenuItem
import freemind.controller.MenuBar.LastOpenedActionListener
import freemind.main.FreeMind
import javax.swing.SwingUtilities
import java.awt.event.KeyEvent
import java.awt.print.PageFormat
import java.awt.Graphics2D
import java.awt.print.Printable
import javax.swing.JOptionPane
import javax.swing.JToolBar
import freemind.controller.printpreview.BrowseAction
import java.awt.FlowLayout
import javax.swing.Icon
import freemind.main.FreeMindMain
import freemind.controller.MapModuleManager.MapTitleChangeListener
import freemind.controller.MapModuleManager.MapTitleContributor
import freemind.modes.ModesCreator
import java.awt.print.PrinterJob
import freemind.controller.Controller.OptionAntialiasAction
import freemind.controller.Controller.PropertyAction
import freemind.controller.Controller.OpenURLAction
import javax.swing.JTabbedPane
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
import javax.swing.JColorChooser
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.text.MessageFormat
import java.lang.StringBuffer
import freemind.main.FreeMindCommon
import java.lang.SecurityException
import java.awt.print.Paper
import javax.swing.JTextField
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
import java.io.FileNotFoundException
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
import javax.swing.JCheckBoxMenuItem
import freemind.main.HtmlTools
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
import javax.swing.JSeparator
import java.awt.event.MouseWheelListener
import java.awt.event.MouseWheelEvent
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.awt.datatransfer.Clipboard
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.controller.filter.condition.*
import freemind.view.mindmapview.NodeView
import java.util.TreeMap

/**
 * @author dimitri 07.05.2005
 */
class DefaultFilter(condition: Condition?, areAnchestorsShown: Boolean,
                    areDescendantsShown: Boolean) : Filter {
    private override val condition: Condition? = null
    private var options = 0

    /**
     */
    init {
        this.condition = condition
        options = Filter.Companion.FILTER_INITIAL_VALUE or Filter.Companion.FILTER_SHOW_MATCHED
        if (areAnchestorsShown) options += Filter.Companion.FILTER_SHOW_ANCESTOR
        options += Filter.Companion.FILTER_SHOW_ECLIPSED
        if (areDescendantsShown) options += Filter.Companion.FILTER_SHOW_DESCENDANT
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.Filter#applyFilter(freemind.modes.MindMap)
	 */
    override fun applyFilter(c: Controller) {
        if (condition != null) {
            try {
                c.frame.setWaitingCursor(true)
                val map = c.model
                val mapView = c.view
                val root = map!!.rootNode
                resetFilter(root)
                if (filterChildren(root, c, condition.checkNode(c, root), false)) {
                    addFilterResult(root, Filter.Companion.FILTER_SHOW_ANCESTOR)
                }
                selectVisibleNode(mapView)
            } finally {
                c.frame.setWaitingCursor(false)
            }
        }
    }

    /**
     * @param c
     * TODO
     */
    private fun filterChildren(parent: MindMapNode, c: Controller,
                               isAncestorSelected: Boolean, isAncestorEclipsed: Boolean): Boolean {
        val iterator = parent.childrenUnfolded()
        var isDescendantSelected = false
        while (iterator.hasNext()) {
            val node = iterator.next() as MindMapNode
            isDescendantSelected = applyFilter(node, c, isAncestorSelected,
                    isAncestorEclipsed, isDescendantSelected)
        }
        return isDescendantSelected
    }

    private fun applyFilter(node: MindMapNode, c: Controller,
                            isAncestorSelected: Boolean, isAncestorEclipsed: Boolean,
                            isDescendantSelected: Boolean): Boolean {
        var isDescendantSelected = isDescendantSelected
        resetFilter(node)
        if (isAncestorSelected) addFilterResult(node, Filter.Companion.FILTER_SHOW_DESCENDANT)
        val conditionSatisfied = condition!!.checkNode(c, node)
        if (conditionSatisfied) {
            isDescendantSelected = true
            addFilterResult(node, Filter.Companion.FILTER_SHOW_MATCHED)
        } else {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_HIDDEN)
        }
        if (isAncestorEclipsed) {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_ECLIPSED)
        }
        if (filterChildren(node, c, conditionSatisfied || isAncestorSelected,
                        !conditionSatisfied || isAncestorEclipsed)) {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_ANCESTOR)
            isDescendantSelected = true
        }
        return isDescendantSelected
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.Filter#isVisible(freemind.modes.MindMapNode)
	 */
    override fun isVisible(node: MindMapNode): Boolean {
        if (condition == null) return true
        val filterResult = node.filterInfo.get()
        return ((options and Filter.Companion.FILTER_SHOW_ANCESTOR != 0 || options and Filter.Companion.FILTER_SHOW_ECLIPSED >= filterResult and Filter.Companion.FILTER_SHOW_ECLIPSED)
                && options and filterResult and Filter.Companion.FILTER_SHOW_ECLIPSED.inv() != 0)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areMatchedShown()
	 */
    override fun areMatchedShown(): Boolean {
        return true
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areHiddenShown()
	 */
    override fun areHiddenShown(): Boolean {
        return false
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areAncestorsShown()
	 */
    override fun areAncestorsShown(): Boolean {
        return 0 != options and Filter.Companion.FILTER_SHOW_ANCESTOR
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areDescendantsShown()
	 */
    override fun areDescendantsShown(): Boolean {
        return 0 != options and Filter.Companion.FILTER_SHOW_DESCENDANT
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areEclipsedShown()
	 */
    override fun areEclipsedShown(): Boolean {
        return true
    }

    override fun getCondition(): Any? {
        return condition
    }

    companion object {
        fun selectVisibleNode(mapView: MapView?) {
            val selectedNodes = mapView!!.selecteds
            val lastSelectedIndex = selectedNodes.size - 1
            if (lastSelectedIndex == -1) {
                return
            }
            val iterator: ListIterator<NodeView> = selectedNodes.listIterator(lastSelectedIndex)
            while (iterator.hasPrevious()) {
                val previous = iterator.previous()
                if (!previous.model.isVisible) {
                    mapView.toggleSelected(previous)
                }
            }
            var selected = mapView.selected
            if (!selected.model.isVisible) {
                selected = getNearestVisibleParent(selected)
                mapView.selectAsTheOnlyOneSelected(selected)
            }
            mapView.siblingMaxLevel = selected.model.nodeLevel
        }

        private fun getNearestVisibleParent(selectedNode: NodeView): NodeView {
            return if (selectedNode.model.isVisible) selectedNode else getNearestVisibleParent(selectedNode.parentView)
        }

        fun resetFilter(node: MindMapNode) {
            node.filterInfo.reset()
        }

        fun addFilterResult(node: MindMapNode, flag: Int) {
            node.filterInfo.add(flag)
        }
    }
}