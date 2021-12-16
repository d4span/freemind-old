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
import javax.swing.AbstractListModel
import freemind.controller.filter.util.SortedListModel
import freemind.controller.filter.util.SortedMapListModel
import javax.swing.ComboBoxModel
import javax.swing.DefaultComboBoxModel
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import freemind.controller.filter.util.ExtendedComboBoxModel.ExtensionDataListener
import freemind.controller.filter.util.ExtendedComboBoxModel
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
import javax.swing.JDialog
import java.lang.NullPointerException
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.MouseAdapter
import java.lang.Runnable
import java.awt.event.ActionListener
import freemind.controller.filter.FilterComposerDialog.MindMapFilterFileFilter
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
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import freemind.common.JOptionalSplitPane
import freemind.controller.Controller.SplitComponentType
import java.awt.HeadlessException
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
import java.awt.datatransfer.Clipboard
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.controller.MenuItemEnabledListener
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.modes.*
import java.io.*
import java.net.URL
import java.util.*

/**
 * Manages the list of MapModules. As this task is very complex, I exported it
 * from Controller to this class to keep Controller simple.
 *
 * The information exchange between controller and this class is managed by
 * oberser pattern (the controller observes changes to the map modules here).
 *
 * TODO: Use an vector with the map modules ordered by the screen order.
 */
class MapModuleManager internal constructor(c: Controller?) {
    interface MapModuleChangeObserver {
        /**
         * The params may be null to indicate the there was no previous map, or
         * that the last map is closed now.
         */
        fun isMapModuleChangeAllowed(oldMapModule: MapModule?, oldMode: Mode?,
                                     newMapModule: MapModule?, newMode: Mode?): Boolean

        fun beforeMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                  newMapModule: MapModule?, newMode: Mode?)

        fun afterMapClose(oldMapModule: MapModule?, oldMode: Mode?)
        fun afterMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                 newMapModule: MapModule?, newMode: Mode)

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

        override fun isMapModuleChangeAllowed(oldMapModule: MapModule?,
                                              oldMode: Mode?, newMapModule: MapModule?, newMode: Mode?): Boolean {
            var returnValue = true
            for (observer in Vector(listeners)) {
                returnValue = observer.isMapModuleChangeAllowed(oldMapModule,
                        oldMode, newMapModule, newMode)
                if (!returnValue) {
                    break
                }
            }
            return returnValue
        }

        override fun beforeMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                           newMapModule: MapModule?, newMode: Mode?) {
            for (observer in Vector(listeners)) {
                observer.beforeMapModuleChange(oldMapModule, oldMode,
                        newMapModule, newMode)
            }
        }

        override fun afterMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                          newMapModule: MapModule?, newMode: Mode) {
            for (observer in Vector(listeners)) {
                observer.afterMapModuleChange(oldMapModule, oldMode,
                        newMapModule, newMode)
            }
        }

        override fun numberOfOpenMapInformation(number: Int, pIndex: Int) {
            for (observer in Vector(listeners)) {
                observer.numberOfOpenMapInformation(number, pIndex)
            }
        }

        override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {
            for (observer in Vector(listeners)) {
                observer.afterMapClose(pOldMapModule, pOldMode)
            }
        }
    }

    /**
     * You can register yourself to this listener at the main controller.
     */
    interface MapTitleChangeListener {
        fun setMapTitle(pNewMapTitle: String, pMapModule: MapModule?,
                        pModel: MindMap?)
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
        fun getMapTitle(pOldTitle: String?, pMapModule: MapModule?,
                        pModel: MindMap?): String?
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
    @get:Deprecated("""use getMapModuleVector instead (and get the displayname as
	              MapModule.getDisplayName().""")
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
        val mapModule = MapModule(map, MapView(map, modeController),
                modeController.mode, modeController)
        addToOrChangeInMapModules(mapModule.toString(), mapModule)
        setMapModule(mapModule, modeController.mode)
    }

    fun getModuleGivenModeController(pModeController: ModeController): MapModule? {
        var mapModule: MapModule? = null
        for ((_, value) in mapModules) {
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
            File(urlToCheck.file) == File(moduleUrl
                    .file)
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
    fun setMapModule(newMapModule: MapModule?, newMode: Mode): Boolean {
        // allowed?
        val oldMapModule = mapModule
        val oldMode = mCurrentMode
        if (!listener.isMapModuleChangeAllowed(oldMapModule, oldMode,
                        newMapModule, newMode)) {
            return false
        }
        listener.beforeMapModuleChange(oldMapModule, oldMode, newMapModule,
                newMode)
        mapModule = newMapModule
        mCurrentMode = newMode
        listener.afterMapModuleChange(oldMapModule, oldMode, newMapModule,
                newMode)
        fireNumberOfOpenMapInformation()
        return true
    }

    private fun fireNumberOfOpenMapInformation() {
        listener.numberOfOpenMapInformation(mapModuleVector.size,
                mapModuleVector.indexOf(mapModule))
    }

    // private
    private fun addToOrChangeInMapModules(key: String,
                                          newOrChangedMapModule: MapModule?) {
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
        val closingNotCancelled = module!!.modeController.close(force,
                this)
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