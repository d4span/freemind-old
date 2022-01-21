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
 * Created on 21.05.2004
 */
/*$Id: StructuredMenuHolder.java,v 1.1.4.7.4.11 2010/09/30 22:38:47 christianfoltin Exp $*/
package freemind.controller

import freemind.main.HtmlTools
import freemind.main.Resources
import freemind.main.Tools
import freemind.view.ImageFactory
import java.util.StringTokenizer
import java.util.Vector
import java.util.logging.Logger
import javax.swing.Action
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JSeparator
import javax.swing.JToolBar
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener

/**
 * @author foltin
 */
class StructuredMenuHolder {
    private var mOutputString: String? = null
    var menuMap: MutableMap<String, Any>
    private var mIndent = 0

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        menuMap = HashMap()
        val order = Vector<String>()
        menuMap[ORDER_NAME] = order
        if (sSelectedIcon == null) {
            sSelectedIcon = ImageFactory.getInstance().createIcon(
                Resources.getInstance().getResource(
                    SELECTED_ICON_PATH
                )
            )
        }
    }

    /**
     */
    fun addMenu(item: JMenu, category: String?): JMenu {
        StringTokenizer(category, "/")
        return addMenu(item) as JMenu
    }

    /**
     */
    fun addMenuItem(item: JMenuItem, category: String?): JMenuItem {
        StringTokenizer(category, "/")
        val holder = StructuredMenuItemHolder()
        holder.action = item.action
        holder.menuItem = item
        adjustTooltips(holder)
        addMenu(holder)
        return item
    }

    /**
     * @param item is an action. If it derives from MenuItemSelectedListener,
     * a check box is used.
     */
    fun addAction(item: Action?, category: String?): JMenuItem? {
        StringTokenizer(category, "/")
        val holder = StructuredMenuItemHolder()
        holder.action = item
        /*
		 * Dimitry, Eric and Dan requested to have the check marks with the
		 * original JCheckBoxMenuItem.
		 */if (item is MenuItemSelectedListener) {
            val checkBox = JCheckBoxMenuItem(item)
            holder.menuItem = checkBox
        } else {
            holder.menuItem = JMenuItem(item)
        }
        adjustTooltips(holder)
        addMenu(holder)
        return holder.menuItem
    }

    /**
     * Under Mac, no HTML is rendered for menus.
     *
     * @param holder
     */
    private fun adjustTooltips(holder: StructuredMenuItemHolder) {
        if (Tools.isMacOsX()) {
            // remove html tags from tooltips:
            val toolTipText = holder.menuItem!!.toolTipText
            if (toolTipText != null) {
                val toolTipTextWithoutTags = HtmlTools
                    .removeHtmlTagsFromString(toolTipText)
                logger!!.finest(
                    "Old tool tip: " + toolTipText +
                        ", New tool tip: " + toolTipTextWithoutTags
                )
                holder.menuItem!!.toolTipText = toolTipTextWithoutTags
            }
        }
    }

    fun addCategory(category: String) {
        StringTokenizer("$category/blank", "/")
        // with this call, the category is created.
        getCategoryMap()
    }

    fun addSeparator(category: String) {
        var sep = category
        if (!sep.endsWith("/")) {
            sep += "/"
        }
        sep += SEPARATOR_TEXT
        StringTokenizer(sep, "/")
        // separators can occur as doubles.
        val categoryPair = getCategoryMap()
        // add an separator
        categoryPair!!.map!![categoryPair.token] = SeparatorHolder()
        categoryPair.order!!.add(categoryPair.token)
    }

    /**
     */
    private fun addMenu(item: Any): Any {
        val categoryPair = getCategoryMap()
        // add the item:
        categoryPair!!.map!![categoryPair.token] = item
        categoryPair.order!!.add(categoryPair.token)
        return item
    }

    private inner class PrintMenuAdder : MenuAdder {
        override fun addMenuItem(holder: StructuredMenuItemHolder) {
            print("JMenuItem '" + holder.menuItem!!.actionCommand + "'")
        }

        override fun addSeparator() {
            print("Separator '" + "'")
        }

        // public void addAction(Action action) {
        // print("Action    '"+action.getValue(Action.NAME)+"'");
        // }
        override fun addCategory(category: String) {
            print("Category: '$category'")
        }
    }

    private inner class MapTokenPair internal constructor(
        var map: MutableMap<String, Any>?,
        var token: String,
        var order: Vector<String>?
    )

    private fun getCategoryMap(): MapTokenPair? {
        TODO()
//        if (tokens.hasMoreTokens()) {
//            val nextToken = tokens.nextToken()
//            return if (tokens.hasMoreTokens()) {
//                if (!thisMap!!.containsKey(nextToken)) {
//                    val newMap: MutableMap<String, Any> = HashMap()
//                    val newOrder: Vector<String> = Vector()
//                    newMap[ORDER_NAME] = newOrder
//                    thisMap[nextToken] = newMap
//                }
//                val nextMap = thisMap[nextToken]
//                if (nextMap is MutableMap<*, *>?) {
//                    val order = thisMap[ORDER_NAME] as Vector<*>?
//                    if (!order!!.contains(nextToken)) {
//                        order.add(nextToken)
//                    }
//                    getCategoryMap(tokens, nextMap)
//                } else TODO()
//            } else {
//                val order = thisMap!![ORDER_NAME] as Vector<*>?
//                MapTokenPair(thisMap, nextToken, order)
//            }
//        }
//        // error case?
//        return null
    }

    fun updateMenus(myItem: JMenuBar) {
        val pair = getCategoryMap()
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(
            object : MenuAdder {
                override fun addMenuItem(holder: StructuredMenuItemHolder) {
                    Tools.setLabelAndMnemonic(holder.menuItem, null)
                    myItem.add(holder.menuItem)
                }

                override fun addSeparator() {
                    throw NoSuchMethodError("addSeparator for JMenuBar")
                }

                // public void addAction(Action action) {
                // throw new NoSuchMethodError("addAction for JMenuBar");
                // }
                override fun addCategory(category: String) {}
            },
            myMap, DefaultMenuAdderCreator()
        )
    }

    fun updateMenus(myItem: JPopupMenu) {
        val pair = getCategoryMap()
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(
            object : MenuAdder {
                var listener = StructuredMenuListener()
                override fun addMenuItem(holder: StructuredMenuItemHolder) {
                    Tools.setLabelAndMnemonic(holder.menuItem, null)
                    val menuItem = holder.menuItem
                    adjustMenuItem(menuItem)
                    myItem.add(menuItem)
                    if (myItem is MenuEventSupplier) {
                        val receiver = myItem as MenuEventSupplier
                        receiver.addMenuListener(listener)
                        listener.addItem(holder)
                    }
                }

                override fun addSeparator() {
                    if (lastItemIsASeparator(myItem)) return
                    myItem.addSeparator()
                }

                // public void addAction(Action action) {
                // myItem.add(action);
                // }
                override fun addCategory(category: String) {}
            },
            myMap, DefaultMenuAdderCreator()
        )
    }

    /**
     */
    fun updateMenus(bar: JToolBar) {
        val pair = getCategoryMap()
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(
            object : MenuAdder {
                override fun addMenuItem(holder: StructuredMenuItemHolder) {
                    bar.add(holder.action)
                }

                override fun addSeparator() {
                    // no separators to save place. But they look good. fc,
                    // 16.6.2005.
                    bar.addSeparator()
                }

                // public void addAction(Action action) {
                // bar.add(action);
                // }
                override fun addCategory(category: String) {}
            },
            myMap, DefaultMenuAdderCreator()
        )
    }

    private interface MenuAdder {
        fun addMenuItem(holder: StructuredMenuItemHolder)
        fun addSeparator()

        // void addAction(Action action);
        fun addCategory(category: String)
    }

    private class MenuItemAdder(private var myMenuItem: JMenu?) : MenuAdder {
        /**
         *
         */
        private var mAmountOfVisibleMenuItems = 20
        private var mItemCounter = 0
        private var mMenuCounter = 0
        private val mBaseMenuItem: JMenu?
        private val listener: StructuredMenuListener

        init {
            mBaseMenuItem = myMenuItem
            mAmountOfVisibleMenuItems = Resources.getInstance().getIntProperty(AMOUNT_OF_VISIBLE_MENU_ITEMS, 20)
            listener = StructuredMenuListener()
            myMenuItem?.addMenuListener(listener)
        }

        override fun addMenuItem(holder: StructuredMenuItemHolder) {
            mItemCounter++
            if (mItemCounter > mAmountOfVisibleMenuItems) {
                var label = Resources.getInstance().getResourceString("StructuredMenuHolder.next")
                if (mMenuCounter > 0) {
                    label += " $mMenuCounter"
                }
                val jMenu = JMenu(label)
                mBaseMenuItem!!.add(jMenu)
                myMenuItem = jMenu
                mItemCounter = 0
                mMenuCounter++
            }
            Tools.setLabelAndMnemonic(holder.menuItem, null)
            val item = holder.menuItem
            adjustMenuItem(item)
            listener.addItem(holder)
            myMenuItem!!.add(item)
        }

        override fun addSeparator() {
            if (lastItemIsASeparator(myMenuItem)) {
                return
            }
            myMenuItem!!.addSeparator()
        }

        // public void addAction(Action action) {
        // myItem.add(action);
        // }
        override fun addCategory(category: String) {}
    }

    private interface MenuAdderCreator {
        fun createAdder(baseObject: JMenu?): MenuAdder
    }

    private inner class DefaultMenuAdderCreator : MenuAdderCreator {
        /*
		 * (non-Javadoc)
		 *
		 * @see
		 * freemind.controller.StructuredMenuHolder.MenuAdderCreator#createAdder
		 * (javax.swing.JMenu)
		 */
        override fun createAdder(baseObject: JMenu?): MenuAdder {
            return MenuItemAdder(baseObject)
        }
    }

    private inner class SeparatorHolder

    private fun updateMenus(
        menuAdder: MenuAdder,
        thisMap: Map<*, *>?,
        factory: MenuAdderCreator
    ) {
        // System.out.println(thisMap);
        // iterate through maps and do the changes:
        val myVector = thisMap!![ORDER_NAME] as Vector<*>
        for (category in myVector) {
            if (category is String) {
                // The "." target was handled earlier.
                if (category == ".") continue
                val nextObject: Any = thisMap[category]!!
                if (nextObject is SeparatorHolder) {
                    menuAdder.addSeparator()
                    continue
                }
                if (nextObject is StructuredMenuItemHolder) {
                    menuAdder.addMenuItem(nextObject)
                } /*
			 * if(nextObject instanceof JMenuItem) {
			 * menuAdder.addMenuItem((JMenuItem) nextObject); }
			 */
                /*
				 * else if(nextObject instanceof Action){
				 * menuAdder.addAction((Action) nextObject); }
				 */ else if (nextObject is Map<*, *>) {
                    menuAdder.addCategory(category)
                    val nextMap = nextObject
                    var nextItem: MenuAdder
                    if (nextMap.containsKey(".")) {
                        // add this item to the current place:
                        val baseObject = nextMap["."] as JMenu?
                        val holder = StructuredMenuItemHolder()
                        holder.menuItem = baseObject
                        menuAdder.addMenuItem(holder)
                        nextItem = factory.createAdder(baseObject)
                    } else {
                        nextItem = menuAdder
                    }
                    mIndent++
                    updateMenus(nextItem, nextMap, factory)
                    mIndent--
                }
            }
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
    override fun toString(): String {
        mIndent = 0
        mOutputString = ""
        updateMenus(PrintMenuAdder(), menuMap, PrintMenuAdderCreator())
        return mOutputString!!
    }

    private inner class PrintMenuAdderCreator : MenuAdderCreator {
        /*
		 * (non-Javadoc)
		 *
		 * @see
		 * freemind.controller.StructuredMenuHolder.MenuAdderCreator#createAdder
		 * (javax.swing.JMenu)
		 */
        override fun createAdder(baseObject: JMenu?): MenuAdder {
            return PrintMenuAdder()
        }
    }

    private fun print(string: String) {
        for (i in 0 until mIndent) {
            mOutputString += "  "
        }
        mOutputString += """
            $string
            
        """.trimIndent()
    }

    interface MenuEventSupplier {
        fun addMenuListener(listener: MenuListener?)
        fun removeMenuListener(listener: MenuListener?)
    }

    class StructuredMenuListener : MenuListener {
        private val menuItemHolder = Vector<StructuredMenuItemHolder>()
        override fun menuSelected(arg0: MenuEvent) {
// 			System.out.println("Selected menu items " + arg0);
            for (holder in menuItemHolder) {
                val action = holder.action
                var isEnabled = false
                val menuItem = holder.menuItem
                if (holder.enabledListener != null) {
                    try {
                        isEnabled = holder.enabledListener!!.isEnabled(
                            menuItem, action
                        )
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                    }
                    action!!.isEnabled = isEnabled
                    // 					menuItem.setEnabled(isEnabled);
                }
                isEnabled = menuItem!!.isEnabled
                if (isEnabled && holder.selectionListener != null) {
                    var selected = false
                    try {
                        selected = holder.selectionListener!!.isSelected(
                            menuItem, action
                        )
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                    }
                    if (menuItem is JCheckBoxMenuItem) {
                        menuItem.isSelected = selected
                    } else {
                        // Do icon change if not a check box menu!
                        setSelected(menuItem, selected)
                    }
                }
            }
        }

        override fun menuDeselected(arg0: MenuEvent) {}
        override fun menuCanceled(arg0: MenuEvent) {}
        fun addItem(holder: StructuredMenuItemHolder) {
            menuItemHolder.add(holder)
        }
    }

    companion object {
        /**
         *
         */
        const val AMOUNT_OF_VISIBLE_MENU_ITEMS = "AMOUNT_OF_VISIBLE_MENU_ITEMS"
        const val ICON_SIZE = 16
        private val blindIcon: Icon = BlindIcon(ICON_SIZE)
        private const val SELECTED_ICON_PATH = "images/button_ok.png"
        private const val SEPARATOR_TEXT = "000"
        private const val ORDER_NAME = "/order"
        private var logger: Logger? = null
        private var sSelectedIcon: ImageIcon? = null

        /**
         */
        private fun adjustMenuItem(item: JMenuItem?) {
            if (item!!.icon == null) {
                item.icon = blindIcon
            } else {
                // align
                if (item.icon.iconWidth < ICON_SIZE) {
                    item.iconTextGap = (
                        item.iconTextGap +
                            (ICON_SIZE - item.icon.iconWidth)
                        )
                }
            }
        }

        fun lastItemIsASeparator(menu: JMenu?): Boolean {
            if (menu!!.itemCount > 0) {
                if (menu.menuComponents[menu.itemCount - 1] is JSeparator) {
                    // no separator, if the last was such.
                    return true
                }
            }
            return false
        }

        fun lastItemIsASeparator(menu: JPopupMenu): Boolean {
            if (menu.componentCount > 0) {
                if (menu.getComponent(menu.componentCount - 1) is JPopupMenu.Separator) {
                    // no separator, if the last was such.
                    return true
                }
            }
            return false
        }

        private fun setSelected(menuItem: JMenuItem?, state: Boolean) {
            if (state) {
                menuItem!!.icon = sSelectedIcon
            } else {
                val normalIcon = menuItem!!.action.getValue(
                    Action.SMALL_ICON
                ) as Icon
                menuItem.icon = normalIcon
            }
        }
    }
}
