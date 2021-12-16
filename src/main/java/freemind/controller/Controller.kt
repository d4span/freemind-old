/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: Controller.java,v 1.40.14.21.2.64 2010/02/22 21:18:53 christianfoltin Exp $*/
package freemind.controller

import freemind.common.NamedObject.equals
import freemind.common.NamedObject.name
import freemind.common.JOptionalSplitPane.lastDividerPosition
import freemind.common.JOptionalSplitPane.setComponent
import freemind.common.JOptionalSplitPane.removeComponent
import freemind.common.JOptionalSplitPane.amountOfComponents
import freemind.common.JOptionalSplitPane.dividerPosition
import freemind.controller.color.ColorPair
import java.awt.image.BufferedImage
import freemind.controller.color.JColorCombo.ColorIcon
import freemind.controller.color.JColorCombo
import freemind.controller.color.JColorCombo.ComboBoxRenderer
import kotlin.jvm.JvmStatic
import freemind.controller.filter.util.SortedMapVector.MapElement
import freemind.controller.filter.util.SortedMapVector
import freemind.controller.filter.util.SortedListModel
import freemind.controller.filter.util.SortedMapListModel
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import freemind.controller.filter.util.ExtendedComboBoxModel.ExtensionDataListener
import freemind.controller.filter.util.ExtendedComboBoxModel
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
import freemind.controller.filter.condition.ConditionRenderer
import freemind.controller.filter.FilterController
import freemind.controller.filter.condition.CompareConditionAdapter
import java.lang.NumberFormatException
import freemind.controller.filter.condition.NoFilteringCondition
import freemind.controller.filter.condition.NodeCondition
import freemind.controller.filter.condition.SelectedViewCondition
import kotlin.Throws
import freemind.view.mindmapview.MapView
import freemind.controller.FreeMindToolBar
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
import freemind.controller.filter.util.SortedComboBoxModel
import freemind.controller.filter.FilterComposerDialog.ConditionListSelectionListener
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
import freemind.controller.StructuredMenuHolder
import freemind.controller.MenuBar.MapsMenuActionListener
import freemind.controller.FreeMindPopupMenu
import freemind.controller.MenuBar.ModesMenuActionListener
import freemind.controller.MapModuleManager
import freemind.controller.LastOpenedList
import freemind.controller.MenuBar.LastOpenedActionListener
import java.awt.event.KeyEvent
import java.awt.print.PageFormat
import java.awt.print.Printable
import freemind.controller.printpreview.BrowseAction
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
import freemind.controller.Controller.DefaultLocalLinkConverter
import freemind.preferences.FreemindPropertyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.text.MessageFormat
import java.lang.StringBuffer
import freemind.controller.LastStateStorageManagement
import java.lang.SecurityException
import java.awt.print.Paper
import java.net.MalformedURLException
import freemind.controller.Controller.LocalLinkConverter
import freemind.modes.browsemode.BrowseMode
import freemind.controller.MenuItemSelectedListener
import freemind.common.BooleanProperty
import freemind.preferences.layout.OptionPanel
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import freemind.common.JOptionalSplitPane
import freemind.controller.Controller.SplitComponentType
import java.net.URISyntaxException
import kotlin.jvm.JvmOverloads
import java.awt.event.KeyListener
import freemind.controller.MapModuleManager.MapModuleChangeObserverCompound
import java.awt.dnd.DragGestureListener
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
import java.awt.event.MouseWheelListener
import java.awt.event.MouseWheelEvent
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Clipboard
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.controller.MenuItemEnabledListener
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.controller.printpreview.PreviewDialog
import freemind.main.*
import freemind.modes.*
import freemind.view.ImageFactory
import java.awt.*
import java.awt.font.TextAttribute
import java.io.*
import java.lang.Exception
import java.net.URL
import java.util.*
import java.util.logging.Logger
import javax.swing.*

/**
 * Provides the methods to edit/change a Node. Forwards all messages to
 * MapModel(editing) or MapView(navigation).
 */
class Controller(var frame: FreeMindMain) : MapModuleChangeObserver {
    private val mMapTitleChangeListenerSet = HashSet<MapTitleChangeListener>()
    private val mZoomListenerSet = HashSet<ZoomListener>()
    private val mMapTitleContributorSet = HashSet<MapTitleContributor>()
    var lastOpenedList // A list of the pathnames of all the maps
            : LastOpenedList? = null
        private set

    // that were opened in the last time
    var mapModuleManager // new MapModuleManager();
            : MapModuleManager? = null
        private set

    /** The current mode  */
    var mode: Mode? = null
        private set
    private var toolbar: MainToolBar? = null
    private var filterToolbar: JToolBar? = null
    private var northToolbarPanel: JPanel? = null
    var nodeMouseMotionListener: NodeMouseMotionListener? = null
        private set
    var nodeMotionListener: NodeMotionListener? = null
        private set
    var nodeKeyListener: NodeKeyListener? = null
        private set
    var nodeDragListener: NodeDragListener? = null
        private set
    var nodeDropListener: NodeDropListener? = null
        private set
    var mapMouseMotionListener: MapMouseMotionListener? = null
        private set
    var mapMouseWheelListener: MapMouseWheelListener? = null
        private set
    private val mModescreator = ModesCreator(this)
    var pageFormat: PageFormat? = null
        private set
    private var printerJob: PrinterJob? = null
    private val fontMap: MutableMap<String, Font> = HashMap()
    var filterController: FilterController? = null
        private set
    var isPrintingAllowed = true
    var menubarVisible = true
    var toolbarVisible = true
    var leftToolbarVisible = true
    var close: CloseAction? = null
    var print: Action? = null
    var printDirect: Action? = null
    var printPreview: Action? = null
    var page: Action? = null
    var quit: Action? = null
    var optionAntialiasAction: OptionAntialiasAction? = null
    var optionHTMLExportFoldingAction: Action? = null
    var optionSelectionMechanismAction: Action? = null
    var about: Action? = null
    var faq: Action? = null
    var keyDocumentation: Action? = null
    var webDocu: Action? = null
    var documentation: Action? = null
    var license: Action? = null
    var showFilterToolbarAction: Action? = null
    var navigationPreviousMap: Action? = null
    var navigationNextMap: Action? = null
    var navigationMoveMapLeftAction: Action? = null
    var navigationMoveMapRightAction: Action? = null
    var moveToRoot: Action? = null
    var toggleMenubar: Action? = null
    var toggleToolbar: Action? = null
    var toggleLeftToolbar: Action? = null
    var zoomIn: Action? = null
    var zoomOut: Action? = null
    var showSelectionAsRectangle: Action? = null
    var propertyAction: PropertyAction? = null
    var freemindUrl: OpenURLAction? = null
    private var mTabbedPaneMapModules: Vector<MapModule>? = null
    private var mTabbedPane: JTabbedPane? = null
    private var mTabbedPaneSelectionUpdate = true
    fun init() {
        initialization()
        nodeMouseMotionListener = NodeMouseMotionListener(this)
        nodeMotionListener = NodeMotionListener(this)
        nodeKeyListener = NodeKeyListener(this)
        nodeDragListener = NodeDragListener(this)
        nodeDropListener = NodeDropListener(this)
        mapMouseMotionListener = MapMouseMotionListener(this)
        mapMouseWheelListener = MapMouseWheelListener(this)
        close = CloseAction(this)
        print = PrintAction(this, true)
        printDirect = PrintAction(this, false)
        printPreview = PrintPreviewAction(this)
        page = PageAction(this)
        quit = QuitAction(this)
        about = AboutAction(this)
        freemindUrl = OpenURLAction(this, getResourceString("FreeMind"),
                getProperty("webFreeMindLocation"))
        faq = OpenURLAction(this, getResourceString("FAQ"),
                getProperty("webFAQLocation"))
        keyDocumentation = KeyDocumentationAction(this)
        webDocu = OpenURLAction(this, getResourceString("webDocu"),
                getProperty("webDocuLocation"))
        documentation = DocumentationAction(this)
        license = LicenseAction(this)
        navigationPreviousMap = NavigationPreviousMapAction(this)
        navigationNextMap = NavigationNextMapAction(this)
        navigationMoveMapLeftAction = NavigationMoveMapLeftAction(this)
        navigationMoveMapRightAction = NavigationMoveMapRightAction(this)
        showFilterToolbarAction = ShowFilterToolbarAction(this)
        toggleMenubar = ToggleMenubarAction(this)
        toggleToolbar = ToggleToolbarAction(this)
        toggleLeftToolbar = ToggleLeftToolbarAction(this)
        optionAntialiasAction = OptionAntialiasAction()
        optionHTMLExportFoldingAction = OptionHTMLExportFoldingAction(this)
        optionSelectionMechanismAction = OptionSelectionMechanismAction(
                this)
        zoomIn = ZoomInAction(this)
        zoomOut = ZoomOutAction(this)
        propertyAction = PropertyAction(this)
        showSelectionAsRectangle = ShowSelectionAsRectangleAction(this)
        moveToRoot = MoveToRootAction(this)

        // Create the ToolBar
        northToolbarPanel = JPanel(BorderLayout())
        toolbar = MainToolBar(this)
        filterController = FilterController(this)
        filterToolbar = filterController!!.filterToolbar
        frame.contentPane.add(northToolbarPanel, BorderLayout.NORTH)
        northToolbarPanel!!.add(toolbar, BorderLayout.NORTH)
        northToolbarPanel!!.add(filterToolbar, BorderLayout.SOUTH)
        setAllActions(false)
    }

    /**
     * Does basic initializations of this class. Normally, init is called, but
     * if you don't need the actions, call this method instead.
     */
    fun initialization() {
        /**
         * Arranges the keyboard focus especially after opening FreeMind.
         */
        val focusManager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager()
        focusManager.addPropertyChangeListener { e ->
            val prop = e.propertyName
            if ("focusOwner" == prop) {
                val comp = e.newValue as Component
                logger!!.fine("Focus change for $comp")
                if (comp is FreeMindMain) {
                    obtainFocusForSelected()
                }
            }
        }
        localDocumentationLinkConverter = DefaultLocalLinkConverter()
        lastOpenedList = LastOpenedList(this, getProperty("lastOpened"))
        mapModuleManager = MapModuleManager(this)
        mapModuleManager!!.addListener(this)
        if (!Tools.isAvailableFontFamily(getProperty("defaultfont"))) {
            logger!!.warning("Warning: the font you have set as standard - "
                    + getProperty("defaultfont") + " - is not available.")
            frame.setProperty("defaultfont", "SansSerif")
        }
    }

    fun getProperty(property: String?): String {
        return frame.getProperty(property)
    }

    fun getIntProperty(property: String?, defaultValue: Int): Int {
        return frame.getIntProperty(property, defaultValue)
    }

    fun setProperty(property: String?, value: String?) {
        val oldValue = getProperty(property)
        frame.setProperty(property, value)
        firePropertyChanged(property, value, oldValue)
    }

    private fun firePropertyChanged(property: String?, value: String?,
                                    oldValue: String?) {
        if (oldValue == null || oldValue != value) {
            for (listener in getPropertyChangeListeners()) {
                listener.propertyChanged(property, value, oldValue)
            }
        }
    }

    val jFrame: JFrame?
        get() {
            val f = frame
            return if (f is JFrame) f else null
        }

    fun getResource(resource: String?): URL {
        return frame.getResource(resource)
    }

    fun getResourceString(resource: String?): String {
        return frame.getResourceString(resource)
    }// no map present: we take the default:

    /**
     * @return the current modeController, or null, if FreeMind is just starting
     * and there is no modeController present.
     */
    val modeController: ModeController?
        get() {
            if (mapModule != null) {
                return mapModule!!.modeController
            }
            return if (mode != null) {
                // no map present: we take the default:
                mode!!.defaultModeController
            } else null
        }

    /** Returns the current model  */
    val model: MindMap?
        get() = if (mapModule != null) {
            mapModule!!.model
        } else null

    // System.err.println("[Freemind-Developer-Internal-Warning (do not write a bug report, please)]: Tried to get view without being able to get map module.");
    val view: MapView?
        get() = if (mapModule != null) {
            mapModule!!.view
        } else {
            // System.err.println("[Freemind-Developer-Internal-Warning (do not write a bug report, please)]: Tried to get view without being able to get map module.");
            null
        }
    val modes: Set<String>
        get() = mModescreator.allModes
    val zooms: Array<String?>
        get() {
            val zooms = arrayOfNulls<String>(zoomValues.size)
            for (i in zoomValues.indices) {
                val `val` = zoomValues[i]
                zooms[i] = (`val` * 100f).toInt().toString() + "%"
            }
            return zooms
        }

    //
    val mapModule: MapModule?
        get() = mapModuleManager.getMapModule()
    private val toolBar: JToolBar?
        private get() = toolbar

    //
    fun getFontThroughMap(font: Font): Font? {
        if (!fontMap.containsKey(getFontStringCode(font))) {
            fontMap[getFontStringCode(font)] = font
        }
        return fontMap[getFontStringCode(font)]
    }

    private fun getFontStringCode(font: Font): String {
        return font.toString() + "/" + font.attributes[TextAttribute.STRIKETHROUGH]
    }// Maybe implement handling for cases when the font is not

    // available on this system.
    //
    val defaultFont: Font?
        get() {
            // Maybe implement handling for cases when the font is not
            // available on this system.
            val fontSize = defaultFontSize
            val fontStyle = defaultFontStyle
            val fontFamily = defaultFontFamilyName
            return getFontThroughMap(Font(fontFamily, fontStyle, fontSize))
        }

    /**
     */
    val defaultFontFamilyName: String
        get() = getProperty("defaultfont")

    /**
     */
    val defaultFontStyle: Int
        get() = frame.getIntProperty("defaultfontstyle", 0)

    /**
     */
    val defaultFontSize: Int
        get() = frame.getIntProperty("defaultfontsize", 12)

    private class ColorTracker(var chooser: JColorChooser) : ActionListener, Serializable {
        var color: Color? = null
            get() = color

        override fun actionPerformed(e: ActionEvent) {
            color = chooser.color
        }
    }

    internal class Closer : WindowAdapter(), Serializable {
        override fun windowClosing(e: WindowEvent) {
            val w = e.window
            w.isVisible = false
        }
    }

    internal class DisposeOnClose : ComponentAdapter(), Serializable {
        override fun componentHidden(e: ComponentEvent) {
            val w = e.component as Window
            w.dispose()
        }
    }

    override fun isMapModuleChangeAllowed(oldMapModule: MapModule?,
                                          oldMode: Mode?, newMapModule: MapModule?, newMode: Mode?): Boolean {
        return true
    }

    override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {}
    override fun beforeMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                       newMapModule: MapModule?, newMode: Mode?) {
        val oldModeController: ModeController
        mode = newMode
        if (oldMapModule != null) {
            // shut down screens of old view + frame
            oldModeController = oldMapModule.modeController
            oldModeController.setVisible(false)
            oldModeController.shutdownController()
        } else {
            oldModeController = if (oldMode != null) {
                oldMode.defaultModeController
            } else {
                return
            }
        }
        if (oldModeController.modeToolBar != null) {
            toolbar!!.remove(oldModeController.modeToolBar)
            toolbar!!.activate(true)
            // northToolbarPanel.remove(oldModeController.getModeToolBar());
            // northToolbarPanel.add(toolbar, BorderLayout.NORTH);
        }
        /* other toolbars are to be removed too. */if (oldModeController.leftToolBar != null) {
            frame.contentPane.remove(
                    oldModeController.leftToolBar)
        }
    }

    override fun afterMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                      newMapModule: MapModule?, newMode: Mode) {
        val newModeController: ModeController
        if (newMapModule != null) {
            frame.view = newMapModule.view
            setAllActions(true)
            if (view!!.selected == null) {
                // moveToRoot();
                view!!.selectAsTheOnlyOneSelected(view!!.root)
            }
            lastOpenedList!!.mapOpened(newMapModule)
            changeZoomValueProperty(newMapModule.view.zoom)
            // ((MainToolBar) getToolbar()).setZoomComboBox(zoomValue);
            // old
            // obtainFocusForSelected();
            newModeController = newMapModule.modeController
            newModeController.startupController()
            newModeController.setVisible(true)
            // old
            // obtainFocusForSelected();
        } else {
            newModeController = newMode.defaultModeController
            frame.view = null
            setAllActions(false)
        }
        setTitle()
        val newToolBar = newModeController.modeToolBar
        if (newToolBar != null) {
            toolbar!!.activate(false)
            toolbar!!.add(newToolBar, 0)
            // northToolbarPanel.remove(toolbar);
            // northToolbarPanel.add(newToolBar, BorderLayout.NORTH);
            newToolBar.repaint()
        }
        /* new left toolbar. */
        val newLeftToolBar = newModeController.leftToolBar
        if (newLeftToolBar != null) {
            frame.contentPane.add(newLeftToolBar, BorderLayout.WEST)
            if (leftToolbarVisible) {
                newLeftToolBar.isVisible = true
                newLeftToolBar.repaint()
            } else {
                newLeftToolBar.isVisible = false
            }
        }
        toolbar!!.validate()
        toolbar!!.repaint()
        val menuBar = frame.freeMindMenuBar
        menuBar.updateMenus(newModeController)
        menuBar.revalidate()
        menuBar.repaint()
        // new
        obtainFocusForSelected()
    }

    protected fun changeZoomValueProperty(zoomValue: Float) {
        for (listener in mZoomListenerSet) {
            listener.setZoom(zoomValue)
        }
    }

    override fun numberOfOpenMapInformation(number: Int, pIndex: Int) {
        navigationPreviousMap!!.isEnabled = number > 0
        navigationNextMap!!.isEnabled = number > 0
        logger!!.info("number $number, pIndex $pIndex")
        navigationMoveMapLeftAction!!.isEnabled = number > 1 && pIndex > 0
        navigationMoveMapRightAction!!.isEnabled = (number > 1
                && pIndex < number - 1)
    }

    /**
     * Creates a new mode (controller), activates the toolbars, title and
     * deactivates all actions. Does nothing, if the mode is identical to the
     * current mode.
     *
     * @return false if the change was not successful.
     */
    fun createNewMode(mode: String): Boolean {
        if (mode != null && mode == mode) {
            return true
        }

        // Check if the mode is available and create ModeController.
        val newMode = mModescreator.getMode(mode)
        if (newMode == null) {
            errorMessage(getResourceString("mode_na") + ": " + mode)
            return false
        }

        // change the map module to get changed toolbars etc.:
        mapModuleManager!!.setMapModule(null, newMode)
        setTitle()
        mode.activate()
        val messageArguments = arrayOf<Any>(mode.toLocalizedString())
        val formatter = MessageFormat(
                getResourceString("mode_status"))
        frame.out(formatter.format(messageArguments))
        return true
    }

    fun setMenubarVisible(visible: Boolean) {
        menubarVisible = visible
        frame.freeMindMenuBar.isVisible = menubarVisible
    }

    fun setToolbarVisible(visible: Boolean) {
        toolbarVisible = visible
        toolbar!!.isVisible = toolbarVisible
    }

    /**
     * @return Returns the main toolbar.
     */
    fun getToolbar(): JToolBar? {
        return toolbar
    }

    fun setLeftToolbarVisible(visible: Boolean) {
        leftToolbarVisible = visible
        if (mode == null) {
            return
        }
        val leftToolBar = modeController!!.leftToolBar
        if (leftToolBar != null) {
            leftToolBar.isVisible = leftToolbarVisible
            (leftToolBar.parent as JComponent).revalidate()
        }
    }

    fun moveToRoot() {
        if (mapModule != null) {
            view!!.moveToRoot()
        }
    }

    /**
     * Closes the actual map.
     *
     * @param force
     * true= without save.
     */
    fun close(force: Boolean) {
        mapModuleManager!!.close(force, null)
    }

    // (PN) %%%
    // public void select( NodeView node) {
    // getView().select(node,false);
    // getView().setSiblingMaxLevel(node.getModel().getNodeLevel()); // this
    // level is default
    // }
    //
    // void selectBranch( NodeView node, boolean extend ) {
    // getView().selectBranch(node,extend);
    // }
    //
    // boolean isSelected( NodeView node ) {
    // return getView().isSelected(node);
    // }
    //
    // void centerNode() {
    // getView().centerNode(getView().getSelected());
    // }
    //
    // private MindMapNode getSelected() {
    // return getView().getSelected().getModel();
    // }
    fun informationMessage(message: Any) {
        JOptionPane
                .showMessageDialog(frame.contentPane,
                        message.toString(), "FreeMind",
                        JOptionPane.INFORMATION_MESSAGE)
    }

    fun informationMessage(message: Any, component: JComponent?) {
        JOptionPane.showMessageDialog(component, message.toString(),
                "FreeMind", JOptionPane.INFORMATION_MESSAGE)
    }

    fun errorMessage(message: Any?) {
        var myMessage = ""
        if (message != null) {
            myMessage = message.toString()
        } else {
            myMessage = getResourceString("undefined_error")
            if (myMessage == null) {
                myMessage = "Undefined error"
            }
        }
        JOptionPane.showMessageDialog(frame.contentPane, myMessage,
                "FreeMind", JOptionPane.ERROR_MESSAGE)
    }

    fun errorMessage(message: Any, component: JComponent?) {
        JOptionPane.showMessageDialog(component, message.toString(),
                "FreeMind", JOptionPane.ERROR_MESSAGE)
    }

    fun obtainFocusForSelected() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .clearGlobalFocusOwner()
        // logger.finest("obtainFocusForSelected");
        if (view != null) { // is null if the last map was closed.
            logger!!.fine("Requesting Focus for " + view + " in model "
                    + view!!.model)
            view!!.requestFocusInWindow()
        } else {
            // fc, 6.1.2004: bug fix, that open and quit are not working if no
            // map is present.
            // to avoid this, the menu bar gets the focus, and everything seems
            // to be all right!!
            // but I cannot avoid thinking of this change to be a bad hack ....
            logger!!.info("No view present. No focus!")
            frame.freeMindMenuBar.requestFocus()
        }
    }

    //
    // Map Navigation
    //
    //
    // other
    //
    fun setZoom(zoom: Float) {
        view!!.zoom = zoom
        changeZoomValueProperty(zoom)
        // ((MainToolBar) toolbar).setZoomComboBox(zoom);
        // show text in status bar:
        val messageArguments = arrayOf<Any>((zoom * 100f).toString())
        val stringResult = Resources.getInstance().format(
                "user_defined_zoom_status_bar", messageArguments)
        frame.out(stringResult)
    }
    // ////////////
    // Private methods. Internal implementation
    // //////////
    //
    // Node editing
    //
    // (PN)
    // private void getFocus() {
    // getView().getSelected().requestFocus();
    // }
    //
    // Multiple Views management
    //
    /**
     * Set the Frame title with mode and file if exist
     */
    fun setTitle() {
        val messageArguments = arrayOf<Any>(mode!!.toLocalizedString())
        val formatter = MessageFormat(
                getResourceString("mode_title"))
        var title = formatter.format(messageArguments)
        var rawTitle = ""
        var model: MindMap? = null
        val mapModule = mapModule
        if (mapModule != null) {
            model = mapModule.model
            rawTitle = mapModule.toString()
            title = (rawTitle
                    + (if (model.isSaved) "" else "*")
                    + " - "
                    + title
                    + if (model.isReadOnly) " ("
                    + getResourceString("read_only") + ")" else "")
            val file = model.file
            if (file != null) {
                title += " " + file.absolutePath
            }
            for (contributor in mMapTitleContributorSet) {
                title = contributor.getMapTitle(title, mapModule, model)
            }
        }
        frame.setTitle(title)
        for (listener in mMapTitleChangeListenerSet) {
            listener.setMapTitle(rawTitle, mapModule, model)
        }
    }

    fun registerMapTitleChangeListener(
            pMapTitleChangeListener: MapTitleChangeListener) {
        mMapTitleChangeListenerSet.add(pMapTitleChangeListener)
    }

    fun deregisterMapTitleChangeListener(
            pMapTitleChangeListener: MapTitleChangeListener) {
        mMapTitleChangeListenerSet.remove(pMapTitleChangeListener)
    }

    fun registerZoomListener(pZoomListener: ZoomListener) {
        mZoomListenerSet.add(pZoomListener)
    }

    fun deregisterZoomListener(pZoomListener: ZoomListener) {
        mZoomListenerSet.remove(pZoomListener)
    }

    fun registerMapTitleContributor(
            pMapTitleContributor: MapTitleContributor) {
        mMapTitleContributorSet.add(pMapTitleContributor)
    }

    fun deregisterMapTitleContributor(
            pMapTitleContributor: MapTitleContributor) {
        mMapTitleContributorSet.remove(pMapTitleContributor)
    }
    //
    // Actions management
    //
    /**
     * Manage the availabilty of all Actions dependend of whether there is a map
     * or not
     */
    fun setAllActions(enabled: Boolean) {
        print!!.isEnabled = enabled && isPrintingAllowed
        printDirect!!.isEnabled = enabled && isPrintingAllowed
        printPreview!!.isEnabled = enabled && isPrintingAllowed
        page!!.isEnabled = enabled && isPrintingAllowed
        close!!.isEnabled = enabled
        moveToRoot!!.isEnabled = enabled
        (toolBar as MainToolBar?)!!.setAllActions(enabled)
        showSelectionAsRectangle!!.isEnabled = enabled
    }

    //
    // program/map control
    //
    private fun quit() {
        val currentMapRestorable = if (model != null) model!!
                .restorable else null
        storeOptionSplitPanePosition()
        // collect all maps:
        val restorables = Vector<String>()
        // move to first map in the window.
        val mapModuleVector = mapModuleManager!!.mapModuleVector
        if (mapModuleVector!!.size > 0) {
            val displayName = (mapModuleVector[0] as MapModule)
                    .displayName
            mapModuleManager!!.changeToMapModule(displayName)
        }
        while (mapModuleVector.size > 0) {
            if (mapModule != null) {
                val restorableBuffer = StringBuffer()
                val closingNotCancelled = mapModuleManager!!.close(
                        false, restorableBuffer)
                if (!closingNotCancelled) {
                    return
                }
                if (restorableBuffer.length != 0) {
                    val restorableString = restorableBuffer.toString()
                    logger!!.info("Closed the map $restorableString")
                    restorables.add(restorableString)
                }
            } else {
                // map module without view open.
                // FIXME: This seems to be a bad hack. correct me!
                mapModuleManager!!.nextMapModule()
            }
        }
        // store last tab session:
        var index = 0
        val lastStateMapXml = getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE)
        val management = LastStateStorageManagement(
                lastStateMapXml)
        management.lastFocussedTab = -1
        management.clearTabIndices()
        for (restorable in restorables) {
            val storage = management.getStorage(restorable)
            if (storage != null) {
                storage.tabIndex = index
            }
            if (Tools.safeEquals(restorable, currentMapRestorable)) {
                management.lastFocussedTab = index
            }
            index++
        }
        setProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE,
                management.xml)
        val lastOpenedString = lastOpenedList!!.save()
        setProperty("lastOpened", lastOpenedString)
        frame.setProperty(FreeMindCommon.ON_START_IF_NOT_SPECIFIED,
                currentMapRestorable ?: "")
        // getFrame().setProperty("menubarVisible",menubarVisible ? "true" :
        // "false");
        // ^ Not allowed in application because of problems with not working key
        // shortcuts
        setProperty("toolbarVisible", if (toolbarVisible) "true" else "false")
        setProperty("leftToolbarVisible", if (leftToolbarVisible) "true" else "false")
        if (!frame.isApplet) {
            val winState = frame.winState
            if (JFrame.MAXIMIZED_BOTH != winState and JFrame.MAXIMIZED_BOTH) {
                setProperty("appwindow_x", frame.winX.toString())
                setProperty("appwindow_y", frame.winY.toString())
                setProperty("appwindow_width", frame.winWidth.toString())
                setProperty("appwindow_height", frame.winHeight.toString())
            }
            setProperty("appwindow_state", winState.toString())
        }
        // Stop edit server!
        frame.saveProperties(true)
        // save to properties
        System.exit(0)
    }

    private fun acquirePrinterJobAndPageFormat(): Boolean {
        if (printerJob == null) {
            try {
                printerJob = PrinterJob.getPrinterJob()
            } catch (ex: SecurityException) {
                isPrintingAllowed = false
                return false
            }
        }
        if (pageFormat == null) {
            pageFormat = printerJob!!.defaultPage()
        }
        if (Tools.safeEquals(getProperty("page_orientation"), "landscape")) {
            pageFormat!!.orientation = PageFormat.LANDSCAPE
        } else if (Tools
                        .safeEquals(getProperty("page_orientation"), "portrait")) {
            pageFormat!!.orientation = PageFormat.PORTRAIT
        } else if (Tools.safeEquals(getProperty("page_orientation"),
                        "reverse_landscape")) {
            pageFormat!!.orientation = PageFormat.REVERSE_LANDSCAPE
        }
        val pageFormatProperty = getProperty(PAGE_FORMAT_PROPERTY)
        if (!pageFormatProperty.isEmpty()) {
            logger!!.info("Page format (stored): $pageFormatProperty")
            val storedPaper = Paper()
            Tools.setPageFormatFromString(storedPaper, pageFormatProperty)
            pageFormat!!.paper = storedPaper
        }
        return true
    }
    // ////////////
    // Inner Classes
    // //////////
    /**
     * Manages the history of visited maps. Maybe explicitly closed maps should
     * be removed from History too?
     */
    //
    // program/map control
    //
    private inner class QuitAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("quit")) {
        override fun actionPerformed(e: ActionEvent) {
            quit()
        }
    }

    /** This closes only the current map  */
    class CloseAction internal constructor(controller: Controller) : AbstractAction() {
        private val controller: Controller

        init {
            Tools.setLabelAndMnemonic(this,
                    controller.getResourceString("close"))
            this.controller = controller
        }

        override fun actionPerformed(e: ActionEvent) {
            controller.close(false)
        }
    }

    private inner class PrintAction internal constructor(controller: Controller, isDlg: Boolean) : AbstractAction(if (isDlg) controller.getResourceString("print_dialog") else controller.getResourceString("print"), ImageFactory.getInstance().createIcon(
            getResource("images/fileprint.png"))) {
        var isDlg: Boolean

        init {
            isEnabled = false
            this.isDlg = isDlg
        }

        override fun actionPerformed(e: ActionEvent) {
            if (!acquirePrinterJobAndPageFormat()) {
                return
            }
            printerJob!!.setPrintable(view, pageFormat)
            if (!isDlg || printerJob!!.printDialog()) {
                try {
                    frame.setWaitingCursor(true)
                    printerJob!!.print()
                    storePageFormat()
                } catch (ex: Exception) {
                    Resources.getInstance().logException(ex)
                } finally {
                    frame.setWaitingCursor(false)
                }
            }
        }
    }

    private inner class PrintPreviewAction internal constructor(var controller: Controller) : AbstractAction(controller.getResourceString("print_preview")) {
        override fun actionPerformed(e: ActionEvent) {
            if (!acquirePrinterJobAndPageFormat()) {
                return
            }
            val previewDialog = PreviewDialog(
                    controller.getResourceString("print_preview_title"),
                    view,
                    pageFormat)
            previewDialog.pack()
            previewDialog.setLocationRelativeTo(JOptionPane
                    .getFrameForComponent(view))
            previewDialog.isVisible = true
        }
    }

    private inner class PageAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("page")) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(e: ActionEvent) {
            if (!acquirePrinterJobAndPageFormat()) {
                return
            }

            // Ask about custom printing settings
            val dialog = JDialog(frame as JFrame,
                    getResourceString("printing_settings"),  /* modal= */true)
            val fitToPage = JCheckBox(
                    getResourceString("fit_to_page"), Resources.getInstance()
                    .getBoolProperty("fit_to_page"))
            val userZoomL = JLabel(getResourceString("user_zoom"))
            val userZoom = JTextField(
                    getProperty("user_zoom"), 3)
            userZoom.isEditable = !fitToPage.isSelected
            val okButton = JButton()
            Tools.setLabelAndMnemonic(okButton, getResourceString("ok"))
            val eventSource = Tools.IntHolder()
            val panel = JPanel()
            val gridbag = GridBagLayout()
            val c = GridBagConstraints()
            eventSource.value = 0
            okButton.addActionListener {
                eventSource.value = 1
                dialog.dispose()
            }
            fitToPage.addItemListener { e -> userZoom.isEditable = e.stateChange == ItemEvent.DESELECTED }

            // c.weightx = 0.5;
            c.gridx = 0
            c.gridy = 0
            c.gridwidth = 2
            gridbag.setConstraints(fitToPage, c)
            panel.add(fitToPage)
            c.gridy = 1
            c.gridwidth = 1
            gridbag.setConstraints(userZoomL, c)
            panel.add(userZoomL)
            c.gridx = 1
            c.gridwidth = 1
            gridbag.setConstraints(userZoom, c)
            panel.add(userZoom)
            c.gridy = 2
            c.gridx = 0
            c.gridwidth = 3
            c.insets = Insets(10, 0, 0, 0)
            gridbag.setConstraints(okButton, c)
            panel.add(okButton)
            panel.layout = gridbag
            dialog.defaultCloseOperation = JDialog.DISPOSE_ON_CLOSE
            dialog.contentPane = panel
            dialog.setLocationRelativeTo(frame as JFrame)
            dialog.rootPane.defaultButton = okButton
            dialog.pack() // calculate the size
            dialog.isVisible = true
            if (eventSource.value == 1) {
                setProperty("user_zoom", userZoom.text)
                setProperty("fit_to_page", if (fitToPage.isSelected) "true" else "false")
            } else return

            // Ask user for page format (e.g., portrait/landscape)
            pageFormat = printerJob!!.pageDialog(pageFormat)
            storePageFormat()
        }
    }

    interface LocalLinkConverter {
        /**
         * @throws MalformedURLException
         * if the conversion didn't work
         */
        @Throws(MalformedURLException::class)
        fun convertLocalLink(link: String): URL?
    }

    private inner class DefaultLocalLinkConverter : LocalLinkConverter {
        @Throws(MalformedURLException::class)
        override fun convertLocalLink(map: String): URL? {
            /* new handling for relative urls. fc, 29.10.2003. */
            val applicationPath = frame.freemindBaseDir
            // remove "." and make url
            return Tools
                    .fileToUrl(File(applicationPath + map.substring(1)))
            /* end: new handling for relative urls. fc, 29.10.2003. */
        }
    }

    //
    // Help
    //
    private inner class DocumentationAction internal constructor(var controller: Controller) : AbstractAction(controller.getResourceString("documentation")) {
        override fun actionPerformed(e: ActionEvent) {
            try {
                var map = controller.frame.getResourceString(
                        "browsemode_initial_map")
                // if the current language does not provide its own translation,
                // POSTFIX_TRANSLATE_ME is appended:
                map = Tools.removeTranslateComment(map)
                var url: URL? = null
                url = if (map != null && map.startsWith(".")) {
                    localDocumentationLinkConverter!!.convertLocalLink(map)
                } else {
                    Tools.fileToUrl(File(map))
                }
                val endUrl = url
                // invokeLater is necessary, as the mode changing removes
                // all
                // menus (inclusive this action!).
                SwingUtilities.invokeLater {
                    try {
                        createNewMode(BrowseMode.MODENAME)
                        controller.modeController!!.load(endUrl)
                    } catch (e1: Exception) {
                        Resources.getInstance().logException(
                                e1)
                    }
                }
            } catch (e1: MalformedURLException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e1)
            }
        }
    }

    private inner class KeyDocumentationAction internal constructor(var controller: Controller) : AbstractAction(controller.getResourceString("KeyDoc")) {
        override fun actionPerformed(e: ActionEvent) {
            var urlText = controller.frame.getResourceString(
                    "pdfKeyDocLocation")
            // if the current language does not provide its own translation,
            // POSTFIX_TRANSLATE_ME is appended:
            urlText = Tools.removeTranslateComment(urlText)
            try {
                var url: URL? = null
                url = if (urlText != null && urlText.startsWith(".")) {
                    localDocumentationLinkConverter
                            .convertLocalLink(urlText)
                } else {
                    Tools.fileToUrl(File(urlText))
                }
                logger!!.info("Opening key docs under $url")
                controller.frame.openDocument(url)
            } catch (e2: Exception) {
                Resources.getInstance().logException(e2)
                return
            }
        }
    }

    private inner class AboutAction internal constructor(var controller: Controller) : AbstractAction(controller.getResourceString("about")) {
        override fun actionPerformed(e: ActionEvent) {
            JOptionPane.showMessageDialog(view, controller.getResourceString("about_text")
                    + frame.freemindVersion,
                    controller.getResourceString("about"),
                    JOptionPane.INFORMATION_MESSAGE)
        }
    }

    private inner class LicenseAction internal constructor(var controller: Controller) : AbstractAction(controller.getResourceString("license")) {
        override fun actionPerformed(e: ActionEvent) {
            JOptionPane.showMessageDialog(view,
                    controller.getResourceString("license_text"),
                    controller.getResourceString("license"),
                    JOptionPane.INFORMATION_MESSAGE)
        }
    }

    //
    // Map navigation
    //
    private inner class NavigationPreviousMapAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("previous_map"), ImageFactory.getInstance().createIcon(
            getResource("images/1leftarrow.png"))) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(event: ActionEvent) {
            mapModuleManager!!.previousMapModule()
        }
    }

    private inner class ShowFilterToolbarAction internal constructor(controller: Controller?) : AbstractAction(getResourceString("filter_toolbar"), ImageFactory.getInstance().createIcon(
            getResource("images/filter.gif"))) {
        override fun actionPerformed(event: ActionEvent) {
            if (!filterController!!.isVisible) {
                filterController!!.showFilterToolbar(true)
            } else {
                filterController!!.showFilterToolbar(false)
            }
        }
    }

    private inner class NavigationNextMapAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("next_map"), ImageFactory.getInstance().createIcon(
            getResource("images/1rightarrow.png"))) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(event: ActionEvent) {
            mapModuleManager!!.nextMapModule()
        }
    }

    private inner class NavigationMoveMapLeftAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("move_map_left"), ImageFactory.getInstance().createIcon(
            getResource("images/draw-arrow-back.png"))) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(event: ActionEvent) {
            if (mTabbedPane != null) {
                val selectedIndex = mTabbedPane!!.selectedIndex
                val previousIndex = if (selectedIndex > 0) selectedIndex - 1 else mTabbedPane!!.tabCount - 1
                moveTab(selectedIndex, previousIndex)
            }
        }
    }

    private inner class NavigationMoveMapRightAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("move_map_right"),
            ImageFactory.getInstance().createIcon(getResource("images/draw-arrow-forward.png"))) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(event: ActionEvent) {
            if (mTabbedPane != null) {
                val selectedIndex = mTabbedPane!!.selectedIndex
                val previousIndex = if (selectedIndex >= mTabbedPane!!.tabCount - 1) 0 else selectedIndex + 1
                moveTab(selectedIndex, previousIndex)
            }
        }
    }

    fun moveTab(src: Int, dst: Int) {
        // snippet taken from
        // http://www.exampledepot.com/egs/javax.swing/tabbed_TpMove.html
        // Get all the properties
        val comp = mTabbedPane!!.getComponentAt(src)
        val label = mTabbedPane!!.getTitleAt(src)
        val icon = mTabbedPane!!.getIconAt(src)
        val iconDis = mTabbedPane!!.getDisabledIconAt(src)
        val tooltip = mTabbedPane!!.getToolTipTextAt(src)
        val enabled = mTabbedPane!!.isEnabledAt(src)
        val keycode = mTabbedPane!!.getMnemonicAt(src)
        val mnemonicLoc = mTabbedPane!!.getDisplayedMnemonicIndexAt(src)
        val fg = mTabbedPane!!.getForegroundAt(src)
        val bg = mTabbedPane!!.getBackgroundAt(src)
        mTabbedPaneSelectionUpdate = false
        // Remove the tab
        mTabbedPane!!.remove(src)
        // Add a new tab
        mTabbedPane!!.insertTab(label, icon, comp, tooltip, dst)
        Tools.swapVectorPositions(mTabbedPaneMapModules, src, dst)
        mapModuleManager!!.swapModules(src, dst)
        mTabbedPane!!.selectedIndex = dst
        mTabbedPaneSelectionUpdate = true

        // Restore all properties
        mTabbedPane!!.setDisabledIconAt(dst, iconDis)
        mTabbedPane!!.setEnabledAt(dst, enabled)
        mTabbedPane!!.setMnemonicAt(dst, keycode)
        mTabbedPane!!.setDisplayedMnemonicIndexAt(dst, mnemonicLoc)
        mTabbedPane!!.setForegroundAt(dst, fg)
        mTabbedPane!!.setBackgroundAt(dst, bg)
    }

    //
    // Node navigation
    //
    private inner class MoveToRootAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("move_to_root")) {
        init {
            isEnabled = false
        }

        override fun actionPerformed(event: ActionEvent) {
            moveToRoot()
        }
    }

    private inner class ToggleMenubarAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("toggle_menubar")), MenuItemSelectedListener {
        init {
            isEnabled = true
        }

        override fun actionPerformed(event: ActionEvent) {
            menubarVisible = !menubarVisible
            setMenubarVisible(menubarVisible)
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return menubarVisible
        }
    }

    private inner class ToggleToolbarAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("toggle_toolbar")), MenuItemSelectedListener {
        init {
            isEnabled = true
        }

        override fun actionPerformed(event: ActionEvent) {
            toolbarVisible = !toolbarVisible
            setToolbarVisible(toolbarVisible)
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            logger!!.info("ToggleToolbar was asked for selectedness.")
            return toolbarVisible
        }
    }

    private inner class ToggleLeftToolbarAction internal constructor(controller: Controller) : AbstractAction(controller.getResourceString("toggle_left_toolbar")), MenuItemSelectedListener {
        init {
            isEnabled = true
        }

        override fun actionPerformed(event: ActionEvent) {
            leftToolbarVisible = !leftToolbarVisible
            setLeftToolbarVisible(leftToolbarVisible)
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return leftToolbarVisible
        }
    }

    protected inner class ZoomInAction(controller: Controller) : AbstractAction(controller.getResourceString("zoom_in")) {
        override fun actionPerformed(e: ActionEvent) {
            // logger.info("ZoomInAction actionPerformed");
            val currentZoom = view!!.zoom
            for (i in zoomValues.indices) {
                val `val` = zoomValues[i]
                if (`val` > currentZoom) {
                    setZoom(`val`)
                    return
                }
            }
            setZoom(zoomValues[zoomValues.size - 1])
        }
    }

    protected inner class ZoomOutAction(controller: Controller) : AbstractAction(controller.getResourceString("zoom_out")) {
        override fun actionPerformed(e: ActionEvent) {
            val currentZoom = view!!.zoom
            var lastZoom = zoomValues[0]
            for (i in zoomValues.indices) {
                val `val` = zoomValues[i]
                if (`val` >= currentZoom) {
                    setZoom(lastZoom)
                    return
                }
                lastZoom = `val`
            }
            setZoom(lastZoom)
        }
    }

    protected inner class ShowSelectionAsRectangleAction(controller: Controller) : AbstractAction(controller.getResourceString("selection_as_rectangle")), MenuItemSelectedListener {
        override fun actionPerformed(e: ActionEvent) {
            // logger.info("ShowSelectionAsRectangleAction action Performed");
            toggleSelectionAsRectangle()
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return isSelectionAsRectangle
        }
    }

    fun toggleSelectionAsRectangle() {
        if (isSelectionAsRectangle) {
            setProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION,
                    BooleanProperty.FALSE_VALUE)
        } else {
            setProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION,
                    BooleanProperty.TRUE_VALUE)
        }
    }

    private val isSelectionAsRectangle: Boolean
        private get() = getProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION)
                .equals(BooleanProperty.TRUE_VALUE, ignoreCase = true)

    /**
     */
    val map: MindMap
        get() = mapModule!!.model

    /**
     * @author foltin
     */
    inner class PropertyAction
    /**
     *
     */(private val controller: Controller) : AbstractAction(controller.getResourceString("property_dialog")) {
        override fun actionPerformed(arg0: ActionEvent) {
            val dialog = JDialog(frame.jFrame, true /* modal */)
            dialog.isResizable = true
            dialog.isUndecorated = false
            val options = OptionPanel(frame as FreeMind,
                    dialog) { props ->
                val sortedKeys = Vector<String>()
                sortedKeys.addAll(props.stringPropertyNames())
                Collections.sort(sortedKeys)
                var propertiesChanged = false
                val i: Iterator<String> = sortedKeys.iterator()
                while (i.hasNext()) {
                    val key = i.next()
                    // save only changed keys:
                    val newProperty = props.getProperty(key)
                    propertiesChanged = (propertiesChanged
                            || newProperty != controller
                            .getProperty(key))
                    controller.setProperty(key, newProperty)
                }
                if (propertiesChanged) {
                    JOptionPane.showMessageDialog(null,
                            getResourceString("option_changes_may_require_restart"))
                    controller.frame.saveProperties(false)
                }
            }
            options.buildPanel()
            options.setProperties()
            dialog.title = "Freemind Properties"
            dialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            dialog.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(event: WindowEvent) {
                    options.closeWindow()
                }
            })
            val action: Action = object : AbstractAction() {
                override fun actionPerformed(arg0: ActionEvent) {
                    options.closeWindow()
                }
            }
            Tools.addEscapeActionToDialog(dialog, action)
            dialog.isVisible = true
        }
    }

    inner class OptionAntialiasAction : AbstractAction() {
        override fun actionPerformed(e: ActionEvent) {
            val command = e.actionCommand
            changeAntialias(command)
        }

        /**
         */
        fun changeAntialias(command: String?) {
            if (command == null) {
                return
            }
            setProperty(FreeMindCommon.RESOURCE_ANTIALIAS, command)
            if (view != null) view!!.repaint()
        }
    }

    private inner class OptionHTMLExportFoldingAction internal constructor(controller: Controller?) : AbstractAction() {
        override fun actionPerformed(e: ActionEvent) {
            setProperty("html_export_folding", e.actionCommand)
        }
    }

    // switch auto properties for selection mechanism fc, 7.12.2003.
    private inner class OptionSelectionMechanismAction internal constructor(var c: Controller) : AbstractAction(), FreemindPropertyListener {
        init {
            addPropertyChangeListener(this)
        }

        override fun actionPerformed(e: ActionEvent) {
            val command = e.actionCommand
            changeSelection(command)
        }

        /**
         */
        private fun changeSelection(command: String) {
            setProperty("selection_method", command)
            // and update the selection method in the NodeMouseMotionListener
            c.nodeMouseMotionListener!!.updateSelectionMethod()
            val statusBarString = c.getResourceString(command)
            if (statusBarString != null) // should not happen
                c.frame.out(statusBarString)
        }

        override fun propertyChanged(propertyName: String, newValue: String,
                                     oldValue: String) {
            if (propertyName == FreeMind.RESOURCES_SELECTION_METHOD) {
                changeSelection(newValue)
            }
        }
    }

    // open faq url from freeminds page:
    inner class OpenURLAction internal constructor(var c: Controller, description: String?, private val url: String) : AbstractAction(description, ImageFactory.getInstance().createIcon(
            controller.getResource("images/Link.png"))) {
        override fun actionPerformed(e: ActionEvent) {
            try {
                c.frame.openDocument(URL(url))
            } catch (ex: MalformedURLException) {
                c.errorMessage("""
    ${c.getResourceString("url_error")}
    $ex
    """.trimIndent())
            } catch (ex: Exception) {
                c.errorMessage(ex)
            }
        }
    }

    fun addTabbedPane(pTabbedPane: JTabbedPane?) {
        mTabbedPane = pTabbedPane
        mTabbedPaneMapModules = Vector()
        mTabbedPane!!.addChangeListener { tabSelectionChanged() }
        mapModuleManager!!.addListener(object : MapModuleChangeObserver {
            override fun afterMapModuleChange(pOldMapModule: MapModule?,
                                              pOldMode: Mode?, pNewMapModule: MapModule?, pNewMode: Mode) {
                val selectedIndex = mTabbedPane!!.selectedIndex
                if (pNewMapModule == null) {
                    return
                }
                // search, if already present:
                for (i in mTabbedPaneMapModules!!.indices) {
                    if (mTabbedPaneMapModules!![i] === pNewMapModule) {
                        if (selectedIndex != i) {
                            mTabbedPane!!.selectedIndex = i
                        }
                        return
                    }
                }
                // create new tab:
                mTabbedPaneMapModules!!.add(pNewMapModule)
                mTabbedPane!!.addTab(pNewMapModule.toString(), JPanel())
                mTabbedPane!!.selectedIndex = mTabbedPane!!.tabCount - 1
            }

            override fun beforeMapModuleChange(pOldMapModule: MapModule?,
                                               pOldMode: Mode?, pNewMapModule: MapModule?, pNewMode: Mode?) {
            }

            override fun isMapModuleChangeAllowed(pOldMapModule: MapModule?,
                                                  pOldMode: Mode?, pNewMapModule: MapModule?, pNewMode: Mode?): Boolean {
                return true
            }

            override fun numberOfOpenMapInformation(pNumber: Int, pIndex: Int) {}
            override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {
                for (i in mTabbedPaneMapModules!!.indices) {
                    if (mTabbedPaneMapModules!![i] === pOldMapModule) {
                        logger!!.fine("Remove tab:" + i + " with title:"
                                + mTabbedPane!!.getTitleAt(i))
                        mTabbedPaneSelectionUpdate = false
                        mTabbedPane!!.removeTabAt(i)
                        mTabbedPaneMapModules!!.removeAt(i)
                        mTabbedPaneSelectionUpdate = true
                        tabSelectionChanged()
                        return
                    }
                }
            }
        })
        registerMapTitleChangeListener { pNewMapTitle, pMapModule, pModel ->
            for (i in mTabbedPaneMapModules!!.indices) {
                if (mTabbedPaneMapModules!![i] === pMapModule) {
                    mTabbedPane!!.setTitleAt(i,
                            pNewMapTitle + if (pModel!!.isSaved) "" else "*")
                }
            }
        }
    }

    private fun tabSelectionChanged() {
        if (!mTabbedPaneSelectionUpdate) return
        val selectedIndex = mTabbedPane!!.selectedIndex
        // display nothing on the other tabs:
        for (j in 0 until mTabbedPane!!.tabCount) {
            if (j != selectedIndex) mTabbedPane!!.setComponentAt(j, JPanel())
        }
        if (selectedIndex < 0) {
            // nothing selected. probably, the last map was closed
            return
        }
        val module = mTabbedPaneMapModules!![selectedIndex] as MapModule
        logger!!.fine("Selected index of tab is now: " + selectedIndex
                + " with title:" + module.toString())
        if (module !== mapModule) {
            // we have to change the active map actively:
            mapModuleManager!!.changeToMapModule(module.toString())
        }
        // mScrollPane could be set invisible by JTabbedPane
        frame.scrollPane.isVisible = true
        mTabbedPane!!.setComponentAt(selectedIndex, frame.contentComponent)
        // double call, due to mac strangeness.
        obtainFocusForSelected()
    }

    protected fun storePageFormat() {
        if (pageFormat!!.orientation == PageFormat.LANDSCAPE) {
            setProperty("page_orientation", "landscape")
        } else if (pageFormat!!.orientation == PageFormat.PORTRAIT) {
            setProperty("page_orientation", "portrait")
        } else if (pageFormat!!.orientation == PageFormat.REVERSE_LANDSCAPE) {
            setProperty("page_orientation", "reverse_landscape")
        }
        setProperty(PAGE_FORMAT_PROPERTY,
                Tools.getPageFormatAsString(pageFormat!!.paper))
    }

    enum class SplitComponentType(val index: Int) {
        NOTE_PANEL(0), ATTRIBUTE_PANEL(1);

    }

    private var mOptionalSplitPane: JOptionalSplitPane? = null

    //
    // Constructors
    //
    init {
        if (logger == null) {
            logger = frame.getLogger(this.javaClass.name)
        }
    }

    /**
     * Inserts a (south) component into the split pane. If the screen isn't
     * split yet, a split pane should be created on the fly.
     *
     * @param pMindMapComponent
     * south panel to be inserted
     * @return the split pane in order to move the dividers.
     */
    fun insertComponentIntoSplitPane(pMindMapComponent: JComponent?, pSplitComponentType: SplitComponentType) {
        if (mOptionalSplitPane == null) {
            mOptionalSplitPane = JOptionalSplitPane()
            mOptionalSplitPane!!.lastDividerPosition = getIntProperty(
                    FreeMind.RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION, -1)
            mOptionalSplitPane!!.setComponent(pMindMapComponent!!, pSplitComponentType.index)
            frame.insertComponentIntoSplitPane(mOptionalSplitPane)
        } else {
            mOptionalSplitPane!!.setComponent(pMindMapComponent!!, pSplitComponentType.index)
        }
    }

    /**
     * Indicates that the south panel should be made invisible.
     */
    fun removeSplitPane(pSplitComponentType: SplitComponentType) {
        if (mOptionalSplitPane != null) {
            mOptionalSplitPane!!.removeComponent(pSplitComponentType.index)
            if (mOptionalSplitPane!!.amountOfComponents == 0) {
                frame.removeSplitPane()
                mOptionalSplitPane = null
            }
        }
    }

    /**
     *
     */
    private fun storeOptionSplitPanePosition() {
        if (mOptionalSplitPane != null) {
            setProperty(FreeMind.RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION, ""
                    + mOptionalSplitPane!!.dividerPosition)
        }
    }

    companion object {
        /**
         *
         */
        private const val PAGE_FORMAT_PROPERTY = "page_format"

        /**
         * Converts from a local link to the real file URL of the documentation map.
         * (Used to change this behaviour under MacOSX).
         */
        private var logger: Logger? = null

        /** Used for MAC!!!  */
        var localDocumentationLinkConverter: LocalLinkConverter? = null
        /** Static JColorChooser to have the recent colors feature.  */
        val commonJColorChooser = JColorChooser()
        private val zoomValues = floatArrayOf(25 / 100f, 50 / 100f,
                75 / 100f, 100 / 100f, 150 / 100f, 200 / 100f, 300 / 100f,
                400 / 100f)
        private val propertyChangeListeners = Vector<FreemindPropertyListener>()

        //
        // get/set methods
        //
        val JAVA_VERSION = System
                .getProperty("java.version")

        @Throws(HeadlessException::class)
        fun showCommonJColorChooserDialog(component: Component?,
                                          title: String?, initialColor: Color?): Color? {
            val pane = commonJColorChooser
            pane.color = initialColor
            val ok = ColorTracker(pane)
            val dialog = JColorChooser.createDialog(component, title, true,
                    pane, ok, null)
            dialog.addWindowListener(Closer())
            dialog.addComponentListener(DisposeOnClose())
            dialog.isVisible = true
            // blocks until user brings dialog down...
            return ok.getColor()
        }

        //
        // Preferences
        //
        fun getPropertyChangeListeners(): Collection<FreemindPropertyListener> {
            return Collections.unmodifiableCollection(propertyChangeListeners)
        }

        fun addPropertyChangeListener(
                listener: FreemindPropertyListener) {
            propertyChangeListeners.add(listener)
        }

        /**
         * @param listener
         * The new listener. All currently available properties are sent
         * to the listener after registration. Here, the oldValue
         * parameter is set to null.
         */
        fun addPropertyChangeListenerAndPropagate(listener: FreemindPropertyListener) {
            addPropertyChangeListener(listener)
            val properties = Resources.getInstance().properties
            for (key in properties.keys) {
                listener.propertyChanged(key as String, properties.getProperty(key), null)
            }
        }

        fun removePropertyChangeListener(
                listener: FreemindPropertyListener) {
            propertyChangeListeners.remove(listener)
        }
    }
}