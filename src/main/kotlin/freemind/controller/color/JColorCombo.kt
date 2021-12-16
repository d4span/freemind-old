/*
 * FreeMind - A Program for creating and viewing MindmapsCopyright (C) 2000-2015
 * Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and
 * others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/ormodify it under
 * the terms of the GNU General Public Licenseas published by the Free Software
 * Foundation; either version 2of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty ofMERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See theGNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public Licensealong with
 * this program; if not, write to the Free SoftwareFoundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.controller.color

import freemind.common.NamedObject.equals
import freemind.common.NamedObject.name
import freemind.common.JOptionalSplitPane.lastDividerPosition
import freemind.common.JOptionalSplitPane.setComponent
import freemind.common.JOptionalSplitPane.removeComponent
import freemind.common.JOptionalSplitPane.amountOfComponents
import freemind.common.JOptionalSplitPane.dividerPosition
import javax.swing.JComboBox
import freemind.controller.color.ColorPair
import javax.swing.ImageIcon
import java.awt.image.BufferedImage
import freemind.controller.color.JColorCombo.ColorIcon
import freemind.main.Tools
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
import java.awt.print.Printable
import javax.swing.JOptionPane
import javax.swing.JToolBar
import freemind.controller.printpreview.BrowseAction
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
import java.util.StringTokenizer
import java.io.FileNotFoundException
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
import java.awt.*
import java.util.TreeMap

class JColorCombo : JComboBox<ColorPair?>() {
    class ColorIcon(pColor: Color?) : ImageIcon(BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB)) {
        private val mImage: BufferedImage

        init {
            mImage = image as BufferedImage
            val g = mImage.graphics
            g.color = pColor
            g.fillRect(0, 0, ICON_SIZE, ICON_SIZE)
            g.dispose()
        }

        companion object {
            private val ICON_SIZE = (Tools.getScalingFactor() * 16).toInt()
        }
    }

    /** See [MindMapToolBar]  */
    override fun getMaximumSize(): Dimension {
        return preferredSize
    }

    inner class ComboBoxRenderer : JLabel(), ListCellRenderer<ColorPair> {
        init {
            isOpaque = true
            horizontalAlignment = LEFT
            verticalAlignment = CENTER
        }

        /*
	     * This method finds the image and text corresponding
	     * to the selected value and returns the label, set up
	     * to display the text and image.
	     */
        override fun getListCellRendererComponent(list: JList<out ColorPair>, value: ColorPair, index: Int,
                                                  isSelected: Boolean, cellHasFocus: Boolean): Component {
            if (isSelected) {
                background = list.selectionBackground
                foreground = list.selectionForeground
            } else {
                background = list.background
                foreground = list.foreground
            }
            val pair = value
            val icon: ImageIcon = ColorIcon(pair.color)
            setIcon(icon)
            text = pair.displayName
            return this
        }
    }

    init {
        val colorList = sColorList
        for (i in colorList.indices) {
            val colorPair = colorList[i]
            addItem(colorPair)
        }
        val renderer = ComboBoxRenderer()
        setRenderer(renderer)
        setMaximumRowCount(20)
    }

    companion object {
        @JvmStatic
        fun main(s: Array<String>) {
            val frame = JFrame("JColorChooser")
            val colorChooser = JColorCombo()
            frame.contentPane.add(colorChooser)
            frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            frame.pack()
            frame.isVisible = true
        }

        var sColorList = arrayOf( // taken from http://wiki.selfhtml.org/wiki/Grafik/Farbpaletten#Farbnamen
                // default 16bit colors
                ColorPair(Color(0x000000), "black"),
                ColorPair(Color(0x808080), "gray"),
                ColorPair(Color(0x800000), "maroon"),
                ColorPair(Color(0xFF0000), "red"),
                ColorPair(Color(0x008000), "green"),
                ColorPair(Color(0x00FF00), "lime"),
                ColorPair(Color(0x808000), "olive"),
                ColorPair(Color(0xFFFF00), "yellow"),
                ColorPair(Color(0x000080), "navy"),
                ColorPair(Color(0x0000FF), "blue"),
                ColorPair(Color(0x800080), "purple"),
                ColorPair(Color(0xFF00FF), "fuchsia"),
                ColorPair(Color(0x008080), "teal"),
                ColorPair(Color(0x00FFFF), "aqua"),
                ColorPair(Color(0xC0C0C0), "silver"),
                ColorPair(Color(0xFFFFFF), "white"),  // automatic layout colors:
                ColorPair(Color(0x0033ff), "level1"),
                ColorPair(Color(0x00b439), "level2"),
                ColorPair(Color(0x990000), "level3"),
                ColorPair(Color(0x111111), "level4"),  // netscape colors
                ColorPair(Color(0xFFC0CB), "pink"),
                ColorPair(Color(0xFFB6C1), "lightpink"),
                ColorPair(Color(0xFF69B4), "hotpink"),
                ColorPair(Color(0xFF1493), "deeppink"),
                ColorPair(Color(0xDB7093), "palevioletred"),
                ColorPair(Color(0xC71585), "mediumvioletred"),
                ColorPair(Color(0xFFA07A), "lightsalmon"),
                ColorPair(Color(0xFA8072), "salmon"),
                ColorPair(Color(0xE9967A), "darksalmon"),
                ColorPair(Color(0xF08080), "lightcoral"),
                ColorPair(Color(0xCD5C5C), "indianred"),
                ColorPair(Color(0xDC143C), "crimson"),
                ColorPair(Color(0xB22222), "firebrick"),
                ColorPair(Color(0x8B0000), "darkred"),
                ColorPair(Color(0xFF0000), "red"),
                ColorPair(Color(0xFF4500), "orangered"),
                ColorPair(Color(0xFF6347), "tomato"),
                ColorPair(Color(0xFF7F50), "coral"),
                ColorPair(Color(0xFF8C00), "darkorange"),
                ColorPair(Color(0xFFA500), "orange"),
                ColorPair(Color(0xFFFF00), "yellow"),
                ColorPair(Color(0xFFFFE0), "lightyellow"),
                ColorPair(Color(0xFFFACD), "lemonchiffon"),
                ColorPair(Color(0xFFEFD5), "papayawhip"),
                ColorPair(Color(0xFFE4B5), "moccasin"),
                ColorPair(Color(0xFFDAB9), "peachpuff"),
                ColorPair(Color(0xEEE8AA), "palegoldenrod"),
                ColorPair(Color(0xF0E68C), "khaki"),
                ColorPair(Color(0xBDB76B), "darkkhaki"),
                ColorPair(Color(0xFFD700), "gold"),
                ColorPair(Color(0xFFF8DC), "cornsilk"),
                ColorPair(Color(0xFFEBCD), "blanchedalmond"),
                ColorPair(Color(0xFFE4C4), "bisque"),
                ColorPair(Color(0xFFDEAD), "navajowhite"),
                ColorPair(Color(0xF5DEB3), "wheat"),
                ColorPair(Color(0xDEB887), "burlywood"),
                ColorPair(Color(0xD2B48C), "tan"),
                ColorPair(Color(0xBC8F8F), "rosybrown"),
                ColorPair(Color(0xF4A460), "sandybrown"),
                ColorPair(Color(0xDAA520), "goldenrod"),
                ColorPair(Color(0xB8860B), "darkgoldenrod"),
                ColorPair(Color(0xCD853F), "peru"),
                ColorPair(Color(0xD2691E), "chocolate"),
                ColorPair(Color(0x8B4513), "saddlebrown"),
                ColorPair(Color(0xA0522D), "sienna"),
                ColorPair(Color(0xA52A2A), "brown"),
                ColorPair(Color(0x800000), "maroon"),
                ColorPair(Color(0x556B2F), "darkolivegreen"),
                ColorPair(Color(0x808000), "olive"),
                ColorPair(Color(0x6B8E23), "olivedrab"),
                ColorPair(Color(0x9ACD32), "yellowgreen"),
                ColorPair(Color(0x32CD32), "limegreen"),
                ColorPair(Color(0x00FF00), "lime"),
                ColorPair(Color(0x7CFC00), "lawngreen"),
                ColorPair(Color(0x7FFF00), "chartreuse"),
                ColorPair(Color(0xADFF2F), "greenyellow"),
                ColorPair(Color(0x00FF7F), "springgreen"),
                ColorPair(Color(0x00FA9A), "mediumspringgreen"),
                ColorPair(Color(0x90EE90), "lightgreen"),
                ColorPair(Color(0x98FB98), "palegreen"),
                ColorPair(Color(0x8FBC8F), "darkseagreen"),
                ColorPair(Color(0x3CB371), "mediumseagreen"),
                ColorPair(Color(0x2E8B57), "seagreen"),
                ColorPair(Color(0x228B22), "forestgreen"),
                ColorPair(Color(0x008000), "green"),
                ColorPair(Color(0x006400), "darkgreen"),
                ColorPair(Color(0x66CDAA), "mediumaquamarine"),
                ColorPair(Color(0x00FFFF), "aqua"),
                ColorPair(Color(0x00FFFF), "cyan"),
                ColorPair(Color(0xE0FFFF), "lightcyan"),
                ColorPair(Color(0xAFEEEE), "paleturquoise"),
                ColorPair(Color(0x7FFFD4), "aquamarine"),
                ColorPair(Color(0x40E0D0), "turquoise"),
                ColorPair(Color(0x48D1CC), "mediumturquoise"),
                ColorPair(Color(0x00CED1), "darkturquoise"),
                ColorPair(Color(0x20B2AA), "lightseagreen"),
                ColorPair(Color(0x5F9EA0), "cadetblue"),
                ColorPair(Color(0x008B8B), "darkcyan"),
                ColorPair(Color(0x008080), "teal"),
                ColorPair(Color(0xB0C4DE), "lightsteelblue"),
                ColorPair(Color(0xB0E0E6), "powderblue"),
                ColorPair(Color(0xADD8E6), "lightblue"),
                ColorPair(Color(0x87CEEB), "skyblue"),
                ColorPair(Color(0x87CEFA), "lightskyblue"),
                ColorPair(Color(0x00BFFF), "deepskyblue"),
                ColorPair(Color(0x1E90FF), "dodgerblue"),
                ColorPair(Color(0x6495ED), "cornflowerblue"),
                ColorPair(Color(0x4682B4), "steelblue"),
                ColorPair(Color(0x4169E1), "royalblue"),
                ColorPair(Color(0x0000FF), "blue"),
                ColorPair(Color(0x0000CD), "mediumblue"),
                ColorPair(Color(0x00008B), "darkblue"),
                ColorPair(Color(0x000080), "navy"),
                ColorPair(Color(0x191970), "midnightblue"),
                ColorPair(Color(0xE6E6FA), "lavender"),
                ColorPair(Color(0xD8BFD8), "thistle"),
                ColorPair(Color(0xDDA0DD), "plum"),
                ColorPair(Color(0xEE82EE), "violet"),
                ColorPair(Color(0xDA70D6), "orchid"),
                ColorPair(Color(0xFF00FF), "fuchsia"),
                ColorPair(Color(0xFF00FF), "magenta"),
                ColorPair(Color(0xBA55D3), "mediumorchid"),
                ColorPair(Color(0x9370DB), "mediumpurple"),
                ColorPair(Color(0x8A2BE2), "blueviolet"),
                ColorPair(Color(0x9400D3), "darkviolet"),
                ColorPair(Color(0x9932CC), "darkorchid"),
                ColorPair(Color(0x8B008B), "darkmagenta"),
                ColorPair(Color(0x800080), "purple"),
                ColorPair(Color(0x4B0082), "indigo"),
                ColorPair(Color(0x483D8B), "darkslateblue"),
                ColorPair(Color(0x6A5ACD), "slateblue"),
                ColorPair(Color(0x7B68EE), "mediumslateblue"),
                ColorPair(Color(0xFFFFFF), "white"),
                ColorPair(Color(0xFFFAFA), "snow"),
                ColorPair(Color(0xF0FFF0), "honeydew"),
                ColorPair(Color(0xF5FFFA), "mintcream"),
                ColorPair(Color(0xF0FFFF), "azure"),
                ColorPair(Color(0xF0F8FF), "aliceblue"),
                ColorPair(Color(0xF8F8FF), "ghostwhite"),
                ColorPair(Color(0xF5F5F5), "whitesmoke"),
                ColorPair(Color(0xFFF5EE), "seashell"),
                ColorPair(Color(0xF5F5DC), "beige"),
                ColorPair(Color(0xFDF5E6), "oldlace"),
                ColorPair(Color(0xFFFAF0), "floralwhite"),
                ColorPair(Color(0xFFFFF0), "ivory"),
                ColorPair(Color(0xFAEBD7), "antiquewhite"),
                ColorPair(Color(0xFAF0E6), "linen"),
                ColorPair(Color(0xFFF0F5), "lavenderblush"),
                ColorPair(Color(0xFFE4E1), "mistyrose"),
                ColorPair(Color(0xDCDCDC), "gainsboro"),
                ColorPair(Color(0xD3D3D3), "lightgray"),
                ColorPair(Color(0xC0C0C0), "silver"),
                ColorPair(Color(0xA9A9A9), "darkgray"),
                ColorPair(Color(0x808080), "gray"),
                ColorPair(Color(0x696969), "dimgray"),
                ColorPair(Color(0x778899), "lightslategray"),
                ColorPair(Color(0x708090), "slategray"),
                ColorPair(Color(0x2F4F4F), "darkslategray"),
                ColorPair(Color(0x000000), "black"))
    }
}