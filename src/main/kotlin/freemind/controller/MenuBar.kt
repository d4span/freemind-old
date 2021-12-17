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
/*$Id: MenuBar.java,v 1.24.14.17.2.22 2008/11/12 21:44:33 christianfoltin Exp $*/
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
import freemind.main.*
import java.lang.Exception
import java.util.TreeMap
import java.util.logging.Logger

/**
 * This is the menu bar for FreeMind. Actions are defined in MenuListener.
 * Moreover, the StructuredMenuHolder of all menus are hold here.
 */
class MenuBar(var c: Controller) : JMenuBar() {
    /**
     */
    var menuHolder: StructuredMenuHolder? = null
        private set

    // visible only in controller package
    var mapsPopupMenu: JPopupMenu? = null
    var mapsMenuActionListener: ActionListener = MapsMenuActionListener()

    init {
        if (logger == null) {
            logger = c.frame.getLogger(this.javaClass.name)
        }
        // updateMenus();
    } // Constructor

    /**
     * This is the only public method. It restores all menus.
     *
     * @param newModeController
     */
    fun updateMenus(newModeController: ModeController?) {
        this.removeAll()
        menuHolder = StructuredMenuHolder()

        // filemenu
        menuHolder!!.addMenu(JMenu(c.getResourceString("file")), FILE_MENU + ".")
        // filemenu.setMnemonic(KeyEvent.VK_F);
        menuHolder!!.addCategory(FILE_MENU + "open")
        menuHolder!!.addCategory(FILE_MENU + "close")
        menuHolder!!.addSeparator(FILE_MENU)
        menuHolder!!.addCategory(FILE_MENU + "export")
        menuHolder!!.addSeparator(FILE_MENU)
        menuHolder!!.addCategory(FILE_MENU + "import")
        menuHolder!!.addSeparator(FILE_MENU)
        menuHolder!!.addCategory(FILE_MENU + "print")
        menuHolder!!.addSeparator(FILE_MENU)
        menuHolder!!.addCategory(FILE_MENU + "last")
        menuHolder!!.addSeparator(FILE_MENU)
        menuHolder!!.addCategory(FILE_MENU + "quit")

        // editmenu
        menuHolder!!.addMenu(JMenu(c.getResourceString("edit")), EDIT_MENU + ".")
        menuHolder!!.addCategory(EDIT_MENU + "undo")
        menuHolder!!.addSeparator(EDIT_MENU)
        menuHolder!!.addCategory(EDIT_MENU + "select")
        menuHolder!!.addSeparator(EDIT_MENU)
        menuHolder!!.addCategory(EDIT_MENU + "paste")
        menuHolder!!.addSeparator(EDIT_MENU)
        menuHolder!!.addCategory(EDIT_MENU + "edit")
        menuHolder!!.addSeparator(EDIT_MENU)
        menuHolder!!.addCategory(EDIT_MENU + "find")

        // view menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_view")),
                VIEW_MENU + ".")

        // insert menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_insert")),
                INSERT_MENU + ".")
        menuHolder!!.addCategory(INSERT_MENU + "nodes")
        menuHolder!!.addSeparator(INSERT_MENU)
        menuHolder!!.addCategory(INSERT_MENU + "icons")
        menuHolder!!.addSeparator(INSERT_MENU)

        // format menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_format")), FORMAT_MENU + ".")

        // navigate menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_navigate")),
                NAVIGATE_MENU + ".")

        // extras menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_extras")),
                EXTRAS_MENU + ".")
        menuHolder!!.addCategory(EXTRAS_MENU + "first")

        // Mapsmenu
        menuHolder!!.addMenu(JMenu(c.getResourceString("mindmaps")), MINDMAP_MENU + ".")
        // mapsmenu.setMnemonic(KeyEvent.VK_M);
        menuHolder!!.addCategory(MINDMAP_MENU + "navigate")
        menuHolder!!.addSeparator(MINDMAP_MENU)
        menuHolder!!.addCategory(MENU_MINDMAP_CATEGORY)
        menuHolder!!.addSeparator(MINDMAP_MENU)
        // Modesmenu
        menuHolder!!.addCategory(MODES_MENU)

        // maps popup menu
        mapsPopupMenu = FreeMindPopupMenu()
        mapsPopupMenu.setName(c.getResourceString("mindmaps"))
        menuHolder!!.addCategory(POPUP_MENU + "navigate")
        // menuHolder.addSeparator(POPUP_MENU);

        // formerly, the modes menu was an own menu, but to need less place for
        // the menus,
        // we integrated it into the maps menu.
        // JMenu modesmenu = menuHolder.addMenu(new
        // JMenu(c.getResourceString("modes")), MODES_MENU+".");
        menuHolder!!.addMenu(JMenu(c.getResourceString("help")), HELP_MENU
                + ".")
        menuHolder!!.addAction(c.documentation, HELP_MENU + "doc/documentation")
        menuHolder!!.addAction(c.freemindUrl, HELP_MENU + "doc/freemind")
        menuHolder!!.addAction(c.faq, HELP_MENU + "doc/faq")
        menuHolder!!.addAction(c.keyDocumentation, HELP_MENU
                + "doc/keyDocumentation")
        menuHolder!!.addSeparator(HELP_MENU)
        menuHolder!!.addCategory(HELP_MENU + "bugs")
        menuHolder!!.addSeparator(HELP_MENU)
        menuHolder!!.addAction(c.license, HELP_MENU + "about/license")
        menuHolder!!.addAction(c.about, HELP_MENU + "about/about")
        updateFileMenu()
        updateViewMenu()
        updateEditMenu()
        updateModeMenu()
        updateMapsMenu(menuHolder!!, MENU_MINDMAP_CATEGORY + "/")
        updateMapsMenu(menuHolder!!, POPUP_MENU)
        addAdditionalPopupActions()
        // the modes:
        newModeController.updateMenus(menuHolder)
        menuHolder!!.updateMenus(this, MENU_BAR_PREFIX)
        menuHolder!!.updateMenus(mapsPopupMenu, GENERAL_POPUP_PREFIX)
    }

    private fun updateModeMenu() {
        val group = ButtonGroup()
        val modesMenuActionListener: ActionListener = ModesMenuActionListener()
        val keys: List<String?> = LinkedList(c.modes)
        for (key in keys) {
            val item = JRadioButtonMenuItem(
                    c.getResourceString("mode_$key"))
            item.actionCommand = key
            val newItem = menuHolder
                    .addMenuItem(item, MODES_MENU + key) as JRadioButtonMenuItem
            group.add(newItem)
            if (c.mode != null) {
                newItem.isSelected = c.mode.toString() == key
            } else {
                newItem.isSelected = false
            }
            val keystroke = c.frame.getAdjustableProperty(
                    "keystroke_mode_$key")
            if (keystroke != null) {
                newItem.accelerator = KeyStroke.getKeyStroke(keystroke)
            }
            newItem.addActionListener(modesMenuActionListener)
        }
    }

    private fun addAdditionalPopupActions() {
        menuHolder!!.addSeparator(POPUP_MENU)
        var newPopupItem: JMenuItem?
        if (c.frame.isApplet) {
            // We have enabled hiding of menubar only in applets. It it because
            // when we hide menubar in application, the key accelerators from
            // menubar do not work.
            newPopupItem = menuHolder!!.addAction(c.toggleMenubar, POPUP_MENU
                    + "toggleMenubar")
            newPopupItem!!.foreground = Color(100, 80, 80)
        }
        newPopupItem = menuHolder!!.addAction(c.toggleToolbar, POPUP_MENU
                + "toggleToolbar")
        newPopupItem!!.foreground = Color(100, 80, 80)
        newPopupItem = menuHolder!!.addAction(c.toggleLeftToolbar, POPUP_MENU
                + "toggleLeftToolbar")
        newPopupItem!!.foreground = Color(100, 80, 80)
    }

    private fun updateMapsMenu(holder: StructuredMenuHolder, basicKey: String) {
        val mapModuleManager = c.mapModuleManager
        val mapModuleVector = mapModuleManager!!.mapModuleVector ?: return
        val group = ButtonGroup()
        for (mapModule in mapModuleVector) {
            val displayName = mapModule!!.displayName
            val newItem = JRadioButtonMenuItem(displayName)
            newItem.isSelected = false
            group.add(newItem)
            newItem.addActionListener(mapsMenuActionListener)
            newItem.setMnemonic(displayName[0])
            val currentMapModule = mapModuleManager.mapModule
            if (currentMapModule != null) {
                if (mapModule === currentMapModule) {
                    newItem.isSelected = true
                }
            }
            holder.addMenuItem(newItem, basicKey + displayName)
        }
    }

    private fun updateFileMenu() {
        menuHolder!!.addAction(c.page, FILE_MENU + "print/pageSetup")
        val print = menuHolder!!.addAction(c.print, FILE_MENU
                + "print/print")
        print!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_print"))
        val printPreview = menuHolder!!.addAction(c.printPreview, FILE_MENU
                + "print/printPreview")
        printPreview!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_print_preview"))
        val close = menuHolder!!.addAction(c.close, FILE_MENU
                + "close/close")
        close!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_close"))
        val quit = menuHolder!!.addAction(c.quit, FILE_MENU + "quit/quit")
        quit!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_quit"))
        updateLastOpenedList()
    }

    private fun updateLastOpenedList() {
        menuHolder!!.addMenu(JMenu(c.getResourceString("most_recent_files")),
                FILE_MENU + "last/.")
        var firstElement = true
        val lst = c.lastOpenedList
        val it = lst!!.listIterator()
        while (it!!.hasNext()) {
            val key = it.next()
            val item = JMenuItem(key)
            if (firstElement) {
                firstElement = false
                item.accelerator = KeyStroke.getKeyStroke(c.frame
                        .getAdjustableProperty(
                                "keystroke_open_first_in_history"))
            }
            item.addActionListener(LastOpenedActionListener(key))
            menuHolder!!.addMenuItem(item,
                    FILE_MENU + "last/" + key!!.replace('/', '_'))
        }
    }

    private fun updateEditMenu() {
        val moveToRoot = menuHolder!!.addAction(c.moveToRoot, NAVIGATE_MENU
                + "nodes/moveToRoot")
        moveToRoot!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_moveToRoot"))
        val previousMap = menuHolder!!.addAction(c.navigationPreviousMap,
                MINDMAP_MENU + "navigate/navigationPreviousMap")
        previousMap!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_PREVIOUS_MAP))
        val nextMap = menuHolder!!.addAction(c.navigationNextMap,
                MINDMAP_MENU + "navigate/navigationNextMap")
        nextMap!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_NEXT_MAP))
        val MoveMapLeft = menuHolder!!.addAction(
                c.navigationMoveMapLeftAction, MINDMAP_MENU
                + "navigate/navigationMoveMapLeft")
        MoveMapLeft!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_LEFT))
        val MoveMapRight = menuHolder!!.addAction(
                c.navigationMoveMapRightAction, MINDMAP_MENU
                + "navigate/navigationMoveMapRight")
        MoveMapRight!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_RIGHT))

        // option menu item moved to mindmap_menus.xml

        // if (false) {
        // preferences.add(c.background);
        // // Background is disabled from preferences, because it has no real
        // function.
        // // To complete the function, one would either make sure that the
        // color is
        // // saved and read from auto.properties or think about storing the
        // background
        // // color into map (just like <map backgroud="#eeeee0">).
        // }
    }

    private fun updateViewMenu() {
        menuHolder!!.addAction(c.toggleToolbar, VIEW_MENU + "toolbars/toggleToolbar")
        menuHolder!!.addAction(c.toggleLeftToolbar, VIEW_MENU + "toolbars/toggleLeftToolbar")
        menuHolder!!.addSeparator(VIEW_MENU)
        menuHolder!!.addAction(c.showSelectionAsRectangle, VIEW_MENU + "general/selectionAsRectangle")
        val zoomIn = menuHolder!!.addAction(c.zoomIn, VIEW_MENU + "zoom/zoomIn")
        zoomIn!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_zoom_in"))
        val zoomOut = menuHolder!!.addAction(c.zoomOut, VIEW_MENU
                + "zoom/zoomOut")
        zoomOut!!.accelerator = KeyStroke.getKeyStroke(c.frame
                .getAdjustableProperty("keystroke_zoom_out"))
        menuHolder!!.addSeparator(VIEW_MENU)
        menuHolder!!.addCategory(VIEW_MENU + "note_window")
    }

    private inner class MapsMenuActionListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            SwingUtilities.invokeLater {
                c.mapModuleManager.changeToMapModule(
                        e.actionCommand)
            }
        }
    }

    private inner class LastOpenedActionListener(private val mKey: String?) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val restoreable = mKey
            try {
                c.lastOpenedList.open(restoreable)
            } catch (ex: Exception) {
                c.errorMessage("An error occured on opening the file: "
                        + restoreable + ".")
                Resources.getInstance().logException(ex)
            }
        }
    }

    private inner class ModesMenuActionListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            SwingUtilities.invokeLater { c.createNewMode(e.actionCommand) }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JMenuBar#processKeyBinding(javax.swing.KeyStroke,
	 * java.awt.event.KeyEvent, int, boolean)
	 */
    public override fun processKeyBinding(ks: KeyStroke, e: KeyEvent, condition: Int,
                                          pressed: Boolean): Boolean {
        return super.processKeyBinding(ks, e, condition, pressed)
    }

    companion object {
        private var logger: Logger? = null
        const val MENU_BAR_PREFIX = "menu_bar/"
        const val GENERAL_POPUP_PREFIX = "popup/"
        const val POPUP_MENU = GENERAL_POPUP_PREFIX + "popup/"
        const val INSERT_MENU = MENU_BAR_PREFIX + "insert/"
        const val NAVIGATE_MENU = MENU_BAR_PREFIX + "navigate/"
        const val VIEW_MENU = MENU_BAR_PREFIX + "view/"
        const val HELP_MENU = MENU_BAR_PREFIX + "help/"
        const val MINDMAP_MENU = MENU_BAR_PREFIX + "mindmaps/"
        private const val MENU_MINDMAP_CATEGORY = (MINDMAP_MENU
                + "mindmaps")
        const val MODES_MENU = MINDMAP_MENU

        // public static final String MODES_MENU = MENU_BAR_PREFIX+"modes/";
        const val EDIT_MENU = MENU_BAR_PREFIX + "edit/"
        const val FILE_MENU = MENU_BAR_PREFIX + "file/"
        const val FORMAT_MENU = MENU_BAR_PREFIX + "format/"
        const val EXTRAS_MENU = MENU_BAR_PREFIX + "extras/"
    }
}