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

import freemind.main.FreeMind
import freemind.main.Resources
import freemind.modes.ModeController
import freemind.view.MapModule
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.util.LinkedList
import java.util.logging.Logger
import javax.swing.ButtonGroup
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JRadioButtonMenuItem
import javax.swing.KeyStroke
import javax.swing.SwingUtilities

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
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("menu_view")),
            VIEW_MENU + "."
        )

        // insert menu
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("menu_insert")),
            INSERT_MENU + "."
        )
        menuHolder!!.addCategory(INSERT_MENU + "nodes")
        menuHolder!!.addSeparator(INSERT_MENU)
        menuHolder!!.addCategory(INSERT_MENU + "icons")
        menuHolder!!.addSeparator(INSERT_MENU)

        // format menu
        menuHolder!!.addMenu(JMenu(c.getResourceString("menu_format")), FORMAT_MENU + ".")

        // navigate menu
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("menu_navigate")),
            NAVIGATE_MENU + "."
        )

        // extras menu
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("menu_extras")),
            EXTRAS_MENU + "."
        )
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
        mapsPopupMenu?.setName(c.getResourceString("mindmaps"))
        menuHolder!!.addCategory(POPUP_MENU + "navigate")
        // menuHolder.addSeparator(POPUP_MENU);

        // formerly, the modes menu was an own menu, but to need less place for
        // the menus,
        // we integrated it into the maps menu.
        // JMenu modesmenu = menuHolder.addMenu(new
        // JMenu(c.getResourceString("modes")), MODES_MENU+".");
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("help")),
            (
                HELP_MENU +
                    "."
                )
        )
        menuHolder!!.addAction(c.documentation, HELP_MENU + "doc/documentation")
        menuHolder!!.addAction(c.freemindUrl, HELP_MENU + "doc/freemind")
        menuHolder!!.addAction(c.faq, HELP_MENU + "doc/faq")
        menuHolder!!.addAction(
            c.keyDocumentation,
            (
                HELP_MENU +
                    "doc/keyDocumentation"
                )
        )
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
        newModeController?.updateMenus(menuHolder)
        menuHolder!!.updateMenus(this)

        val currentPopupMenu = mapsPopupMenu
        if (currentPopupMenu is FreeMindPopupMenu) menuHolder!!.updateMenus(currentPopupMenu)
    }

    private fun updateModeMenu() {
        val group = ButtonGroup()
        val modesMenuActionListener: ActionListener = ModesMenuActionListener()
        val keys: List<String> = LinkedList(c.modes)
        for (key: String in keys) {
            val item = JRadioButtonMenuItem(
                c.getResourceString("mode_$key")
            )
            item.actionCommand = key
            val newItem = menuHolder
                ?.addMenuItem(item, MODES_MENU + key) as JRadioButtonMenuItem
            group.add(newItem)
            if (c.mode != null) {
                newItem.isSelected = (c.mode.toString() == key)
            } else {
                newItem.isSelected = false
            }
            val keystroke = c.frame.getAdjustableProperty(
                "keystroke_mode_$key"
            )
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
            newPopupItem = menuHolder!!.addAction(
                c.toggleMenubar,
                (
                    POPUP_MENU +
                        "toggleMenubar"
                    )
            )
            newPopupItem?.foreground = Color(100, 80, 80)
        }
        newPopupItem = menuHolder!!.addAction(
            c.toggleToolbar,
            (
                POPUP_MENU +
                    "toggleToolbar"
                )
        )
        newPopupItem?.foreground = Color(100, 80, 80)
        newPopupItem = menuHolder!!.addAction(
            c.toggleLeftToolbar,
            (
                POPUP_MENU +
                    "toggleLeftToolbar"
                )
        )
        newPopupItem?.foreground = Color(100, 80, 80)
    }

    private fun updateMapsMenu(holder: StructuredMenuHolder, basicKey: String) {
        val mapModuleManager = c.mapModuleManager
        val mapModuleVector: List<MapModule?> = mapModuleManager!!.getMapModuleVector()
        val group = ButtonGroup()
        for (mapModule: MapModule? in mapModuleVector) {
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
        val print = menuHolder!!.addAction(
            c.print,
            (
                FILE_MENU +
                    "print/print"
                )
        )
        print?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_print")
        )
        val printPreview = menuHolder!!.addAction(
            c.printPreview,
            (
                FILE_MENU +
                    "print/printPreview"
                )
        )
        printPreview?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_print_preview")
        )
        val close = menuHolder!!.addAction(
            c.close,
            (
                FILE_MENU +
                    "close/close"
                )
        )
        close?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_close")
        )
        val quit = menuHolder!!.addAction(c.quit, FILE_MENU + "quit/quit")
        quit?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_quit")
        )
        updateLastOpenedList()
    }

    private fun updateLastOpenedList() {
        menuHolder!!.addMenu(
            JMenu(c.getResourceString("most_recent_files")),
            FILE_MENU + "last/."
        )
        var firstElement = true
        val lst = c.lastOpenedList
        val it = lst!!.listIterator()
        while (it.hasNext()) {
            val key = it.next()
            val item = JMenuItem(key)
            if (firstElement) {
                firstElement = false
                item.accelerator = KeyStroke.getKeyStroke(
                    c.frame
                        .getAdjustableProperty(
                            "keystroke_open_first_in_history"
                        )
                )
            }
            item.addActionListener(LastOpenedActionListener(key))
            menuHolder!!.addMenuItem(
                item,
                FILE_MENU + "last/" + (key.replace('/', '_'))
            )
        }
    }

    private fun updateEditMenu() {
        val moveToRoot = menuHolder!!.addAction(
            c.moveToRoot,
            (
                NAVIGATE_MENU +
                    "nodes/moveToRoot"
                )
        )
        moveToRoot?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_moveToRoot")
        )
        val previousMap = menuHolder!!.addAction(
            c.navigationPreviousMap,
            MINDMAP_MENU + "navigate/navigationPreviousMap"
        )
        previousMap?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_PREVIOUS_MAP)
        )
        val nextMap = menuHolder!!.addAction(
            c.navigationNextMap,
            MINDMAP_MENU + "navigate/navigationNextMap"
        )
        nextMap?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_NEXT_MAP)
        )
        val MoveMapLeft = menuHolder!!.addAction(
            c.navigationMoveMapLeftAction,
            (
                MINDMAP_MENU +
                    "navigate/navigationMoveMapLeft"
                )
        )
        MoveMapLeft?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_LEFT)
        )
        val MoveMapRight = menuHolder!!.addAction(
            c.navigationMoveMapRightAction,
            (
                MINDMAP_MENU +
                    "navigate/navigationMoveMapRight"
                )
        )
        MoveMapRight?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_RIGHT)
        )

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
        zoomIn?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_zoom_in")
        )
        val zoomOut = menuHolder!!.addAction(
            c.zoomOut,
            (
                VIEW_MENU +
                    "zoom/zoomOut"
                )
        )
        zoomOut?.accelerator = KeyStroke.getKeyStroke(
            c.frame
                .getAdjustableProperty("keystroke_zoom_out")
        )
        menuHolder!!.addSeparator(VIEW_MENU)
        menuHolder!!.addCategory(VIEW_MENU + "note_window")
    }

    private inner class MapsMenuActionListener() : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            SwingUtilities.invokeLater(
                Runnable {
                    c.mapModuleManager!!.changeToMapModule(
                        e.actionCommand
                    )
                }
            )
        }
    }

    private inner class LastOpenedActionListener(private val mKey: String) : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val restoreable = mKey
            try {
                c.lastOpenedList!!.open(restoreable)
            } catch (ex: Exception) {
                c.errorMessage(
                    (
                        "An error occured on opening the file: " +
                            restoreable + "."
                        )
                )
                Resources.getInstance().logException(ex)
            }
        }
    }

    private inner class ModesMenuActionListener() : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            SwingUtilities.invokeLater(object : Runnable {
                override fun run() {
                    c.createNewMode(e.actionCommand)
                }
            })
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JMenuBar#processKeyBinding(javax.swing.KeyStroke,
	 * java.awt.event.KeyEvent, int, boolean)
	 */
    public override fun processKeyBinding(
        ks: KeyStroke,
        e: KeyEvent,
        condition: Int,
        pressed: Boolean
    ): Boolean {
        return super.processKeyBinding(ks, e, condition, pressed)
    }

    companion object {
        private var logger: Logger? = null
        val MENU_BAR_PREFIX = "menu_bar/"
        val GENERAL_POPUP_PREFIX = "popup/"
        val POPUP_MENU = GENERAL_POPUP_PREFIX + "popup/"
        @JvmField
        val INSERT_MENU = MENU_BAR_PREFIX + "insert/"
        val NAVIGATE_MENU = MENU_BAR_PREFIX + "navigate/"
        val VIEW_MENU = MENU_BAR_PREFIX + "view/"
        val HELP_MENU = MENU_BAR_PREFIX + "help/"
        val MINDMAP_MENU = MENU_BAR_PREFIX + "mindmaps/"
        private val MENU_MINDMAP_CATEGORY = (
            MINDMAP_MENU +
                "mindmaps"
            )
        val MODES_MENU = MINDMAP_MENU

        // public static final String MODES_MENU = MENU_BAR_PREFIX+"modes/";
        @JvmField
        val EDIT_MENU = MENU_BAR_PREFIX + "edit/"
        val FILE_MENU = MENU_BAR_PREFIX + "file/"
        @JvmField
        val FORMAT_MENU = MENU_BAR_PREFIX + "format/"
        val EXTRAS_MENU = MENU_BAR_PREFIX + "extras/"
    }
}
