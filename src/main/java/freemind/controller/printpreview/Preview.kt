/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package freemind.controller.printpreview

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
import freemind.controller.filter.condition.NodeContainsCondition
import freemind.controller.filter.condition.IgnoreCaseNodeContainsCondition
import freemind.controller.filter.condition.NodeCompareCondition
import freemind.controller.filter.condition.IconContainedCondition
import freemind.controller.filter.condition.IconNotContainedCondition
import freemind.controller.filter.condition.AttributeCompareCondition
import freemind.controller.filter.condition.AttributeExistsCondition
import freemind.controller.filter.condition.AttributeNotExistsCondition
import freemind.controller.filter.condition.ConditionNotSatisfiedDecorator
import freemind.controller.filter.condition.ConjunctConditions
import freemind.controller.filter.condition.DisjunctConditions
import freemind.controller.filter.condition.JCondition
import freemind.modes.MindIcon
import freemind.controller.filter.condition.ConditionRenderer
import freemind.controller.filter.FilterController
import freemind.controller.filter.condition.CompareConditionAdapter
import java.lang.NumberFormatException
import freemind.controller.filter.condition.NoFilteringCondition
import freemind.controller.filter.condition.NodeCondition
import freemind.controller.filter.condition.SelectedViewCondition
import kotlin.Throws
import java.util.Locale
import freemind.modes.MindMap
import freemind.view.mindmapview.MapView
import java.util.LinkedList
import freemind.controller.FreeMindToolBar
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
import freemind.controller.StructuredMenuHolder
import javax.swing.JPopupMenu
import freemind.controller.MenuBar.MapsMenuActionListener
import freemind.modes.ModeController
import javax.swing.JMenu
import freemind.controller.FreeMindPopupMenu
import javax.swing.ButtonGroup
import freemind.controller.MenuBar.ModesMenuActionListener
import javax.swing.JRadioButtonMenuItem
import javax.swing.KeyStroke
import javax.swing.JMenuItem
import freemind.controller.MapModuleManager
import freemind.controller.LastOpenedList
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
import freemind.controller.ZoomListener
import freemind.controller.MapModuleManager.MapTitleContributor
import freemind.controller.MainToolBar
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListener
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
import freemind.controller.LastStateStorageManagement
import java.lang.SecurityException
import java.awt.print.Paper
import javax.swing.JTextField
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.net.MalformedURLException
import freemind.controller.Controller.LocalLinkConverter
import freemind.modes.browsemode.BrowseMode
import freemind.controller.MenuItemSelectedListener
import freemind.common.BooleanProperty
import freemind.preferences.layout.OptionPanel
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import java.util.Properties
import java.util.Collections
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import freemind.common.JOptionalSplitPane
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
import freemind.controller.MindMapNodesSelection
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
import freemind.controller.StructuredMenuItemHolder
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
import freemind.controller.BlindIcon
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
import freemind.controller.MenuItemEnabledListener
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import java.util.TreeMap

internal class Preview(protected var view: MapView?, zoom: Double, private val pageFormat: PageFormat?) : JComponent() {
    private var previewPageImage: BufferedImage? = null
    private var imageGraphics: Graphics2D? = null
    protected fun paintPaper(g: Graphics, format: PageFormat?) {
        g.color = Color.white
        g.fillRect(0, 0, getPageWidth(format), getPageHeight(format))
        g.color = Color.black
        g.drawRect(0, 0, getPageWidth(format) - 1, getPageHeight(format) - 1)
    }

    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        val format = pageFormat
        paintPaper(g, format)
        if (previewPageImage == null) {
            previewPageImage = createImage(
                    getPageWidth(format) - 1, getPageHeight(format) - 1) as BufferedImage
            imageGraphics = previewPageImage!!.createGraphics()
            imageGraphics.scale(zoom, zoom)
            while (Printable.NO_SUCH_PAGE == view!!.print(imageGraphics, format,
                            pageIndex) && pageIndex > 0) {
                pageIndex -= 1
            }
        }
        g2d.drawImage(previewPageImage, 0, 0, this)
    }

    private fun getPageHeight(format: PageFormat?): Int {
        return (format!!.height * zoom).toInt()
    }

    private fun getPageWidth(format: PageFormat?): Int {
        return (format!!.width * zoom).toInt()
    }

    fun moveIndex(indexStep: Int) {
        val newIndex = pageIndex + indexStep
        if (newIndex >= 0) {
            pageIndex = newIndex
            previewPageImage = null
        }
    }

    fun changeZoom(zoom: Double) {
        this.zoom = Math.max(MINIMUM_ZOOM_FACTOR, this.zoom + zoom)
        resize()
    }

    fun resize() {
        val size = Math.max(pageFormat!!.width * zoom,
                pageFormat.height * zoom).toInt()
        preferredSize = Dimension(size, size)
        previewPageImage = null
        revalidate()
    }

    override fun getMinimumSize(): Dimension {
        return preferredSize
    }

    var pageIndex = 0
        protected set
    protected var zoom = 0.0

    init {
        val format = pageFormat
        if (zoom == 0.0) {
            if (format!!.orientation == PageFormat.PORTRAIT) this.zoom = DEFAULT_PREVIEW_SIZE / format.height else this.zoom = DEFAULT_PREVIEW_SIZE / format.width
        } else this.zoom = zoom
        resize()
    }

    companion object {
        private const val DEFAULT_PREVIEW_SIZE = 300
        private const val MINIMUM_ZOOM_FACTOR = 0.1
    }
}