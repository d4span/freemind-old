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
import freemind.controller.filter.util.SortedListModel
import freemind.controller.filter.util.SortedMapListModel
import javax.swing.event.ListDataListener
import javax.swing.event.ListDataEvent
import freemind.controller.filter.util.ExtendedComboBoxModel.ExtensionDataListener
import freemind.controller.filter.util.ExtendedComboBoxModel
import freemind.modes.MindMapNode
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
import freemind.modes.MindMap
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
import java.io.IOException
import java.io.FileWriter
import java.io.FileReader
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
import freemind.controller.StructuredMenuHolder
import freemind.controller.MenuBar.MapsMenuActionListener
import freemind.modes.ModeController
import freemind.controller.FreeMindPopupMenu
import freemind.controller.MenuBar.ModesMenuActionListener
import freemind.controller.MapModuleManager
import freemind.controller.LastOpenedList
import freemind.controller.MenuBar.LastOpenedActionListener
import java.awt.event.KeyEvent
import java.awt.print.PageFormat
import java.awt.Graphics2D
import java.awt.print.Printable
import freemind.controller.printpreview.BrowseAction
import java.awt.FlowLayout
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
import freemind.controller.LastStateStorageManagement
import java.lang.SecurityException
import java.awt.print.Paper
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
import java.io.ByteArrayInputStream
import java.awt.datatransfer.Clipboard
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.controller.MenuItemEnabledListener
import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage
import freemind.main.*
import freemind.view.ImageFactory
import java.lang.Exception
import java.util.*
import java.util.logging.Logger
import javax.swing.*

/**
 * @author foltin
 */
class StructuredMenuHolder {
    private var mOutputString: String? = null
    var menuMap: MutableMap<String, Vector<String>>
    private var mIndent = 0

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
        menuMap = HashMap()
        val order = Vector<String>()
        menuMap[ORDER_NAME] = order
        if (sSelectedIcon == null) {
            sSelectedIcon = ImageFactory.getInstance().createIcon(Resources.getInstance().getResource(
                    SELECTED_ICON_PATH))
        }
    }

    /**
     */
    fun addMenu(item: JMenu, category: String?): JMenu {
        val tokens = StringTokenizer(category, "/")
        return addMenu(item, tokens) as JMenu
    }

    /**
     */
    fun addMenuItem(item: JMenuItem, category: String?): JMenuItem {
        val tokens = StringTokenizer(category, "/")
        val holder = StructuredMenuItemHolder()
        holder.action = item.action
        holder.menuItem = item
        adjustTooltips(holder)
        addMenu(holder, tokens)
        return item
    }

    /**
     * @param item is an action. If it derives from MenuItemSelectedListener,
     * a check box is used.
     */
    fun addAction(item: Action?, category: String?): JMenuItem? {
        val tokens = StringTokenizer(category, "/")
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
        addMenu(holder, tokens)
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
            val toolTipText = holder.menuItem.toolTipText
            if (toolTipText != null) {
                val toolTipTextWithoutTags = HtmlTools
                        .removeHtmlTagsFromString(toolTipText)
                logger!!.finest("Old tool tip: " + toolTipText
                        + ", New tool tip: " + toolTipTextWithoutTags)
                holder.menuItem.toolTipText = toolTipTextWithoutTags
            }
        }
    }

    fun addCategory(category: String) {
        val tokens = StringTokenizer("$category/blank", "/")
        // with this call, the category is created.
        getCategoryMap(tokens, menuMap)
    }

    fun addSeparator(category: String) {
        var sep = category
        if (!sep.endsWith("/")) {
            sep += "/"
        }
        sep += SEPARATOR_TEXT
        val tokens = StringTokenizer(sep, "/")
        // separators can occur as doubles.
        val categoryPair = getCategoryMap(tokens, menuMap)
        // add an separator
        categoryPair!!.map!![categoryPair.token] = SeparatorHolder()
        categoryPair.order!!.add(categoryPair.token)
    }

    /**
     */
    private fun addMenu(item: Any, tokens: StringTokenizer): Any {
        val categoryPair = getCategoryMap(tokens, menuMap)
        // add the item:
        categoryPair!!.map!![categoryPair.token] = item
        categoryPair.order!!.add(categoryPair.token)
        return item
    }

    private inner class PrintMenuAdder : MenuAdder {
        override fun addMenuItem(holder: StructuredMenuItemHolder) {
            print("JMenuItem '" + holder.menuItem.actionCommand + "'")
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

    private inner class MapTokenPair internal constructor(var map: MutableMap<*, *>?, var token: String, var order: Vector<String>?)

    private fun getCategoryMap(tokens: StringTokenizer, thisMap: MutableMap<*, *>?): MapTokenPair? {
        if (tokens.hasMoreTokens()) {
            val nextToken = tokens.nextToken()
            return if (tokens.hasMoreTokens()) {
                if (!thisMap!!.containsKey(nextToken)) {
                    val newMap: MutableMap<*, *> = HashMap<Any, Any>()
                    val newOrder: Vector<*> = Vector<Any>()
                    newMap[ORDER_NAME] = newOrder
                    thisMap[nextToken] = newMap
                }
                val nextMap = thisMap[nextToken] as MutableMap<*, *>?
                val order = thisMap[ORDER_NAME] as Vector<String>?
                if (!order!!.contains(nextToken)) {
                    order.add(nextToken)
                }
                getCategoryMap(tokens, nextMap)
            } else {
                val order = thisMap!![ORDER_NAME] as Vector<String>?
                MapTokenPair(thisMap, nextToken, order)
            }
        }
        // error case?
        return null
    }

    fun updateMenus(myItem: JMenuBar, prefix: String?) {
        val pair = getCategoryMap(StringTokenizer(prefix, "/"),
                menuMap)
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(object : MenuAdder {
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
        }, myMap, DefaultMenuAdderCreator())
    }

    fun updateMenus(myItem: JPopupMenu, prefix: String?) {
        val pair = getCategoryMap(StringTokenizer(prefix, "/"),
                menuMap)
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(object : MenuAdder {
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
        }, myMap, DefaultMenuAdderCreator())
    }

    /**
     */
    fun updateMenus(bar: JToolBar, prefix: String?) {
        val pair = getCategoryMap(StringTokenizer(prefix, "/"),
                menuMap)
        val myMap = pair!!.map!![pair.token] as Map<*, *>?
        updateMenus(object : MenuAdder {
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
        }, myMap, DefaultMenuAdderCreator())
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
            pMenuItem.addMenuListener(listener)
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

    private fun updateMenus(menuAdder: MenuAdder, thisMap: Map<String, Vector<String>>?,
                            factory: MenuAdderCreator) {
        // System.out.println(thisMap);
        // iterate through maps and do the changes:
        val myVector = thisMap!![ORDER_NAME] as Vector<String>
        for (category in myVector) {
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
        fun addMenuListener(listener: MenuListener)
        fun removeMenuListener(listener: MenuListener)
    }

    class StructuredMenuListener : MenuListener {
        private val menuItemHolder = Vector<StructuredMenuItemHolder>()
        override fun menuSelected(arg0: MenuEvent) {
//			System.out.println("Selected menu items " + arg0);
            for (holder in menuItemHolder) {
                val action = holder.action
                var isEnabled = false
                val menuItem = holder.menuItem
                if (holder.enabledListener != null) {
                    try {
                        isEnabled = holder.enabledListener.isEnabled(
                                menuItem, action)
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                    }
                    action!!.isEnabled = isEnabled
                    //					menuItem.setEnabled(isEnabled);
                }
                isEnabled = menuItem!!.isEnabled
                if (isEnabled && holder.selectionListener != null) {
                    var selected = false
                    try {
                        selected = holder.selectionListener.isSelected(
                                menuItem, action)
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
                    item.iconTextGap = (item.iconTextGap
                            + (ICON_SIZE - item.icon.iconWidth))
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
                var normalIcon = menuItem!!.action.getValue(
                        Action.SMALL_ICON) as Icon
                if (normalIcon == null) {
                    normalIcon = blindIcon
                }
                menuItem.icon = normalIcon
            }
        }
    }
}